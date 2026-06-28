package view;
import javax.swing.*;
import model.Expense;
import java.awt.*;

public class AddExpenseDialog extends JDialog {

    private JTextField dateField;
    private JComboBox<String> categoryBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private boolean saved = false;

    public AddExpenseDialog(JFrame parent){
        super(parent,
              "Add Expense",
              true);
        setSize(400,300);
        setLocationRelativeTo(parent);
        initializeUI();
    }

    private void initializeUI(){
        JPanel panel = new JPanel(new GridLayout(5,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        panel.add(new JLabel("Date(yyyy-mm-dd)"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Category"));
        String[] categories = {"Food","Shopping","Travel","Education","Bills","Entertainment","Health", "Other"};
        categoryBox = new JComboBox<>(categories);
        panel.add(categoryBox);

        panel.add(new JLabel("Amount"));
        amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Description"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {saved = true; dispose();});

        panel.add(new JLabel(""));
        panel.add(saveButton);

        add(panel);
    }

    //getters
    public String getDate() {return dateField.getText();}
    public String getCategory() {return (String) categoryBox.getSelectedItem();}
    public String getAmount() {return amountField.getText();}
    public String getDescription() {return descriptionField.getText();}
    public boolean isSaved() {return saved;}

    public AddExpenseDialog(JFrame parent,Expense expense){
        this(parent);
        dateField.setText(expense.getDate());
        categoryBox.setSelectedItem(expense.getCategory());
        amountField.setText(String.valueOf(expense.getAmount()));
        descriptionField.setText(expense.getDescription());
}

}
