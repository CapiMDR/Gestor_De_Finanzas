package com.mycompany.construccion;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import accounts.account_model.Account;
import accounts.account_model.AccountManagerSubject;
import accounts.account_model.AccountObserver;
import filters.modelFilter.CategoriesModule;
import goals.GoalsModule;
import movements.movement_view.MovementsModule;
import recurringMoves.recurring_view.RecurringsModule;
import reminders.reminder_view.RemindersModule;
import reports.controllerReport.ReportController;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author villa
 */
public class FrmMain extends javax.swing.JFrame {
        private Account selectedAccount;

        /**
         * Creates new form FrmMain
         */
        public FrmMain(Account selectedAccount) {
                this.selectedAccount = selectedAccount;

                initComponents();

                // Goal
                ImageIcon iconGoal = (ImageIcon) goal.getIcon();
                Image imgGoal = iconGoal.getImage().getScaledInstance(
                                goal.getWidth(),
                                goal.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                goal.setIcon(new ImageIcon(imgGoal));

                // Subscription
                ImageIcon iconSubscription = (ImageIcon) subscription.getIcon();
                Image imgSubscription = iconSubscription.getImage().getScaledInstance(
                                subscription.getWidth(),
                                subscription.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                subscription.setIcon(new ImageIcon(imgSubscription));
                // Notification
                ImageIcon iconNotification = (ImageIcon) notification.getIcon();
                Image imgNotification = iconNotification.getImage().getScaledInstance(
                                notification.getWidth(),
                                notification.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                notification.setIcon(new ImageIcon(imgNotification));

                // Today
                ImageIcon iconToday = (ImageIcon) btnToday.getIcon();
                Image imgToday = iconToday.getImage().getScaledInstance(
                                btnToday.getWidth(),
                                btnToday.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                btnToday.setIcon(new ImageIcon(imgToday));

                // Week
                ImageIcon iconWeek = (ImageIcon) btnWeek.getIcon();
                Image imgWeek = iconWeek.getImage().getScaledInstance(
                                btnWeek.getWidth(),
                                btnWeek.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                btnWeek.setIcon(new ImageIcon(imgWeek));
              
                // Credit
                ImageIcon iconCredit = (ImageIcon) credit.getIcon();
                Image imgCredit = iconCredit.getImage().getScaledInstance(
                                credit.getWidth(),
                                credit.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                credit.setIcon(new ImageIcon(imgCredit));

                // Account
                ImageIcon iconAccount = (ImageIcon) categories.getIcon();
                Image imgAccount = iconAccount.getImage().getScaledInstance(
                                categories.getWidth(),
                                categories.getHeight(),
                                Image.SCALE_SMOOTH);
                // Escalar la imagen
                categories.setIcon(new ImageIcon(imgAccount));
        }

        public void updateCharts(DefaultPieDataset dataset) {
                crearGrafica(dataset);
        }

        public void updateBarChart(DefaultCategoryDataset dataset) {
                crearGraficaBarra(dataset);
        }

        private void crearGrafica(DefaultPieDataset dataset) {

                JFreeChart chart = ChartFactory.createPieChart(
                                "Movements",
                                dataset,
                                true, true, false);

                ChartPanel chartPanel = new ChartPanel(chart);

                chartPanel.setPreferredSize(pieChart.getSize());

                pieChart.removeAll();
                pieChart.setLayout(new BorderLayout());
                pieChart.add(chartPanel, BorderLayout.CENTER);

                pieChart.revalidate();
                pieChart.repaint();
        }

        private void crearGraficaBarra(DefaultCategoryDataset dataset) {

                JFreeChart chart = ChartFactory.createBarChart(
                                "Movements",
                                "Date",
                                "Amount",
                                dataset);

                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                CategoryAxis xAxis = plot.getDomainAxis();
                xAxis.setCategoryLabelPositions(
                                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2));

                ChartPanel chartPanel = new ChartPanel(chart);

                chartPanel.setPreferredSize(barChart.getSize());

                barChart.removeAll();
                barChart.setLayout(new BorderLayout());
                barChart.add(chartPanel, BorderLayout.CENTER);

                barChart.revalidate();
                barChart.repaint();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                addMovement = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jPanel4 = new javax.swing.JPanel();
                goal = new javax.swing.JLabel();
                jPanel5 = new javax.swing.JPanel();
                notification = new javax.swing.JLabel();
                jPanel6 = new javax.swing.JPanel();
                categories = new javax.swing.JLabel();
                jPanel7 = new javax.swing.JPanel();
                subscription = new javax.swing.JLabel();
                jPanel3 = new javax.swing.JPanel();
                pieChart = new javax.swing.JPanel();
                barChart = new javax.swing.JPanel();

                jPanel8 = new javax.swing.JPanel();
                labelName = new javax.swing.JLabel();
                jPanel9 = new javax.swing.JPanel();
                labelMoney = new javax.swing.JLabel();
                btnToday = new javax.swing.JLabel();
                btnWeek = new javax.swing.JLabel();
                credit = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setMinimumSize(new java.awt.Dimension(1012, 720));
                setSize(new java.awt.Dimension(1012, 720));

                jPanel1.setBackground(new java.awt.Color(255, 195, 137));

                jPanel2.setBackground(new java.awt.Color(48, 45, 76));

                addMovement.setBackground(new java.awt.Color(249, 147, 34));
                addMovement.setBorder(
                                javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                addMovement.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                addMovementMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                addMovementMouseEntered(evt);
                        }
                });

                jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(255, 255, 255));
                jLabel1.setText("Agregar Movimiento");

                javax.swing.GroupLayout addMovementLayout = new javax.swing.GroupLayout(addMovement);
                addMovement.setLayout(addMovementLayout);
                addMovementLayout.setHorizontalGroup(
                                addMovementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                addMovementLayout.createSequentialGroup()
                                                                                .addContainerGap(52, Short.MAX_VALUE)
                                                                                .addComponent(jLabel1)
                                                                                .addGap(37, 37, 37)));
                addMovementLayout.setVerticalGroup(
                                addMovementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                addMovementLayout.createSequentialGroup()
                                                                                .addContainerGap(21, Short.MAX_VALUE)
                                                                                .addComponent(jLabel1)
                                                                                .addGap(20, 20, 20)));

                jPanel4.setBackground(new java.awt.Color(253, 157, 102));

                goal.setForeground(new java.awt.Color(255, 153, 153));
                goal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/goal.png"))); // NOI18N
                goal.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                goal.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                goalMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                goalMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                                .addGap(20, 20, 20)
                                                                .addComponent(goal,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(24, Short.MAX_VALUE)));
                jPanel4Layout.setVerticalGroup(
                                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(goal,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                jPanel5.setBackground(new java.awt.Color(253, 157, 102));

                notification.setForeground(new java.awt.Color(255, 153, 153));
                notification.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/notification.png"))); // NOI18N
                notification.setVerticalAlignment(javax.swing.SwingConstants.TOP);
                notification.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                notificationMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                notificationMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
                jPanel5.setLayout(jPanel5Layout);
                jPanel5Layout.setHorizontalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(21, 21, 21)
                                                                .addComponent(notification,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(23, Short.MAX_VALUE)));
                jPanel5Layout.setVerticalGroup(
                                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(notification,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                jPanel6.setBackground(new java.awt.Color(253, 157, 102));

                categories.setForeground(new java.awt.Color(255, 153, 153));
                categories.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/movementsSearch.png"))); // NOI18N
                categories.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                categoriesMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                categoriesMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
                jPanel6.setLayout(jPanel6Layout);
                jPanel6Layout.setHorizontalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addGap(22, 22, 22)
                                                                .addComponent(categories,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(22, Short.MAX_VALUE)));
                jPanel6Layout.setVerticalGroup(
                                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(categories,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));

                jPanel7.setBackground(new java.awt.Color(253, 157, 102));

                subscription.setForeground(new java.awt.Color(255, 153, 153));
                subscription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/subscription.png"))); // NOI18N
                subscription.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                subscriptionMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                subscriptionMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
                jPanel7.setLayout(jPanel7Layout);
                jPanel7Layout.setHorizontalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel7Layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(subscription,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(18, Short.MAX_VALUE)));
                jPanel7Layout.setVerticalGroup(
                                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(subscription,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                44,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                jPanel3.setBackground(new java.awt.Color(53, 61, 136));

                javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                jPanel3Layout.setVerticalGroup(
                                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 11, Short.MAX_VALUE));

                javax.swing.GroupLayout pieChartLayout = new javax.swing.GroupLayout(pieChart);
                pieChart.setLayout(pieChartLayout);
                pieChartLayout.setHorizontalGroup(
                                pieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));
                pieChartLayout.setVerticalGroup(
                                pieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 251, Short.MAX_VALUE));

