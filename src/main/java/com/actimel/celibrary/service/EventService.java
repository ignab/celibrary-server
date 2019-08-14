package com.actimel.celibrary.service;

import com.actimel.celibrary.exceptions.BadRequestException;
import com.actimel.celibrary.exceptions.ResourceNotFoundException;
import com.actimel.celibrary.models.*;
import com.actimel.celibrary.payload.PagedResponse;
import com.actimel.celibrary.payload.EventRequest;
import com.actimel.celibrary.payload.EventResponse;
import com.actimel.celibrary.repositories.CommentRepository;
//import com.actimel.celibrary.payload.VoteRequest;
import com.actimel.celibrary.repositories.EventRepository;
import com.actimel.celibrary.repositories.UserRepository;
//import com.actimel.celibrary.repositories.VoteRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public PagedResponse<EventResponse> getAllEvents(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Event> events = eventRepository.findAll(pageable);

        if(events.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), events.getNumber(),
                    events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
        }

        // Map events to PollResponses containing vote counts and poll creator details
        List<Long> eventIds = events.map(Event::getId).getContent();
        //Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(eventIds);
        //Map<Long, Long> eventUserVoteMap = getEventUserVoteMap(currentUser, eventIds);
        Map<Long, User> creatorMap = getEventCreatorMap(events.getContent());

        List<EventResponse> eventResponses = events.map(event -> {
            return ModelMapper.mapEventToEventResponse(event,
                    //choiceVoteCountMap,
                    creatorMap.get(event.getCreatedBy()));
                    //eventUserVoteMap == null ? null : eventUserVoteMap.getOrDefault(event.getId(), null));
        }).getContent();

        return new PagedResponse<>(eventResponses, events.getNumber(),
                events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
    }

    public PagedResponse<EventResponse> getEventsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all polls created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Event> events = eventRepository.findByCreatedBy(user.getId(), pageable);

        if (events.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), events.getNumber(),
                    events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
        List<Long> eventIds = events.map(Event::getId).getContent();
        // Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
        // Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

        List<EventResponse> eventResponses = events.map(event -> {
            return ModelMapper.mapEventToEventResponse(event,
                    //choiceVoteCountMap,
                    user);
                    //pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
        }).getContent();

        return new PagedResponse<>(eventResponses, events.getNumber(),
            events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
    }

    public Event createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setName(eventRequest.getName());
        event.setPlaces_id(eventRequest.getPlaces_id());
        event.setDate(eventRequest.getDate());
        
        return eventRepository.save(event);
    }

    public EventResponse getEventById(Long eventId, UserPrincipal currentUser) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Poll", "id", eventId));

        User creator = userRepository.findById(event.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", event.getCreatedBy()));

        return ModelMapper.mapEventToEventResponse(event, creator);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    Map<Long, User> getEventCreatorMap(List<Event> events) {
        // Get Poll Creator details of the given list of polls
        List<Long> creatorIds = events.stream()
                .map(Event::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}