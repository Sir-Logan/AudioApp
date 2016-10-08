package edu.ksu.ece590.androideffectsdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ksu.ece590.androideffectsdemo.renders.CustomDrawableView;


public class ChooseSound extends ActionBarActivity {

    //TextView MainContent;
    //TextView TitleContent;

    Button ThirdOrderButton;
    Button SquareButton;
    Button AppButton;
    Button TriangleButton;
    Button SineButton;

    CustomDrawableView customDrawableView;
    CustomDrawableView customDrawableView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sound);

        // find View-elements
        //TitleContent = (TextView) findViewById(R.id.TitleContent);
        //MainContent = (TextView) findViewById(R.id.MainContent);

        // find buttons
        SineButton = (Button) findViewById(R.id.SineButton);
        TriangleButton = (Button) findViewById(R.id.TriangleButton);
        SquareButton = (Button) findViewById(R.id.SquareButton);
        ThirdOrderButton = (Button) findViewById(R.id.ThirdOrderButton);
        AppButton = (Button) findViewById(R.id.AppButton);

        customDrawableView = (CustomDrawableView) findViewById(R.id.view);
        customDrawableView2 = (CustomDrawableView) findViewById(R.id.view);


        View.OnClickListener SineClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load sine wave
            }
        };

        View.OnClickListener TriangleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load triangle wave
            }
        };

        View.OnClickListener SquareClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load square wave
            }
        };

        View.OnClickListener ThirdOrderClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load third order square wave
            }
        };

        // Back to Application button click
        View.OnClickListener AppClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        };

        // assign click listener to the OK button (btnOK)
        SineButton.setOnClickListener(SineClick);
        AppButton.setOnClickListener(AppClick);
        TriangleButton.setOnClickListener(TriangleClick);
        SquareButton.setOnClickListener(SquareClick);
        ThirdOrderButton.setOnClickListener(ThirdOrderClick);
    }
}
