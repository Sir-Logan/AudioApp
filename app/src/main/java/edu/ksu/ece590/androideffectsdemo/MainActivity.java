package edu.ksu.ece590.androideffectsdemo;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
//import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.ksu.ece590.androideffectsdemo.effects.ChangeSpeedEffect;
import edu.ksu.ece590.androideffectsdemo.effects.Complex;
import edu.ksu.ece590.androideffectsdemo.effects.DoubleSampleEffect;
import edu.ksu.ece590.androideffectsdemo.effects.EffectsController;
import edu.ksu.ece590.androideffectsdemo.effects.FFT;
import edu.ksu.ece590.androideffectsdemo.effects.HalveSampleEffect;
import edu.ksu.ece590.androideffectsdemo.effects.HighPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.LowPassEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverbEffect;
import edu.ksu.ece590.androideffectsdemo.effects.ReverseEffect;
import edu.ksu.ece590.androideffectsdemo.renders.CustomDrawableView;
import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;

import static edu.ksu.ece590.androideffectsdemo.effects.FFT.*;
import static java.lang.String.*;


public class MainActivity extends ActionBarActivity {

    GraphView graph;
    LineGraphSeries Series;
    boolean recording = false;
    int sampleFreq = 44100;
    int StopMultiPlay = 0;
    int MinX = 300;
    int MaxX = 4000;
    int touchDebounce = 0;

    TextView MainContent;
    TextView TitleContent;
    ToggleButton ReverbButton;
    ToggleButton DoubleSampleButton;
    ToggleButton HalveSampleButton;

    ToggleButton LowPassButton;
    ToggleButton HighPassButton;
    ToggleButton ReverseButton;


    Button PlayButton;
    Button RecordButton;

    SeekBar seekbar;

    CustomDrawableView customDrawableView;
    CustomDrawableView customDrawableView2;

    AudioPlayTask audioTask;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // find View-elements
        TitleContent = (TextView) findViewById(R.id.TitleContent);
        MainContent = (TextView) findViewById(R.id.MainContent);
        ReverbButton = (ToggleButton) findViewById(R.id.ReverbButton);
        //DoubleSampleButton = (ToggleButton) findViewById(R.id.DoubleSampleButton);
        //HalveSampleButton = (ToggleButton) findViewById(R.id.HalveSampleButton);

        LowPassButton = (ToggleButton) findViewById(R.id.lowpassFilterButton);
        HighPassButton = (ToggleButton) findViewById(R.id.highpassButton);
        ReverseButton = (ToggleButton) findViewById(R.id.reverseButton);

        PlayButton = (Button) findViewById(R.id.PlayButton);
        RecordButton = (Button) findViewById(R.id.RecordButton);
        graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(MinX);
        graph.getViewport().setMaxX(MaxX);
        graph.getViewport().setMinY(0);
        graph.setTitle("Spoken Audio Range");
        //LegendRenderer legend = new LegendRenderer(graph);
        //graph.setLegendRenderer(legend);
        //graph.setTitle("Frequency in Hz");

        seekbar = (SeekBar) findViewById(R.id.seekbar);

        customDrawableView = (CustomDrawableView) findViewById(R.id.view);
        //customDrawableView2 = (CustomDrawableView) findViewById(R.id.view2);

