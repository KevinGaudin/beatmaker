package com.frandoid.beatmaker;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class SoundPoolSoundManager {

	public static final int MAX_SIMULTANEOUS_SOUND = 8;
    public static final int PAD1_SOUND = 1;
	public static final int PAD2_SOUND = 2;
	public static final int PAD3_SOUND = 3;
	public static final int PAD4_SOUND = 4;
	public static final int PAD5_SOUND = 5;
	public static final int PAD6_SOUND = 6;
	public static final int PAD7_SOUND = 7;
	public static final int PAD8_SOUND = 8;
	public static final int PAD9_SOUND = 9;
	public static final int PAD10_SOUND = 10;
	public static final int PAD11_SOUND = 11;
	public static final int PAD12_SOUND = 12;
	public static final int PAD13_SOUND = 13;
	public static final int PAD14_SOUND = 14;
	public static final int PAD15_SOUND = 15;
	public static final int PAD16_SOUND = 16;

    private boolean enabled = true;
    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    public SoundPoolSoundManager(Context context) {
            this.context = context;
    }

    public void reInit() {
            init();
    }

    public void init() {
            if (enabled) {
                    release();
                    soundPool = new SoundPool(MAX_SIMULTANEOUS_SOUND, AudioManager.STREAM_MUSIC, 100);
                    soundPoolMap = new HashMap<Integer, Integer>();
                    soundPoolMap.put(PAD1_SOUND, soundPool.load(context, R.raw.s1,1));
					soundPoolMap.put(PAD2_SOUND, soundPool.load(context, R.raw.s2,1));
					soundPoolMap.put(PAD3_SOUND, soundPool.load(context, R.raw.s3,1));
					soundPoolMap.put(PAD4_SOUND, soundPool.load(context, R.raw.s4,1));
					soundPoolMap.put(PAD5_SOUND, soundPool.load(context, R.raw.s5,1));
					soundPoolMap.put(PAD6_SOUND, soundPool.load(context, R.raw.s6,1));
					soundPoolMap.put(PAD7_SOUND, soundPool.load(context, R.raw.s7,1));
					soundPoolMap.put(PAD8_SOUND, soundPool.load(context, R.raw.s8,1));
					soundPoolMap.put(PAD9_SOUND, soundPool.load(context, R.raw.s9,1));
					soundPoolMap.put(PAD10_SOUND, soundPool.load(context, R.raw.s10,1));
					soundPoolMap.put(PAD11_SOUND, soundPool.load(context, R.raw.s11,1));
					soundPoolMap.put(PAD12_SOUND, soundPool.load(context, R.raw.s12,1));
					soundPoolMap.put(PAD13_SOUND, soundPool.load(context, R.raw.s13,1));
					soundPoolMap.put(PAD14_SOUND, soundPool.load(context, R.raw.s14,1));
					soundPoolMap.put(PAD15_SOUND, soundPool.load(context, R.raw.s15,1));
					soundPoolMap.put(PAD16_SOUND, soundPool.load(context, R.raw.s16,1));
            }
    }

    public void release() {
            if (soundPool != null) {
                    soundPool.release();
                    soundPool = null;
                    return;
            }
    }

    public void playSound(int sound) {
            if (soundPool != null) {
                    AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                    Integer soundId = soundPoolMap.get(sound);
                    if (soundId != null) {
                            soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f);
                    }
            }
    }

    public void setEnabled(boolean enabled) {
            this.enabled = enabled;
    }

}

