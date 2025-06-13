package com.sii.collection_boxes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CollectionBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean empty;
    private boolean assigned;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private BigDecimal amount;

    public CollectionBox(){
        this.empty = true;
        this.assigned = false;
        this.event = null;
        this.amount = BigDecimal.ZERO;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public Event getEvent() {
        return event;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }
}
