package com.hinstein.android.experiment.entity;

import javax.persistence.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.entity
 * @Author: Hinstein
 * @CreateTime: 2019-12-08 15:44
 * @Description:
 */
@Entity
@Table(name = "car")
public class Car {

    /**
     * 停车场id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer car_id;

    /**
     * 车牌号
     */
    @Column(length = 50)
    private String car_number;

    @Column(length = 50)
    private String car_address;

    private int car_userId;

    private byte car_isAlways;

    /**
     * 照片路径
     */
    private String car_photoURL;

    public String getCar_photoURL() {
        return car_photoURL;
    }

    public void setCar_photoURL(String car_photoURL) {
        this.car_photoURL = car_photoURL;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public String getCar_number() {
        return car_number;
    }

    public int getCar_userId() {
        return car_userId;
    }

    public void setCar_userId(int car_userId) {
        this.car_userId = car_userId;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getCar_address() {
        return car_address;
    }

    public void setCar_address(String car_address) {
        this.car_address = car_address;
    }

    public byte getCar_isAlways() {
        return car_isAlways;
    }

    public void setCar_isAlways(byte car_isAlways) {
        this.car_isAlways = car_isAlways;
    }

}
