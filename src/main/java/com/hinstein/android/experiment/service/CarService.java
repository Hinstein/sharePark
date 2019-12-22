package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Car;
import com.hinstein.android.experiment.entity.Park;
import com.hinstein.android.experiment.repository.CarRepository;
import com.hinstein.android.experiment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-08 15:54
 * @Description:
 */
@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    UserRepository userRepository;

    public List<Car> findMyCar(int userId) {
        return carRepository.findMyCar(userId);
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public Car findByCarNumber(String carNumber) {
        return carRepository.findByCarNumber(carNumber);
    }

    public void setDefaultCar(int carId) {
        carRepository.setDefaultCar(carId);
    }

    public void cancelDefaultCar(int userId) {
        carRepository.cancelDefaultCar(userId);
    }

    public void deleteByCarId(int parkId, int userId, String carNumber) {
        carRepository.deleteByCarId(parkId);
        userRepository.cancelDefaultCar(carNumber, userId);
        //判断用户的
    }

    public Car findByCarId(int carId) {
        return carRepository.findByCarId(carId);
    }
}
