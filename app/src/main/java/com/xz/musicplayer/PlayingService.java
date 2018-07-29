package com.xz.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.io.IOException;

import static com.xz.musicplayer.PlayingActivity.getSeekBar;

public class PlayingService extends Service {

    private static final String TAG = PlayingService.class.getSimpleName();
    private static final String URL_PREFIX = "http://musicplayer780.com/music/";
    private static MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String path = intent.getStringExtra("com.xz.musicplayer.MUSICNAME");
        String musicFileName = convert(path);
        String url = URL_PREFIX + musicFileName + ".mp3";
        Log.d(TAG, url);

        PlayingThread pt = new PlayingThread(url);
        new Thread(pt).start();

        return super.onStartCommand(intent, flags, startId);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
           /* Runnable r = new Runnable() {
                @Override
                public void run() {
                    PlayingActivity.getSeekBar().setProgress(mMediaPlayer.getCurrentPosition() / 1000);
                }
            };*/

        }
    }

    public static MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    private String convert(String path) {
        String str = path.replaceAll(" ", "%20");
        //Log.d(TAG, str);
        return str;
    }
}
