package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.R;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 3/11/2015.
 */
public class ReverbEffect implements IEffect {

    float decay = .25f;
    int delay = 250; //in ms
    float frequency = 44.1f;

    public ReverbEffect(float decay, int delay, int frequency) {
        this.decay = decay;
        this.delay = delay;


        this.frequency = (float) frequency / 1000;
    }

    @Override
    public SoundPCM Calculate(SoundPCM input) {

        int delaySamples = (int) ((float) delay * frequency);
        short[] buffer = new short[input.NumberOfSamples() + delaySamples];

        for (int i = 0; i < input.NumberOfSamples(); i++) {
            buffer[i] += input.GetValueAtIndex(i);


            buffer[i + delaySamples] += (short) ((float) input.GetValueAtIndex(i) * decay);

        }

        return new SoundPCM(buffer, input.SampleRate());
    }


}


