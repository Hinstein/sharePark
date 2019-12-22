package com.hinstein.android.experiment.entity;

import javax.persistence.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.entity
 * @Author: Hinstein
 * @CreateTime: 2019-12-02 10:51
 * @Description:
 */
@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer test_id;

    @Column(length = 50)
    private String message;

    public Integer getTest_id() {
        return test_id;
    }

    public void setTest_id(Integer test_id) {
        this.test_id = test_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
