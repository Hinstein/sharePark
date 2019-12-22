package com.hinstein.android.experiment.controller;

import com.hinstein.android.experiment.ExperimentApplication;
import com.hinstein.android.experiment.entity.Test;
import com.hinstein.android.experiment.service.TestService;
import com.hinstein.android.experiment.test3.Server;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.hinstein.android.experiment.test3.Server;
/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.controller
 * @Author: Hinstein
 * @CreateTime: 2019-12-02 10:46
 * @Description:
 */
@Controller
@ResponseBody
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/test")
    public Map<String, String> test(String message) {
        Test test = new Test();
        test.setMessage(message);
        testService.save(test);
        Map map = new HashMap<String, String>();
        map.put("message", message);
        System.out.println(message);
        return map;
    }

    @GetMapping("/getMessage")
    public Map<String, Object> getMessage() {
        Map map = new HashMap<String, Object>();
        List<Test> all = testService.findAll();
        map.put("message", all);
        return map;
    }

    @ResponseBody
    @PostMapping("/addPhoto")
    public Map<String, Object> addPhoto(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        //通过session查看当前登录的用户信息
//        User user = (User) session.getAttribute("user");
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
                String leagueIdPath = "user" + "/";
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
                //设置photo实体类的数据
                //返回json数据
                map.put("code", 0);
                map.put("msg", "上传成功！");
                map.put("relativePath", relativePath);
                map.put("data", pathName);
            } else {
                System.out.println("文件为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    @PostMapping("/openDoor")
    public Map<String, Object> openDoor(int parkId) {

        //todo 与硬件交互， 开灯
        Map<String, Object> map1 = new HashMap<>();
        map1.put("parkId",parkId);
        map1.put("status",0);
        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());

        return map1;
    }

    @PostMapping("/closeDoor")
    public Map<String, Object> closeDoor(int parkId) {

        //todo 与硬件交互， 关灯
        Map<String, Object> map1 = new HashMap<>();
        map1.put("parkId",parkId);
        map1.put("status",1);
        JSONObject jsonObject=JSONObject.fromObject(map1);
        ExperimentApplication.server.push(jsonObject.toString());

        return map1;
    }
}
