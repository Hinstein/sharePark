package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Test;
import com.hinstein.android.experiment.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-02 10:54
 * @Description:
 */
@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public void save(Test test) {
        testRepository.save(test);
    }
}
