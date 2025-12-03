package movements.movement_view;

public class MovementCategoriesView extends javax.swing.JFrame {

        public MovementCategoriesView() {
                initComponents();
        }

        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel5 = new javax.swing.JPanel();
                lblTittle = new javax.swing.JLabel();
                jPanel4 = new javax.swing.JPanel();
                lblNewNameCateogory = new javax.swing.JLabel();
                lblNewTypeCateogory = new javax.swing.JLabel();
                txtNewNameCateogory = new javax.swing.JTextField();
                jScrollPane1 = new javax.swing.JScrollPane();
                listCategoryType = new javax.swing.JList<>();
                btnConfirm = new javax.swing.JButton();
                btnDeleteCategory = new javax.swing.JButton();
                jScrollPane3 = new javax.swing.JScrollPane();
                listCategories = new javax.swing.JList<>();

                jPanel1.setBackground(new java.awt.Color(245, 144, 68));

                jPanel5.setBackground(new java.awt.Color(246, 107, 14));

                lblTittle.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
                lblTittle.setForeground(new java.awt.Color(255, 255, 255));
                lblTittle.setText("EDITAR CATEGORIAS");


                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(111, 111, 111)
                                                                .addComponent(lblTittle)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addContainerGap(13, Short.MAX_VALUE)));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel5Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(lblTittle,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                44,
                                                                                                Short.MAX_VALUE))
                                                                                .addContainerGap(14, Short.MAX_VALUE)));

                jPanel4.setBackground(new java.awt.Color(239, 239, 239));

                lblNewNameCateogory.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblNewNameCateogory.setForeground(new java.awt.Color(17, 43, 60));
                lblNewNameCateogory.setText("Nombre:");

                lblNewTypeCateogory.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblNewTypeCateogory.setForeground(new java.awt.Color(17, 43, 60));
                lblNewTypeCateogory.setText("Tipo:");

                txtNewNameCateogory.setBackground(new java.awt.Color(210, 210, 210));
                txtNewNameCateogory.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
                txtNewNameCateogory.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                txtNewNameCateogoryActionPerformed(evt);
                        }
                });

                listCategoryType.setBackground(new java.awt.Color(89, 137, 169));
                listCategoryType.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
                listCategoryType.setForeground(new java.awt.Color(255, 255, 255));
                listCategoryType.setModel(new javax.swing.AbstractListModel<String>() {
                        String[] strings = { "INCOME", "EXPENSE" };

                        public int getSize() {
                                return strings.length;
                        }

                        public String getElementAt(int i) {
                                return strings[i];
                        }
                });
                listCategoryType.setSelectionBackground(new java.awt.Color(210, 210, 210));
                listCategoryType.setSelectionForeground(new java.awt.Color(89, 137, 169));
                jScrollPane1.setViewportView(listCategoryType);

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lblNewNameCateogory,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                109,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(lblNewTypeCateogory,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                74,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(183, 183, 183))
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                100,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(txtNewNameCateogory,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                177,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lblNewNameCateogory,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtNewNameCateogory,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(lblNewTypeCateogory,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                52,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(26, 26, 26)));

                btnConfirm.setBackground(new java.awt.Color(32, 83, 117));
                btnConfirm.setFont(new java.awt.Font("Inter", 1, 18)); // NOI18N
                btnConfirm.setForeground(new java.awt.Color(255, 255, 255));
                btnConfirm.setText("Agregar");
                btnConfirm.setBorder(null);
                btnConfirm.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnConfirmActionPerformed(evt);
                        }
                });

                btnDeleteCategory.setBackground(new java.awt.Color(210, 210, 210));
                btnDeleteCategory.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
                btnDeleteCategory.setForeground(new java.awt.Color(255, 255, 255));
                btnDeleteCategory
                                .setIcon(new javax.swing.ImageIcon(
                                                getClass().getResource("../../images/image 25.png"))); // NOI18N
                btnDeleteCategory.setBorder(null);

                listCategories.setBackground(new java.awt.Color(179, 65, 65));
                listCategories.setBorder(null);
                listCategories.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                listCategories.setForeground(new java.awt.Color(255, 255, 255));
                listCategories.setModel(new javax.swing.AbstractListModel<String>() {
                        String[] strings = { "lista" };

                        public int getSize() {
                                return strings.length;
                        }

                        public String getElementAt(int i) {
                                return strings[i];
                        }
                });
                listCategories.setSelectionBackground(new java.awt.Color(89, 137, 169));
                listCategories.setSelectionForeground(new java.awt.Color(255, 255, 255));
                jScrollPane3.setViewportView(listCategories);

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(24, 24, 24)
                                                                                                .addComponent(jPanel4,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                190,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(67, 67, 67)
                                                                                                .addComponent(btnConfirm,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                98,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(jScrollPane3,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                271,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addContainerGap())
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                .addComponent(btnDeleteCategory)
                                                                                                                .addGap(108, 108,
                                                                                                                                108)))));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jPanel5,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jScrollPane3,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                326,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(btnDeleteCategory)
                                                                                                .addGap(12, 12, 12))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jPanel4,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(btnConfirm,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                52,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap()));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE));

                pack();
        }// </editor-fold>

        private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {
                
        }


        private void txtNewNameCateogoryActionPerformed(java.awt.event.ActionEvent evt) {
        }

        public void clearFields() {
                getTxtNewNameCateogory().setText("");
                getListCategoryType().clearSelection();
        }

        // Variables declaration - do not modify
        private javax.swing.JButton btnConfirm;
        private javax.swing.JButton btnDeleteCategory;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JLabel lblNewNameCateogory;
        private javax.swing.JLabel lblNewTypeCateogory;
        private javax.swing.JLabel lblTittle;
        private javax.swing.JList<String> listCategories;
        private javax.swing.JList<String> listCategoryType;
        private javax.swing.JTextField txtNewNameCateogory;
        // End of variables declaration

        public javax.swing.JButton getBtnConfirm() {
                return btnConfirm;
        }

        public javax.swing.JButton getBtnDeleteCategory() {
                return btnDeleteCategory;
        }

        public javax.swing.JTextField getTxtNewNameCateogory() {
                return txtNewNameCateogory;
        }

        public javax.swing.JList<String> getListCategoryType() {
                return listCategoryType;
        }

        // JList para mostrar las categorías existentes
        public javax.swing.JList<String> getListCategories() {
                return listCategories;
        }

}
