package com.phildev.pcs.domain;

import jakarta.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private int CurveId;
    private Timestamp asOfDate;
    private double term;
    private double value;
    private Timestamp creationDate;

    public CurvePoint() {
    }

    public CurvePoint(int curveId, double term, double value) {
        CurveId = curveId;
        this.term = term;
        this.value = value;
    }

    public int getId() {
        return Id;
    }

    public int getCurveId() {
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

    public double getTerm() {
        return term;
    }

    public void setTerm(double term) {
        this.term = term;
    }

    public double getValue() {
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
