package com.sii.collection_boxes.dto;

import com.sii.collection_boxes.entity.Event;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record CreateEventDTO(String name, String currency) {
        @Contract(value = " -> new", pure = true)
        public @NotNull Event toEvent(){
            return new Event(name, currency);
        }
}
