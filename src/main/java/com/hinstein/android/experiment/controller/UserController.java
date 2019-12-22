package com.hinstein.android.experiment.controller;

import com.google.common.collect.Lists;
import com.hinstein.android.experiment.ExperimentApplication;
import com.hinstein.android.experiment.entity.Collection;
import com.hinstein.android.experiment.entity.*;
import com.hinstein.android.experiment.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.controller
 * @Author: Hinstein
 * @CreateTime: 2019-11-19 20:36
 * @Description:
 */
@Controller
@ResponseBody
@RequestMapping("/user")
@Api(tags = "用户控制层")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentService commentService;


    @Autowired
    private SuggestionService suggestionService;
    /**
     * 用户登录请求
     *
     * @param password
     * @param username
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户名、密码都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    public Map<String, Object> userLogin(String password, String username) throws Exception{
        Map<String, Object> map = new HashMap<>();
        System.out.println(username);
        System.out.println(password);
        //查找该用户
        User user = userService.findByUsername(username);
        //如查找不到用户这说明用户不存在
        if (user == null) {
            map.put("state", "0");
            map.put("msg", "用户不存在");
        }//如果密码正确则登录成功
        else if (user.getPassword().equals(password)) {
            map.put("msg", "登录成功");
            JSONArray userJson = JSONArray.fromObject(user);
            map.put("userData", userJson);
            map.put("state", "1");
        }//否则登录失败，密码错误！
        else {
            map.put("msg", "密码错误");
            map.put("state", "0");
        }
        return map;
    }


    /**
     * 用户注册请求
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户名、密码都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    public Map<String, String> userRegister(String username, String password) throws Exception{
        System.out.println(username);
        System.out.println(password);
        Map<String, String> map = new HashMap<>();
        //查找是否存在该用户
        if (userService.findByUsername(username) == null) {
            userService.create(username, password);
            map.put("msg", "注册成功");
            map.put("state", "1");
        } else {
            map.put("msg", "注册失败用户名存在");
            map.put("state", "0");
        }
        return map;
    }


    @ApiOperation(value = "用户修改密码", notes = "用户名、新密码、旧密码都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true)
    })
    @PostMapping("/changePassword")
    public Map<String, String> userChangePassword(String username, String newPassword, String oldPassword) throws Exception{
        System.out.println(username);
        System.out.println(newPassword);
        System.out.println(oldPassword);
        Map<String, String> map = new HashMap<>();
        if (userService.findByUsernameAndPassword(username, oldPassword) == null) {
            map.put("msg", "旧密码错误");
            map.put("state", "0");
        } else {
            userService.changePassword(newPassword, username);
            map.put("msg", "密码修改成功");
            map.put("state", "1");
        }
        return map;
    }

    /**
     * 需要传入参数（地址，姓名，电话，用户名）
     *
     * @param user
     * @return
     */
    @PostMapping("/changeInformation")
    @ApiOperation(value = "用户更新信息", notes = "地址、姓名、电话、用户名都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "name", value = "新密码", required = true),
            @ApiImplicitParam(name = "address", value = "地址", required = true),
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true)
    })
    public Map<String, String> userChangeInformation(User user)throws Exception {
        Map<String, String> map = new HashMap<>();
        if (userService.findByUsername(user.getUsername()) == null) {
            map.put("msg", "用户不存在");
            map.put("state", "0");
        } else {
            userService.changeInformation(user);
            map.put("msg", "信息修改成功");
            map.put("state", "1");
        }
        return map;
    }

    @PostMapping("/changeHeadPhoto")
    @ApiOperation(value = "用户更新头像", notes = "地址、姓名、电话、用户名都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
            @ApiImplicitParam(name = "file", value = "当前车位照片", required = true)
    })
    public Map<String, Object> changeHeadPhoto(int userId, @RequestParam("file") MultipartFile file)throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        try {
            //如果文件不为空
            if (null != file) {
                User user = userService.findById(userId);
                //生成uuid作为文件名称
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                //获得文件类型（判断如果不是图片文件类型，则禁止上传）
                String contentType = file.getContentType();
                //获得文件后缀名称
                String imageName = contentType.substring(contentType.indexOf("/") + 1);
                //获取文件的项目路径
                String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/images/";

                //根据日期来创建对应的文件夹
                String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                //根据id分类来创建对应的文件夹
                String leagueIdPath = "userHeadPhoto" + "/";
                //userId
                String path = filePath + leagueIdPath;
                //如果不存在，则创建新文件夹
                File f = new File(path);
                if (!f.exists()) {
                    //创建文件夹
                    f.mkdirs();
                }
                //新生成的文件名称
                String fileName = uuid + "." + imageName;
                //图片保存的完整路径
                String pathName = path + fileName;
                //图片保存的相对路径
                String relativePath = "/images/" + leagueIdPath + fileName;
                //将图片从源位置复制到目标位置
                file.transferTo(new File(pathName));

                user.setHeadPhotoURL(relativePath);
                userService.save(user);
                //返回json数据
                map.put("code", 0);
                map.put("msg", "上传成功图片！");
                map.put("relativePath", relativePath);
                map.put("data", pathName);
            } else {
                System.out.println("文件为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("state", "1");
        map.put("message", "用户更改头像成功");
        return map;
    }

    /**
     * 通过用户id找到自己的车辆信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/findMyCar")
    @ApiOperation(value = "用户查找自己的车辆", notes = "用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "电话号码", required = true),
    })
    public Map<String, Object> findMyCar(int userId) throws Exception{
        Map<String, Object> map = new HashMap<>();
        List<Car> myCar = carService.findMyCar(userId);
        map.put("state", "1");
        JSONArray myCarData = JSONArray.fromObject(myCar);
        map.put("data", myCarData);
        return map;
    }

    /**
     * 添加车辆
     *
     * @param car_number 车牌号  car_address 车辆地址 car_userId 用户id
     * @return
     */
    @PostMapping("/addCar")
    @ApiOperation(value = "用户添加自己的车辆", notes = "车牌号，车辆地址，用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "car_number", value = "车牌号", required = true),
            @ApiImplicitParam(name = "car_address", value = "车辆地址", required = true),
            @ApiImplicitParam(name = "car_userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "file", value = "当前车位照片", required = true)
    })
    public Map<String, Object> addCar(Car car, @RequestParam("file") MultipartFile file) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        if (carService.findByCarNumber(car.getCar_number()) != null) {
            map.put("state", "0");
            map.put("msg", "车辆已被绑定");
        } else {
            try {
                //如果文件不为空
                if (null != file) {
                    //生成uuid作为文件名称
                    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                    //获得文件类型（判断如果不是图片文件类型，则禁止上传）
                    String contentType = file.getContentType();
                    //获得文件后缀名称
                    String imageName = contentType.substring(contentType.indexOf("/") + 1);
                    //获取文件的项目路径
                    String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/images/";
                    //根据日期来创建对应的文件夹
                    String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                    //根据id分类来创建对应的文件夹
                    String leagueIdPath = "carPhoto" + "/";
                    //userId
                    String path = filePath + leagueIdPath;
                    //如果不存在，则创建新文件夹
                    File f = new File(path);
                    if (!f.exists()) {
                        //创建文件夹
                        f.mkdirs();
                    }
                    //新生成的文件名称
                    String fileName = uuid + "." + imageName;
                    //图片保存的完整路径
                    String pathName = path + fileName;
                    //图片保存的相对路径
                    String relativePath = "/images/" + leagueIdPath + fileName;
                    //将图片从源位置复制到目标位置
                    file.transferTo(new File(pathName));
                    System.out.println(pathName);
                    System.out.println(relativePath);
                    car.setCar_photoURL(relativePath);
                    carService.addCar(car);
                    //返回json数据
                    map.put("code", 0);
                    map.put("msg", "上传成功图片！");
                    map.put("relativePath", relativePath);
                    map.put("data", pathName);
                } else {
                    System.out.println("文件为空");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("state", "1");
            map.put("msg", "添加车辆成功");
        }
        return map;
    }

    @PostMapping("/deleteCar")
    @ApiOperation(value = "用户删除自己的车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆ID", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true),
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true),
    })
    public Map<String, String> deletePark(int carId, int userId, String carNumber) throws Exception{
        carService.deleteByCarId(carId, userId, carNumber);
        HashMap<String, String> map = new HashMap<>();
        map.put("state", "1");
        map.put("message", "删除车辆成功");
        return map;
    }

    /**
     * 设置默认车辆
     *
     * @param carNumber 车牌号
     * @param userId    用户Id
     * @return
     */
    @PostMapping("/setDefaultCar")
    @ApiOperation(value = "设置默认车辆", notes = "车牌号，用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true),
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    })
    public Map<String, Object> defaultCar(String carNumber, int carId, int userId)throws Exception {
        Map<String, Object> map = new HashMap<>();
        System.out.println(carNumber);
        System.out.println(carId);
        System.out.println(userId);
        carService.cancelDefaultCar(userId);
        carService.setDefaultCar(carId);
        userService.changeDefaultCar(carNumber, userId);
        User user = userService.findById(userId);
        map.put("state", "1");
        map.put("msg", "设置默认车辆成功");
        map.put("userData", user);
        return map;
    }

    @GetMapping("/findMyPark")
    @ApiOperation(value = "查找用户自己的全部车位", notes = "用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "电话号码", required = true),
    })
    public Map<String, Object> findMyPark(int userId)throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Park> myPark = parkService.finMyPark(userId);
        map.put("state", "1");
        JSONArray myParkData = JSONArray.fromObject(myPark);
        map.put("data", myParkData);
        return map;
    }

    @PostMapping("/collectPark")
    @ApiOperation(value = "收藏车位", notes = "车位Id，用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    })
    public Map<String, String> collectPark(int parkId, int userId)throws Exception {
        Map<String, String> map = new HashMap<>();
        if(collectionService.findByUserIdAndParkId(userId,parkId )==null){
            collectionService.save(parkId, userId);
            map.put("state", "1");
            map.put("msg", "收藏车位成功");
        }
     else{
            map.put("state", "1");
            map.put("msg", "车位已收藏");
        }

        return map;
    }

    @PostMapping("/cancelCollect")
    @ApiOperation(value = "取消收藏车位", notes = "车位Id，用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    })
    public Map<String, String> cancelCollect(int parkId, int userId) throws Exception{
        Map<String, String> map = new HashMap<>();
        if(collectionService.findByUserIdAndParkId(userId,parkId )==null){
            collectionService.save(parkId, userId);
            map.put("state", "1");
            map.put("msg", "收藏车位失败，不存在车位");
        }
        else{
            collectionService.delete(parkId, userId);
            map.put("state", "1");
            map.put("msg", "取消收藏车位成功");
        }


        return map;
    }

    //todo 通过id查找车辆信息（待测试）
    @GetMapping("/findByCarId")
    @ApiOperation(value = "查看车辆信息", notes = "车辆id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆Id", required = true),
    })
    public Map<String, Object> findByCarId(int carId)throws Exception {
        Car car = carService.findByCarId(carId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "1");
        map.put("parkData", car);
        return map;
    }

    //todo 查看所有的车位（待完善）
