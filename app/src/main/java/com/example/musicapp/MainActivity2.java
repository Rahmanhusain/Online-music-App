package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.musicapp.Models.Recmodel;
import com.example.musicapp.database.dbhandler;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ArrayList<Recmodel> list=new ArrayList<>();
    dbhandler db=new dbhandler(MainActivity2.this);
    int p;
    ImageView image,prev,next,play,b,song_pic,back;
    LinearLayout upnextsong;

    TextView t,Ltime,Rtime,song_name,song_artist,song_artist_name;
    ProgressBar progressBar;
    SeekBar seekBar;
    private ExoPlayer mediaPlayer;
    Runnable runnable;
    CountDownTimer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }
        image = findViewById(R.id.imageView2);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        play = findViewById(R.id.play);
        seekBar=findViewById(R.id.seekBar);
        t = findViewById(R.id.textView);
        Ltime=findViewById(R.id.Ltime);
        Rtime=findViewById(R.id.Rtime);
        t.setSelected(true);
        progressBar=findViewById(R.id.progressBar);
        b=findViewById(R.id.like);
        song_artist=findViewById(R.id.artist_name);
        song_artist_name=findViewById(R.id.song_ar_name);
        song_name=findViewById(R.id.song_name);
        song_pic=findViewById(R.id.song_pic);
        back=findViewById(R.id.back);
        upnextsong=findViewById(R.id.upnext);
        Intent i = getIntent();
        String pic = i.getStringExtra("rpic");
        String text = i.getStringExtra("rtext");
        Picasso.get().load(pic).fit().into(image);
        t.setText(text);
        list  =i.getParcelableArrayListExtra("list");
        p = i.getIntExtra("rpos", 0);
        mediaPlayer= new ExoPlayer.Builder(MainActivity2.this).build();
        seekBar.setMax(0);
        init(p);
        dbcheck();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.itemcheck(list.get(p).getSongname())){
                    db.delete(list.get(p).getSongname());
                    b.setImageResource(R.drawable.nlike);
                }else {
                    db.Adddata(list.get(p).getPic(), list.get(p).getSongname(), list.get(p).getSongurl(),list.get(p).getSong_artist());
                    b.setImageResource(R.drawable.like);
                    Toast.makeText(MainActivity2.this, "Added to Favourite", Toast.LENGTH_SHORT).show();
                }
            }
        });
       upnext();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    count.cancel();
                    play.setImageResource(R.drawable.play_pause);
                }else {
                    mediaPlayer.play();
                    count.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
                dbcheck();
                upnext();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrev();
                dbcheck();
                upnext();
            }
        });
        upnextsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNext();
                dbcheck();
                upnext();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mediaPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState){
                    case Player.STATE_READY :
                        if(count!=null){
                            count.cancel();
                        }
                        update();
                        break;
                    case Player.STATE_ENDED :
                        count.onFinish();
                        setNext();
                        upnext();
                        break;
                    case Player.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);
                        play.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()||mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (count!=null){
            count.cancel();
        }
        super.onDestroy();
    }
    public void init(int p){

        try{
            MediaItem mediaItem = MediaItem.fromUri(list.get(p).getSongurl());
            mediaPlayer.setMediaItem(mediaItem);
            mediaPlayer.prepare();
            song_artist_name.setText(list.get(p).getSong_artist());
            play.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }
        catch (Exception e){
            Log.d("music",e.toString());

        }
    }
    protected void setNext(){
        seekBar.setSecondaryProgress(0);
        seekBar.setMax(0);
        if (mediaPlayer.isPlaying()||mediaPlayer!=null) {
            mediaPlayer.stop();

        }
        if (count != null) {
            count.cancel();
            count.onFinish();
        }
        play.setImageResource(R.drawable.pause);
        seekBar.setProgress(0);
        if (p==list.size()-1){
            p=0;
            t.setText(list.get(p).getSongname());
            Picasso.get().load(list.get(p).getPic()).fit().into(image);
        }else {
            p += 1;
            t.setText(list.get(p).getSongname());
            Picasso.get().load(list.get(p).getPic()).fit().into(image);

        }
        init(p);
    }
    protected void setPrev(){
        seekBar.setSecondaryProgress(0);
        seekBar.setMax(0);
        if (mediaPlayer.isPlaying()||mediaPlayer!=null) {
            mediaPlayer.stop();

        }
        if (count != null) {
            count.cancel();
            count.onFinish();
        }
        play.setImageResource(R.drawable.pause);
        seekBar.setProgress(0);

        if (p==0){
            p=list.size()-1;
            t.setText(list.get(p).getSongname());
            Picasso.get().load(list.get(p).getPic()).fit().into(image);
        }else {
            p -= 1;
            t.setText(list.get(p).getSongname());
            Picasso.get().load(list.get(p).getPic()).fit().into(image);
        }
        init(p);
    }
    protected void dbcheck(){
        if (db.itemcheck(list.get(p).getSongname())){
            b.setImageResource(R.drawable.like);
        }else{
            b.setImageResource(R.drawable.nlike);
        }
    }
    protected void upnext(){
        if(p==list.size()-1) {
            Picasso.get().load(list.get(0).getPic()).fit().into(song_pic);
            song_name.setText(list.get(0).getSongname());
            song_artist.setText(list.get(0).getSong_artist());
        }
        else{
            Picasso.get().load(list.get(p + 1).getPic()).fit().into(song_pic);
            song_name.setText(list.get(p + 1).getSongname());
            song_artist.setText(list.get(p + 1).getSong_artist());
        }
    }
    protected void rotation(){
        image.setRotation(0);
        image.clearAnimation();
        image.animate().rotation(360f).setDuration(1000);

    }
    public void update(){
        play.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        mediaPlayer.setPlayWhenReady(true);
        rotation();
        seekBar.setMax((int) mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    mediaPlayer.setPlayWhenReady(false);
                    mediaPlayer.seekTo(i);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (mediaPlayer.isPlaying()) {
            count = new CountDownTimer(mediaPlayer.getDuration(), 1000) {

                public void onTick(long millisUntilFinished) {
                    NumberFormat f = new DecimalFormat("00");
                    Ltime.setText(f.format(mediaPlayer.getCurrentPosition() / 1000 / 60) + ":"
                            + f.format(mediaPlayer.getCurrentPosition() / 1000 % 60));
                    Rtime.setText("-" + f.format((mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()) / 1000 / 60) + ":"
                            + f.format((mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()) / 1000 % 60));
                }

                public void onFinish() {
                    Ltime.setText("00:00");
                    Rtime.setText("-00:00");

                }

            };
            count.start();
        }
        if (mediaPlayer.isPlaying()) {
            Handler handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress((int) mediaPlayer.getCurrentPosition(),true);
                    handler.postDelayed(runnable, 500);
                }

            };
            handler.postDelayed(runnable, 0);
            if (!mediaPlayer.getPlayWhenReady()) {
                handler.removeCallbacks(runnable);
            }
        }
    }
}