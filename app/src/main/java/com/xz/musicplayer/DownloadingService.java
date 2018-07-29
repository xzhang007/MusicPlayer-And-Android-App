package com.xz.musicplayer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadingService {
    @GET
    Call<List<MusicList>> downloadMusicListWithDynamicUrlSync(
            @Url String fileUrl);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(
            @Url String fileUrl);

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://musicplayer780.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}