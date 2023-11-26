package com.biybiruza.quoteapp.networking;

import com.biybiruza.quoteapp.data.QuoteData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("quotes/random")
    Call<QuoteData> getQuotes();
}
