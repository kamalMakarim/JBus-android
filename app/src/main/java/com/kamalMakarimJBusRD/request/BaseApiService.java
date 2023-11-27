package com.kamalMakarimJBusRD.request;
import com.kamalMakarimJBusRD.model.Account;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.BusType;
import com.kamalMakarimJBusRD.model.Facility;
import com.kamalMakarimJBusRD.model.Renter;
import com.kamalMakarimJBusRD.model.Station;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<BaseResponse<Account>> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login (
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp (
            @Path("id") int id,
            @Query("amount") double amount);

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter (
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("PhoneNumber") String PhoneNumber);
    @GET("bus/getMyBus")
    Call<BaseResponse<List<Bus>>> getMyBus (
            @Query("accountId") int accountId);

    @POST("bus/create")
    Call<BaseResponse<Bus>> createBus (
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    @GET("station/getAll")
    Call<List<Station>> getAllStation();
}
