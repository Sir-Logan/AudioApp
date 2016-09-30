package edu.ksu.ece590.androideffectsdemo.renders;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioTrack;
import android.util.AttributeSet;
import android.view.View;


import java.util.Stack;

import edu.ksu.ece590.androideffectsdemo.sounds.SoundPCM;


/**
 * Created by el on 4/24/2015.
 * NOTE: each renderable type should be it's own separate class.
 * i.e WaveFormRenderer, BarGraphRenderer...
 */
public class CustomDrawableView extends View {

    public enum RenderState
    {
        BITMAP_RENDER
    }

    RenderState renderState = RenderState.BITMAP_RENDER;

    int framesPerSecond = 60;
    long startTime;

    float totalTimeMs;

    AudioTrack audioTrack;
    SoundPCM soundData;

    Paint paint = new Paint();
    Paint bluePaint = new Paint();
    Paint black = new Paint();
    int previousState;
    float timePosition;

    int samplesSinceLastDraw = 0;

    Bitmap imageBuffer = null;

    Stack<PointF> drawableStack = new Stack<PointF>();

    public CustomDrawableView(Context context) {
        super(context);
        Setup();
    }

    public CustomDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Setup();
    }

    public void Setup() {

        paint.setColor(Color.GREEN);
        bluePaint.setColor(Color.BLUE);
        black.setColor(Color.BLACK);
        this.startTime = System.currentTimeMillis();
        this.postInvalidate();
     }

    protected void onDraw(Canvas canvas) {

        long elapsedTime = System.currentTimeMillis() - startTime;

        if(renderState == RenderState.BITMAP_RENDER) {
            if(imageBuffer != null){
                canvas.drawBitmap(imageBuffer,0,0,paint);
            }
        }

        if(audioTrack != null) {

            /*
            if(previousState == AudioTrack.PLAYSTATE_PLAYING && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED){
                int i = 0;
                i++;
            }
            */

            if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                previousState = AudioTrack.PLAYSTATE_PLAYING;

                timePosition = (float) audioTrack.getPlaybackHeadPosition() / (float) audioTrack.getSampleRate() * 1000;


                if (renderState == RenderState.BITMAP_RENDER) {

                    float xPos = (timePosition / totalTimeMs) * (float) getWidth();

                    // canvas.drawBitmap(imageBuffer,0,0,paint);
                    canvas.drawLine(xPos, 0, xPos, getHeight(), bluePaint);
                }
            }
        }

        this.startTime = elapsedTime;

        this.postInvalidateDelayed( 1000 / framesPerSecond);

    }

    public void clearImageBuffer()
    {
        imageBuffer = null;
        audioTrack = null;
        this.postInvalidate();
    }

    public void DrawImageBuffer(SoundPCM soundData)
    {
        imageBuffer = Bitmap.createBitmap(this.getWidth(), this.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(imageBuffer);

        float xScale =(float) soundData.NumberOfSamples() /(float)imageBuffer.getWidth() ;
        //float xScale =(float)imageBuffer.getWidth() / (float) soundData.NumberOfSamples() ;
        float yScale =  (float) 32767/(float)imageBuffer.getHeight(); //java shorts are -32,768 to 32,767

        xScale = (float)Math.ceil((double)xScale);

        float dc = getHeight() / 2;

        //lets average out the difference
        float ySum = 0;
        float x = 0;
        float xPos = 0;

        float previousX = 0;
        float previousY = dc;

        float debugYMax = 0;
        float width = getWidth(); //for debugging

        for(int i = 0; i < soundData.NumberOfSamples(); i++)
        {

            if(x <= xScale)
            {
                ySum += soundData.GetValueAtIndex(i);
                x++;
            }

            else
            {
                ySum = ySum / x; //average .

                //float normalized = (float)ySum / (float)maxData ;
                ySum = ySum / yScale;

                // c.drawPoint(xPos, getHeight() - dc - ySum,paint);
                c.drawLine(previousX, previousY,xPos, getHeight()-dc-ySum, paint);
                previousX = xPos;
                previousY = getHeight() - dc - ySum;

                if(previousY > debugYMax) debugYMax = previousY;

                x = 0;
                ySum = 0;
                xPos++;

            }
        }

        this.postInvalidate();
    }

    public void update(AudioTrack audioTrack, SoundPCM soundData)
    {
        this.audioTrack = audioTrack;
        this.soundData = soundData;

        totalTimeMs = (float)soundData.NumberOfSamples()/ (float)soundData.SampleRate() * 1000;

        previousState = AudioTrack.PLAYSTATE_STOPPED;

        drawableStack.clear();
        samplesSinceLastDraw = 0;

        //Draw to Bitmap
        DrawImageBuffer(soundData);

        float test = 0;

    }
}
