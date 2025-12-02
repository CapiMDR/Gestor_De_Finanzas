/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerFilter;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import modelFilter.FilterModel;
import modelFilter.FilterObserver;
import modelFilter.FilterSubject;
import modelReport.Movement;
import viewReport.FrmMain;

/**
 *
 * @author villa
 */
public class FilterController implements FilterObserver {

    private FrmMain view;
    private FilterModel model;

    public void setViewModule(FrmMain view, FilterModel model) {
        this.view = view;
        this.model = model;

        this.model.addObserver(this);

        loadCategoriesToView();
        configureTableColumns();
        assignActions();
    }

    private void loadCategoriesToView() {
        view.categoryBox.removeAllItems();
        view.categoryBox.addItem("Renta");
        view.categoryBox.addItem("Trabajo");
        view.categoryBox.addItem("Despensa");
        view.categoryBox.addItem("Comida");
        view.categoryBox.addItem("Salario");
        view.categoryBox.addItem("Deporte");
        view.categoryBox.addItem("Otros");
    }

    private void configureTableColumns() {

        view.tableFilter.getColumnModel().getColumn(0).setHeaderValue("Nombre");
        view.tableFilter.getColumnModel().getColumn(1).setHeaderValue("Type");
        view.tableFilter.getColumnModel().getColumn(2).setHeaderValue("Amount");
        view.tableFilter.getColumnModel().getColumn(3).setHeaderValue("Date");

        view.tableFilter.getTableHeader().repaint();
    }

    private void assignActions() {
        view.btnFiltrar.addActionListener(e -> {
            String selected = (String) view.categoryBox.getSelectedItem();
            if (selected != null && !selected.trim().isEmpty()) {
                model.filterByCategory(selected);
            }
        });
    }

    @Override
    public void update(List<Movement> filtered) {
        DefaultTableModel tm = (DefaultTableModel) view.tableFilter.getModel();
        tm.setRowCount(0);

        for (Movement m : filtered) {
            tm.addRow(new Object[]{
                m.getDescription(), // Nombre
                m.getCategoryName(), // Type
                m.getAmount(), // Amount
                m.getDate().toLocalDate().toString() // Date
            });
        }
    }
}
