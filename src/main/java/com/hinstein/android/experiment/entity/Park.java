package com.hinstein.android.experiment.entity;

import javax.persistence.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.entity
 * @Author: Hinstein
 * @CreateTime: 2019-11-26 19:30
 * @Description:
 */
@Entity
@Table(name = "park")
public class Park {

    /**
     * 停车场id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer park_id;

    /**
     * 停车场名称
     */
    @Column(length = 50)
    private String park_name;

    /**
     * 停车场地址
     */
    @Column(length = 100)
    private String park_address;

    /**
     * 价格
     */
    private int park_price;


    /**
     * 距离 不在数据库中添加字段
     */

    private double park_distance;

    /**
     * 持有人姓名
     */
    @Column(length = 50)
    private String park_ownerName;

    /**
     * 持有人id
     */
    private int park_ownerId;

    /**
     * 经度
     */
    private double park_longitude;

    /**
     * 纬度
     */
    private double park_latitude;

    /**
     * 车位是否使用
     */
    private byte park_use;


    /**
     * 车位是否分享
     */
    private byte park_share;

    /**
     * 照片路径
     */
    private String park_photoURL;

    private int Park_collectNumber;

    @Transient
    private int park_collect;

    public String getPark_photoURL() {
        return park_photoURL;
    }

    public void setPark_photoURL(String park_photoURL) {
        this.park_photoURL = park_photoURL;
    }

    public int getPark_collectNumber() {
        return Park_collectNumber;
    }

    public void setPark_collectNumber(int park_collectNumber) {
        Park_collectNumber = park_collectNumber;
    }

    public String getPark_ownerName() {
        return park_ownerName;
    }

    public void setPark_ownerName(String park_ownerName) {
        this.park_ownerName = park_ownerName;
    }

    public double getPark_longitude() {
        return park_longitude;
    }

    public void setPark_longitude(double park_longitude) {
        this.park_longitude = park_longitude;
    }

    public double getPark_latitude() {
        return park_latitude;
    }

    public void setPark_latitude(double park_latitude) {
        this.park_latitude = park_latitude;
    }

    public byte getPark_use() {
        return park_use;
    }

    public void setPark_use(byte park_use) {
        this.park_use = park_use;
    }

    public byte getPark_share() {
        return park_share;
    }

    public int getPark_ownerId() {
        return park_ownerId;
    }

    public void setPark_ownerId(int park_ownerId) {
        this.park_ownerId = park_ownerId;
    }

    public void setPark_share(byte park_share) {
        this.park_share = park_share;
    }

    public Integer getPark_id() {
        return park_id;
    }

    public void setPark_id(Integer park_id) {
        this.park_id = park_id;
    }

    public String getPark_name() {
        return park_name;
    }

    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }

    public String getPark_address() {
        return park_address;
    }

    public void setPark_address(String park_address) {
        this.park_address = park_address;
    }

    public int getPark_price() {
        return park_price;
    }

    public void setPark_price(int park_price) {
        this.park_price = park_price;
    }

    public double getPark_distance() {
        return park_distance;
    }

    public void setPark_distance(double park_distance) {
        this.park_distance = park_distance;
    }

    public int getPark_collect() {
        return park_collect;
    }

    public void setPark_collect(int park_collect) {
        this.park_collect = park_collect;
    }
}
