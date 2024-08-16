package com.hriday.movie.Api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get name of the file
        String fileName = file.getOriginalFilename();

        //get file path
        String filePath = path+ File.pathSeparator+fileName; //if we simply concat then spring will not understand so we use File.seperator

        //create file Object
        File f = new File(path);
        if(!f.exists()){
            //so if the path is not there we need to create it
            f.mkdir();
        }

        // upload file to path
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String filename) throws FileNotFoundException {
        String filePath = path+ File.pathSeparator+filename;
        return new FileInputStream(filePath);
    }
}
