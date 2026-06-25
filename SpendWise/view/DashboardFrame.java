package view;

import model.Expense;
import service.AnalyticsService;
import service.ExpenseService;
import storage.FileManager;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class DashboardFrame extends JFrame {
    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JLabel totalSpentValue;
    private JLabel expenseCountValue;
    private JLabel budgetValue;

    public DashboardFrame() {

        setTitle("Expense Tracker");
        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        initializeUI();
    }

    private void initializeUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.BACKGROUND);

        //----------------------------------
        // Header
        //----------------------------------

        JLabel title =new JLabel("Expense Tracker");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.PRIMARY);
        JPanel header = new JPanel();
        header.setBackground(Theme.BACKGROUND);
        header.add(title);

        //----------------------------------
        // Sidebar
        //----------------------------------

        JPanel sidebar =new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(Theme.PRIMARY);
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        JButton dashboardBtn = new JButton("Dashboard");
        JButton expensesBtn = new JButton("Expenses");
        JButton analyticsBtn = new JButton("Analytics");

        //connecting analytics frame
        analyticsBtn.addActionListener(e -> {
            AnalyticsFrame frame = new AnalyticsFrame();
            frame.setVisible(true);
        });
        
        sidebar.add(dashboardBtn);
        sidebar.add(expensesBtn);
        sidebar.add(analyticsBtn);

        //----------------------------------
        // Content Area
        //----------------------------------

        JPanel content = new JPanel();
        content.setBackground(Theme.BACKGROUND);
        content.setLayout(new FlowLayout(FlowLayout.LEFT,20,20));

        ArrayList<Expense> expenses = FileManager.loadExpenses();
        String[] columns = {"ID", "Date", "Category", "Amount", "Description"};
        tableModel = new DefaultTableModel(columns, 0);
        for(Expense e : expenses){
            tableModel.addRow(
                new Object[]{
                    e.getId(),
                    e.getDate(),
                    e.getCategory(),
                    e.getAmount(),
                    e.getDescription()
                }
            );
        }

        //adding JTable
        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(25);
        expenseTable.setFont(Theme.NORMAL_FONT);
        expenseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD,14));
        expenseTable.getTableHeader().setBackground(Theme.PRIMARY);
        expenseTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane tableScroll = new JScrollPane(expenseTable);
        tableScroll.setPreferredSize(new Dimension(700,350));

        AnalyticsService analytics = new AnalyticsService();

        double totalSpent = analytics.calculateTotal(expenses);
        int expenseCount = expenses.size();

        // Card 1
        totalSpentValue =new JLabel("Rs." + totalSpent);
        JPanel totalCard = createDynamicCard("Total Spent",totalSpentValue);
        content.add(totalCard);

        // Card 2
        //budget card dynamic
        double monthlyBudget = FileManager.loadBudget();
        double remainingBudget = monthlyBudget - totalSpent;
        budgetValue = new JLabel("Rs. " + remainingBudget);
        JPanel budgetCard =createDynamicCard("Budget Left",budgetValue);
        content.add(budgetCard);

        // Card 3
        expenseCountValue = new JLabel(String.valueOf(expenseCount));
        JPanel countCard =createDynamicCard("Expenses",expenseCountValue);
        content.add(countCard);

        JTextField searchField = new JTextField(15);
        
        JButton searchBtn = new JButton("Search");
        
        JButton resetBtn = new JButton("Reset");

        JButton budgetBtn = new JButton("Set Budget");
        content.add(budgetBtn);
        
        content.add(searchField);
        content.add(searchBtn);
        content.add(resetBtn);

        content.add(tableScroll);

        JButton addExpenseBtn = new JButton("+ Add Expense");
        addExpenseBtn.setBackground(Theme.PRIMARY);
        addExpenseBtn.setForeground(Color.WHITE);
        addExpenseBtn.setFont(Theme.NORMAL_FONT);
        content.add(addExpenseBtn);
        addExpenseBtn.addActionListener(e -> {
            AddExpenseDialog dialog = new AddExpenseDialog(this);
            dialog.setVisible(true);
            if(dialog.isSaved()){
                try{
                    ExpenseService expenseService = new ExpenseService();
                    expenseService.setExpenses(FileManager.loadExpenses());
                    Expense expense = new Expense(
                        expenseService.generateNextId(),
                        dialog.getDate(),
                        dialog.getCategory(),
                        Double.parseDouble(dialog.getAmount()),
                        dialog.getDescription()
                    );
                    expenseService.addExpense(expense);
                    FileManager.saveExpenses(expenseService.getAllExpenses());
                    tableModel.addRow(new Object[]{
                            expense.getId(),
                            expense.getDate(),
                            expense.getCategory(),
                            expense.getAmount(),
                            expense.getDescription()});
                    JOptionPane.showMessageDialog(this, "Expense Added Successfully!");
                    refreshDashboard();
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Invalid Input!");
                }
                
            }
        });

        JButton deleteExpenseBtn = new JButton("Delete Selected");
        
        deleteExpenseBtn.setBackground(Theme.SECONDARY);
        deleteExpenseBtn.setForeground(Theme.TEXT);
        deleteExpenseBtn.setFont(Theme.NORMAL_FONT);
        content.add(deleteExpenseBtn);

        deleteExpenseBtn.addActionListener(e -> {
            int selectedRow = expenseTable.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(this,"Please select an expense first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this expense?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            if(confirm != JOptionPane.YES_OPTION){return;}
            try{
                int expenseId = (Integer) tableModel.getValueAt(selectedRow,0);
                ExpenseService expenseService = new ExpenseService();
                expenseService.setExpenses(FileManager.loadExpenses());
                expenseService.deleteExpense(expenseId);
                FileManager.saveExpenses(expenseService.getAllExpenses());
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this,"Expense deleted successfully!");
                refreshDashboard();
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Error deleting expense.");
                ex.printStackTrace();
            }
        });

        //edit response
        JButton editExpenseBtn = new JButton("Edit Selected");
        editExpenseBtn.setBackground(Theme.SECONDARY);
        editExpenseBtn.setForeground(Theme.TEXT);
        editExpenseBtn.setFont(Theme.NORMAL_FONT);
        content.add(editExpenseBtn);

        //edit button
        editExpenseBtn.addActionListener(e -> {
            int selectedRow = expenseTable.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(
                this,
                "Select an expense first.");
                return;
            }
            try{
                int expenseId = (Integer) tableModel.getValueAt(
                        selectedRow,
                        0
                );
                ExpenseService expenseService = new ExpenseService();
                expenseService.setExpenses(FileManager.loadExpenses());
                Expense selectedExpense = null;
                for(Expense exp : expenseService.getAllExpenses()){

            if(exp.getId() == expenseId){

                selectedExpense = exp;
                break;
            }
        }

        if(selectedExpense == null){
            return;
        }

        AddExpenseDialog dialog =
                new AddExpenseDialog(
                        this,
                        selectedExpense
                );

        dialog.setVisible(true);

        if(dialog.isSaved()){

            expenseService.updateExpense(
                    expenseId,
                    dialog.getDate(),
                    dialog.getCategory(),
                    Double.parseDouble(
                            dialog.getAmount()
                    ),
                    dialog.getDescription()
            );

            FileManager.saveExpenses(
                    expenseService.getAllExpenses()
            );

            tableModel.setValueAt(
                    dialog.getDate(),
                    selectedRow,
                    1
            );

            tableModel.setValueAt(
                    dialog.getCategory(),
                    selectedRow,
                    2
            );

            tableModel.setValueAt(
                    Double.parseDouble(
                            dialog.getAmount()
                    ),
                    selectedRow,
                    3
            );

            tableModel.setValueAt(
                    dialog.getDescription(),
                    selectedRow,
                    4
            );

            refreshDashboard();

            JOptionPane.showMessageDialog(
                    this,
                    "Expense Updated!"
            );
        }

    }catch(Exception ex){

        JOptionPane.showMessageDialog(
                this,
                "Error updating expense."
        );
    }
});

        //budget button
        budgetBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter Monthly Budget:"
            );
            if(input != null){
                try{
                    double budget = Double.parseDouble(input);
                    FileManager.saveBudget(budget);
                    refreshDashboard();
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid Budget!"
                    );
                }}
        });

        //search
        searchBtn.addActionListener(e -> {
            String category = searchField.getText();
            ExpenseService expenseService = new ExpenseService();
            expenseService.setExpenses(FileManager.loadExpenses());
            ArrayList<Expense> results = expenseService.searchByCategory(category);
            tableModel.setRowCount(0);
            for(Expense exp : results){
                tableModel.addRow(
                    new Object[]{
                        exp.getId(),
                        exp.getDate(),
                        exp.getCategory(),
                        exp.getAmount(),
                        exp.getDescription()
                    });
                }
            });

        
        //reset
        resetBtn.addActionListener(e -> {
            tableModel.setRowCount(0);
            ArrayList<Expense> allExpenses = FileManager.loadExpenses();
            for(Expense exp : allExpenses){
                tableModel.addRow(
                    new Object[]{
                        exp.getId(),
                        exp.getDate(),
                        exp.getCategory(),
                        exp.getAmount(),
                        exp.getDescription()
                });}
            });

        //----------------------------------

        mainPanel.add(header,BorderLayout.NORTH);

        mainPanel.add(sidebar,BorderLayout.WEST);

        mainPanel.add(content,BorderLayout.CENTER);

        add(mainPanel);
    }

    //----------------------------------
    // Card Creator Method
    //----------------------------------

    private JPanel createDynamicCard(String title, JLabel valueLabel){
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(220,120));
        card.setBackground(Theme.SECONDARY);
        card.setBorder(
            BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Theme.PRIMARY,2),
            BorderFactory.createEmptyBorder(10,10,10,10))
        );
        card.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Theme.NORMAL_FONT);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI",Font.BOLD,28));
        valueLabel.setForeground(Theme.PRIMARY);
        card.add(titleLabel,BorderLayout.NORTH);
        card.add(valueLabel,BorderLayout.CENTER);
        
        return card;
    }
    //refresh-method
    private void refreshDashboard(){
        ArrayList<Expense> expenses = FileManager.loadExpenses();
        AnalyticsService analytics = new AnalyticsService();
        double totalSpent = analytics.calculateTotal(expenses);
        double budget = FileManager.loadBudget();
        double remaining = budget - totalSpent;
        totalSpentValue.setText("Rs." + totalSpent);
        expenseCountValue.setText(String.valueOf(expenses.size()));
        budgetValue.setText("Rs. " + remaining);
    }

}