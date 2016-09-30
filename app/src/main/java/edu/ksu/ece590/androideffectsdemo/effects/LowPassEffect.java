package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.R;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 3/11/2015.
 */
public class LowPassEffect implements IEffect {
    float rc = .25f;
    float dt = 1; //time interval
    public LowPassEffect(float rc, int dt) {
        this.rc = rc;
        this.dt = dt;
    }
    @Override
    public SoundPCM Calculate(SoundPCM input) {
        float alpha = dt / (rc + dt);
        short[] buffer = new short[input.NumberOfSamples()];
        buffer[0] = input.GetValueAtIndex(0);
        for (int i = 1; i < input.NumberOfSamples(); i++) {
            buffer[i] = (short) (float) (
                    buffer[i - 1] + alpha * (input.GetValueAtIndex(i) - buffer[i - 1]));
        }
        return new SoundPCM(buffer, input.SampleRate());
    }
}
