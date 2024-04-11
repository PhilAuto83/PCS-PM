package com.phildev.pcs.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Pattern(regexp = "A{1,3}[+-]?|B{1,3}[+-]?|C{1,3}[+-]?", message = "Rating must follow agency notation A, B or C like AA, AAA, AAA+ or AAA- for A")
    private String moodysRating;
    @Pattern(regexp = "A{1,3}[+-]?|B{1,3}[+-]?|C{1,3}[+-]?", message = "Rating must follow agency notation A, B or C like AA, AAA, AAA+ or AAA- for A")
    private String sandPRating;
    @Pattern(regexp = "A{1,3}[+-]?|B{1,3}[+-]?|C{1,3}[+-]?", message = "Rating must follow agency notation A, B or C like AA, AAA, AAA+ or AAA- for A")
    private String fitchRating;
    @Min(value=1, message="minimum order number must be 1")
    private int orderNumber;

    public Rating() {
    }

    public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {

        this.Id = id;
    }

    public String getMoodysRating() {
        return moodysRating;
    }

    public void setMoodysRating(String moodysRating) {
        this.moodysRating = moodysRating;
    }

    public String getSandPRating() {
        return sandPRating;
    }

    public void setSandPRating(String sandPRating) {
        this.sandPRating = sandPRating;
    }

    public String getFitchRating() {
        return fitchRating;
    }

    public void setFitchRating(String fitchRating) {
        this.fitchRating = fitchRating;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
