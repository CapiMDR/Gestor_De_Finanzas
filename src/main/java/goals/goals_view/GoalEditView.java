package goals.goals_view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.*;

/**
 * Modal dialog for Creating or Editing a Goal.
 *
 * @author Jose Pablo Martinez
 */

public class GoalEditView extends JDialog {

    private final JTextField txtName;
    private final JTextField txtTargetAmount;
    private final JTextField txtDescription;
    private final JButton btnSave;
    private final JButton btnCancel;

    public GoalEditView() {
        this.setSize(400, 300);
        this.setLayout(new GridLayout(5, 2, 10, 10));

        txtName = new JTextField();
        txtTargetAmount = new JTextField();
        txtDescription = new JTextField();
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");

        add(new JLabel("  Nombre:"));
        add(txtName);
        add(new JLabel("  Monto Objetivo:"));
        add(txtTargetAmount);
        add(new JLabel("  Descripción:"));
        add(txtDescription);
        add(new JLabel(""));
        add(new JLabel(""));
        add(btnCancel);
        add(btnSave);

        btnCancel.addActionListener(e -> dispose());
    }

    // Clear fields for a new entry
    public void clearFields() {
        txtName.setText("");
        txtTargetAmount.setText("");
        txtDescription.setText("");
        setTitle("Agregar Nueva Meta");
    }

    // Populates fields for editing
    public void populateFields(String name, BigDecimal targetAmount, String description) {
        txtName.setText(name);
        txtTargetAmount.setText(targetAmount.toString());
        txtDescription.setText(description);
        setTitle("Editar Meta: " + name);
    }

    public void addSaveListener(ActionListener listener) {
        for (ActionListener al : btnSave.getActionListeners()) {
            btnSave.removeActionListener(al);
        }
        btnSave.addActionListener(listener);
    }

    public String getNameInput() {
        return txtName.getText().trim();
    }

    public String getDescriptionInput() {
        return txtDescription.getText().trim();
    }

    public BigDecimal getTargetInput() {
        try {
            return new BigDecimal(txtTargetAmount.getText().trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public void closeDialog() {
        this.dispose();
    }

    public void showDialog() {
        this.setVisible(true);
    }
}
