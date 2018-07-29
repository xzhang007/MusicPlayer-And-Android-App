package com.xz.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String MUSIC_LIST = "musiclist/musicList.json";
    private String[] titles = null;
    private String[] singers = null;
    private String[] albums = null;
    private String[] lengths = null;
    private String[] years = null;
    private String[] images = null;
    private int[] durations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myOnClick(View view) {
        try {
            DownloadingService downloadingService = DownloadingService.retrofit.create(DownloadingService.class);
            final Call<List<MusicList>> call =
                    downloadingService.downloadMusicListWithDynamicUrlSync(MUSIC_LIST);

            call.enqueue(new Callback<List<MusicList>>() {
                @Override
                public void onResponse(Call<List<MusicList>> call, Response<List<MusicList>> response) {
                    int size = response.body().size();
                    titles = new String[size];
                    singers = new String[size];
                    albums = new String[size];
                    lengths = new String[size];
                    years = new String[size];
                    images = new String[size];
                    durations = new int[size];
                    for (int i = 0; i < titles.length; i++) {
                        images[i] = response.body().get(i).getImage();
                        titles[i] = response.body().get(i).getTitle();
                        singers[i] = response.body().get(i).getSinger();
                        albums[i] = response.body().get(i).getAlbum();
                        lengths[i] = response.body().get(i).getLength();
                        years[i] = response.body().get(i).getYear();
                        durations[i] = response.body().get(i).getDuration();
                        //Log.d(LOG_TAG, "" + durations[i]);
                    }


                    Intent intent = new Intent(MainActivity.this, MusicListActivity.class);
                    intent.putExtra("com.xz.musicplayer.IMAGES", images);
                    intent.putExtra("com.xz.musicplayer.TITLES", titles);
                    intent.putExtra("com.xz.musicplayer.SINGERS", singers);
                    intent.putExtra("com.xz.musicplayer.ALBUMS", albums);
                    intent.putExtra("com.xz.musicplayer.LENGTHS", lengths);
                    intent.putExtra("com.xz.musicplayer.YEARS", years);
                    intent.putExtra("com.xz.musicplayer.DURATIONS", durations);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<List<MusicList>> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }
}
