package com.HanifNurIlhamSanjayaJBusBR.request;

import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.model.Account;
import com.HanifNurIlhamSanjayaJBusBR.model.Bus;
import com.HanifNurIlhamSanjayaJBusBR.model.BusType;
import com.HanifNurIlhamSanjayaJBusBR.model.Facility;
import com.HanifNurIlhamSanjayaJBusBR.model.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {
    @GET("account/{id}")
    Call<Account> getAccountbyId (@Path("id") int id);

    @POST("account/register")
    Call<BaseResponse<Account>> register (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/login")
    Call<BaseResponse<Account>> login (
            @Query("email") String email,
            @Query("password") String password);

    @POST("account/topUp")
    Call<BaseResponse<Double>> topUp (
            @Query("id") int id,
            @Query("amount") double amount);

    @POST("account/registerRenter")
    Call<BaseResponse<Account>> registerRenter(
            @Query ("id") int id,
            @Query ("companyName") String companyName,
            @Query ("address") String address,
            @Query ("phoneNumber") String phoneNumber);

    @GET("station/getAll")
    Call<List<Station>> getAllStation();

    @POST("bus/create")
    Call<BaseResponse<Account>> create (
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId);

    @GET("bus/getAll")
    Call<List<Bus>> getAllBus();

    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId);

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(@Query("busId") int busId,
                                        @Query("time") String time);

    @GET("bus/total")
    Call<Integer> numberOfBuses();

    @GET("bus/page")
    Call<List<Bus>> getBus(@Query("page") int page, @Query("size") int pageSize);

    @POST("bus/create")
    Call<BaseResponse<Bus>> addBus(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facilities") List<Facility> facilities,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    @GET("bus/{id}")
    Call<Bus> getBusbyId(@Path("id") int busId);

}
