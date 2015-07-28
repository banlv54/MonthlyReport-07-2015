package com.example.rayleigh.monthlyreport_07_2015;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;

import static com.example.rayleigh.monthlyreport_07_2015.R.*;


@SuppressWarnings("ALL")
public class MainActivity extends Activity {
    Button play, play_video, stop, stop_video, capture, record, record_video;
    private MediaRecorder myAudioRecorder;
    private MediaRecorder mMediaRecorder;
    private String outputFile3gp = null, outputFilemp4 = null;
    private Camera mCamera;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Drawable logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        play=(Button)findViewById(id.button3);
        stop=(Button)findViewById(id.button2);
        record=(Button)findViewById(id.button);
        record_video=(Button)findViewById(id.record_video);
        play_video = (Button)findViewById(id.play_video);
        stop_video = (Button)findViewById(id.stop_video);
        capture = (Button)findViewById(id.capture);

        surfaceView = (SurfaceView)findViewById(id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        logo = getResources().getDrawable(drawable.logo);
        stop.setEnabled(false);
        play.setEnabled(false);
        stop_video.setEnabled(false);
        play_video.setEnabled(false);

        outputFile3gp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording2.3gp";

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            myAudioRecorder=new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile3gp);
            try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();
            }
            catch (IllegalStateException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            record.setEnabled(false);
            stop.setEnabled(true);

            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder  = null;

            stop.setEnabled(false);
            play.setEnabled(true);

            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile3gp);
                    m.prepare();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });

        record_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_video.setEnabled(false);
                stop_video.setEnabled(true);
                play_video.setEnabled(false);

                outputFilemp4 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording4.mp4";
                surfaceView.setBackground(null);
                mCamera = Camera.open();
                mCamera.unlock();
                myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setCamera(mCamera);
                // Step 2: Set sources
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                myAudioRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                myAudioRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                // Step 4: Set output file
                myAudioRecorder.setOutputFile(outputFilemp4);
                myAudioRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                try {
                    myAudioRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myAudioRecorder.start();
            }
        });

        stop_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_video.setEnabled(false);
                play_video.setEnabled(true);
                myAudioRecorder.stop();
                myAudioRecorder.reset();
//                surfaceHolder.getSurface().
                myAudioRecorder.release();
                mCamera.release();
                surfaceView.setBackground(logo);
                stop.setEnabled(false);
                play.setEnabled(true);
            }
        });

        play_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer m = new MediaPlayer();
                stop_video.setEnabled(false);
                play_video.setEnabled(true);
                surfaceView.setBackground(null);
                surfaceHolder = surfaceView.getHolder();
//                surfaceHolder = surfaceView.getHolder();
                m.setDisplay(surfaceHolder);
                try {
                    m.setDataSource(outputFilemp4);
                    m.setDisplay(surfaceHolder);
                    m.prepare();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing video", Toast.LENGTH_LONG).show();
                stop.setEnabled(false);
                play.setEnabled(false);
            }
        });


        // Chụp màn hình
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(android.provider.MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                startActivityForResult(intent, 0);
            }
        });
    }

    //Hiển thị ảnh chụp màn hình
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap) data.getExtras().get("data");
        BitmapDrawable ob = new BitmapDrawable(getResources(), bp);
        surfaceView.setBackground(ob);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}