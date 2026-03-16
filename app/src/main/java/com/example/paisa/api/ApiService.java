package com.example.paisa.api;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("query")
    Call<JsonObject> getGlobalQuote(
        @Query("function") String function,
        @Query("symbol") String symbol,
        @Query("apikey") String apiKey
    );

    @GET("query")
    Call<JsonObject> getSymbolSearch(
        @Query("function") String function,
        @Query("keywords") String keywords,
        @Query("apikey") String apiKey
    );

    @GET("query")
    Call<JsonObject> getTimeSeriesDaily(
        @Query("function") String function,
        @Query("symbol") String symbol,
        @Query("apikey") String apiKey
    );
}