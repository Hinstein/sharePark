package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Park;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-12-06 11:56
 * @Description:
 */
public interface ParkRepository extends JpaRepository<Park, Integer> {


    List<Park> findAll();

    @Query(value = "select p from Park p  where p.park_id=?1")
    Park findById(int userId);

    /**
     * 使用车位
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Park park  SET park.park_use=1 " +
            "WHERE park.park_id = ?1")
    void usePark(int parkId);

    /**
     * 取消使用车位
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Park park  SET park.park_use=0 " +
            "WHERE park.park_id = ?1")
    void cancelUsePark(int parkId);


    /**
     * 分享车位
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Park park  SET park.park_share=1 " +
            "WHERE park.park_id = ?1")
    void sharePark(int parkId);

    /**
     * 取消分享车位
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Park park  SET park.park_share=0 " +
            "WHERE park.park_id = ?1")
    void cancelSharePark(int parkId);

    @Query(value = "select p from Park p where p.park_ownerId =?1")
    List<Park> findAllByParkOwnerId(int id);

    @Query(value = "select p from Park p where p.park_share=1 and p.park_ownerId <>?1")
    List<Park> findAllSharePark(int userId);

    @Query(value = "select p from Park p  where p.park_ownerId=?1")
    List<Park> finMyPark(int userId);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "delete from  Park p where p.park_id=?1")
    void deleteByParkId(int id);

    @Query(value = "select * from park where  (park_distance < ?2 and park_share=1 and park_owner_id !=?1) ",
            countQuery = "SELECT COUNT(*) from park where (park_distance < ?2 and park_share=1 and park_owner_id !=?1)",
            nativeQuery = true)
    Page<Park> findParkByDistance(int userId, int distance, Pageable pageable);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value =
            "UPDATE park INNER JOIN (select park_id,POWER(park_latitude - ?2 , 2) + POWER(park_longitude - ?1 , 2) * POWER(COS((park_latitude + ?2) / 2) , 2)  AS distance from park) b ON park.park_id = b.park_id SET park.park_distance = b.distance*1000000",
            nativeQuery = true)
    void updateDistance(double longitude, double latitude);


    @Query(value = "select * from park where  park_share=1 and park_owner_id !=?1 order by park_distance asc limit 0,1 ",
            nativeQuery = true)
    Park findClosePark(int userId);

    /**
     * 收藏数加一
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE park   SET park_collect_number= park_collect_number+1 WHERE park_id = ?1",nativeQuery = true)
    void collectPlus(int parkId);

    /**
     * 收藏数减一
     *
     * @param parkId
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE park   SET park_collect_number= park_collect_number-1 WHERE park_id = ?1",nativeQuery = true)
    void collectReduce(int parkId);
}
