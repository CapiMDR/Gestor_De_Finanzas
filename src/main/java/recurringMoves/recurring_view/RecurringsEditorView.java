package recurringMoves.recurring_view;

import javax.swing.*;

import movements.movement_model.CategoryManager;
import movements.movement_model.MovementCategory;
import recurringMoves.recurring_model.RecurrenceType;
import recurringMoves.recurring_model.RecurringMove;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecurringsEditorView extends JDialog {
    private JTextField conceptField;
    private JTextArea descriptionArea;
    private JTextField amountField;
    private JComboBox<RecurrenceType> recurrenceCombo;
    private JComboBox<String> movementTypeCombo;
    private CategoryManager categoryManager = new CategoryManager();

    private RecurringMove resultRecMove = null;

    public RecurringsEditorView(Frame parent, RecurringMove recMove) {
        super(parent, "Editar movimiento recurrente", true);
        setLayout(new BorderLayout(10, 10));

        conceptField = new JTextField(recMove.getConcept(), 20);
        descriptionArea = new JTextArea(recMove.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        amountField = new JTextField(recMove.getAmount().toPlainString(), 20);
        recurrenceCombo = new JComboBox<>(RecurrenceType.values());
        recurrenceCombo.setSelectedItem(recMove.getRecurrence());

        movementTypeCombo = new JComboBox<>(categoryManager.getCategories().keySet().toArray(new String[0]));
        movementTypeCombo.setSelectedItem(recMove.getCategory().getName());

        // Panel principal
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Concepto:"), c);
        c.gridx = 1;
        form.add(conceptField, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Descripción:"), c);
        c.gridx = 1;
        form.add(new JScrollPane(descriptionArea), c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Cantidad:"), c);
        c.gridx = 1;
        form.add(amountField, c);

        c.gridx = 0;
        c.gridy = 3;
        form.add(new JLabel("Frecuencia:"), c);
        c.gridx = 1;
        form.add(recurrenceCombo, c);

        c.gridx = 0;
        c.gridy = 4;
        form.add(new JLabel("Categoría:"), c);
        c.gridx = 1;
        form.add(movementTypeCombo, c);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel buttons = new JPanel();
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");

        saveBtn.addActionListener(e -> {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());

                RecurrenceType recurrence = (RecurrenceType) recurrenceCombo.getSelectedItem();

                String type = (String) movementTypeCombo.getSelectedItem();
                MovementCategory category = categoryManager.getCategoryByName(type);

                RecurringMove r = new RecurringMove(
                        conceptField.getText().trim(),
                        amount,
                        descriptionArea.getText().trim(),
                        LocalDateTime.now(),
                        recurrence,
                        category);

                resultRecMove = r;
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error en los datos. Verifica:\n- Cantidad válida\n- Campos vacíos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> {
            resultRecMove = null;
            dispose();
        });

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public RecurringMove getEditedRecMove() {
        return resultRecMove;
    }
}