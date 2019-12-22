package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Park;
import com.hinstein.android.experiment.repository.ParkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-06 11:56
 * @Description:
 */
@Service
public class ParkService {

    @Autowired
    ParkRepository parkRepository;

    public List<Park> findAll() {
        return parkRepository.findAll();
    }

    public void create(Park park) {
        parkRepository.save(park);
    }

    public void cancelUsePark(int parkId) {
        parkRepository.cancelUsePark(parkId);
    }

    public void usePark(int parkId) {
        parkRepository.usePark(parkId);
    }

    public void sharePark(int parkId) {
        parkRepository.sharePark(parkId);
    }

    public void cancelSharePark(int parkId) {
        parkRepository.cancelSharePark(parkId);
    }

    public List<Park> findAllByParkOwnerId(int ownerId) {
        return parkRepository.findAllByParkOwnerId(ownerId);
    }

    public List<Park> findAllSharePark(int ownerId) {
        return parkRepository.findAllSharePark(ownerId);
    }

    public List<Park> finMyPark(int userId) {
        return parkRepository.finMyPark(userId);
    }

    public void deleteByParkId(int userId) {
        parkRepository.deleteByParkId(userId);
    }

    public Park findById(int userId) {
        return parkRepository.findById(userId);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<Park> findParkByDistance(int userId, int distance, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "park_distance");
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return parkRepository.findParkByDistance(userId, distance, pageable);
    }

    public void updateDistance(double longitude, double latitude) {
        parkRepository.updateDistance(longitude, latitude);
    }

    public Park findClosePark(int userId) {
        return parkRepository.findClosePark(userId);
    }
}
