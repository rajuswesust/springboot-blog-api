package com.spring.boot.blog.app.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FileUpload {

    public final String UPLOAD_DIR = new ClassPathResource("static/image/").getFile().getAbsolutePath();

    public FileUpload() throws IOException {
    }

    public boolean upload(MultipartFile multipartFile) {
        boolean flg = false;

        try {

//            //Read
//           InputStream ins = multipartFile.getInputStream();
//           byte data[] = new byte[ins.available()];
//           ins.read(data);
//
//           //write
//            FileOutputStream fs = new FileOutputStream(UPLOAD_DIR + "\\" + multipartFile.getOriginalFilename());
//            fs.write(data);
//            fs.flush();
//            fs.close();
            System.out.println("dir: " + UPLOAD_DIR);
            flg = true;
            Files.copy(multipartFile.getInputStream(), Path.of(UPLOAD_DIR + File.separator + multipartFile.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flg;
    }

}
