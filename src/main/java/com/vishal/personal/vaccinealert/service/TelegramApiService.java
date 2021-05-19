package com.vishal.personal.vaccinealert.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TelegramApiService {

    @GET("/bot1812306918:AAFeaEyUmShGhVDXlc3G5aecrQpvmz5SwhA/sendMessage")
    Call<Object> sendMessageToTelegramChannel(@Query("chat_id") String chatId,
                                              @Query("text") String text
    );

}
