package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Car;
import com.hinstein.android.experiment.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-12-08 15:46
 * @Description:
 */

public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query(value = "select c from Car c where c.car_userId=?1")
    List<Car> findMyCar(int userId);

    @Query(value = "select c from Car c where c.car_number=?1")
    Car findByCarNumber(String car_number);

    @Query(value = "select c from Car c where c.car_id=?1")
    Car findByCarId(int carId);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from Car c where c.car_id=?1")
    void deleteByCarId(int id);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Car c  SET c.car_isAlways=1 " +
            "WHERE c.car_id = ?1")
    void setDefaultCar(int carId);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Car c SET c.car_isAlways=0 " +
            "WHERE c.car_userId = ?1")
    void cancelDefaultCar(int userId);

}
