package com.xz.musicplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayingActivity extends AppCompatActivity {

    private static final String TAG = PlayingActivity.class.getSimpleName();
    private String path;
    private ImageView playing_image;
    private String imageName;
    private MediaPlayer mMediaPlayer;
    private Intent intent;
    private boolean flag = false;
    private static SeekBar mSeekBar;
    private static final String HOST_IMAGE_URI = "http://musicplayer780.com/images/";
    private static final String LYRICS_FOLDER = "lyrics/";
    public Handler handler;
    private static final String URL_PREFIX = "http://musicplayer780.com/music/";
    public int progressBarStatus = 0;
    public int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        intent = getIntent();
        path = intent.getStringExtra("com.xz.musicplayer.MUSICNAME");
        duration = intent.getIntExtra("com.xz.musicplayer.DURATION", 200);
        imageName = path + ".jpg";
        //Log.d(TAG, "000 " + imageName);

        handler = new Handler();
        playing_image = (ImageView) findViewById(R.id.playing_imgage);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setMax(duration);
        progressBarStatus = 0;
        mSeekBar.setProgress(progressBarStatus);

        downloadLyrics();
        loadImage();
    }

    public void onClick1(View v) {
        if (!flag) {
            Log.d(TAG, path);
            Intent intent = new Intent(PlayingActivity.this, PlayingService.class);

            intent.putExtra("com.xz.musicplayer.MUSICNAME", path);
            //mMediaPlayer = new MediaPlayer();
            //intent.putExtra("com.xz.musicplayer.MEDIAPLAYER", mMediaPlayer);
            //Log.d(TAG, "yes");
            startService(intent);
            flag = true;
           //mMediaPlayer = PlayingService.getmMediaPlayer();
           // mSeekBar.setMax(mMediaPlayer.getDuration());
           // String musicFileName = convert(path);
            //String url = URL_PREFIX + musicFileName + ".mp3";
            /*mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSeekBar.setMax(mMediaPlayer.getDuration());*/
            /*PlayingThread pt = new PlayingThread(url);
            new Thread(pt).start(); */
        } else {
            mMediaPlayer.start();
        }

        new Thread(new Runnable() {
            private boolean mFlag = false;
            @Override
            public void run() {
                if (!mFlag) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mFlag = true;
                }

                mMediaPlayer = PlayingService.getmMediaPlayer();
                while (progressBarStatus < duration) {
                    if (mMediaPlayer == null) {
                        break;
                    }
                    progressBarStatus = mMediaPlayer.getCurrentPosition() / 1000;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSeekBar.setProgress(progressBarStatus);
                        }
                    });
                }
            }
        }).start();
    }

    public void onClick2(View v) {
        //mMediaPlayer = PlayingService.getmMediaPlayer();
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.pause();
    }

    public void onClick3(View v) {
        if (mMediaPlayer == null) {
            //mMediaPlayer = PlayingService.getmMediaPlayer();
            flag = false;
            progressBarStatus = 0;
            mSeekBar.setProgress(progressBarStatus);
            Log.d(TAG, "001 yes");
            return;
        }
        progressBarStatus = 0;
        mSeekBar.setProgress(progressBarStatus);
        mMediaPlayer.stop();
        mMediaPlayer = null;
        Log.d(TAG, "002 yes");
        flag = false;
    }

    private void downloadLyrics() {
        DownloadingService gitHubService = DownloadingService.retrofit.create(DownloadingService.class);
        final Call<ResponseBody> call =
                gitHubService.downloadFileWithDynamicUrlSync(LYRICS_FOLDER + path + ".txt");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final TextView textView = (TextView) findViewById(R.id.lyrics);
                try {
                    // lyricsArr = response.body().string().split("\n");
                    String lyrics = response.body().string();
                    // Log.d(LOG_TAG, "" + titles.length);
                    //for (String s : titles) {
                    //Log.d(LOG_TAG, s);
                    //}

                    //textView.setText(response.body().string());
                    textView.setText(lyrics);
                    //Log.d(LOG_TAG, response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private void loadImage() {
        Glide.with(PlayingActivity.this.getBaseContext())
                .load(HOST_IMAGE_URI + imageName)
                .override(650, 600)
                //.centerCrop()
                //.asBitmap()
                //.format(DecodeFormat.PREFER_RGB_565)
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.imagenotfound)
                .into(playing_image);
    }

    public static SeekBar getSeekBar() {
        return mSeekBar;
    }

    private class PlayingThread implements Runnable {

        private String url;

        PlayingThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
               // mSeekBar.setMax(mMediaPlayer.getDuration());
            } catch (IOException e) {
                e.printStackTrace();
            }
           /*Runnable r = new Runnable() {
                @Override
                public void run() {
                    //mSeekBar.setMax(mMediaPlayer.getDuration());
                    //Log.d(TAG, "" + mMediaPlayer.getDuration());
                    mSeekBar.setMax(10);
                    Log.d(TAG, "" + mMediaPlayer.getCurrentPosition());
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition() / 100);
                }
            };
            handler.postDelayed(r, 1000);*/
        }
    }

    private String convert(String path) {
        String str = path.replaceAll(" ", "%20");
        //Log.d(TAG, str);
        return str;
    }
}
