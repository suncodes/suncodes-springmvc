package controller;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import pojo.UploadBO;

import java.io.*;

@Controller
public class UploadController {

    @RequestMapping("/uploadFile")
    public String f(MultipartFile file) throws IOException {
        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        // ------------------

        byte[] bytes = file.getBytes();
        FileUtils.writeByteArrayToFile(new File("1.docx"), bytes);

        return "index";
    }

    @RequestMapping("/uploadFile1")
    public String f1(MultipartFile file, String param) throws IOException {

        System.out.println("参数param：" + param);

        String name = file.getName();
        String originalFilename = file.getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        // ------------------

        byte[] bytes = file.getBytes();
        FileUtils.writeByteArrayToFile(new File("1.docx"), bytes);

        return "index";
    }

    @RequestMapping("/uploadFile2")
    public String f1(MultipartFile[] file, String param) throws IOException {

        System.out.println("参数param：" + param);
        for (MultipartFile multipartFile : file) {
            System.out.println(multipartFile.getOriginalFilename());
        }
        return "index";
    }


    @RequestMapping("/uploadFile3")
    public String f3(UploadBO uploadBO) throws IOException {

        System.out.println("参数param：" + uploadBO.getParam());

        String name = uploadBO.getFile().getName();
        String originalFilename = uploadBO.getFile().getOriginalFilename();
        System.out.println("name: " + name);
        System.out.println("originalFilename: " + originalFilename);

        return "index";
    }
}
