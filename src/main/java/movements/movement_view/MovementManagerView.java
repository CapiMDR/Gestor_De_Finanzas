package movements.movement_view;

public class MovementManagerView extends javax.swing.JFrame {
        public MovementManagerView() {
                initComponents();
        }

        private void initComponents() {

                jPanel2 = new javax.swing.JPanel();
                jPanel3 = new javax.swing.JPanel();
                btnAddIncome = new javax.swing.JButton();
                jPanel4 = new javax.swing.JPanel();
                lblAmontIncome = new javax.swing.JLabel();
                lblCategoryIncome = new javax.swing.JLabel();
                lblDateIncome = new javax.swing.JLabel();
                lblAccountIncome = new javax.swing.JLabel();
                txtAmountIncome = new javax.swing.JTextField();
                jScrollPane1 = new javax.swing.JScrollPane();
                listCategoriesIncome = new javax.swing.JList<>();
                dateIncome = new com.toedter.calendar.JDateChooser();
                txtAccountIncome = new javax.swing.JTextField();
                lblDescriptionIncome = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                txtDescriptionIncome = new javax.swing.JTextArea();
                btnAddCategoryIncome = new javax.swing.JButton();
                jPanel5 = new javax.swing.JPanel();
                jPanel7 = new javax.swing.JPanel();
                btnAddExpense = new javax.swing.JButton();
                jPanel8 = new javax.swing.JPanel();
                lblAmountExpense = new javax.swing.JLabel();
                lblTypeAccount1 = new javax.swing.JLabel();
                lblDateExpese = new javax.swing.JLabel();
                lblAccountExpense = new javax.swing.JLabel();
                txtAmountExpense = new javax.swing.JTextField();
                jScrollPane3 = new javax.swing.JScrollPane();
                listCategoriesExpense = new javax.swing.JList<>();
                dateExpense = new com.toedter.calendar.JDateChooser();
                txtAccountExpense = new javax.swing.JTextField();
                lblDescripctionExpense = new javax.swing.JLabel();
                jScrollPane4 = new javax.swing.JScrollPane();
                txtDescriptionExpense = new javax.swing.JTextArea();
                btnAddCategoryExpense = new javax.swing.JButton();
                jPanel6 = new javax.swing.JPanel();
                lblTittle = new javax.swing.JLabel();

                jPanel2.setBackground(new java.awt.Color(245, 144, 68));
                jPanel2.setForeground(new java.awt.Color(246, 107, 14));

                jPanel3.setBackground(new java.awt.Color(255, 177, 119));

                btnAddIncome.setBackground(new java.awt.Color(32, 83, 117));
                btnAddIncome.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
                btnAddIncome.setForeground(new java.awt.Color(255, 255, 255));
                btnAddIncome.setText("Agregar Ingreso");
                btnAddIncome.setBorder(null);
                btnAddIncome.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnAddIncomeActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(52, 52, 52)
                                                                .addComponent(btnAddIncome,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                394,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(btnAddIncome,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                88,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(27, Short.MAX_VALUE)));

                jPanel4.setBackground(new java.awt.Color(239, 239, 239));

                lblAmontIncome.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblAmontIncome.setForeground(new java.awt.Color(17, 43, 60));
                lblAmontIncome.setText("Monto: $");

                lblCategoryIncome.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblCategoryIncome.setForeground(new java.awt.Color(17, 43, 60));
                lblCategoryIncome.setText("Categoría:");

                lblDateIncome.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblDateIncome.setForeground(new java.awt.Color(17, 43, 60));
                lblDateIncome.setText("Fecha:");

                lblAccountIncome.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblAccountIncome.setForeground(new java.awt.Color(17, 43, 60));
                lblAccountIncome.setText("Cuenta destino:");

                txtAmountIncome.setBackground(new java.awt.Color(210, 210, 210));
                txtAmountIncome.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N

                listCategoriesIncome.setBackground(new java.awt.Color(89, 137, 169));
                listCategoriesIncome.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
                listCategoriesIncome.setForeground(new java.awt.Color(255, 255, 255));
                listCategoriesIncome.setModel(new javax.swing.AbstractListModel<String>() {
                        String[] strings = { "Físico", "Digital" };

                        public int getSize() {
                                return strings.length;
                        }

                        public String getElementAt(int i) {
                                return strings[i];
                        }
                });
                listCategoriesIncome.setSelectionBackground(new java.awt.Color(210, 210, 210));
                listCategoriesIncome.setSelectionForeground(new java.awt.Color(89, 137, 169));
                jScrollPane1.setViewportView(listCategoriesIncome);

                dateIncome.setBackground(new java.awt.Color(210, 210, 210));

                txtAccountIncome.setEditable(false);
                txtAccountIncome.setBackground(new java.awt.Color(89, 137, 169));
                txtAccountIncome.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
                txtAccountIncome.setForeground(new java.awt.Color(255, 255, 255));
                txtAccountIncome.setText("Cuenta1");

                lblDescriptionIncome.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblDescriptionIncome.setForeground(new java.awt.Color(17, 43, 60));
                lblDescriptionIncome.setText("Descripción:");

                txtDescriptionIncome.setBackground(new java.awt.Color(210, 210, 210));
                txtDescriptionIncome.setColumns(20);
                txtDescriptionIncome.setRows(5);
                jScrollPane2.setViewportView(txtDescriptionIncome);

                btnAddCategoryIncome.setBackground(new java.awt.Color(245, 144, 68));
                btnAddCategoryIncome
                                .setIcon(new javax.swing.ImageIcon(
                                                getClass().getResource("/images/image 2.png"))); // NOI18N

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(23, 23, 23)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblAccountIncome)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txtAccountIncome))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDateIncome,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                109,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(dateIncome,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                180,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel4Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblCategoryIncome)
                                                                                                                .addComponent(lblAmontIncome,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                109,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addGroup(jPanel4Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(txtAmountIncome,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                195,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(jPanel4Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                222,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(btnAddCategoryIncome,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                40,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDescriptionIncome)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jScrollPane2,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                255,
                                                                                                                Short.MAX_VALUE)))
                                                                .addContainerGap()));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addGroup(jPanel4Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(lblAmontIncome,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                44,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(txtAmountIncome))
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(jPanel4Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblCategoryIncome,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                44,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jScrollPane1,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                152,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(57, 57, 57)
                                                                                                .addComponent(btnAddCategoryIncome,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                40,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                12,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(dateIncome,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(lblDateIncome,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lblAccountIncome,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(txtAccountIncome,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                42,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(31, 31, 31)
                                                                .addGroup(jPanel4Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel4Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDescriptionIncome,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                44,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                .addComponent(jScrollPane2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                101,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(32, Short.MAX_VALUE)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(30, 30, 30)));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap(19, Short.MAX_VALUE)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                jPanel5.setBackground(new java.awt.Color(32, 83, 117));
                jPanel5.setForeground(new java.awt.Color(246, 107, 14));

                jPanel7.setBackground(new java.awt.Color(89, 137, 169));

                btnAddExpense.setBackground(new java.awt.Color(179, 65, 65));
                btnAddExpense.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
                btnAddExpense.setForeground(new java.awt.Color(255, 255, 255));
                btnAddExpense.setText("Agregar Egreso");
                btnAddExpense.setBorder(null);
                btnAddExpense.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                btnAddExpenseActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(52, 52, 52)
                                                                .addComponent(btnAddExpense,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                394,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel7Layout.setVerticalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(btnAddExpense,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                88,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(27, Short.MAX_VALUE)));

                jPanel8.setBackground(new java.awt.Color(239, 239, 239));

                lblAmountExpense.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblAmountExpense.setForeground(new java.awt.Color(17, 43, 60));
                lblAmountExpense.setText("Monto: $");

                lblTypeAccount1.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblTypeAccount1.setForeground(new java.awt.Color(17, 43, 60));
                lblTypeAccount1.setText("Categoría:");

                lblDateExpese.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblDateExpese.setForeground(new java.awt.Color(17, 43, 60));
                lblDateExpese.setText("Fecha:");

                lblAccountExpense.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblAccountExpense.setForeground(new java.awt.Color(17, 43, 60));
                lblAccountExpense.setText("Cuenta origen:");

                txtAmountExpense.setBackground(new java.awt.Color(210, 210, 210));
                txtAmountExpense.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N

                listCategoriesExpense.setBackground(new java.awt.Color(255, 177, 119));
                listCategoriesExpense.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
                listCategoriesExpense.setForeground(new java.awt.Color(255, 255, 255));
                listCategoriesExpense.setModel(new javax.swing.AbstractListModel<String>() {
                        String[] strings = { "Físico", "Digital" };

                        public int getSize() {
                                return strings.length;
                        }

                        public String getElementAt(int i) {
                                return strings[i];
                        }
                });
                listCategoriesExpense.setSelectionBackground(new java.awt.Color(210, 210, 210));
                listCategoriesExpense.setSelectionForeground(new java.awt.Color(89, 137, 169));
                jScrollPane3.setViewportView(listCategoriesExpense);

                dateExpense.setBackground(new java.awt.Color(210, 210, 210));

                txtAccountExpense.setEditable(false);
                txtAccountExpense.setBackground(new java.awt.Color(255, 177, 119));
                txtAccountExpense.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
                txtAccountExpense.setForeground(new java.awt.Color(255, 255, 255));
                txtAccountExpense.setText("Cuenta1");

                lblDescripctionExpense.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
                lblDescripctionExpense.setForeground(new java.awt.Color(17, 43, 60));
                lblDescripctionExpense.setText("Descripción:");

                txtDescriptionExpense.setBackground(new java.awt.Color(210, 210, 210));
                txtDescriptionExpense.setColumns(20);
                txtDescriptionExpense.setRows(5);
                jScrollPane4.setViewportView(txtDescriptionExpense);

                btnAddCategoryExpense.setBackground(new java.awt.Color(89, 137, 169));
                btnAddCategoryExpense
                                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/image 2.png"))); // NOI18N

                javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                jPanel8.setLayout(jPanel8Layout);
                jPanel8Layout.setHorizontalGroup(
                                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel8Layout.createSequentialGroup()
                                                                .addGap(23, 23, 23)
                                                                .addGroup(jPanel8Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblAccountExpense)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(txtAccountExpense))
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDateExpese,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                109,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(dateExpense,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                180,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel8Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblTypeAccount1)
                                                                                                                .addComponent(lblAmountExpense,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                109,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addGroup(jPanel8Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(txtAmountExpense,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                195,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(jPanel8Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jScrollPane3,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                222,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(btnAddCategoryExpense,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                40,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDescripctionExpense)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(jScrollPane4,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                255,
                                                                                                                Short.MAX_VALUE)))
                                                                .addContainerGap()));
                jPanel8Layout.setVerticalGroup(
                                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel8Layout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addGroup(jPanel8Layout
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                .addComponent(lblAmountExpense,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                44,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(txtAmountExpense))
                                                                .addGroup(jPanel8Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addGroup(jPanel8Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(lblTypeAccount1,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                44,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(jScrollPane3,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                152,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(59, 59, 59)
                                                                                                .addComponent(btnAddCategoryExpense,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                40,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(jPanel8Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                12,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addComponent(dateExpense,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(lblDateExpese,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(jPanel8Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(lblAccountExpense,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(txtAccountExpense,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                42,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(31, 31, 31)
                                                                .addGroup(jPanel8Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel8Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(lblDescripctionExpense,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                44,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                                                .addComponent(jScrollPane4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                101,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(30, 30, 30)
                                                                .addComponent(jPanel8,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(32, Short.MAX_VALUE)));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jPanel8,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jPanel7,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                jPanel6.setBackground(new java.awt.Color(246, 107, 14));

                lblTittle.setFont(new java.awt.Font("Inter", 1, 28)); // NOI18N
                lblTittle.setForeground(new java.awt.Color(255, 255, 255));
                lblTittle.setText("MOVIMIENTOS");

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(lblTittle)
                                                                .addGap(401, 401, 401)));
                jPanel6Layout.setVerticalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(lblTittle,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(11, Short.MAX_VALUE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jPanel5,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addComponent(jPanel6,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel5,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))));

                pack();
        }

        private void btnAddIncomeActionPerformed(java.awt.event.ActionEvent evt) {
        }

        private void btnAddExpenseActionPerformed(java.awt.event.ActionEvent evt) {
        }

        public void clearIncomeFields() {
                getTxtAmountIncome().setText("");
                getTxtDescriptionIncome().setText("");
                getListCategoriesIncome().clearSelection();

                getDateIncome().setDate(new java.util.Date());
        }

        public void clearExpenseFields() {
                getTxtAmountExpense().setText("");
                getTxtDescriptionExpense().setText("");
                getListCategoriesExpense().clearSelection();

                getDateExpense().setDate(new java.util.Date());
        }

        // Variables declaration - do not modify
        private javax.swing.JButton btnAddCategoryExpense;
        private javax.swing.JButton btnAddCategoryIncome;
        private javax.swing.JButton btnAddExpense;
        private javax.swing.JButton btnAddIncome;
        private com.toedter.calendar.JDateChooser dateExpense;
        private com.toedter.calendar.JDateChooser dateIncome;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JLabel lblAccountExpense;
        private javax.swing.JLabel lblAccountIncome;
        private javax.swing.JLabel lblAmontIncome;
        private javax.swing.JLabel lblAmountExpense;
        private javax.swing.JLabel lblCategoryIncome;
        private javax.swing.JLabel lblDateExpese;
        private javax.swing.JLabel lblDateIncome;
        private javax.swing.JLabel lblDescripctionExpense;
        private javax.swing.JLabel lblDescriptionIncome;
        private javax.swing.JLabel lblTittle;
        private javax.swing.JLabel lblTypeAccount1;
        private javax.swing.JList<String> listCategoriesExpense;
        private javax.swing.JList<String> listCategoriesIncome;
        private javax.swing.JTextField txtAccountExpense;
        private javax.swing.JTextField txtAccountIncome;
        private javax.swing.JTextField txtAmountExpense;
        private javax.swing.JTextField txtAmountIncome;
        private javax.swing.JTextArea txtDescriptionExpense;
        private javax.swing.JTextArea txtDescriptionIncome;
        // End of variables declaration

        public javax.swing.JButton getBtnAddIncome() {
                return btnAddIncome;
        }

        public javax.swing.JButton getBtnAddExpense() {
                return btnAddExpense;
        }

        public javax.swing.JTextField getTxtAmountIncome() {
                return txtAmountIncome;
        }

        public javax.swing.JTextArea getTxtDescriptionIncome() {
                return txtDescriptionIncome;
        }

        public com.toedter.calendar.JDateChooser getDateIncome() {
                return dateIncome;
        }

        public javax.swing.JList<String> getListCategoriesIncome() {
                return listCategoriesIncome;
        }

        public javax.swing.JTextField getTxtAccountIncome() {
                return txtAccountIncome;
        }

        public javax.swing.JTextField getTxtAmountExpense() {
                return txtAmountExpense;
        }

        public javax.swing.JTextArea getTxtDescriptionExpense() {
                return txtDescriptionExpense;
        }

        public com.toedter.calendar.JDateChooser getDateExpense() {
                return dateExpense;
        }

        public javax.swing.JList<String> getListCategoriesExpense() {
                return listCategoriesExpense;
        }

        public javax.swing.JTextField getTxtAccountExpense() {
                return txtAccountExpense;
        }

        public javax.swing.JButton getBtnAddCategoryExpense() {
                return btnAddCategoryExpense;
        }

        public javax.swing.JButton getBtnAddCategoryIncome() {
                return btnAddCategoryIncome;
        }
}