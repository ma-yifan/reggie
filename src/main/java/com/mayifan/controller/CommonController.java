package com.mayifan.controller;

import com.mayifan.common.R;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.imgpath}")
    private String basePath;

    @GetMapping("/download")
    public void downLoadImage(@RequestParam("name") String fileName, HttpServletResponse response,
                              HttpServletRequest request) throws IOException {
        //获取文件在服务器中的真实路径，并读取到流中
        File path_file = new File(basePath +"\\"+ fileName);
        FileInputStream fileInputStream = new FileInputStream(path_file);
        IOUtils.copy(fileInputStream,response.getOutputStream());
    }

    @PostMapping("/upload")
    public R<String> uploadImg(@RequestPart("file") MultipartFile file){
        //获取文件名，并改成UUID文件名
        String originalFilename = file.getOriginalFilename();
        String new_fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //获取保存路径，没有就创建一个
        File path_file = new File(basePath);
        if (!path_file.exists()) {
            path_file.mkdir();
        }
        //保存
        try {
            file.transferTo(new File(basePath+"\\"+new_fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //返回保存的文件名
        return R.success(new_fileName);
    }
}
