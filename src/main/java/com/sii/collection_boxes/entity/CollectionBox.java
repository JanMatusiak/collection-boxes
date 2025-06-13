package com.sii.collection_boxes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "box_balance",
            joinColumns = @JoinColumn(name = "box_id")
    )
    @MapKeyColumn(name = "currency_code")
    @Column(name = "amount", precision = 19, scale = 2)
    Map <String, BigDecimal> balance = new HashMap<>();

    public CollectionBox(){
        this.empty = true;
        this.assigned = false;
        this.event = null;
        this.balance.put("PLN", BigDecimal.ZERO);
        this.balance.put("USD", BigDecimal.ZERO);
        this.balance.put("EUR", BigDecimal.ZERO);
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

    public void clearBalance(){
        for (Map.Entry<String, BigDecimal> entry : balance.entrySet()){
            balance.put(entry.getKey(), BigDecimal.ZERO);
            setEmpty(true);
        }
    }

    public BigDecimal getBalance(String currency) {
        return balance.get(currency);
    }

    public Long getId() {
        return id;
    }

    public void addAmount(BigDecimal amount, String currency) {
        this.balance.put(currency, balance.get(currency).add(amount));
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setEvent(Event event) {
        this.event = event;
        this.assigned = true;
    }
}
