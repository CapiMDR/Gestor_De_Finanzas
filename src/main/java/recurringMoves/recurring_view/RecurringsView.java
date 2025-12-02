package recurringMoves.recurring_view;

import java.awt.Component;
import java.awt.Dimension;
import java.time.*;
import java.util.Date;
import java.util.TreeSet;
import javax.swing.*;

import recurringMoves.recurring_controller.RecurringsController;
import recurringMoves.recurring_model.RecurrenceType;
import recurringMoves.recurring_model.RecurringMove;
import recurringMoves.recurring_model.RecurringObserver;
import recurringMoves.recurring_model.RecurringsModel;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class RecurringsView extends javax.swing.JFrame implements RecurringObserver {
        private final RecurringsModel model;
        private final RecurringsController controller;

        public RecurringsView(RecurringsController controller, RecurringsModel model) {
                setTitle("Movimientos recurrentes");
                initComponents();
                this.model = model;
                this.controller = controller;
                this.model.addObserver(this);

                SpinnerDateModel dateModel = new SpinnerDateModel();
                recMoveDateSpinner.setModel(dateModel);
                recMoveDateSpinner.setEditor(new JSpinner.DateEditor(recMoveDateSpinner, "yyyy-MM-dd"));

                listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
                updateRecurringList(model.getRecurrings());
        }

        @Override
        public void observeRecurrings(TreeSet<RecurringMove> recMoves) {
                updateRecurringList(recMoves);
        }

        private void updateRecurringList(TreeSet<RecurringMove> recMoves) {
                listPanel.removeAll();
                for (RecurringMove r : recMoves) {
                        listPanel.add(createRecMoveRow(r));
                }
                listPanel.revalidate();
                listPanel.repaint();
        }

        private JPanel createRecMoveRow(RecurringMove recMove) {
                JPanel row = new JPanel();
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                JLabel label = new JLabel(
                                recMove.getConcept() + " - Siguiente fecha: "
                                                + recMove.getInitialDate().format(formatter));
                label.setAlignmentY(Component.CENTER_ALIGNMENT);

                JButton editBtn = new JButton("🖊");
                JButton deleteBtn = new JButton("🗑");

                editBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
                deleteBtn.setAlignmentY(Component.CENTER_ALIGNMENT);

                editBtn.addActionListener(e -> {
                        controller.onEditRequest(recMove);

                });

                deleteBtn.addActionListener(e -> {
                        controller.handleRecurringDeletion(recMove);
                });

                // Panel para los botones de cada recordatorio
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
                buttonPanel.setOpaque(false);
                buttonPanel.add(editBtn);
                buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                buttonPanel.add(deleteBtn);
                buttonPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

                row.add(label);
                // Empuja los botones a la derecha
                row.add(Box.createHorizontalGlue());
                row.add(buttonPanel);

                return row;
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel2 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                listPanel = new javax.swing.JPanel();
                jPanel1 = new javax.swing.JPanel();
                jPanel3 = new javax.swing.JPanel();
                jPanel4 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jLabel5 = new javax.swing.JLabel();
                jLabel4 = new javax.swing.JLabel();
                jPanel5 = new javax.swing.JPanel();
                recMoveConceptTXT = new javax.swing.JTextField();
                recMoveDescTXT = new javax.swing.JTextField();
                recMoveDateSpinner = new javax.swing.JSpinner();
                recMoveAmountTXT = new javax.swing.JTextField();
                recMoveCombo = new javax.swing.JComboBox<>();
                jLabel3 = new javax.swing.JLabel();
                jLabel7 = new javax.swing.JLabel();
                jPanel7 = new javax.swing.JPanel();
                addBTN = new javax.swing.JButton();

                jPanel2.setBackground(new java.awt.Color(246, 107, 14));

                jLabel1.setBackground(new java.awt.Color(89, 137, 169));
                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(255, 255, 255));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Movimientos recurrentes");

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                jPanel6Layout.setVerticalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));

                javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
                listPanel.setLayout(listPanelLayout);
                listPanelLayout.setHorizontalGroup(
                                listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 313, Short.MAX_VALUE));
                listPanelLayout.setVerticalGroup(
                                listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 352, Short.MAX_VALUE));

                jScrollPane1.setViewportView(listPanel);

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                325,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                .addComponent(jScrollPane1))
                                                                .addContainerGap()));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGap(26, 26, 26)
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                67,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                289,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jPanel1.setBackground(new java.awt.Color(17, 43, 60));

                jPanel3.setBackground(new java.awt.Color(255, 255, 255));
                jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

                jPanel4.setBackground(new java.awt.Color(255, 255, 255));

                jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel2.setForeground(new java.awt.Color(17, 43, 60));
                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel2.setText("Concepto:");

                jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel5.setForeground(new java.awt.Color(17, 43, 60));
                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel5.setText("Frecuencia:");

                jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel4.setForeground(new java.awt.Color(17, 43, 60));
                jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel4.setText("Descripción:");

                jPanel5.setBackground(new java.awt.Color(255, 255, 255));

                recMoveConceptTXT.setBackground(new java.awt.Color(210, 210, 210));

                recMoveDescTXT.setBackground(new java.awt.Color(210, 210, 210));

                recMoveAmountTXT.setBackground(new java.awt.Color(210, 210, 210));

                recMoveCombo.setModel(new DefaultComboBoxModel<>(RecurrenceType.values()));

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(16, Short.MAX_VALUE)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(recMoveCombo,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(recMoveDateSpinner,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel5Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addComponent(recMoveConceptTXT,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                258,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(recMoveDescTXT,
                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                258,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(recMoveAmountTXT,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                130,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap()));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(recMoveConceptTXT,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(recMoveAmountTXT,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(recMoveDateSpinner,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(33, 33, 33)
                                                                .addComponent(recMoveCombo,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(24, 24, 24)
                                                                .addComponent(recMoveDescTXT,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                59,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel3.setForeground(new java.awt.Color(17, 43, 60));
                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel3.setText("Monto:");

                jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel7.setForeground(new java.awt.Color(17, 43, 60));
                jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                jLabel7.setText("Fecha inicial:");

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addContainerGap(21, Short.MAX_VALUE)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                false)
                                                                                                .addComponent(jLabel2,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                123,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(jLabel3,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                123,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(jLabel7,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                123,
                                                                                                                Short.MAX_VALUE))
                                                                                .addComponent(jLabel4,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                123,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(jLabel5,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                123,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(27, 27, 27)
                                                                .addComponent(jPanel5,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(jPanel5,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel2,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                45,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jLabel3,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                45,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(jLabel7,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                45,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jLabel5,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                45,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(jLabel4,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                45,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 13, Short.MAX_VALUE)))
                                                                .addGap(63, 63, 63)));

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(16, 16, 16)));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(59, Short.MAX_VALUE)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(37, 37, 37)));

                jPanel7.setBackground(new java.awt.Color(89, 137, 169));

                addBTN.setBackground(new java.awt.Color(246, 107, 14));
                addBTN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                addBTN.setForeground(new java.awt.Color(255, 255, 255));
                addBTN.setText("Agregar nuevo movimiento");
                addBTN.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
                addBTN.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                addBTNActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(154, 154, 154)
                                                                .addComponent(addBTN,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                212,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel7Layout.setVerticalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(addBTN,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                48,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(33, Short.MAX_VALUE)));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(24, 24, 24)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(37, Short.MAX_VALUE))
                                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addGap(35, 35, 35)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                22,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jPanel7,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void addBTNActionPerformed(java.awt.event.ActionEvent evt) {
                String concept = recMoveConceptTXT.getText();
                String description = recMoveDescTXT.getText();
                Date date = (Date) recMoveDateSpinner.getValue();
                LocalDate localDate = date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                LocalTime now = LocalTime.now();
                LocalDateTime initialDate = LocalDateTime.of(localDate, now);
                BigDecimal amount;
                try {
                        amount = new BigDecimal(recMoveAmountTXT.getText());
                } catch (NumberFormatException e) {
                        amount = BigDecimal.ZERO;
                }
                RecurrenceType recurrence = (RecurrenceType) recMoveCombo.getSelectedItem();

                controller.handleRecurringAddition(concept, amount, description, initialDate, recurrence);
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton addBTN;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JPanel listPanel;
        private javax.swing.JTextField recMoveAmountTXT;
        private javax.swing.JComboBox<RecurrenceType> recMoveCombo;
        private javax.swing.JTextField recMoveConceptTXT;
        private javax.swing.JSpinner recMoveDateSpinner;
        private javax.swing.JTextField recMoveDescTXT;
        // End of variables declaration//GEN-END:variables
}