                javax.swing.GroupLayout barChartLayout = new javax.swing.GroupLayout(barChart);
                barChart.setLayout(barChartLayout);
                barChartLayout.setHorizontalGroup(
                                barChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 494, Short.MAX_VALUE));
                barChartLayout.setVerticalGroup(
                                barChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 251, Short.MAX_VALUE));

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout
                                                                .createSequentialGroup()
                                                                .addGap(69, 69, 69)
                                                                .addComponent(jPanel6,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                73, Short.MAX_VALUE)
                                                                .addComponent(jPanel7,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(54, 54, 54)
                                                                .addComponent(addMovement,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(46, 46, 46)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(77, 77, 77)
                                                                .addComponent(jPanel5,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(54, 54, 54))
                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(pieChart,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(barChart,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(pieChart,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(barChart,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(addMovement,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(26, 26, 26)
                                                                                                .addGroup(jPanel2Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(jPanel7,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGroup(jPanel2Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGroup(jPanel2Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(jPanel5,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addComponent(jPanel4,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                .addGap(34, 34, 34))))
                                                                                .addGroup(jPanel2Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGap(25, 25, 25)
                                                                                                .addComponent(jPanel6,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))));

                jPanel8.setBackground(new java.awt.Color(241, 241, 241));

                labelName.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
                labelName.setForeground(new java.awt.Color(77, 106, 196));
                labelName.setText("Cuenta 4");

                jPanel9.setBackground(new java.awt.Color(232, 231, 231));

                labelMoney.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
                labelMoney.setForeground(new java.awt.Color(102, 102, 102));
                labelMoney.setText("$400.00");

                btnToday.setForeground(new java.awt.Color(255, 153, 153));
                btnToday.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N
                btnToday.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                btnTodayMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                btnTodayMouseEntered(evt);
                        }
                });

                btnWeek.setForeground(new java.awt.Color(255, 153, 153));
                btnWeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7.png"))); // NOI18N
                btnWeek.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                btnWeekMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                btnWeekMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
                jPanel9.setLayout(jPanel9Layout);
                jPanel9Layout.setHorizontalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel9Layout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(labelMoney)
                                                                .addGap(37, 37, 37)
                                                                .addComponent(btnToday,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                26, Short.MAX_VALUE)
                                                                .addComponent(btnWeek,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                46,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(22, 22, 22)));
                jPanel9Layout.setVerticalGroup(
                                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(14, Short.MAX_VALUE)
                                                                .addGroup(jPanel9Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(btnWeek,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(btnToday,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                44,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(labelMoney))
                                                                .addGap(11, 11, 11)));

                credit.setForeground(new java.awt.Color(255, 153, 153));
                credit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/credit.png"))); // NOI18N
                credit.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                creditMouseClicked(evt);
                        }

                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                                creditMouseEntered(evt);
                        }
                });

                javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
                jPanel8.setLayout(jPanel8Layout);
                jPanel8Layout.setHorizontalGroup(
                                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel8Layout.createSequentialGroup()
                                                                .addGap(48, 48, 48)
                                                                .addComponent(labelName)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(credit,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                80,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(104, 104, 104)));
                jPanel8Layout.setVerticalGroup(
                                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel8Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(labelName)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jPanel9,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(credit,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                79,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap()));

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout.createSequentialGroup()

                                                                                                                .addContainerGap())
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                .addComponent(jPanel8,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addGap(359, 359,
                                                                                                                                359)))));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()

                                                                .addGap(17, 17, 17)
                                                                .addComponent(jPanel8,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                35, Short.MAX_VALUE)
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

                pack();
        }// </editor-fold>

        private void goalMouseEntered(java.awt.event.MouseEvent evt) {
                goal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void goalMouseClicked(java.awt.event.MouseEvent evt) {
                int selectedIndex = 0;
                System.out.println("Mostrando vista de metas");
                // AQUÍ METAS
                GoalsModule.initGoals(selectedAccount);
        }

        private void notificationMouseClicked(java.awt.event.MouseEvent evt) {
                RemindersModule.controller.showRemindersView();
        }

        private void notificationMouseEntered(java.awt.event.MouseEvent evt) {
                notification.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void subscriptionMouseClicked(java.awt.event.MouseEvent evt) {
                RecurringsModule.controller.showRecMovesView();
        }

        private void subscriptionMouseEntered(java.awt.event.MouseEvent evt) {
                subscription.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void addMovementMouseEntered(java.awt.event.MouseEvent evt) {
                addMovement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void addMovementMouseClicked(java.awt.event.MouseEvent evt) {
                MovementsModule.initMovements(selectedAccount);
        }

        private void btnWeekMouseClicked(java.awt.event.MouseEvent evt) {

        }

        private void btnWeekMouseEntered(java.awt.event.MouseEvent evt) {
                btnWeek.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void btnTodayMouseClicked(java.awt.event.MouseEvent evt) {
        }

        private void btnTodayMouseEntered(java.awt.event.MouseEvent evt) {
                btnToday.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        private void creditMouseClicked(java.awt.event.MouseEvent evt) {
        }

        private void creditMouseEntered(java.awt.event.MouseEvent evt) {
        }

        private void categoriesMouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println("Mostrando vista de categorías");
                CategoriesModule.initCategories(selectedAccount);
        }

        private void categoriesMouseEntered(java.awt.event.MouseEvent evt) {

                categories.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }

        /**
         * @param args the command line arguments
         */

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel categories;

        public javax.swing.JLabel labelName;
        public javax.swing.JLabel labelMoney;

        private javax.swing.JPanel addMovement;
        private javax.swing.JPanel barChart;
        public javax.swing.JLabel btnToday;
        public javax.swing.JLabel btnWeek;

        public javax.swing.JLabel credit;
        private javax.swing.JLabel goal;
        private javax.swing.JLabel jLabel1;

        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JLabel notification;
        private javax.swing.JPanel pieChart;
        private javax.swing.JLabel subscription;
        // End of variables declaration//GEN-END:variables

}
