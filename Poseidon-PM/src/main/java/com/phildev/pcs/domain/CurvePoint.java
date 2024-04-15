package com.phildev.pcs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.sql.Timestamp;


@Getter
@Setter
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

}
