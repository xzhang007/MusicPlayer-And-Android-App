package com.xz.musicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static String[] myTitles;
    private static String[] mySingers;
    private static String[] myAlbums;
    private static String[] myLengths;
    private static String[] myYears;
    private static String[] myImages;
    private static int[] myDurations;
    private static final String LOG_TAG = MusicListActivity.class.getSimpleName();
    private static final String HOST_IMAGE_URI = "http://musicplayer780.com/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        Intent intent = getIntent();
        myImages = intent.getStringArrayExtra("com.xz.musicplayer.IMAGES");
        myTitles = intent.getStringArrayExtra("com.xz.musicplayer.TITLES");
        mySingers = intent.getStringArrayExtra("com.xz.musicplayer.SINGERS");
        myAlbums = intent.getStringArrayExtra("com.xz.musicplayer.ALBUMS");
        myLengths = intent.getStringArrayExtra("com.xz.musicplayer.LENGTHS");
        myYears = intent.getStringArrayExtra("com.xz.musicplayer.YEARS");
        myDurations = intent.getIntArrayExtra("com.xz.musicplayer.DURATIONS");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter(myTitles, mySingers, myAlbums, myLengths, myYears, myImages, myDurations));
    }

    private class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleTextView;
        public TextView mSingerTextView;
        public TextView mAlbumTextView;
        public TextView mLengthTextView;
        public TextView mYearTextView;
        public ImageView mImageView;
        public int mDuration;

        public ListViewHolder(View v) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.musiclist_title);
            mSingerTextView = (TextView) v.findViewById(R.id.musiclist_singer);
            mAlbumTextView = (TextView) v.findViewById(R.id.musiclist_album);
            mLengthTextView = (TextView) v.findViewById(R.id.musiclist_length);
            //mYearTextView = (TextView) v.findViewById(R.id.musiclist_year);
            mImageView = (ImageView) v.findViewById(R.id.musiclist_image);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MusicListActivity.this, PlayingActivity.class);
                    intent.putExtra("com.xz.musicplayer.MUSICNAME", mTitleTextView.getText().toString());
                    intent.putExtra("com.xz.musicplayer.DURATION", mDuration);
                    Log.d(LOG_TAG, mTitleTextView.getText().toString());
                    startActivity(intent);
                }
            });
        }

        public void bindView(String title, String singer, String album, String length, String year, String image, int duration) {
            //Log.d(LOG_TAG, "000 " + image);
            Glide.with(MusicListActivity.this.getBaseContext())
                    .load(HOST_IMAGE_URI + image)
                    .override(250, 200)
                    //.centerCrop()
                    //.asBitmap()
                    //.format(DecodeFormat.PREFER_RGB_565)
                    //.placeholder(R.drawable.placeholder)
                    //.error(R.drawable.imagenotfound)
                    .into(mImageView);

            mTitleTextView.setText(title);
            mSingerTextView.setText("Singer: " + singer);
            mAlbumTextView.setText("Album: " + album);
            mLengthTextView.setText("Length: " + length);
            mDuration = duration;
            //mYearTextView.setText("Year: " + year);

            //mImageView.setImageURI(Uri.parse("http://musicplayer780.com/music/" + image));
            //downloadImage(image);
            //Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + image);
            //mImageView.setImageBitmap(bitmap);
            //Uri uri = Uri.parse("http://musicplayer780.com/music/" + image);


        }
    }



    private class MyAdapter extends RecyclerView.Adapter<ListViewHolder> {

        private String[] mTitles;
        private String[] mSingers;
        private String[] mAlbums;
        private String[] mLengths;
        private String[] mYears;
        private String[] mImages;
        private int[] mDurations;

        private MyAdapter(String[] myTitles, String[] mySingers, String[] myAlbums, String[] myLengths, String[] myYears, String[] myImages, int[] myDurations) {
            mTitles = myTitles;
            mSingers = mySingers;
            mAlbums = myAlbums;
            mLengths = myLengths;
            mYears = myYears;
            mImages = myImages;
            mDurations = myDurations;
        }

        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.music_list_item, parent, false);

            return new ListViewHolder(v);
            //return new ViewHolder(new TextView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.bindView(mTitles[position], mSingers[position], mAlbums[position], mLengths[position], mYears[position], mImages[position], mDurations[position]);
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }
    }
}
