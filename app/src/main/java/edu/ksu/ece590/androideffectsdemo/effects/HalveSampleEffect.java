package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.R;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 4/29/2015.
 */
public class HalveSampleEffect implements IEffect {

    public HalveSampleEffect() {
    }


    @Override
    public SoundPCM Calculate(SoundPCM input) {
        int oldLength = input.GetBuffer().length;
        int newLength = oldLength / 2;

        short[] buffer = new short[newLength];

        int index = 0;
        for (int i = 0; i < oldLength; i += 2) {
            if (index < buffer.length && i < oldLength) {
                short value = input.GetValueAtIndex(i);
                buffer[index] = value;
                index++;
            }
        }

        return new SoundPCM(buffer, input.SampleRate());

    }

}
