package com.phildev.pcs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "rulename")
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @NotBlank(message = "name cannot be null or empty")
    private String name;
    @NotBlank(message = "description cannot be null or empty")
    private String description;
    @NotBlank(message = "json cannot be null or empty")
    private String json;
    @NotBlank(message = "template cannot be null or empty")
    private String template;
    @NotBlank(message = "sqlStr cannot be null or empty")
    private String sqlStr;
    @NotBlank(message = "sqlPart cannot be null or empty")
    private String sqlPart;

    public RuleName() {
    }

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

}
