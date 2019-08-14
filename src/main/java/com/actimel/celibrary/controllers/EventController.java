package com.actimel.celibrary.controllers;

// import java.util.Optional;
import com.actimel.celibrary.service.EventService;
import com.actimel.celibrary.models.Event;
import com.actimel.celibrary.repositories.EventRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import com.actimel.celibrary.payload.ApiResponse;
import com.actimel.celibrary.payload.PagedResponse;
import com.actimel.celibrary.payload.EventRequest;
import com.actimel.celibrary.payload.EventResponse;
import com.actimel.celibrary.security.CurrentUser;
import com.actimel.celibrary.security.UserPrincipal;
import com.actimel.celibrary.utils.AppConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

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
import com.actimel.celibrary.service.FileStorageService;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.URI;


@RestController
@RequestMapping(path="/api/events")
public class EventController {
    
    // private final EventRepository eventRepository;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);


    @GetMapping
    public PagedResponse<EventResponse> getEvents(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return eventService.getAllEvents(currentUser, page, size);
    }


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{eventId}")
                .buildAndExpand(event.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Event Created Successfully"));
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long eventId) {
        return eventService.getEventById(eventId, currentUser);
    }

    // ------------------------------------------------
    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/downloadFile/{id}")
    public @ResponseBody ResponseEntity < Resource > downloadFile(@PathVariable("id") long id, HttpServletRequest request) {

        Event newEvent = eventRepository.findById(id).get();
        String fileName = newEvent.getPhoto_name();

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
    public @ResponseBody Event uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("id") long id ) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       
        System.out.println(fileName);
        Random random = new Random();
        int randomIndex = random.nextInt(200);
        String fileNameNew = "event_"+id+"_"+randomIndex+fileName;
        Event newEvent = eventRepository.findById(id).get();
        
       fileStorageService.storeFile(file, fileNameNew);
		
            
            String fileDownloadUriNew = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/upload/")
            .path(fileNameNew)
            .toUriString();

            newEvent.setPhoto_name(fileNameNew);
            final Event updatedEvent = eventRepository.save(newEvent);
            return newEvent;
            // new Response(fileName, fileDownloadUri,file.getContentType(), file.getSize(), "user_");
    }

    @PutMapping("/update/{id}")
    public @ResponseBody Event update(      
        @PathVariable(value = "id") long id, @Valid @RequestBody Event newEvent) {
        Event event = eventRepository.findById(id).get();

        event.setName(newEvent.getName());
        event.setPlaces_id(newEvent.getPlaces_id());
        event.setDate(newEvent.getDate());
        event.setPhoto_name(newEvent.getPhoto_name());
        
        final Event updatedEvent = eventRepository.save(event);
        return updatedEvent;
    }

    @GetMapping("/delete/{id}")
    public @ResponseBody String delete(@PathVariable("id") long id) {
        Event event = eventRepository.findById(id).get();
        eventRepository.delete(event);
        return "deleted";
    }
   
}