        // create click listener
        final View.OnClickListener ReverbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)

                //MainContent.setText(R.string.reverb_effect_desc);
                TitleContent.setText(R.string.reverb_effect_name);
            }
        };

        View.OnClickListener GraphClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (MinX) {
                    case 0: //spoken audio
                        MinX = 300;
                        MaxX = 4000;
                        graph.setTitle("Spoken Audio Range");
                        break;
                    case 300://regular audio
                        MinX = 100;
                        MaxX = 10000;
                        graph.setTitle("Regular Audio Range");
                        break;
                    case 100: //expanded audio
                        MinX = 0;
                        MaxX = 15000;
                        graph.setTitle("Expanded Audio Range");
                        break;
                    default:
                        MinX = 300;
                        MaxX = 4000;
                        graph.setTitle("Spoken Audio Range");
                        break;
                }

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(MinX);
                graph.getViewport().setMaxX(MaxX);
                graph.getViewport().setMinY(0);
                DataPoint o = new DataPoint(0,0);
                DataPoint[] values = new DataPoint[1];
                values[0] = o;
                LineGraphSeries temp = new LineGraphSeries<DataPoint>(values);
                graph.addSeries(temp);
            }
        };

        View.OnClickListener DoubleSampleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                //MainContent.setText(R.string.double_sample_text);
                TitleContent.setText(R.string.double_sample_title);
            }
        };

        View.OnClickListener HalveSampleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                //MainContent.setText(R.string.halve_sample_text);
                TitleContent.setText(R.string.halve_sample_title);

            }
        };


        // create click listener
        View.OnClickListener LowPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                //MainContent.setText(R.string.lowpass_effect_desc);
                TitleContent.setText(R.string.lowpass_effect_name);
            }
        };

        View.OnClickListener HighPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                //MainContent.setText(R.string.highpass_effect_desc);
                TitleContent.setText(R.string.highpass_effect_name);
            }
        };

        View.OnClickListener ReverseClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                //MainContent.setText(R.string.reverse_effect_desc);
                TitleContent.setText(R.string.reverse_effect_name);
            }
        };

        View.OnClickListener PlayClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlayButton.setClickable(false);
                RecordButton.setEnabled(false);
                RecordButton.setClickable(false);

                if(StopMultiPlay == 0) {//only play if it's not currently playing. This stops it from queuing
                    playRecord();
                    /*PlayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    PlayButton.setText("Stop");
                } else {
                    StopPlaying = 1;
                    PlayButton.setBackground(getResources().getDrawable(R.drawable.arrow_right));
                    PlayButton.setText("Play                  ");*/
                }

                //while(StopMultiPlay == 1);

                PlayButton.setClickable(true); //re-enable buttons. Probably for debounce?
                RecordButton.setEnabled(true);
                RecordButton.setClickable(true);
            }
        };

        View.OnClickListener RecordClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StopMultiPlay == 0) {//this way you cant record while it's playing
                    //if the button was pressed and we were recording, we want to stop
                    if (recording) {
                        RecordButton.setText("Rec");
                        RecordButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                        PlayButton.setEnabled(true);
                        PlayButton.setClickable(true);

                        recording = false;
                    } else {
                        //we were not recording, and we want to start
                        RecordButton.setText("Stop");
                        RecordButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                        PlayButton.setEnabled(false);
                        PlayButton.setClickable(false);


                        //reset the toggle buttons
                        //HalveSampleButton.setChecked(false);
                        //DoubleSampleButton.setChecked(false);
                        LowPassButton.setChecked(false);
                        HighPassButton.setChecked(false);
                        ReverseButton.setChecked(false);
                        ReverbButton.setChecked(false);
                        customDrawableView.clearImageBuffer();
                        //customDrawableView2.clearImageBuffer();

                        //reset main text for new record
                        TitleContent.setText(R.string.start_title);
                        //MainContent.setText(R.string.start_text);

                        Thread recordThread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //this should be locked.
                                recording = true;
                                startRecord();
                            }


                        });

                        recordThread.start();

                    }
                }
            }
        };


        // assign click listener to the OK button (btnOK)
        ReverbButton.setOnClickListener(ReverbClick);

        graph.setOnClickListener(GraphClick);

        //HalveSampleButton.setOnClickListener(HalveSampleClick);
        //DoubleSampleButton.setOnClickListener(DoubleSampleClick);

        PlayButton.setOnClickListener(PlayClick);
        RecordButton.setOnClickListener(RecordClick);

        LowPassButton.setOnClickListener(LowPassClick);
        HighPassButton.setOnClickListener(HighPassClick);
        ReverseButton.setOnClickListener(ReverseClick);



    }


    private void startRecord(){

        File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

        /*
        //Debugging popup thing
        final String promptStartRecord =
                "startRecord()\n"
                        + file.getAbsolutePath() + "\n";

        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                Toast.makeText(MainActivity.this,
                        promptStartRecord,
                        Toast.LENGTH_LONG).show();
            }});*/

        try {
            file.createNewFile();

            OutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            short[] audioData = new short[minBufferSize];

            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();

            boolean nonZero = false;
            while(recording){
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                for(int i = 0; i < numberOfShort; i++){
                    if(audioData[i] <= 500 && audioData[i] >= -500 && !nonZero) continue; //try to remove some white space
                    else {
                        nonZero = true;
                        dataOutputStream.writeShort(audioData[i]);
                    }



                }
            }

            audioRecord.stop();
            dataOutputStream.close();


            //load the data again so we can instantly draw it

            int shortSizeInBytes = Short.SIZE / Byte.SIZE;
            int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);

            audioData = new short[bufferSizeInBytes];

            InputStream inputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

            int i = 0;
            while (dataInputStream.available() > 0) {
                audioData[i] += dataInputStream.readShort();
                i++;
            }

            dataInputStream.close();

            //Just something to test with. This should all be refactored
            SoundPCM sound = new SoundPCM(audioData, sampleFreq);

            //FFT stuff
            double[] mag = new double[4096];
            int firsttime=1;
            //FFT fft = new FFT(2048);
            double[] re = new double[2048];
            double[] im = new double[2048];

            for(int j=0; j<audioData.length-2048; j=j+2048) {
                for(int k=0; k<2048; k++) {
                    im[k] = 0;
                    re[k] = audioData[k+j];
                }
                //fft.fft(re, im);
                if(firsttime == 1) {
                    mag = FFT.fft(re, im, true);
                    firsttime = 0;
                } else {
                    double[] temp = FFT.fft(re, im, true);
                    for(int k=0; k<4096; k++) {
                        mag[k] = (mag[k]+temp[k])/2.0;
                    }
                }
            }




            //GraphView graph = (GraphView) findViewById(R.id.graph);
            int fltr = (int) (500/sampleFreq*2048.0);
            DataPoint[] values = new DataPoint[(2048-fltr)];
            for(int j=fltr;j <2048; j++) {
                DataPoint v = new DataPoint(j/2048.0*sampleFreq/2.0, Math.abs(mag[j]));
                values[j-fltr] = v;
            }



            Series = new LineGraphSeries<DataPoint>(values);
            customDrawableView.DrawImageBuffer(sound);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //stuff that updates ui
                    graph.removeAllSeries();
                    graph.addSeries(Series);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(MinX);
                    graph.getViewport().setMaxX(MaxX);
                    graph.getViewport().setMinY(0);
                    //graph.getViewport().setMaxY(2000);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private class AudioPlayTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            try {

                File file = new File(Environment.getExternalStorageDirectory(), "test.pcm");

                int shortSizeInBytes = Short.SIZE / Byte.SIZE;
                int bufferSizeInBytes = (int) (file.length() / shortSizeInBytes);

                short[] audioData = new short[bufferSizeInBytes];

                InputStream inputStream = new FileInputStream(file);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

                int i = 0;
                while (dataInputStream.available() > 0) {
                    audioData[i] += dataInputStream.readShort();

                    i++;
                }

                dataInputStream.close();

                //Just something to test with. This should all be refactored
                SoundPCM sound = new SoundPCM(audioData, sampleFreq);

                EffectsController eController = new EffectsController();

                /*
                if (HalveSampleButton.isChecked())
                {
                    eController.AddEffect(new HalveSampleEffect());
                    //Halve the sample effect
                }*/

                //Leave like this to demonstrate signal loss/degradation.
                //I.e We lost part of the signal from halving. It cannot be regained by doubling.

                /*
                if (DoubleSampleButton.isChecked())
                {
                    eController.AddEffect(new DoubleSampleEffect());
                    //Double the sample effect

                }*/


                if (LowPassButton.isChecked()) {
                    eController.AddEffect((new LowPassEffect(100.0f, sound.NumberOfSamples() / sound.SampleRate())));
                }
                if (HighPassButton.isChecked()) {
                    eController.AddEffect((new HighPassEffect(.5f, sound.NumberOfSamples() / sound.SampleRate())));
                }
                if (ReverseButton.isChecked()) {

                    eController.AddEffect(new ReverseEffect());
                }
                if (ReverbButton.isChecked()) {
                    //add the effects
                    eController.AddEffect(new ReverbEffect(0.25f, 250, 44100));
                }

                sound = eController.CalculateEffects(sound);
                ChangeSpeedEffect tempr = new ChangeSpeedEffect();
                double seekbarvalue = (int)seekbar.getProgress();
                sound = tempr.Calculate(sound, seekbarvalue);

                int minSize = AudioTrack.getMinBufferSize(sampleFreq, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

                AudioTrack audioTrack = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleFreq,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,

                        minSize,
                        AudioTrack.MODE_STREAM);
                //Start FFT addition
                short[] newAudioData = sound.GetBuffer();
                //FFT stuff
                double[] mag = new double[4096];
                int firsttime=1;
                //FFT fft = new FFT(2048);
                double[] re = new double[2048];
                double[] im = new double[2048];

                for(int j=0; j<newAudioData.length-2048; j=j+2048) {
                    for(int k=0; k<2048; k++) {
                        im[k] = 0;
                        re[k] = newAudioData[k+j];
                    }
                    //fft.fft(re, im);
                    if(firsttime == 1) {
                        mag = FFT.fft(re, im, true);
                        firsttime = 0;
                    } else {
                        double[] temp = FFT.fft(re, im, true);
                        for(int k=0; k<4096; k++) {
                            mag[k] = (mag[k]+temp[k])/2.0;
                        }
                    }
                }




                //GraphView graph = (GraphView) findViewById(R.id.graph);
                int fltr = (int) (500/sampleFreq*2048.0);
                DataPoint[] values = new DataPoint[(2048-fltr)];
                for(int j=fltr;j <2048; j++) {
                    DataPoint v = new DataPoint(j/2048.0*sampleFreq/2.0, Math.abs(mag[j]));
                    values[j-fltr] = v;
                }



                Series = new LineGraphSeries<DataPoint>(values);
                //customDrawableView.DrawImageBuffer(sound);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //stuff that updates ui
                        graph.removeAllSeries();
                        graph.addSeries(Series);
                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setMinX(MinX);
                        graph.getViewport().setMaxX(MaxX);
                        graph.getViewport().setMinY(0);
                        //graph.getViewport().setMaxY(2000);
                    }
                });
                //End FFT addition
                customDrawableView.update(audioTrack, sound);
                //customDrawableView2.update(audioTrack, sound);

                audioTrack.play();


                audioTrack.write(sound.GetBuffer(), 0, sound.GetBuffer().length);

                StopMultiPlay = 0;
                audioTrack.stop();
                audioTrack.release();
                StopMultiPlay = 0;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    private void playRecord() {

        StopMultiPlay = 1;
        audioTask = new AudioPlayTask();
        audioTask.execute();
    }

    public void openTutorial(View view) {
        Intent intent = new Intent(this, DisplayTutorial.class);
        startActivity(intent);
    }

}
