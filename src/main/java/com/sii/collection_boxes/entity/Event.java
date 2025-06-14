package com.sii.collection_boxes.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="event",
        uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Event {
    @Id
    @Column(nullable=false, unique = true)
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

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
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
