package com.actimel.celibrary.controllers;

import java.net.URI;
import javax.validation.Valid;
import com.actimel.celibrary.models.Place;
import com.actimel.celibrary.payload.ApiResponse;
import com.actimel.celibrary.payload.CommentRequest;
import com.actimel.celibrary.payload.PagedResponse;
import com.actimel.celibrary.payload.PlaceRequest;
import com.actimel.celibrary.payload.PlaceResponse;
import com.actimel.celibrary.repositories.PlaceRepository;
import com.actimel.celibrary.security.CurrentUser;
import com.actimel.celibrary.security.UserPrincipal;
import com.actimel.celibrary.service.PlaceService;
import com.actimel.celibrary.utils.AppConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;
import org.springframework.util.StringUtils;
import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import com.actimel.celibrary.service.FileStorageService;


@RestController
@RequestMapping(path="/api/places")
public class PlaceController {
    
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService placeService;

    private static final Logger logger = LoggerFactory.getLogger(PlaceController.class);


    @GetMapping
    public PagedResponse<PlaceResponse> getPlaces(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return placeService.getAllPlaces(currentUser, page, size);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPlace(@Valid @RequestBody PlaceRequest placeRequest) {
        Place place = placeService.createPlace(placeRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{placeId}")
                .buildAndExpand(place.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Place Created Successfully"));
    }

    @GetMapping("/{placeId}")
    public PlaceResponse getPlaceById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long placeId) {
        return placeService.getPlaceById(placeId, currentUser);
    }


    @PostMapping("/{placeId}/comment")
    public PlaceResponse createCommentByPlaceId(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long placeId, @RequestBody CommentRequest commentRequest) {
        return placeService.createCommentByPlaceId(placeId, currentUser, commentRequest);
    }

    @Autowired
    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/downloadFile/{id}")
    public @ResponseBody ResponseEntity < Resource > downloadFile(@PathVariable("id") long id, HttpServletRequest request) {

        Place newPlace = placeRepository.findById(id).get();
        String fileName = newPlace.getPhoto_name();

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
    public @ResponseBody Place uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") long id ) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        System.out.println(fileName);
        Random random = new Random();
        int randomIndex = random.nextInt(200);
        String fileNameNew = "place_"+id+"_"+randomIndex+fileName;
        Place newPlace = placeRepository.findById(id).get();
        
       fileStorageService.storeFile(file, fileNameNew);
            
            String fileDownloadUriNew = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/upload/")
            .path(fileNameNew)
            .toUriString();

            newPlace.setPhoto_name(fileNameNew);
            final Place updatedPlace = placeRepository.save(newPlace);
            return newPlace;
    }

    // json
    @GetMapping("/all")
    public @ResponseBody Iterable<Place> getAll(){
        return placeRepository.findAll();
    }

    @PostMapping("/add")
    public  @ResponseBody Place add( @RequestBody Place place) {
        return placeRepository.save(place);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody Place update(      
        @PathVariable(value = "id") long id, @Valid @RequestBody Place newPlace) {
        Place place = placeRepository.findById(id).get();

        place.setName(newPlace.getName());
        place.setRanking(newPlace.getRanking());
        place.setRanking_count(newPlace.getRanking_count());
        place.setPhoto_name(newPlace.getPhoto_name());

        final Place updatedPlace = placeRepository.save(place);
        return updatedPlace;
    }

    @GetMapping("/delete/{id}")
    public @ResponseBody String delete(@PathVariable("id") long id) {
        Place place = placeRepository.findById(id).get();
        placeRepository.delete(place);
        return "deleted";
    }
   
}