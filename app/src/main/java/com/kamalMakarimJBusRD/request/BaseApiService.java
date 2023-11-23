package com.kamalMakarimJBusRD.request;
import com.kamalMakarimJBusRD.model.Account;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);
}
