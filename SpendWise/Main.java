import javax.swing.SwingUtilities;
import view.DashboardFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {DashboardFrame frame = new DashboardFrame();
        frame.setVisible(true);
    });
    }
}
