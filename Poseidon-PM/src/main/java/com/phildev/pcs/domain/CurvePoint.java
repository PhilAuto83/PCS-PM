package com.phildev.pcs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Timestamp asOfDate;


    @DecimalMin(value = "1.000", message="minimum value must 1.000")
    @NotNull(message = "term cannot be null")
    private Double term;


    @DecimalMin(value = "1.000", message="minimum value must 1.000")
    @NotNull(message = "value cannot be null")
    private Double value;
    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Double term, Double value) {
        this.term = term;
        this.value = value;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }


    public Timestamp getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Timestamp asOfDate) {
        this.asOfDate = asOfDate;
    }

    public Double getTerm() {
        return term;
    }

    public void setTerm(Double term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
