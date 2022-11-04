package com.database.services;

import com.database.entities.CarsEntity;

import java.sql.ResultSet;

public interface ICrud {
    CarsEntity getCarById(Integer id);
    ResultSet getCars();
    void insertCar(CarsEntity carsEntity);
    void updateCar(Integer id, String make, String model);
    void deleteCar(Integer id);
}
