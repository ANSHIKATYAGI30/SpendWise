package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import model.Expense;
import service.AnalyticsService;
import storage.FileManager;

public class AnalyticsFrame extends JFrame {
    public AnalyticsFrame(){
        setTitle("Analytics");
        setSize(700,500);
        setLocationRelativeTo(null);
        initializeUI();
    }

    private void initializeUI(){
        ArrayList<Expense> expenses = FileManager.loadExpenses();
        AnalyticsService analytics =new AnalyticsService();
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setBackground(Theme.BACKGROUND);
        panel.setLayout(new GridLayout(
                    3,
                    2,
                    20,
                    20
            )
        );
        panel.setBorder(BorderFactory.createEmptyBorder(
                    20,
                    20,
                    20,
                    20
            )
        );
        JPanel totalCard = createAnalyticsCard(
                    "Total Spent",
                    "₹" + analytics.calculateTotal(expenses)
        );
        JPanel highestCard = createAnalyticsCard(
                    "Highest Expense",
                    "₹" + analytics.calculateHighest(expenses)
        );
        JPanel averageCard = createAnalyticsCard(
                    "Average Expense",
                    "₹" + analytics.calculateAverage(expenses)
        );
        JPanel countCard = createAnalyticsCard(
                    "Total Expenses",
                    String.valueOf(
                            analytics.calculateExpenseCount(
                                    expenses
                            )
                    )
        );

        HashMap<String, Double> categoryData = analytics.getCategoryWiseSpending(expenses);
        JPanel breakdownPanel = new JPanel();
        breakdownPanel.setLayout(new GridLayout(categoryData.size() + 1,1));
        breakdownPanel.setBackground(Theme.BACKGROUND);
        JLabel breakdownTitle = new JLabel("Category Breakdown",SwingConstants.CENTER);
        breakdownTitle.setFont(new Font("Segoe UI",Font.BOLD,18));
        breakdownTitle.setForeground(Theme.PRIMARY);
        breakdownPanel.add(breakdownTitle);
        for(String category : categoryData.keySet()){
                JLabel label = new JLabel(category + " : ₹" + categoryData.get(category));
                label.setFont(Theme.NORMAL_FONT);
                breakdownPanel.add(label);
        }
        add(breakdownPanel,BorderLayout.SOUTH);


        JPanel categoryCard = createAnalyticsCard("Top Category", analytics.getTopCategory(expenses));
        panel.add(totalCard);
        panel.add(highestCard);
        panel.add(averageCard);
        panel.add(countCard);
        panel.add(categoryCard);
        
        JLabel heading = new JLabel("Analytics Dashboard",SwingConstants.CENTER);
        heading.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                28
        ));
        heading.setForeground(Theme.PRIMARY);
        add(heading,BorderLayout.NORTH);
        add(panel,BorderLayout.CENTER);
        add(panel);
}

    private JPanel createAnalyticsCard(String title, String value){
        JPanel card = new JPanel();
        
        card.setLayout(new BorderLayout());
        card.setBackground(Theme.SECONDARY);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            Theme.PRIMARY,
                            2
                    ),
                    BorderFactory.createEmptyBorder(
                            10,
                            10,
                            10,
                            10
                    )
            )
    );

    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);

    titleLabel.setFont(Theme.NORMAL_FONT);

    JLabel valueLabel = new JLabel(value,SwingConstants.CENTER);

    valueLabel.setFont(
        new Font(
            "Segoe UI",
            Font.BOLD,
            22));

    valueLabel.setForeground(Theme.PRIMARY);

    card.add(titleLabel,BorderLayout.NORTH);

    card.add(valueLabel, BorderLayout.CENTER);

    return card;
}
}