package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 3/11/2015.
 */
public interface IEffect {

    SoundPCM Calculate(SoundPCM input);

//    String GetName(Context ctx);
//    String GetDescription(Context ctx);
}
