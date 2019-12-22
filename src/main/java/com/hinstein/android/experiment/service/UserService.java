package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.User;
import com.hinstein.android.experiment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BengsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-11-22 22:00
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 判断用户名和密码是否正确
     *
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    /**
     * 创建新的用户
     *
     * @param username
     * @param password
     */
    public void create(String username, String password) {
        //新建用户
        User user = new User();
        user.setUsername(username);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setAddTime(df.format(new Date()));
        user.setPassword(password);
        userRepository.save(user);
    }

    /**
     * 更新用户密码
     *
     * @param password
     * @param username
     */
    public void changePassword(String password, String username) {
        userRepository.changePassword(password, username);
    }


    /**
     * 更新用户密码
     *
     * @param user
     */
    public void changeInformation(User user) {
        userRepository.changeInformation(user);
    }

    public User findById(int id) {
        return userRepository.findById(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void changeDefaultCar(String carNumber, int id) {
        userRepository.changeDefaultCar(carNumber, id);
    }
}
