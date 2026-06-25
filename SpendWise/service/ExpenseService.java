package service;
import model.Expense;
import java.util.ArrayList;

public class ExpenseService {
    private ArrayList<Expense> expenses;

    public ExpenseService(){
        expenses = new ArrayList<>();
    }

    //auto-id generator
    public int generateNextId(){
        int maxId = 0;
        for(Expense expense : expenses){
            if(expense.getId() > maxId){
                maxId = expense.getId();
            }
        }
        return maxId + 1;
    }

    public void addExpense(Expense expense){
        expenses.add(expense);
    }

    public void deleteExpense(int id){
        expenses.removeIf(expense -> expense.getId() == id);
    }

    public void updateExpense(
        int id,
        String date,
        String category,
        double amount,
        String description){

    for(Expense e : expenses){

        if(e.getId() == id){

            e.setDate(date);
            e.setCategory(category);
            e.setAmount(amount);
            e.setDescription(description);

            break;
        }
    }
}

    public ArrayList<Expense> getAllExpenses(){
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses){
        this.expenses = expenses;
    }

    //search method
    public ArrayList<Expense> searchByCategory(
        String category){

    ArrayList<Expense> results =
            new ArrayList<>();

    for(Expense e : expenses){

        if(e.getCategory()
                .equalsIgnoreCase(category)){

            results.add(e);
        }
    }

    return results;
}
}
