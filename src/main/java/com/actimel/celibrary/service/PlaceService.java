package com.actimel.celibrary.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.actimel.celibrary.exceptions.BadRequestException;
import com.actimel.celibrary.exceptions.ResourceNotFoundException;
import com.actimel.celibrary.models.Comment;
import com.actimel.celibrary.models.Place;
import com.actimel.celibrary.models.User;
import com.actimel.celibrary.payload.CommentRequest;
import com.actimel.celibrary.payload.PagedResponse;
import com.actimel.celibrary.payload.PlaceRequest;
import com.actimel.celibrary.payload.PlaceResponse;
import com.actimel.celibrary.repositories.CommentRepository;
import com.actimel.celibrary.repositories.PlaceRepository;
import com.actimel.celibrary.repositories.UserRepository;
import com.actimel.celibrary.security.UserPrincipal;
import com.actimel.celibrary.utils.AppConstants;
import com.actimel.celibrary.utils.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    public PagedResponse<PlaceResponse> getAllPlaces(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Place> places = placeRepository.findAll(pageable);

        if(places.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), places.getNumber(),
                    places.getSize(), places.getTotalElements(), places.getTotalPages(), places.isLast());
        }

        // Map places to placeResponse containing vote counts and poll creator details
        List<Long> placeIds = places.map(Place::getId).getContent();
        Map<Long, User> creatorMap = getPlaceCreatorMap(places.getContent());

        List<PlaceResponse> placeResponses = places.map(place -> {
            return ModelMapper.mapPlaceToPlaceResponse(place,
                    creatorMap.get(place.getCreatedBy()));
        }).getContent();

        return new PagedResponse<>(placeResponses, places.getNumber(),
                places.getSize(), places.getTotalElements(), places.getTotalPages(), places.isLast());
    }

    public PagedResponse<PlaceResponse> getPlacesCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all polls created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Place> places = placeRepository.findByCreatedBy(user.getId(), pageable);

        if (places.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), places.getNumber(),
                    places.getSize(), places.getTotalElements(), places.getTotalPages(), places.isLast());
        }

        // Map Place to PlaceResponse containing vote counts and poll creator details
        List<Long> placeIds = places.map(Place::getId).getContent();

        List<PlaceResponse> placeResponses = places.map(place -> {
            return ModelMapper.mapPlaceToPlaceResponse(place, user);
        }).getContent();

        return new PagedResponse<>(placeResponses, places.getNumber(),
            places.getSize(), places.getTotalElements(), places.getTotalPages(), places.isLast());
    }


    public Place createPlace(PlaceRequest placeRequest) {
        Place place = new Place();
        place.setName(placeRequest.getName());
        place.setLatitude(placeRequest.getLatitude());
        place.setLongitude(placeRequest.getLongitude());
        
        return placeRepository.save(place);
    }

    public PlaceResponse getPlaceById(Long placeId, UserPrincipal currentUser) {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new ResourceNotFoundException("Place", "id", placeId));

        User creator = userRepository.findById(place.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", place.getCreatedBy()));

        List<Comment> comments = getPlaceComments(placeId);

        return ModelMapper.mapPlaceToPlaceResponse(place, creator, comments);
    }


    public PlaceResponse createCommentByPlaceId(Long placeId, UserPrincipal currentUser, CommentRequest commentRequest) {
        
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> new ResourceNotFoundException("Place", "id", placeId));

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setPlacesId(commentRequest.getPlacesId());
        comment.setUsersId(currentUser.getId());
        comment.setPlace(place);

        Comment savedComment = commentRepository.save(comment);  
        
        User creator = userRepository.findById(place.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", place.getCreatedBy()));

       
        List<Comment> comments = getPlaceComments(placeId);

        return ModelMapper.mapPlaceToPlaceResponse(place, creator, comments);
    }


    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public List<Comment> getPlaceComments(List<Long> placeIds) {

        List<Comment> comments = commentRepository.findByPlaceIdIn(placeIds);

        return comments;
    }

    public List<Comment> getPlaceComments(Long placeId) {

        List<Comment> comments = commentRepository.findByPlaceIdIn(placeId);

        return comments;
    }

    Map<Long, User> getPlaceCreatorMap(List<Place> places) {
        // Get Place Creator details of the given list of places
        List<Long> creatorIds = places.stream()
                .map(Place::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}