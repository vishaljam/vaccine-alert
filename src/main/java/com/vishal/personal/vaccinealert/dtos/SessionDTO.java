package com.vishal.personal.vaccinealert.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SessionDTO {

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("available_capacity")
    private Integer totalVaccinesAvailable;

    @JsonProperty("min_age_limit")
    private Integer minAgeLimit;

    @JsonProperty("vaccine")
    private String vaccine;

    @JsonProperty("slots")
    private Set<String> slots;

    @JsonProperty("available_capacity_dose1")
    private Integer availableForDose1;

    @JsonProperty("available_capacity_dose2")
    private Integer availableForDose2;
}
