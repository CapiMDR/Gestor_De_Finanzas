package reminder_view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import reminder_model.Reminder;

public class RemindersEditorView extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private Reminder resultReminder = null;

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RemindersEditorView(Frame parent, Reminder reminder) {
        super(parent, "Editar recordatorio", true);
        setLayout(new BorderLayout(10, 10));

        // Input de texto
        nameField = new JTextField(reminder.getName(), 20);

        descriptionArea = new JTextArea(reminder.getMessage(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        dateField = new JTextField(reminder.getDate().format(FORMAT), 20);

        // Panel principal
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        form.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        c.gridy = 0;
        form.add(nameField, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(new JLabel("Mensaje:"), c);
        c.gridx = 1;
        c.gridy = 1;
        form.add(new JScrollPane(descriptionArea), c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(new JLabel("Fecha (aaaa-MM-dd HH:mm):"), c);
        c.gridx = 1;
        c.gridy = 2;
        form.add(dateField, c);

        add(form, BorderLayout.CENTER);

        // Botones
        JPanel buttons = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                Reminder r = new Reminder(
                        nameField.getText().trim(),
                        descriptionArea.getText().trim(),
                        LocalDateTime.parse(dateField.getText().trim(), FORMAT));
                resultReminder = r;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Use yyyy-MM-dd HH:mm",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> {
            resultReminder = null;
            dispose();
        });

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        add(buttons, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    public Reminder getEditedReminder() {
        return resultReminder;
    }
}