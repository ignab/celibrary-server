package com.actimel.celibrary.controllers;

import com.actimel.celibrary.exceptions.ResourceNotFoundException;
import com.actimel.celibrary.models.User;
import com.actimel.celibrary.payload.PagedResponse;
import com.actimel.celibrary.payload.PlaceResponse;
import com.actimel.celibrary.payload.UserIdentityAvailability;
import com.actimel.celibrary.payload.UserProfile;
import com.actimel.celibrary.payload.UserSummary;
import com.actimel.celibrary.repositories.PlaceRepository;
import com.actimel.celibrary.repositories.UserRepository;
import com.actimel.celibrary.security.CurrentUser;
import com.actimel.celibrary.security.UserPrincipal;
import com.actimel.celibrary.service.PlaceService;
import com.actimel.celibrary.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.actimel.celibrary.service.FileStorageService;
import java.util.Random;
import org.springframework.util.StringUtils;



@RestController
@RequestMapping(path="/api")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService placeService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/user/downloadFile/{id}")
    public @ResponseBody ResponseEntity < Resource > downloadFile(@PathVariable("id") long id, HttpServletRequest request) {

        User newUser = userRepository.findById(id).get();
        String fileName = newUser.getPhoto_name();

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

    @PostMapping("/user/uploadFile/{id}")
    public @ResponseBody User uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") long id ) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        System.out.println(fileName);
        Random random = new Random();
        int randomIndex = random.nextInt(200);
        String fileNameNew = "user_"+id+"_"+randomIndex+fileName;
        User newUser = userRepository.findById(id).get();
        
       fileStorageService.storeFile(file, fileNameNew);
		
            
            String fileDownloadUriNew = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/upload/")
            .path(fileNameNew)
            .toUriString();

            newUser.setPhoto_name(fileNameNew);
            final User updatedUser = userRepository.save(newUser);
            return newUser;
            // new Response(fileName, fileDownloadUri,file.getContentType(), file.getSize(), "user_");
        }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long placeCount = placeRepository.countByCreatedBy(user.getId());
        
        //long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), placeCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/places")
    public PagedResponse<PlaceResponse> getPlacesCreatedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return placeService.getPlacesCreatedBy(username, currentUser, page, size);
    }
    
}