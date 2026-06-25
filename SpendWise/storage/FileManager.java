package storage;
import model.Expense;
import java.io.*;
import java.util.ArrayList;

public class FileManager {
    public static final String FILE_NAME = "expenses.csv";

    public static void saveExpenses(ArrayList<Expense> expenses){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))){
            for (Expense e : expenses){
                writer.write(
                    e.getId() + "," +
                    e.getDate() + "," +
                    e.getCategory() + "," +
                    e.getAmount() + "," +
                    e.getDescription()
                );

                writer.newLine();
            }
            System.out.println("Expenses saved successfully!");
        }

        catch(IOException e){
            System.out.println("Error saving expenses....!!!!!");
        }
    }

    public static ArrayList<Expense> loadExpenses(){
        ArrayList<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_NAME);

        if(!file.exists()){
            return expenses;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;

            while ((line = reader.readLine()) != null){
                String[] data = line.split(",");

                expenses.add(new Expense(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    Double.parseDouble(data[3]),
                    data[4]
                ));
            }
        }

        catch(IOException e){
            System.out.println("Error loading expenses...!!!!");
        }
        return expenses;

    }

    public static double loadBudget(){
        try(
            BufferedReader br = new BufferedReader(new FileReader("budget.txt"))){
                String line = br.readLine();
                if(line != null){
                    return Double.parseDouble(line);
                }
            }
        catch(Exception e){e.printStackTrace();}
        
        return 10000;
    }

    //saving budget
    public static void saveBudget(double budget){
        try(
        BufferedWriter bw =
            new BufferedWriter(new FileWriter("budget.txt"))){
                bw.write(String.valueOf(budget));
            }
        catch(Exception e){e.printStackTrace();}
    }
    
}
