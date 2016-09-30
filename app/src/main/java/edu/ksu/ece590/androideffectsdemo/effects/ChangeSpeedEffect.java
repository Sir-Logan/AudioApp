package edu.ksu.ece590.androideffectsdemo.effects;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;
/**
 * Created by Logan on 11/29/2015.
 */
public class ChangeSpeedEffect {
    //@Override
    public SoundPCM Calculate(SoundPCM input, double flux) {
        if(flux == 2) flux = 0.5;//fast speed
        if(flux == 0) flux = 2;//slow speed
        int oldLength = input.GetBuffer().length;
        int newLength = (int) (oldLength * flux);
        short[] buffer = new short[newLength];
        int index = 0;
        if(flux==2) {//slow speed
            for (int i = 0; i < oldLength; i++) {
                short value = input.GetValueAtIndex(i);
                buffer[index] = value;
                index++;
                if (index + 1 < buffer.length) {
                    buffer[index + 1] = value;
                    index++;
                }
            }
        } else if(flux == 0.5) {//fast speed
            for (int i = 0; i < oldLength; i += 2) {
                if (index < buffer.length && i < oldLength) {
                    short value = input.GetValueAtIndex(i);
                    buffer[index] = value;
                    index++;
                }
            }
        } else if(flux == 1) {
            return input;
        }
        return new SoundPCM(buffer, input.SampleRate());
    }
}