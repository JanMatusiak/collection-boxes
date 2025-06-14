package com.sii.collection_boxes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String currency;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    protected Event(){}

    public Event(String name, String currency){
        this.name = name;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public Long getId() {
        return id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void addToBalance(BigDecimal amount){
        setBalance(this.balance.add(amount));
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
