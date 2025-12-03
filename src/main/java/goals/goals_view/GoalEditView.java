package goals.goals_view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.*;

/**
 * Diálogo modal para crear o editar una Meta.
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
        
        Color bgColor = new Color(239, 239, 239);
        Color textColor = new Color(17, 43, 60);
        Color inputBg = new Color(210, 210, 210);
        Color btnSaveColor = new Color(246, 107, 14); // orange
        Color btnCancelColor = new Color(32, 83, 117); // blue
        
        Font labelFont = new Font("Inter", Font.BOLD, 14);
        Font inputFont = new Font("Inter", Font.PLAIN, 12);

        this.getContentPane().setBackground(bgColor);

        txtName = new JTextField();
        styleInput(txtName, inputFont, inputBg);

        txtTargetAmount = new JTextField();
        styleInput(txtTargetAmount, inputFont, inputBg);

        txtDescription = new JTextField();
        styleInput(txtDescription, inputFont, inputBg);

        btnSave = new JButton("Guardar");
        styleButton(btnSave, labelFont, btnSaveColor);

        btnCancel = new JButton("Cancelar");
        styleButton(btnCancel, labelFont, btnCancelColor);

        add(createStyledLabel("  Nombre:", labelFont, textColor));
        add(txtName);
        add(createStyledLabel("  Monto Objetivo:", labelFont, textColor));
        add(txtTargetAmount);
        add(createStyledLabel("  Descripción:", labelFont, textColor));
        add(txtDescription);
        add(new JLabel(""));
        add(new JLabel(""));
        add(btnCancel);
        add(btnSave);

        btnCancel.addActionListener(e -> dispose());
    }
    
    // Método para aplicar un estilo sin repetir código
    private void styleInput(JTextField field, Font font, Color bg) {
        field.setFont(font);
        field.setBackground(bg);
        field.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    }

    private void styleButton(JButton btn, Font font, Color bg) {
        btn.setFont(font);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
    }

    private JLabel createStyledLabel(String text, Font font, Color fg) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fg);
        return label;
    }

    // Limpia los campos para un nuevo registro
    public void clearFields() {
        txtName.setText("");
        txtTargetAmount.setText("");
        txtDescription.setText("");
        setTitle("Agregar Nueva Meta");
    }

    // Llena los campos para editar
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