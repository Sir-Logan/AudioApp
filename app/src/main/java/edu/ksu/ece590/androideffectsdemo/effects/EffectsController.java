package edu.ksu.ece590.androideffectsdemo.effects;

import android.content.Context;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;

import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

/**
 * Created by dab on 3/22/2015.
 */
public class EffectsController {

    HashMap<Integer, IEffect> EffectsMap = new HashMap<Integer, IEffect>();

    //We might need to have an "Empty Effect" instead of just using null to avoid crashes
    public EffectsController() {
        EffectsMap.put(0, null);
        EffectsMap.put(1, null);
        EffectsMap.put(2, null);


    }

    public void AddEffect(IEffect effect) {
        Boolean found = false;

        //find the first empty slot to put it in
        for (Integer key : EffectsMap.keySet()) {
            if (EffectsMap.get(key) == null) {
                EffectsMap.remove(key);
                EffectsMap.put(key, effect);
                found = true;
                break;
            }
        }

        //if nothing was found. Throw it in spot 1 because it's in the center
        if (!found) {
            EffectsMap.remove(1);
            EffectsMap.put(1, effect);
        }
    }

    //If we decide to drag to location instead of simply swiping down
    public void SetEffect(Integer key, IEffect effect) {
        EffectsMap.remove(key);
        EffectsMap.put(key, effect);
    }

    //Swap some effects
    public void SwapEffects(Integer index1, Integer index2) {
        IEffect temp = EffectsMap.remove(index1);
        EffectsMap.put(index1, EffectsMap.remove(index2));
        EffectsMap.put(index2, temp);
    }

    /*
    public String GetEffectName(Integer key, Context ctx)
    {
        if(EffectsMap.containsKey(key)) {
            if(EffectsMap.get(key) != null)
            {
                return EffectsMap.get(key).GetName(ctx);
            }
        }

        //if we got here, just return an empty string.
        return "";
    }

    public String GetEffectDescription(Integer key, Context ctx)
    {
        if(EffectsMap.containsKey(key)) {
            if(EffectsMap.get(key) != null)
            {
                return EffectsMap.get(key).GetDescription(ctx);
            }
        }

        //if we got here, just return an empty string.
        return "";
    }
    */

    //loop through and calculate the effects
    public SoundPCM CalculateEffects(SoundPCM input) {

        SoundPCM sound = new SoundPCM(input);

        //loop through and calculate
        for (Integer key : EffectsMap.keySet()) {
            if (EffectsMap.get(key) == null) {
                continue; // it's null, continue to next iteration
            }

            IEffect effect = EffectsMap.get(key);

            sound = effect.Calculate(sound);
        }

        return sound;
    }
}
