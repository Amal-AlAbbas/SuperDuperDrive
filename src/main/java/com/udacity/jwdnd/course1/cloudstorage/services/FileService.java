package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer addFile(File file, Integer userid){
        return fileMapper.addFile(new File(file.getFilename(),file.getContenttype(),
                file.getFilesize(), userid, file.getFiledata()));
    }

    public Integer deleteFile(Integer fileId){
        return fileMapper.deletefile(fileId);
    }

    public List<File> getAllFiles(Integer userid){
        return fileMapper.getFiles(userid);
    }

    public Boolean isFilenameExist(Integer userid, String filename){
        return fileMapper.getFile(userid,filename) != null;
    }

    public File getFile(String filename, Integer userid){
        return fileMapper.getFile(userid,filename);
    }
}