//    @GetMapping("/getCollectPark")
//    @ApiOperation(value = "获取我收藏的车位", notes = "用户id都是必输项")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
//    })
//    public Map<String, String> collectPark(int userId) {
//        Map<String, String> map = new HashMap<>();
//        List<Collection> collections = collectionService.findByUserId(userId);
//        JSONArray json = new JSONArray();
//        for (Collection collection : collections) {
//            JSONObject json1 = new JSONObject();
//            Park park = parkService.findById(collection.getPartId());
//            json1.put(park);
//            json.add(json1);
//        }
//        map.put("state", "1");
//        map.put("msg", "取消收藏车位成功");
//        return map;
//    }


    //todo 查看我收藏的车位（待完善）
    @GetMapping("/collectPark")
    @ApiOperation(value = "获取收藏车位列表", notes = "用户id都是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true)
    })
    public Map<String, Object> collectPark(int userId)throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<Collection> collections = collectionService.findByUserId(userId);
        List<Park> parks = Lists.newArrayList();
        for (Collection collection : collections) {
            Park park = parkService.findById(collection.getParkId());
            parks.add(park);
        }
        JSONArray parkData = JSONArray.fromObject(parks);
        map.put("state", "1");
        map.put("data", parkData);
        map.put("msg", "获取收藏车位成功");
        return map;
    }


    //todo 预约车位（有车辆id、开始时间、结束时间、价格、车牌号、车位状态（未使用0、开锁1开灯、关锁2关灯））（待测试）
    @PostMapping("/orderPark")
    @ApiOperation(value = "预约车位", notes = "开始时间，车位单价，用户id，车牌号，车辆id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "price", value = "车位单价", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true),
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
            @ApiImplicitParam(name = "parkName", value = "车位名称", required = true),
    })
    public Map<String, String> orderPark(int price, int userId, String carNumber, int parkId, String parkName) throws Exception{
        Map<String, String> map = new HashMap<>();
        orderService.save(price, userId, carNumber, parkId, parkName);
        map.put("state", "1");
        map.put("msg", "预约车位成功");
        return map;
    }

    //todo 查找我预约的车位（车位状态2（未使用0、开锁1开灯、关锁2关灯））（待测试）
    @GetMapping("/getOrderPark")
    @ApiOperation(value = "查找我的预约车位", notes = "用户id必填项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前第几页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每一页多少条数据", required = true)
    })
    public Map<String, Object> getOrderPark(int userId, int pageNo, int pageSize)throws Exception {
        Map<String, Object> map = new HashMap<>();
        Page<Order> orderPark = orderService.findOrderPark(userId, pageNo, pageSize);
        JSONArray orderData = JSONArray.fromObject(orderPark.getContent());
        map.put("state", "1");
        map.put("msg", "预约车位查询成功");
        map.put("data", orderData);
        map.put("count", orderPark.getTotalElements());
        return map;
    }

    //todo 查找我使用过车位（车位状态2（未使用0、开锁1开灯、关锁2关灯））（待测试）
    @GetMapping("/getUsedPark")
    @ApiOperation(value = "我使用过的车位记录", notes = "用户id必填项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前第几页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每一页多少条数据", required = true)
    })
    public Map<String, Object> getUsedPark(int userId, int pageNo, int pageSize)throws Exception {
        Map<String, Object> map = new HashMap<>();
        Page<Order> orderPark = orderService.findUsedPark(userId, pageNo, pageSize);
        JSONArray orderData = JSONArray.fromObject(orderPark.getContent());
        map.put("state", "1");
        map.put("msg", "查询使用过的车位成功");
        map.put("data", orderData);
        map.put("count", orderPark.getTotalElements());
        return map;
    }
    //todo 查找预约的车位信息(待测试)

    /**
     * 查看预约车位信息
     *
     * @param parkId
     * @return
     */
    @GetMapping("/findByOrderId")
    @ApiOperation(value = "查看预约车位信息", notes = "预约车位id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
    })
    public Map<String, Object> findByParkId(int parkId)throws Exception {
        Order order = orderService.findById(parkId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "1");
        map.put("parkData", order);
        return map;
    }


    //todo 使用预约车位（待测试）
    @PostMapping("/useOrderPark")
    @ApiOperation(value = "使用预约的车位并且车位开锁", notes = "订单号是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "当前预约的订单号", required = true)
    })
    public Map<String, String> useOrderPark(int id) throws Exception{
        Map<String, String> map = new HashMap<>();
        orderService.usePark(id);

        //todo 与硬件交互， 开灯
        Map<String, Object> map1 = new HashMap<>();
        Order order = orderService.findById(id);
        map1.put("parkId",order.getParkId());
        map1.put("status",0);
        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());

        map.put("state", "1");
        map.put("msg", "车位开锁成功");
        return map;
    }

    //todo 结束使用车位（待测试）
    @PostMapping("/endOrderPark")
    @ApiOperation(value = "结束使用车位，车位上锁", notes = "结束时间，总费用是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "当前预约的订单号", required = true),
    })
    public Map<String, String> endOrderPark(int id) throws Exception {
        Map<String, String> map = new HashMap<>();
        orderService.endPark(id);
        map.put("state", "1");
        map.put("msg", "停车结束，车位上锁成功");
        //todo 与硬件交互， 关灯
        Map<String, Object> map1 = new HashMap<>();
        Order order = orderService.findById(id);
        map1.put("parkId",order.getParkId());
        map1.put("status",1);

        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());

        return map;
    }


    //todo 评论 车位评论（用户账号、评论内容、时间、车位Id）（待测试）
    @PostMapping("/commentPark")
    @ApiOperation(value = "给车位评论", notes = "用户账号，评论内容、车位Id、用户头像路径是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户账号", required = true),
            @ApiImplicitParam(name = "content", value = "评论内容", required = true),
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
            @ApiImplicitParam(name = "userHeadPhoto", value = "用户头像路径", required = true)
    })
    public Map<String, String> commentPark(String username, String content, int parkId, String userHeadPhoto)throws Exception {
        Map<String, String> map = new HashMap<>();
        commentService.save(username, content, parkId, userHeadPhoto);
        map.put("state", "1");
        map.put("msg", "评论成功！");
        return map;
    }

    //todo 查找当前车位的评论信息(待测试)
    @GetMapping("/findComment")
    @ApiOperation(value = "查看当前车位的评论信息", notes = "车位Id是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true)
    })
    public Map<String, Object> findComment(int parkId) throws Exception{
        Map<String, Object> map = new HashMap<>();
        Page<Comment> comments = commentService.findByParkId(parkId);
        JSONArray parkData = JSONArray.fromObject(comments.getContent());
        map.put("count", comments.getTotalElements());
        map.put("parkData", parkData);
        map.put("state", "1");
        map.put("msg", "数据查询成功！");
        return map;
    }


    @GetMapping("/findByCarNumber")
    @ApiOperation(value = "通过车牌号查找车辆信息", notes = "车牌号是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true)
    })
    public Map<String, Object> findByCarNumber(String carNumber) throws Exception{
        Map<String, Object> map = new HashMap<>();
        Car car = carService.findByCarNumber(carNumber);
        map.put("data", car);
        map.put("state", "1");
        map.put("msg", "数据查询成功！");
        return map;
    }

    @PostMapping("/suggestion")
    @ApiOperation(value = "发送建议", notes = "用户账号，意见内容是必选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true),
            @ApiImplicitParam(name = "suggestion", value = "意见内容", required = true)
    })
    public Map<String, String> suggestion(int  userId, String suggestion)throws Exception {
        Map<String, String> map = new HashMap<>();
        suggestionService.save(userId, suggestion);
        map.put("state", "1");
        map.put("msg", "意见发送成功！");
        return map;
    }
}
