package com.actimel.celibrary.controllers;

import com.actimel.celibrary.models.Comment;
import com.actimel.celibrary.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.actimel.celibrary.service.FileStorageService;

//Up & DOwn files
import java.util.Random;
import org.springframework.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Controller
@RequestMapping(path="/api/comment")
public class CommentController {
    
    private final CommentRepository commentRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommentRepository.class);

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/downloadFile/{id}")
    public @ResponseBody ResponseEntity < Resource > downloadFile(@PathVariable("id") long id, HttpServletRequest request) {

        Comment newComment = commentRepository.findById(id).get();
        String fileName = newComment.getPhoto_name();

        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    @PostMapping("/uploadFile/{id}")
    public @ResponseBody Comment uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") long id ) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        System.out.println(fileName);
        Random random = new Random();
        int randomIndex = random.nextInt(200);
        String fileNameNew = "comment_"+id+"_"+randomIndex+fileName;
        Comment newComment = commentRepository.findById(id).get();
        
       fileStorageService.storeFile(file, fileNameNew);
		
            
            String fileDownloadUriNew = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/upload/")
            .path(fileNameNew)
            .toUriString();

            newComment.setPhoto_name(fileNameNew);
            final Comment updatedComment = commentRepository.save(newComment);
            return newComment;
            // new Response(fileName, fileDownloadUri,file.getContentType(), file.getSize(), "user_");
        }


    @GetMapping("/all")
    public @ResponseBody Iterable<Comment> getAll(){
        return commentRepository.findAll();
    }
    
     @PostMapping("/add")
     public @ResponseBody Comment addCommentJSON(@RequestBody Comment comment) {        
        return commentRepository.save(comment);
    }
    
    @GetMapping("/delete/{id}")
    public @ResponseBody String deleteCommentJSON(@PathVariable("id") long id) {
        Comment comment = commentRepository.findById(id).get();
        commentRepository.delete(comment);
        return "deleted";
    }
}