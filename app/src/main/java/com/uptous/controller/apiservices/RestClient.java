package com.uptous.controller.apiservices;


import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by prakash .
 */
public class RestClient {


    private APIServices apiServices;

    public RestClient(String url) {


//        "asmithutu@gmail.com:alpha123"
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
//                String credentials = "asmithutu@gmail.com" + ":" + "alpha123";
//                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Request request = original.newBuilder()
                        .header("Content-Type", "application/x-www-form-urlencoded")
//                        .addHeader("Authorization", "Basic " + base64EncodedCredentials)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        apiServices = retrofit.create(APIServices.class);
    }

    public APIServices getApiService() {
        return apiServices;
    }
}
