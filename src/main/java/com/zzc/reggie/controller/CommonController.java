package com.zzc.reggie.controller;

import com.zzc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: 赵智超
 * @date: 2023/04/12/21:05
 * @Description: 文件上传、下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("file:{}", file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名称，防止文件重复
        String fileName = UUID.randomUUID().toString() + suffix;
        //判断当前目录是否存在
        File dir = new File(basePath);
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            //将临时文件转移到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            response.setContentType("image/jpeg");
            int len = 0;
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
