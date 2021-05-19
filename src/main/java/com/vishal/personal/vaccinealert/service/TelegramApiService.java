package com.vishal.personal.vaccinealert.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TelegramApiService {

    @GET("/botBOT_API_KEY/sendMessage")
    Call<Object> sendMessageToTelegramChannel(@Query("chat_id") String chatId,
                                              @Query("text") String text
    );

}
