package com.phildev.pcs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "bidlist")
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bidListId;

    @NotBlank(message ="account cannot be null or empty")
    private String account;
    @NotBlank(message ="type cannot be null or empty")
    private String type;
    @DecimalMin(value = "1.00", message = "minimum bid quantity must be 1.0, only 2 digits allowed after decimal point")
    @DecimalMax(value = "1000000.00", message="maximum bid quantity should be 1 000 000, only 2 digits allowed after decimal point")
    @NotNull(message="bid quantity cannot be null")
    private Double bidQuantity;
    private Double askQuantity;
    private Double bid;
    private Double ask;
    private String benchmark;
    private Timestamp bidListDate;
    private String commentary;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Time revisionDate;
   private String  dealName;
    private String dealType;
    private String sourceListId;
    private String side;


    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    public BidList(){}

}
