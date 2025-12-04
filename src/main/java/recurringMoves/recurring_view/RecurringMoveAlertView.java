package recurringMoves.recurring_view;

import javax.swing.*;

import accounts.account_model.Account;
import recurringMoves.recurring_model.RecurringMove;

import java.awt.*;
import java.util.List;

public class RecurringMoveAlertView extends JDialog {

    private JComboBox<Account> accountCombo;
    private JButton btnApply;
    private JButton btnCancel;

    public RecurringMoveAlertView(JFrame parent, RecurringMove recMove, List<Account> accounts) {
        super(parent, recMove.getConcept() + " $" + recMove.getAmount() + " - Movimiento recurrente", true); // modal
                                                                                                             // popup

        setLayout(new BorderLayout(10, 10));

        // Description
        JLabel lblDesc = new JLabel(recMove.getDescription());
        lblDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblDesc, BorderLayout.NORTH);

        // Combo Box
        accountCombo = new JComboBox<>(accounts.toArray(new Account[0]));

        // Custom renderer so the combo shows account.getName()
        accountCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Account) {
                    setText(((Account) value).getName() + " Saldo: $" + ((Account) value).getCurrentBalance());
                }

                return this;
            }
        });

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(new JLabel("Selecciona cuenta:"));
        centerPanel.add(accountCombo);
        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        btnApply = new JButton("Aplicar movimiento");
        btnCancel = new JButton("Más tarde");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnApply);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(400, 200);
        setLocationRelativeTo(parent);
    }

    public Account getSelectedAccount() {
        return (Account) accountCombo.getSelectedItem();
    }

    public void setOnApply(Runnable action) {
        btnApply.addActionListener(e -> action.run());
    }

    public void setOnCancel(Runnable action) {
        btnCancel.addActionListener(e -> action.run());
    }
}