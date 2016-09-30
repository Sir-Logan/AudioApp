package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.R;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 4/29/2015.
 */
public class DoubleSampleEffect implements IEffect {


    public DoubleSampleEffect() {

    }

    @Override
    public SoundPCM Calculate(SoundPCM input) {

        int oldLength = input.GetBuffer().length;
        int newLength = oldLength * 2;

        short[] buffer = new short[newLength];

        int index = 0;
        for (int i = 0; i < oldLength; i++) {
            short value = input.GetValueAtIndex(i);
            buffer[index] = value;
            index++;
            if (index + 1 < buffer.length) {
                buffer[index + 1] = value;
                index++;
            }
        }
        return new SoundPCM(buffer, input.SampleRate());

    }



}
