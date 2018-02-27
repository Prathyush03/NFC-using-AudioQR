package com.example.natsudragneel.scrap;

import android.media.AudioTrack;

public class sineToneGenerator {
    public void playSound(double frequency, double time, AudioTrack mAudioTrack) {
        int duration = (int)(44100*time);

        // Sine wave
        double[] mSound = new double[duration];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = Math.sin((2.0*Math.PI * i/(44100/frequency)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }
        mAudioTrack.play();
        mAudioTrack.write(mBuffer, 0, mBuffer.length);
        mAudioTrack.stop();
    }
}
