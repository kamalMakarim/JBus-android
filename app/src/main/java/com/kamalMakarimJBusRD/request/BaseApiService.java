package com.kamalMakarimJBusRD.request;
import com.kamalMakarimJBusRD.model.Account;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.BusType;
import com.kamalMakarimJBusRD.model.Facility;
import com.kamalMakarimJBusRD.model.Payment;
import com.kamalMakarimJBusRD.model.Renter;
import com.kamalMakarimJBusRD.model.Station;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @FormUrlEncoded
    @POST("bus/create")
    Call<BaseResponse<Bus>> createBus (
            @Field("accountId") int accountId,
            @Field("name") String name,
            @Field("capacity") int capacity,
            @Field("facilities") List<Facility> facilities,
            @Field("busType") BusType busType,
            @Field("price") double price,
            @Field("stationDepartureId") int stationDepartureId,
            @Field("stationArrivalId") int stationArrivalId
    );

    @GET("station/getAll")
    Call<List<Station>> getAllStation();
    @GET("bus/getAll")
    Call<List<Bus>> getAllBus();

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule (
            @Query("busId") int busId,
            @Query("time") String time
    );

    @POST("payment/makeBooking")
    Call<BaseResponse<Payment>> makeBooking (
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("busId") int busId,
            @Query("busSeats") List<String> busSeats,
            @Query("departureDate") String departureDate
    );

    @GET("payment/getMyPayment")
    Call<BaseResponse<List<Payment>>> getMyPayment (
            @Query("accountId") int accountId
    );

    @POST("payment/{id}/accept")
    Call<BaseResponse<Payment>> pay (
            @Path("id") int id
    );

    @POST("payment/{id}/cancel")
    Call<BaseResponse<Payment>> cancel (
            @Path("id") int id
    );
}
