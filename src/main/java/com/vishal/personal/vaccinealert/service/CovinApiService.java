package com.vishal.personal.vaccinealert.service;

import com.vishal.personal.vaccinealert.dtos.FetchSlotResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CovinApiService {

    @GET("/api/v2/appointment/sessions/public/calendarByDistrict")
    Call<FetchSlotResponseDTO> fetchSlotsByDistrictAndDate(@Query("district_id") String districtId,
                                                           @Query("date") String date,
                                                           @Query("vaccine") String vaccine,
                                                           @Header(value = "user-agent") String userAgent
    );
}
