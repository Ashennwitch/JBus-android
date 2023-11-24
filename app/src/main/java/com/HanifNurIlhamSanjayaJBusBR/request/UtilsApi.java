package com.HanifNurIlhamSanjayaJBusBR.request;

public class UtilsApi {
    public static final String BASE_URL_API = "http://192.168.137.1:5000/";
    public static BaseApiService getApiService() {
        return
                RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
