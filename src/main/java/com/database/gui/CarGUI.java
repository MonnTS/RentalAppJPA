package com.database.gui;

import com.database.entities.CarsEntity;
import com.database.services.Crud;
import com.database.services.ICrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public final class CarGUI extends JFrame {
    private JPanel mainPanel;
    private JTable tblCars;
    private JTextField txtMake;
    private JTextField txtModel;
    private JButton updateCarButton;
    private JButton addCarButton;
    private JButton deleteCarButton;
    private final transient ICrud crud;
    private static final int COLUMN_ID = 0;

    CarGUI() throws SQLException {
        crud = new Crud();

        initComponents();

        tblCars.setModel(buildTableModel(crud.getCars()));

        addCarButton.addActionListener(e -> {
            addNewCar();
            clearEntries();
        });

        updateCarButton.addActionListener(e -> {
            updateCar();
            clearEntries();
        });

        deleteCarButton.addActionListener(e -> {
            deleteCar();
            clearEntries();
        });

        tblCars.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final int row = tblCars.getSelectedRow();
                final String make = (String) tblCars.getValueAt(row, 1);
                final String model = (String) tblCars.getValueAt(row, 2);
                txtMake.setText(make);
                txtModel.setText(model);
            }
        });
    }

    private void initComponents() {
        this.setVisible(true);
        this.setContentPane(mainPanel);
        this.setTitle("Rental Application");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setSize(1200, 700);
    }

    private void deleteCar() {
        int row = tblCars.getSelectedRow();
        Integer carId = Integer.parseInt(tblCars.getModel().getValueAt(row, COLUMN_ID).toString());
        crud.deleteCar(carId);
        try {
            tblCars.setModel(buildTableModel(crud.getCars()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void updateCar() {
        int row = tblCars.getSelectedRow();
        Integer carId = Integer.parseInt(tblCars.getModel().getValueAt(row, COLUMN_ID).toString());
        crud.updateCar(carId, txtMake.getText(), txtModel.getText());
        try {
            tblCars.setModel(buildTableModel(crud.getCars()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void addNewCar() {
        CarsEntity carsEntity = new CarsEntity();
        carsEntity.setMake(txtMake.getText());
        carsEntity.setModel(txtModel.getText());
        crud.insertCar(carsEntity);
        try {
            tblCars.setModel(buildTableModel(crud.getCars()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void clearEntries() {
        txtMake.setText("");
        txtModel.setText("");
    }

    // updates the view of datatable from ResultSet
    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // read columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // fill data
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}