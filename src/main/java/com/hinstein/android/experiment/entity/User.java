package com.hinstein.android.experiment.entity;

import javax.persistence.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.entity
 * @Author: Hinstein
 * @CreateTime: 2019-11-22 21:59
 * @Description:
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * 用户id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    @Column(length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(length = 50)
    private String password;

    /**
     * 账号添加时间
     */
    @Column(length = 30)
    private String addTime;

    /**
     * 电话号码
     */
    @Column(length = 11)
    private String phone;

    /**
     * 住址
     */
    @Column(length = 50)
    private String address;

    /**
     * 姓名
     */
    @Column(length = 20)
    private String name;

    /**
     * 默认车辆
     */
    private String car_number;

    /**
     * 头像
     *
     * @return
     */
    private String headPhotoURL;

    public String getHeadPhotoURL() {
        return headPhotoURL;
    }

    public void setHeadPhotoURL(String headPhotoURL) {
        this.headPhotoURL = headPhotoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

}
