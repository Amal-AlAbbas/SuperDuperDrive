package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketException;

@Controller
public class FileController {
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public String upload(@RequestParam MultipartFile fileUpload, Authentication authentication, Model model) throws IOException{
        User user = (User) authentication.getPrincipal();

//        // Check if file size exceeds the limit (let's say 10MB)
//        if (fileUpload.getSize() > 10 * 1024 * 1024) { // 10 MB in bytes
//            model.addAttribute("error", true);
//            model.addAttribute("error", "File size exceeds the limit. Please upload a smaller file.");
//            return "redirect:/result?error";
//        }

        File file = new File();
        if(fileUpload.isEmpty()){
            model.addAttribute("error",true);
            model.addAttribute("error","plz choose a file!");
            return "redirect:/result?error";
        }
        if (fileService.isFilenameExist(user.getUserid(),fileUpload.getOriginalFilename())){
           model.addAttribute("error", true);
           model.addAttribute("error", "File name Exist!");
           return "redirect:/result?error";
        }
        file.setUserid(user.getUserid());
        file.setFilesize(Long.toString(fileUpload.getSize()));
        file.setFilename(fileUpload.getOriginalFilename());
        file.setContenttype(fileUpload.getContentType());
        file.setFiledata(fileUpload.getBytes());

        fileService.addFile(file,user.getUserid());
        model.addAttribute("success", true);
        return "redirect:/result?success";
    }

    @GetMapping("/file/delete")
    public String deleteFile(@RequestParam("fileId") Integer fileId, Model model){
        if(fileId > 0){
            fileService.deleteFile(fileId);
            model.addAttribute("success", true);
            return "redirect:/result?success";
        }
        model.addAttribute("error", true);
        model.addAttribute("error", "There is error to delete a file, plz try again!");
        return "redirect:/result?error";
    }


    @GetMapping("/file/download")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename,
                                             HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        File file = fileService.getFile(filename, user.getUserid());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContenttype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFiledata()));
    }

}
