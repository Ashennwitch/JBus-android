package com.HanifNurIlhamSanjayaJBusBR.request;

import com.HanifNurIlhamSanjayaJBusBR.model.BaseResponse;
import com.HanifNurIlhamSanjayaJBusBR.model.Account;
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
}
