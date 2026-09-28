import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

/**
 * BankBalanceApp
 *
 * A simple Swing GUI that lets a user enter a starting balance,
 * then deposit or withdraw funds and view the updated balance.
 * The final balance is shown one last time before the program exits.
 */
public class BankBalanceApp extends JFrame implements ActionListener {

    private double balance;
    private final JLabel balanceLabel;
    private final JTextField amountField;
    private final JButton depositButton;
    private final JButton withdrawButton;
    private final JButton exitButton;
    private final NumberFormat currency = NumberFormat.getCurrencyInstance();

    public BankBalanceApp(double startingBalance) {
        super("Bank Balance Application");
        balance = startingBalance;

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(360, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        balanceLabel = new JLabel("Balance: " + currency.format(balance), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel inputPanel = new JPanel(new FlowLayout());
        amountField = new JTextField(10);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        exitButton = new JButton("Exit");

        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        exitButton.addActionListener(this);

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(exitButton);

        panel.add(balanceLabel);
        panel.add(inputPanel);
        panel.add(buttonPanel);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == exitButton) {
            JOptionPane.showMessageDialog(this,
                    "Final balance: " + currency.format(balance),
                    "Goodbye",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount < 0) {
                throw new NumberFormatException("Negative amount");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid, non-negative amount.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (source == depositButton) {
            balance += amount;
        } else if (source == withdrawButton) {
            if (amount > balance) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient funds for this withdrawal.",
                        "Transaction Denied",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            balance -= amount;
        }

        balanceLabel.setText("Balance: " + currency.format(balance));
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            double startingBalance = 0.0;
            boolean validInput = false;

            while (!validInput) {
                String input = JOptionPane.showInputDialog(
                        null,
                        "Enter your starting bank balance:",
                        "Account Setup",
                        JOptionPane.PLAIN_MESSAGE);

                if (input == null) {
                    System.exit(0);
                }

                try {
                    startingBalance = Double.parseDouble(input.trim());
                    if (startingBalance < 0) {
                        throw new NumberFormatException("Negative balance");
                    }
                    validInput = true;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid, non-negative number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            BankBalanceApp app = new BankBalanceApp(startingBalance);
            app.setVisible(true);
        });
    }
}
