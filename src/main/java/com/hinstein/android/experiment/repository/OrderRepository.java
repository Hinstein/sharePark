package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Collection;
import com.hinstein.android.experiment.entity.Order;
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
 * @CreateTime: 2019-12-20 08:15
 * @Description:
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {


    @Query(value = "select o from Order o where o.userId=?1 and o.status <>2")
    Page<Order> findOrderPark(int userId, Pageable pageable);

    @Query(value = "select o from Order o where o.userId=?1 and o.status =2")
    Page<Order> findUsedPark(int userId, Pageable pageable);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Order o  SET o.status=1 " +
            "WHERE o.id = ?1")
    void usePark(int id);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE Order o  SET o.status=2 " +
            "WHERE o.id = ?1")
    void endPark(int id);

    Order findById(int id);
}
