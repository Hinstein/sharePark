package com.hinstein.android.experiment.repository;


import com.hinstein.android.experiment.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-12-02 10:52
 * @Description:
 */
public interface TestRepository extends JpaRepository<Test, Integer> {

    List<Test> findAll();

}
