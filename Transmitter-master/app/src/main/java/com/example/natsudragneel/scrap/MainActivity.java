package com.example.natsudragneel.scrap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    final int REQUEST_WRITE_STORAGE = 0000;
    String filePath = "f";
    boolean threadRunning = true;
    int clicked = 0;
    double bitRate = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Requesting for permission to read files
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
        }

        Button play = (Button) findViewById(R.id.play);
        final EditText text = (EditText)findViewById(R.id.editText);

        final int mBufferSize = AudioTrack.getMinBufferSize(44100,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
        final AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, mBufferSize, AudioTrack.MODE_STREAM);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!text.getText().toString().isEmpty())
                    filePath = text.getText().toString();

                clicked = 1;
            }
        });

        new Thread(new Runnable() {
            public void run(){
                while (threadRunning){
                    if(clicked ==1){
                        fskModulator(filePath, mAudioTrack);
                        clicked = 0;
                    }
                }
            }
        }).start();
    }
    //Modulation FSK
    void fskModulator(String filePath, AudioTrack mAudio){
        double time = 1/bitRate;
        int duration = (int)(44100*time);
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];

        int frequency1 = 2000;   //For bits 0
        int frequency2 = 4000;   //For bits 1
        double ang1 = 2.0*Math.PI *frequency1/44100;
        double ang2 = 2.0*Math.PI *frequency2/44100;

        int[] bits = bitsFromFile.bytesTobits(filePath);
        for(int k=0;k<bits.length;k++){
            if(bits[k]==0)
            {
                for (int i = 0; i < mSound.length; i++)
                {
                    mSound[i] = Math.sin(ang1*i);
                    mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
                }
                mAudio.play();
                mAudio.write(mBuffer,0,mBuffer.length);
                mAudio.stop();

            }
            else
            {
                for (int i = 0; i < mSound.length; i++)
                {
                    mSound[i] = Math.sin(ang2*i);
                    mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
                }
                mAudio.play();
                mAudio.write(mBuffer,0,mBuffer.length);
                mAudio.stop();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                } else
                {
                    Toast.makeText(MainActivity.this, "The app can not work properly unless you give permissions", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
