package com.qmul.partymania.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qmul.partymania.R;

/**
 * Created by Bo Wang on 2017/10/25.
 */

public class SexBoxView extends LinearLayout {
    private boolean man_selected = false;
    private boolean woman_selected = false;
    ImageView manIcon, womanIcon;

    public SexBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sex_select, this);
        manIcon = findViewById(R.id.man_ic);
        womanIcon = findViewById(R.id.woman_ic);

        manIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getStatus()) {
                    case 0:
                    case 2:
                        turnToMan();
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        });
        womanIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getStatus()) {
                    case 0:
                    case 1:
                        turnToWoman();
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void turnToWoman() {
        manIcon.setImageResource(R.drawable.ic_man);
        womanIcon.setImageResource(R.drawable.ic_woman_x);
        man_selected = false;
        woman_selected = true;
    }

    public void turnToMan() {
        manIcon.setImageResource(R.drawable.ic_man_x);
        womanIcon.setImageResource(R.drawable.ic_woman);
        man_selected = true;
        woman_selected = false;
    }


    public int getStatus() {
        if (!man_selected && !woman_selected) {
            return 0;
        } else if (man_selected && !woman_selected)   //Male selected
        {
            return 1;
        } else if (!man_selected && woman_selected)  //Female selected
        {
            return 2;
        } else
        {
            return -1;
        }
    }
}
