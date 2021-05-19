package com.vishal.personal.vaccinealert.service;

import com.vishal.personal.vaccinealert.dtos.CenterDTO;
import com.vishal.personal.vaccinealert.dtos.FetchSlotResponseDTO;
import com.vishal.personal.vaccinealert.dtos.MessageInformationDTO;
import com.vishal.personal.vaccinealert.dtos.SessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@EnableScheduling
public class VaccineAlertService {

    @Autowired
    private CovinApiService covinApiService;

    @Autowired
    private TelegramApiService telegramApiService;

    @Value("${property.covin.district.id:388}")
    private String filterByDistrictId;

    @Value("${property.covin.vaccine:COVAXIN}")
    private String filterByVaccine;

    @Value("${property.covin.filter.age:45}")
    private Integer filterByAgeGroup;

    @Value("${property.telegram.channelId:@joinMyVaccineAlertChannel}")
    private String telegramChannelId;


    private FetchSlotResponseDTO getVaccineSlots() throws IOException {
        Date today = new Date();
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String todayAsString = df.format(today);
        Call<FetchSlotResponseDTO> fetchSlotResponseDTOCall = covinApiService.fetchSlotsByDistrictAndDate(filterByDistrictId, todayAsString, filterByVaccine, getRandomString());
        Response<FetchSlotResponseDTO> fetchSlotResponseDTOResponse = fetchSlotResponseDTOCall.execute();
        return fetchSlotResponseDTOResponse.body();
    }

    private String getRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    @Scheduled(fixedRate = 7000)
    public void fetchSlotAndSendVaccineAlert() throws IOException {
        FetchSlotResponseDTO fetchSlotResponseDTO = getVaccineSlots();
        Set<MessageInformationDTO> messageInformationDTOS = filterSlotsAsPerCriteria(fetchSlotResponseDTO);
        Set<String> telegramMessages = prepareTelegramMessageFromMessageInformationDTO(messageInformationDTOS);
        if (!CollectionUtils.isEmpty(telegramMessages)) {
            for (String telegramMessage : telegramMessages) {
                sendMessageToTelegram(telegramMessage);
            }
        } else {
            log.info("Vaccines not available in given criteria");
        }
        /*String center = fetchSlotResponseDTO.getCenters().stream().findFirst().get().getName();
        sendMessageToTelegram("Vaccine available at center: " + center);*/
    }

    private Set<MessageInformationDTO> filterSlotsAsPerCriteria(FetchSlotResponseDTO fetchSlotResponseDTO) {
        Set<MessageInformationDTO> messageInformationDTOS = new HashSet<>();
        if (!CollectionUtils.isEmpty(fetchSlotResponseDTO.getCenters())) {
            Set<CenterDTO> centerDTOS = fetchSlotResponseDTO.getCenters();
            for (CenterDTO centerDTO:centerDTOS) {
                if (!CollectionUtils.isEmpty(centerDTO.getSessions())) {
                    Set<SessionDTO> sessionDTOS = centerDTO.getSessions();
                    for (SessionDTO sessionDTO: sessionDTOS) {
                        if (sessionDTO.getMinAgeLimit() >= filterByAgeGroup && sessionDTO.getVaccine().equals(filterByVaccine) && sessionDTO.getTotalVaccinesAvailable() >= 1) {
                            MessageInformationDTO messageInformationDTO = new MessageInformationDTO();
                            messageInformationDTO.setBlock(centerDTO.getBlock());
                            messageInformationDTO.setDistrict(centerDTO.getDistrict());
                            messageInformationDTO.setCenterName(centerDTO.getName());
                            messageInformationDTO.setPinCode(centerDTO.getPincode());
                            messageInformationDTO.setAgeGroup(sessionDTO.getMinAgeLimit());
                            messageInformationDTO.setDate(sessionDTO.getDate());
                            messageInformationDTO.setTotalAvailableDose(sessionDTO.getTotalVaccinesAvailable());
                            messageInformationDTO.setDoseAvailableForFirstDose(sessionDTO.getAvailableForDose1());
                            messageInformationDTO.setDoseAvailableForSecondDose(sessionDTO.getAvailableForDose2());
                            messageInformationDTO.setVaccineName(sessionDTO.getVaccine());
                            messageInformationDTOS.add(messageInformationDTO);
                        }
                    }
                }
            }
        }
        return messageInformationDTOS;
    }

    private Set<String> prepareTelegramMessageFromMessageInformationDTO(Set<MessageInformationDTO> messageInformationDTOS) {
        Set<String> telegramMessages = new HashSet<>();
        if (!CollectionUtils.isEmpty(messageInformationDTOS)) {
            for (MessageInformationDTO messageInformationDTO : messageInformationDTOS) {
                StringBuilder telegramMessageBuilder = new StringBuilder();
                telegramMessageBuilder.append("District: " + messageInformationDTO.getDistrict() + "\n");
                telegramMessageBuilder.append("Center name: " + messageInformationDTO.getCenterName() + "\n");
                telegramMessageBuilder.append("Block: " + messageInformationDTO.getBlock() + "\n");
                telegramMessageBuilder.append("Pin code: " + messageInformationDTO.getPinCode() + "\n");
                telegramMessageBuilder.append("Age: " + messageInformationDTO.getAgeGroup() + "\n");
                telegramMessageBuilder.append("Date: " + messageInformationDTO.getDate() + "\n");
                telegramMessageBuilder.append("Total Available: " + messageInformationDTO.getTotalAvailableDose() + "\n");
                telegramMessageBuilder.append("Available for 1st dose: " + messageInformationDTO.getDoseAvailableForFirstDose() + "\n");
                telegramMessageBuilder.append("Available for 2nd dose: " + messageInformationDTO.getDoseAvailableForSecondDose() + "\n");
                telegramMessageBuilder.append("Vaccine: " + messageInformationDTO.getVaccineName() + "\n");
                telegramMessages.add(telegramMessageBuilder.toString() + "\n");
            }
        }
        return telegramMessages;
    }

    private void sendMessageToTelegram(String text) throws IOException {
        Call<Object> objectCall = telegramApiService.sendMessageToTelegramChannel(telegramChannelId, text);
        objectCall.execute();
    }

}
