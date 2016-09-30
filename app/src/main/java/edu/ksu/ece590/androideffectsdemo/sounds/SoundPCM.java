package edu.ksu.ece590.androideffectsdemo.sounds;

/**
 * Created by dab on 3/11/2015.
 *
 * 3/16 added sample rate so we can track what the recorded rate was
 */
public class SoundPCM {

    short[] buffer;
    int SampleRate;

    //default constructor
    public SoundPCM() {
        buffer = new short[10];
        for (int i = 0; i < 10; i++) {
            buffer[i] = 0;
        }

        SampleRate = 44100;
    }

    //copy constructor
    public SoundPCM(SoundPCM aSoundPCM) {
        this(aSoundPCM.GetBuffer(), aSoundPCM.SampleRate());
    }

    //alternate constructor
    public SoundPCM(short[] input, int sampleRate) {

        buffer = input;
        SampleRate = sampleRate;
    }

    //alternate constructor
    public SoundPCM(int length, int sampleRate)
    {
        buffer = new short[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = 0;
        }

        SampleRate = sampleRate;
    }

    public int NumberOfSamples() {
        return buffer.length;
    }

    public int GetBufferSizeInBytes() {

        int size = buffer.length / (Short.SIZE/Byte.SIZE);

        // we need an even number
        if(size % 2 == 1){
            size = size - 1;
        }

        return size;
    }

    public int SampleRate() { return SampleRate; }

    public short[] GetBuffer() {
        return buffer;
    }

    //Does not check buffer bounds
    public short GetValueAtIndex(int i) {
        return buffer[i];
    }

}
