package com.vishal.personal.vaccinealert.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class CenterDTO {

    @JsonProperty("center_id")
    private Integer centerId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("state_name")
    private String state;

    @JsonProperty("district_name")
    private String district;

    @JsonProperty("block_name")
    private String block;

    @JsonProperty("pincode")
    private Integer pincode;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("sessions")
    private Set<SessionDTO> sessions;
}
