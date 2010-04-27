package com.frandoid.beatmaker;

import java.util.HashMap;
import java.util.Vector;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolManager {

	
	public static final int MAX_SIMULTANEOUS_SOUND = 16;

    private boolean enabled = true;
    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> soundPoolMap;
    
    public SoundPoolManager(Context context) {
            this.context = context;
    }

    public void init(Vector<Object> vUri) {
            if (enabled) {
                    release();
                    soundPool = new SoundPool(MAX_SIMULTANEOUS_SOUND, AudioManager.STREAM_MUSIC, 100);
                    soundPoolMap = new HashMap<Integer, Integer>();
                    soundPoolMap.put(0, soundPool.load(vUri.get(0).toString(),1));
                    soundPoolMap.put(1, soundPool.load(vUri.get(1).toString(),1));
                    soundPoolMap.put(2, soundPool.load(vUri.get(2).toString(),1));
                    soundPoolMap.put(3, soundPool.load(vUri.get(3).toString(),1));
                    soundPoolMap.put(4, soundPool.load(vUri.get(4).toString(),1));
                    soundPoolMap.put(5, soundPool.load(vUri.get(5).toString(),1));
                    soundPoolMap.put(6, soundPool.load(vUri.get(6).toString(),1));
                    soundPoolMap.put(7, soundPool.load(vUri.get(7).toString(),1));
                    soundPoolMap.put(8, soundPool.load(vUri.get(8).toString(),1));
                    soundPoolMap.put(9, soundPool.load(vUri.get(9).toString(),1));
                    soundPoolMap.put(10, soundPool.load(vUri.get(10).toString(),1));
                    soundPoolMap.put(11, soundPool.load(vUri.get(11).toString(),1));
                    soundPoolMap.put(12, soundPool.load(vUri.get(12).toString(),1));
                    soundPoolMap.put(13, soundPool.load(vUri.get(13).toString(),1));
                    soundPoolMap.put(14, soundPool.load(vUri.get(14).toString(),1));
                    soundPoolMap.put(15, soundPool.load(vUri.get(15).toString(),1));
            }
    }

    public void release() {
            if (soundPool != null) {
                    soundPool.release();
                    soundPool = null;
                    return;
            }
    }

    public void playSound(int numpad) {
            if (soundPool != null) {
                    AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
                    Integer soundId = soundPoolMap.get(numpad);
                    if (soundId != null) {
                            soundPool.play(soundPoolMap.get(numpad), streamVolume, streamVolume, 1, 0, 1f);
                    }
            }
    }
}

