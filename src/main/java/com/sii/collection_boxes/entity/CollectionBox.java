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

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ElementCollection
    @CollectionTable(
            name = "collection_box_balance",
            joinColumns = @JoinColumn(name = "box_id")
    )
    @MapKeyColumn(name = "currency")
    @Column(name = "amount", precision = 19, scale = 2)
    Map <String, BigDecimal> balance = new HashMap<>();

    public CollectionBox(){
        this.event = null;
        this.balance.put("PLN", BigDecimal.ZERO);
        this.balance.put("USD", BigDecimal.ZERO);
        this.balance.put("EUR", BigDecimal.ZERO);
    }

    public boolean isEmpty() {
        return balance.values().stream()
                .allMatch(amount -> amount.compareTo(BigDecimal.ZERO) == 0);
    }

    public boolean isAssigned() {
        return event != null;
    }

    public Event getEvent() {
        return event;
    }

    public Map<String, BigDecimal> getBalance() {
        return balance;
    }

    public BigDecimal getBalance(String currency) {
        return balance.get(currency);
    }

    public void clearBalance(){
        balance.replaceAll((currency, amount) -> BigDecimal.ZERO);
    }

    public Long getId() {
        return id;
    }

    public void addAmount(BigDecimal amount, String currency) {
        this.balance.put(currency, balance.get(currency).add(amount));
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
