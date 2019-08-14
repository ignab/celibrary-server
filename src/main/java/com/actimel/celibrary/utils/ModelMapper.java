package com.actimel.celibrary.utils;

import com.actimel.celibrary.models.Comment;
import com.actimel.celibrary.models.Place;
import com.actimel.celibrary.models.User;
import com.actimel.celibrary.payload.CommentResponse;
import com.actimel.celibrary.payload.PlaceResponse;
import com.actimel.celibrary.payload.UserSummary;

import com.actimel.celibrary.models.Event;

import com.actimel.celibrary.payload.EventResponse;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ModelMapper {

    public static PlaceResponse mapPlaceToPlaceResponse(Place place, /*Map<Long, Long> choiceVotesMap, */ User creator/*, Long userVote*/) {
        PlaceResponse placeResponse = new PlaceResponse();
        placeResponse.setId(place.getId());
        placeResponse.setName(place.getName());
        placeResponse.setCreationDateTime(place.getCreatedAt());
        placeResponse.setLatitude(place.getLatitude());
        placeResponse.setLongitude(place.getLongitude());
        //placeResponse.setExpirationDateTime(poll.getExpirationDateTime());
        //Instant now = Instant.now();
        //placeResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        // List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
        //     ChoiceResponse choiceResponse = new ChoiceResponse();
        //     choiceResponse.setId(choice.getId());
        //     choiceResponse.setText(choice.getText());

        //     if(choiceVotesMap.containsKey(choice.getId())) {
        //         choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
        //     } else {
        //         choiceResponse.setVoteCount(0);
        //     }
        //     return choiceResponse;
        // }).collect(Collectors.toList());

        // placeResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        placeResponse.setCreatedBy(creatorSummary);

        // if(userVote != null) {
        //     placeResponse.setSelectedChoice(userVote);
        // }

        // long totalVotes = placeResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        // placeResponse.setTotalVotes(totalVotes);

        return placeResponse;
    }

    public static PlaceResponse mapPlaceToPlaceResponse(Place place, User creator, List<Comment> comments/*, Long userVote*/) {
        PlaceResponse placeResponse = new PlaceResponse();
        placeResponse.setId(place.getId());
        placeResponse.setName(place.getName());
        placeResponse.setCreationDateTime(place.getCreatedAt());
        placeResponse.setLatitude(place.getLatitude());
        placeResponse.setLongitude(place.getLongitude());
        //placeResponse.setExpirationDateTime(poll.getExpirationDateTime());
        //Instant now = Instant.now();
        //placeResponse.setExpired(poll.getExpirationDateTime().isBefore(now));
        
        List<CommentResponse> commentResponses = comments.stream().map(comment -> {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setId(comment.getId());
                commentResponse.setText(comment.getText());
                return commentResponse;
        }).collect(Collectors.toList());

        placeResponse.setComments(commentResponses);
      
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        placeResponse.setCreatedBy(creatorSummary);

        // if(userVote != null) {
        //     placeResponse.setSelectedChoice(userVote);
        // }

        // long totalVotes = placeResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        // placeResponse.setTotalVotes(totalVotes);

        return placeResponse;
    }
    
    public static EventResponse mapEventToEventResponse(Event event, /*Map<Long, Long> choiceVotesMap, */ User creator/*, Long userVote*/) {
        EventResponse eventResponse = new EventResponse();
        eventResponse.setId(event.getId());
        eventResponse.setName(event.getName());
        eventResponse.setCreationDateTime(event.getCreatedAt());
        eventResponse.setPlaces_id(event.getPlaces_id());
        eventResponse.setDate(event.getDate());
        //eventResponse.setExpirationDateTime(poll.getExpirationDateTime());
        //Instant now = Instant.now();
        //eventResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        // List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
        //     ChoiceResponse choiceResponse = new ChoiceResponse();
        //     choiceResponse.setId(choice.getId());
        //     choiceResponse.setText(choice.getText());

        //     if(choiceVotesMap.containsKey(choice.getId())) {
        //         choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
        //     } else {
        //         choiceResponse.setVoteCount(0);
        //     }
        //     return choiceResponse;
        // }).collect(Collectors.toList());

        // eventResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        eventResponse.setCreatedBy(creatorSummary);

        // if(userVote != null) {
        //     eventResponse.setSelectedChoice(userVote);
        // }

        // long totalVotes = eventResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        // eventResponse.setTotalVotes(totalVotes);

        return eventResponse;
    }
}