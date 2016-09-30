package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.R;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by el on 4/7/2015.
 */
public class ReverseEffect implements IEffect {


    @Override
    public SoundPCM Calculate(SoundPCM input) {
        short[] buffer = new short[input.NumberOfSamples()];

        for (int i = 0; i < input.NumberOfSamples(); i++) {
            buffer[input.NumberOfSamples() - i - 1] = input.GetValueAtIndex(i);
        }

        return new SoundPCM(buffer, input.SampleRate());

    }

}
