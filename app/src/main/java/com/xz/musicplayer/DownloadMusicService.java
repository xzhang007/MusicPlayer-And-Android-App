package com.xz.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class DownloadMusicService extends Service {

    private static final String LOG_TAG = DownloadMusicService.class.getSimpleName();
    private String musicFileName;
    private File musicFile;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicFileName = intent.getStringExtra("com.xz.musicplayer.MUSICNAME") + ".mp3";
        //Log.d(LOG_TAG, musicFileName);

        /*DownloadThread dt = new DownloadThread(musicFileName);
        Thread thread = new Thread(dt);
        thread.start();*/
        downloadMusic();
        return super.onStartCommand(intent, flags, startId);
    }

    /*private class DownloadThread implements Runnable {

        private String mFileName;

        DownloadThread(String fileName) {
            this.mFileName = fileName;
        }

        @Override
        public void run() {*/
    public void downloadMusic() {
            DownloadingService downloadingService = DownloadingService.retrofit.create(DownloadingService.class);
            final Call<ResponseBody> call =
                    downloadingService.downloadFileWithDynamicUrlSync(musicFileName);
            //Log.d(LOG_TAG, mFileName);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(LOG_TAG, "server contacted and has file");

                        Intent intent = new Intent(DownloadMusicService.this, PlayingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        musicFile = new File(getExternalFilesDir(null) + File.separator + "1.mp3");
                        intent.putExtra("com.xz.musicplayer.MUSICFILE", musicFile.getAbsolutePath());
                        startActivity(intent);

                        new AsyncTask<Object, Void, Void>() {
                            @Override
                            protected Void doInBackground(Object... params) {
                                boolean writtenToDisk = writeResponseBodyToDisk((Intent) params[0], (ResponseBody) params[1]);

                                Log.d(LOG_TAG, "file download was a success? " + writtenToDisk);

                                return null;
                            }
                        }.execute(intent, response.body());

                    } else {
                        Log.d(LOG_TAG, "server contact failed");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
    }

        private boolean writeResponseBodyToDisk(Intent intent, ResponseBody body) {
            try {
                //Log.d(LOG_TAG, mFileName);
                Log.d(LOG_TAG, musicFile.getAbsolutePath());

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(musicFile);
                    //Log.d(LOG_TAG, "000");

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                        Log.d(LOG_TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    outputStream.flush();

                    return true;
                } catch (IOException e) {
                    //Log.d(LOG_TAG, "1");
                    e.printStackTrace();
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                //Log.d(LOG_TAG, "2");
                return false;
            }
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
