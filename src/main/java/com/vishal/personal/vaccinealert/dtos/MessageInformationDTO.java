package com.vishal.personal.vaccinealert.dtos;

import lombok.Data;

@Data
public class MessageInformationDTO {

    private String district;

    private String centerName;

    private Integer ageGroup;

    private Integer pinCode;

    private String date;

    private Integer totalAvailableDose;

    private Integer doseAvailableForFirstDose;

    private Integer doseAvailableForSecondDose;

    private String vaccineName;

    private String block;
}
