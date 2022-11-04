package com.database.services;

import com.database.entities.CarsEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.postgresql.Driver;

import java.io.Serializable;
import java.sql.*;
import java.util.Properties;

public final class Crud implements ICrud, Serializable {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
    private final transient EntityManager manager = entityManagerFactory.createEntityManager();
    private final transient EntityTransaction entityTransaction = manager.getTransaction();

    @Override
    public CarsEntity getCarById(Integer id) {
        return manager.find(CarsEntity.class, id);
    }

    @Override
    public ResultSet getCars() {
        ResultSet resultSet;
        try {
            DriverManager.registerDriver(new Driver());
            String url = "jdbc:postgresql://localhost/";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "Azure2022");
            PreparedStatement preparedStatement;
            Connection connection = DriverManager.getConnection(url, props);
            String preparedQuery = "SELECT * FROM cars";
            preparedStatement = connection.prepareStatement(preparedQuery);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return resultSet;
    }

    @Override
    public void insertCar(CarsEntity carsEntity) {
        entityTransaction.begin();
        manager.persist(carsEntity);
        manager.getTransaction().commit();
    }

    @Override
    public void updateCar(Integer id, String make, String model) {
        CarsEntity car = getCarById(id);
        if (car != null) {
            car.setMake(make);
            car.setModel(model);
            entityTransaction.begin();
            manager.getTransaction().commit();
        }
    }

    @Override
    public void deleteCar(Integer id) {
        CarsEntity car = getCarById(id);
        if (car != null) {
            entityTransaction.begin();
            manager.remove(car);
            manager.getTransaction().commit();
        }
    }
}
