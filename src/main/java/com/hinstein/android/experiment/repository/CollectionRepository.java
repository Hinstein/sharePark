package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Collection;
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
 * @CreateTime: 2019-12-19 17:17
 * @Description:
 */
public interface CollectionRepository extends JpaRepository<Collection, Integer> {

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from Collection c where c.parkId=?1 and c.userId=?2")
    void delete(int parkId, int userId);


    List<Collection> findByUserId(int userId);

    @Query(value = "select c from Collection c where c.userId=?1 and c.parkId=?2")
    Collection findByUserIdAndParkId(int userId, int parkId);
}
