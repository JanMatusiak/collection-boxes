package com.sii.collection_boxes.dto;

import com.sii.collection_boxes.entity.Event;

    public record CreateEventDTO(String name, String currency) {
        public Event toEvent(){
            return new Event(name, currency);
        }
}
