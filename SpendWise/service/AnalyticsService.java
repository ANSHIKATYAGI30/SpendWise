package service;
import model.Expense;
import java.util.ArrayList;
import java.util.HashMap;

public class AnalyticsService {
    //total expense
    public double calculateTotal(ArrayList<Expense> expenses){
        double total = 0;
        for(Expense e : expenses){
            total += e.getAmount();
        }
        return total;
    }

    //highest expense
    public double calculateHighest(ArrayList<Expense> expenses){
        if(expenses.isEmpty()){return 0;}
        double highest = expenses.get(0).getAmount();
        for(Expense e : expenses){
            if(e.getAmount() > highest){
                highest = e.getAmount();
            }
        }
        return highest;
    }

    //lowest expense
    public double calculateLowest(ArrayList<Expense> expenses){
        if(expenses.isEmpty()){return 0;}
        
        double lowest = expenses.get(0).getAmount();
        
        for(Expense e : expenses){
            if(e.getAmount() < lowest){lowest = e.getAmount();}
        }
        
        return lowest;
    }

    //average expense
    public double calculateAverage(ArrayList<Expense> expenses){
        if(expenses.isEmpty()){return 0;}
        
        return calculateTotal(expenses) / expenses.size();
    }

    public double calculateRemainingBudget(double budget,double spent){
            return budget - spent;
    }

    public int calculateExpenseCount(ArrayList<Expense> expenses){
            return expenses.size();
    }

    public String getTopCategory(ArrayList<Expense> expenses){
        HashMap<String,Integer> categoryCount = new HashMap<>();
        
        for(Expense e : expenses){
            String category = e.getCategory();
            categoryCount.put(
                category,
                categoryCount.getOrDefault(
                        category,
                        0
                ) + 1);
            }
        String topCategory = "N/A";
        int maxCount = 0;
        for(String category : categoryCount.keySet()){
            if(categoryCount.get(category)> maxCount){
                maxCount = categoryCount.get(category);
                topCategory = category;
            }
        }
        return topCategory;
    }

    public HashMap<String, Double> getCategoryWiseSpending(ArrayList<Expense> expenses){
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for(Expense e : expenses){
            String category = e.getCategory();

        categoryTotals.put(category,categoryTotals.getOrDefault(
            category,
            0.0) + e.getAmount()
        );}
        return categoryTotals;
    }
        
}
