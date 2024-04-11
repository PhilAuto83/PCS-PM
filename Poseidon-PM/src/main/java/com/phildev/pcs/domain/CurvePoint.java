package com.phildev.pcs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Min(value=1, message = "Minimum value must be 1")
    private Integer CurveId;
    private Timestamp asOfDate;
    @DecimalMin(value = "1.00", inclusive = true)
    private Double term;
    @DecimalMin(value = "1.00", inclusive = true)
    private Double value;
    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(Integer curveId, Double term, Double value) {
        CurveId = curveId;
        this.term = term;
        this.value = value;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getCurveId() {
        return CurveId;
    }

    public void setCurveId(int curveId) {
        CurveId = curveId;
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

    public void setTerm(double term) {
        this.term = term;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
}
