package com.vishal.personal.vaccinealert.configurations;

import com.vishal.personal.vaccinealert.service.CovinApiService;
import com.vishal.personal.vaccinealert.service.TelegramApiService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class RetrofitConfigurations {

    @Bean
    public CovinApiService getCovinApiService() {
        log.info("Creating covin API caller");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cdn-api.co-vin.in/")
                .client(getOkHttpClient())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(CovinApiService.class);
    }

    @Bean
    public TelegramApiService getTelegramApiService() {
        log.info("Creating Telegram API caller");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.telegram.org/")
                .client(getOkHttpClient())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(TelegramApiService.class);
    }

    private OkHttpClient getOkHttpClient() {
        log.info("Creating OKHTTP client");
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public static class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            };
        }
    }
}
