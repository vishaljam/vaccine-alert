package com.vishal.personal.vaccinealert.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FetchSlotResponseDTO {

    @JsonProperty("centers")
    private Set<CenterDTO> centers;

}
