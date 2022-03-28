import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class GUI {

    private JPanel panel1;
    private JTable table;

    private JProgressBar progressBar;
    private JButton calcBtn;
    private JTextField numInput;
    private JScrollPane scrollPane;

    private DefaultTableModel tableModel;

    public GUI() {
        calcBtn.addActionListener(action -> {
            progressBar.setValue(0);

            // disable Button
            calcBtn.setEnabled(false);

            // clear table
            tableModel.setRowCount(0);

            try {
                final long finalNumber = Long.parseLong(numInput.getText());
                startThread(finalNumber);
            }
            catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(this.panel1, "Die Eingabe ist ungültig");
                calcBtn.setEnabled(true);
            }
        });
    }

    private void startThread(Long finalNumber) {
        SwingWorker sw = new SwingWorker() {
            @Override
            protected ArrayList<Long> doInBackground() throws Exception {
                ArrayList<Long> list = new ArrayList<>();

                for (long i = 1; i <= Math.sqrt(finalNumber); i++) {
                    float progress = (float) i / (float) Math.sqrt(finalNumber) * 100;
                    progressBar.setValue((int) progress);

                    if (finalNumber % i == 0) {
                        list.add(i);

                        if (finalNumber / i != i)
                            list.add(finalNumber / i);
                    }
                }

                return list;
            }

            @Override
            protected void done() {
                ArrayList list = null;
                try {
                    list = (ArrayList) get();

                    Collections.sort(list);

                    for (int i = 0; i <= list.size() - 1; i++) {
                        Vector<String> row = new Vector<>();
                        row.add(String.valueOf(list.get(i)));
                        tableModel.addRow(row);
                    }

                    progressBar.setValue(100);
                    calcBtn.setEnabled(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        sw.execute();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Teilbare Zahlen");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Wert");
        table = new JTable(this.tableModel);
    }
}