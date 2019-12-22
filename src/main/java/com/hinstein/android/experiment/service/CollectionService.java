package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Collection;
import com.hinstein.android.experiment.repository.CollectionRepository;
import com.hinstein.android.experiment.repository.ParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-19 17:19
 * @Description:
 */
@Service
public class CollectionService {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    ParkRepository parkRepository;

    public void save(int parkId, int userId) {
        Collection collection = new Collection();
        collection.setParkId(parkId);
        collection.setUserId(userId);
        collectionRepository.save(collection);
        parkRepository.collectPlus(parkId);
    }

    public void delete(int parkId, int userId) {
        collectionRepository.delete(parkId, userId);
        parkRepository.collectReduce(parkId);
    }

    public List<Collection> findByUserId(int userId) {
        return collectionRepository.findByUserId(userId);
    }

    public Collection findByUserIdAndParkId(int userId, int parkId) {
        return collectionRepository.findByUserIdAndParkId(userId, parkId);
    }
}
