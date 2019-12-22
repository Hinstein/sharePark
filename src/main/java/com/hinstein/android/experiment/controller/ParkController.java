package com.hinstein.android.experiment.controller;

import com.hinstein.android.experiment.ExperimentApplication;
import com.hinstein.android.experiment.entity.Collection;
import com.hinstein.android.experiment.entity.Order;
import com.hinstein.android.experiment.entity.Park;
import com.hinstein.android.experiment.service.CollectionService;
import com.hinstein.android.experiment.service.ParkService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.controller
 * @Author: Hinstein
 * @CreateTime: 2019-12-06 11:47
 * @Description:
 */
@Controller
@ResponseBody
@RequestMapping("/park")
@Api(tags = "停车场的控制层")
public class ParkController {

    @Autowired
    ParkService parkService;

    @Autowired
    CollectionService collectionService;

    @PostMapping("/createPark")
    @ApiOperation(value = "创建停车位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "park_name", value = "停车场名称", required = true),
            @ApiImplicitParam(name = "park_address", value = "停车场地址", required = true),
            @ApiImplicitParam(name = "park_price", value = "价格", required = true),
            @ApiImplicitParam(name = "park_distance", value = "距离", required = true),
            @ApiImplicitParam(name = "park_ownerName", value = "持有人姓名", required = true),
            @ApiImplicitParam(name = "park_ownerId", value = "持有人id", required = true),
            @ApiImplicitParam(name = "park_longitude", value = "经度", required = true),
            @ApiImplicitParam(name = "park_latitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "file", value = "当前车位照片", required = true)
    })
    public Map<String, Object> create(Park park, @RequestParam("file") MultipartFile file) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
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
                String leagueIdPath = "parkPhoto" + "/";
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
                park.setPark_photoURL(relativePath);
                parkService.create(park);
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
        map.put("message", "创建停车位成功");
        return map;
    }

    @PostMapping("/deletePark")
    @ApiOperation(value = "删除停车位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场ID", required = true),
    })
    public Map<String, String> deletePark(int parkId)throws Exception {
        HashMap<String, String> map = new HashMap<>();
        try{
            parkService.deleteByParkId(parkId);
        }catch (Exception e){
        }
        map.put("state", "1");
        map.put("message", "删除停车位成功");
        return map;
    }

    @PostMapping("/usePark")
    @ApiOperation(value = "用户使用自己车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场id", required = true),
    })
    public Map<String, String> usePark(int parkId) throws Exception{
        parkService.usePark(parkId);
        HashMap<String, String> map = new HashMap<>();
        map.put("state", "1");
        map.put("message", "停车成功");


        //todo 与硬件交互， 开灯
        Map<String, Object> map1 = new HashMap<>();
        map1.put("parkId",parkId);
        map1.put("status",0);
        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());
        return map;
    }

    @PostMapping("/cancelUsePark")
    @ApiOperation(value = "用户结束使用自己的车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场id", required = true),
    })
    public Map<String, String> cancelUsePark(int parkId) throws Exception{
        parkService.cancelUsePark(parkId);
        HashMap<String, String> map = new HashMap<>();
        map.put("state", "1");
        map.put("message", "出库成功");

        //todo 与硬件交互， 关灯
        Map<String, Object> map1 = new HashMap<>();
        map1.put("parkId",parkId);
        map1.put("status",1);
        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());
        return map;
    }


    @PostMapping("/sharePark")
    @ApiOperation(value = "用户分享自己的车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场id", required = true),
    })
    public Map<String, String> sharePark(int parkId) throws Exception{
        parkService.sharePark(parkId);
        HashMap<String, String> map = new HashMap<>();
        map.put("state", "1");
        map.put("message", "分享车位成功");
        return map;
    }

    @PostMapping("/cancelSharePark")
    @ApiOperation(value = "用户取消分享自己的车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "停车场id", required = true),
    })
    public Map<String, String> cancelSharePark(int parkId) throws Exception{
        parkService.cancelSharePark(parkId);
        HashMap<String, String> map = new HashMap<>();
        map.put("state", "1");
        map.put("message", "取消分享车位成功");
        return map;
    }

    /**
     * 查找当前用户的全部车位
     *
     * @param parkOwnerId
     * @return
     */
    @GetMapping("/findMyPark")
    @ApiOperation(value = "查找当前用户自己的全部车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkOwnerId", value = "当前用户id", required = true),
    })
    public Map<String, Object> findAllPark(int parkOwnerId) throws Exception{
        List<Park> parks = parkService.findAllByParkOwnerId(parkOwnerId);
        JSONArray parkData = JSONArray.fromObject(parks);
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "1");
        map.put("parkData", parkData);
        return map;
    }

    /**
     * 查找所有已分享车位
     *
     * @param parkOwnerId
     * @return
     */
    @GetMapping("/findAllPark")
    @ApiOperation(value = "查找当前全部分享出去的车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkOwnerId", value = "当前用户id", required = true),
    })
    public Map<String, Object> findAllSharePark(int parkOwnerId)throws Exception {
        List<Park> parks = parkService.findAllSharePark(parkOwnerId);
        for (Park park : parks) {
            if (collectionService.findByUserIdAndParkId(parkOwnerId, park.getPark_id()) != null) {
                park.setPark_collect(1);
            }
        }
        JSONArray parkData = JSONArray.fromObject(parks);
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "1");
        map.put("parkData", parkData);
        return map;
    }

    /**
     * 通过经纬度查找当前需要的地址
     *
     * @param longitude
     * @param latitude
     * @param userId
     * @param pageNo
     * @param distance
     * @param pageSize
     * @return
     */
    @GetMapping("/findParkByDistance")
    @ApiOperation(value = "查找当前用户附近车辆，距离可以自定", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "当前用户经度", required = true),
            @ApiImplicitParam(name = "latitude", value = "当前用户纬度", required = true),
            @ApiImplicitParam(name = "distance", value = "距离范围", required = true),
            @ApiImplicitParam(name = "userId", value = "当前用户ID", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前第几页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每一页多少条数据", required = true),
    })
    public Map<String, Object> findParkByDistance(double longitude, double latitude, int userId, int pageNo, int distance, int pageSize) throws Exception{
        parkService.updateDistance(longitude, latitude);
        Page<Park> parks = parkService.findParkByDistance(userId, distance, pageNo, pageSize);
        for (Park park : parks) {
            if (collectionService.findByUserIdAndParkId(userId, park.getPark_id()) != null) {
                park.setPark_collect(1);
            }
        }
        JSONArray parkData = JSONArray.fromObject(parks.getContent());
        HashMap<String, Object> map = new HashMap<>();
        map.put("count", parks.getTotalElements());
        map.put("state", "1");
        map.put("parkData", parkData);
        return map;
    }

    //todo 通过id查找车位信息（待测试）

    /**
     * 查看停车场信息
     *
     * @param parkId
     * @return
     */
    @GetMapping("/findByParkId")
    @ApiOperation(value = "通过停车场id查找车位信息", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkId", value = "车位Id", required = true),
    })
    public Map<String, Object> findByParkId(int parkId) throws Exception{
        Park park = parkService.findById(parkId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "1");
        map.put("parkData", park);
        return map;
    }

    //todo 推荐最近的车位(待测试)
    @GetMapping("/findClosePark")
    @ApiOperation(value = "查找当前最近车位", notes = "停车场id是必输项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "经度", required = true),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "userId", value = "当前用户Id", required = true),
    })
    public Map<String, Object> findClosePark(double longitude, double latitude, int userId) throws Exception{
        parkService.updateDistance(longitude, latitude);
        HashMap<String, Object> map = new HashMap<>();
        Park closePark = parkService.findClosePark(userId);
        map.put("state", "1");
        map.put("data", closePark);
        return map;
    }
}
