package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Park;
import com.hinstein.android.experiment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-11-19 20:35
 * @Description:
 */

public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return 用户信息
     */
    User findByUsername(String username);


    User findById(int userId);

    /**
     * 通过用户名和密码查找是否存在该用户
     *
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);

    /**
     * 更新密码信息
     *
     * @param password
     * @param username
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE User user  SET user.password=?1 " +
            "WHERE user.username = ?2")
    void changePassword(String password, String username);


    /**
     * 更新用户信息
     *
     * @param user
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE User u  SET u.address= :#{#user.address}," +
            "u.name= :#{#user.name}," +
            "u.phone= :#{#user.phone} " +
            "WHERE u.username = :#{#user.username}")
    void changeInformation(User user);

    /**
     * 更新用那个胡默认车辆
     *
     * @param carNumber
     * @param id
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE User user  SET user.car_number=?1 " +
            "WHERE user.id = ?2")
    void changeDefaultCar(String carNumber, int id);


    /**
     * 如果删除车辆在我的默认车辆中，则将默认车辆设为空
     *
     * @param carNumber
     * @param id
     */
    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query(value = "UPDATE User user  SET user.car_number='' " +
            "WHERE user.id = ?2 and user.car_number=?1")
    void cancelDefaultCar(String carNumber, int id);
}
