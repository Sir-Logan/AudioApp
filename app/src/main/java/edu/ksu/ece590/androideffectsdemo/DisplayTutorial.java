package edu.ksu.ece590.androideffectsdemo;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.ksu.ece590.androideffectsdemo.renders.CustomDrawableView;


public class DisplayTutorial extends ActionBarActivity {

    TextView MainContent;
    TextView TitleContent;

    Button ReverbButton;
    Button LowPassButton;
    Button HighPassButton;
    Button ReverseButton;
    Button DoubleButton;
    Button HalveButton;
    Button AppButton;
    Button EnggButton;
    Button BasicsButton;

    CustomDrawableView customDrawableView;
    CustomDrawableView customDrawableView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tutorial);

        // find View-elements
        TitleContent = (TextView) findViewById(R.id.TitleContent);
        MainContent = (TextView) findViewById(R.id.MainContent);

        // find buttons
        ReverbButton = (Button) findViewById(R.id.ReverbButton);
        LowPassButton = (Button) findViewById(R.id.LowpassButton);
        HighPassButton = (Button) findViewById(R.id.HighpassButton);
        ReverseButton = (Button) findViewById(R.id.ReverseButton);
        AppButton = (Button) findViewById(R.id.AppButton);
        DoubleButton = (Button) findViewById(R.id.DoubleButton);
        HalveButton = (Button) findViewById(R.id.HalveButton);
        EnggButton = (Button) findViewById(R.id.EnggButton);
        BasicsButton = (Button) findViewById(R.id.BasicsButton);


        customDrawableView = (CustomDrawableView) findViewById(R.id.view);
        customDrawableView2 = (CustomDrawableView) findViewById(R.id.view);

        // create click listeners
        //Reverb Click
        View.OnClickListener ReverbClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.reverb_tutorial);
                TitleContent.setText(R.string.reverb_effect_name);
            }
        };


        // Lowpass Click
        View.OnClickListener LowPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.lowpass_tutorial);
                TitleContent.setText(R.string.lowpass_effect_name);
            }
        };

        // Highpass Click
        View.OnClickListener HighPassClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.highpass_tutorial);
                TitleContent.setText(R.string.highpass_effect_name);
            }
        };

        // Reverse Click
        View.OnClickListener ReverseClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.reverse_tutorial);
                TitleContent.setText(R.string.reverse_effect_name);
            }
        };

        // Engineering Click
        View.OnClickListener EnggClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.engg_tutorial);
                TitleContent.setText(R.string.engg_effect_name);
            }
        };

        // Basics Click
        View.OnClickListener BasicsClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.appinfo_tutorial);
                TitleContent.setText(R.string.appinfo_effect_name);
            }
        };

        // Halve Click
        View.OnClickListener HalveClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.halve_tutorial);
                TitleContent.setText(R.string.halve_effect_name);
            }
        };

        //Double Click
        View.OnClickListener DoubleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text of the TextView (MainContent)
                MainContent.setText(R.string.double_tutorial);
                TitleContent.setText(R.string.double_effect_name);
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
        ReverbButton.setOnClickListener(ReverbClick);
        AppButton.setOnClickListener(AppClick);
        LowPassButton.setOnClickListener(LowPassClick);
        HighPassButton.setOnClickListener(HighPassClick);
        ReverseButton.setOnClickListener(ReverseClick);
        EnggButton.setOnClickListener(EnggClick);
        HalveButton.setOnClickListener(HalveClick);
        DoubleButton.setOnClickListener(DoubleClick);
        BasicsButton.setOnClickListener(BasicsClick);
    }

}
