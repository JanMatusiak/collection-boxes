package com.sii.collection_boxes.dto;

import com.sii.collection_boxes.entity.Event;

public class CreateEventDTO {
    private String name;

    private String currency;

    public CreateEventDTO(){}

    public CreateEventDTO(String name, String currency){
        this.name = name;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Event toEvent(){
        return new Event(this.getName(), this.getCurrency());
    }
}
