package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Order;
import com.hinstein.android.experiment.entity.Park;
import com.hinstein.android.experiment.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-20 08:15
 * @Description:
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public void save(int price, int userId, String carNumber, int parkId,String parkName) {
        Order order = new Order();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        order.setStartTime(df.format(new Date()));
        System.out.println(order.getStartTime());
        order.setPrice(price);
        order.setUserId(userId);
        order.setParkId(parkId);
        order.setParkName(parkName);
        order.setCarNumber(carNumber);
        orderRepository.save(order);
    }

    @Transactional(rollbackOn = Exception.class)
    public Page<Order> findOrderPark(int userId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "startTime");
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return orderRepository.findOrderPark(userId, pageable);
    }


    @Transactional(rollbackOn = Exception.class)
    public Page<Order> findUsedPark(int userId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "endTime");
        if (pageNo == 0) {
            pageNo = 1;
        }
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return orderRepository.findUsedPark(userId, pageable);
    }

    public Order findById(int id) {
        return orderRepository.findById(id);
    }

    public void usePark(int orderId) {
        orderRepository.usePark(orderId);
    }

    public void endPark(int orderId) throws Exception {
        Order order = orderRepository.findById(orderId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        order.setEndTime(df.format(new Date()));
        System.out.println(order.getEndTime());
        String endTime = order.getEndTime();
        String startTime = order.getStartTime();

        Date begin = df.parse(endTime);
        Date end = df.parse(startTime);
        long diff = end.getTime() - begin.getTime();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        System.out.println(day + "天" + hour + "小时" + min + "分钟");
        order.setUseTime(day + "天" + hour + "小时" + min + "分钟");
        long amount= hour*order.getPrice();
        order.setAmount((int)amount);
        orderRepository.save(order);
        orderRepository.endPark(orderId);
    }
}
