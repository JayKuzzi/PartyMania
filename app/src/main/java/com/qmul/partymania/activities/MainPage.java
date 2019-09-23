package com.qmul.partymania.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.fragments.DatingHall;
import com.qmul.partymania.fragments.Matching;
import com.qmul.partymania.fragments.Me;
import com.qmul.partymania.fragments.Moments;
import com.qmul.partymania.tools.Constant;

import java.lang.ref.WeakReference;

public class MainPage extends AppCompatActivity implements View.OnClickListener {
    private MyHandler myHandler = new MyHandler(MainPage.this);
    private ImageView matching, hall, me, moment, camera, post, logo;

    private TextView nickName;
    //Fragments
    private Fragment currentFragment;
    private DatingHall frag_datingHall;
    private Matching frag_matching;
    private Me frag_me;
    private Moments frag_moments;
    //FragmentTransaction
    private FragmentTransaction transaction;
    private static boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();
    }

    private void init() {
        nickName = findViewById(R.id.nickMame);
        camera = findViewById(R.id.camera);
        logo = findViewById(R.id.logo);
        post = findViewById(R.id.post);
        moment = findViewById(R.id.moment);
        hall = findViewById(R.id.dating_hall);
        me = findViewById(R.id.me);
        matching = findViewById(R.id.matching);
        post = findViewById(R.id.post);
        matching.setOnClickListener(this);
        hall.setOnClickListener(this);
        me.setOnClickListener(this);
        moment.setOnClickListener(this);
        camera.setOnClickListener(this);
        post.setOnClickListener(this);
        currentFragment = new Fragment();
        frag_moments = new Moments();
        frag_me = new Me();
        frag_matching = new Matching();
        frag_datingHall = new DatingHall();
    }

    @Override
    public void onClick(View v) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.moment:
                nickName.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                moment.setImageResource(R.drawable.ic_moment_selected);
                hall.setImageResource(R.drawable.ic_hall);
                matching.setImageResource(R.drawable.ic_matching);
                me.setImageResource(R.drawable.ic_me);
                transaction.replace(R.id.fragments_layout, frag_moments).commit();
//                showFragment(frag_moments);
                break;
            case R.id.dating_hall:
                nickName.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                hall.setImageResource(R.drawable.ic_hall_selected);
                moment.setImageResource(R.drawable.ic_moment);
                matching.setImageResource(R.drawable.ic_matching);
                me.setImageResource(R.drawable.ic_me);
                transaction.replace(R.id.fragments_layout, frag_datingHall).commit();
//                showFragment(frag_datingHall);
                break;
            case R.id.matching:
                if (nickName.getText().equals("NickName")) {
                    Toast toast = Toast.makeText(this, "Complete your personal information first", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    myHandler.sendEmptyMessageDelayed(0, 2000);
                } else {
                    nickName.setVisibility(View.GONE);
                    logo.setVisibility(View.VISIBLE);
                    moment.setImageResource(R.drawable.ic_moment);
                    hall.setImageResource(R.drawable.ic_hall);
                    matching.setImageResource(R.drawable.ic_matching_selected);
                    me.setImageResource(R.drawable.ic_me);
                    transaction.replace(R.id.fragments_layout, frag_matching).commit();
//                    showFragment(frag_matching);
                }
                break;
            case R.id.me:
                if (nickName.getText().equals("NickName")) {
                    Toast toast = Toast.makeText(this, "Complete your personal information first", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    myHandler.sendEmptyMessageDelayed(0, 2000);
                } else {
                    nickName.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.GONE);
                    moment.setImageResource(R.drawable.ic_moment);
                    hall.setImageResource(R.drawable.ic_hall);
                    matching.setImageResource(R.drawable.ic_matching);
                    me.setImageResource(R.drawable.ic_me_selected);
                    transaction.replace(R.id.fragments_layout, frag_me).commit();
//                    showFragment(frag_me);
                }
                break;
            case R.id.camera:
                if (nickName.getText().equals("NickName")) {
                    Toast toast = Toast.makeText(this, "Complete your personal information first", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    myHandler.sendEmptyMessageDelayed(0, 2000);
                } else {
                    Intent intent = new Intent(this, NewPost.class);
                    startActivity(intent);
                }
                break;
            case R.id.post:
                Intent intent2 = new Intent(this, Friends.class);
                startActivity(intent2);
                break;
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainPage> mTarget;

        public MyHandler(MainPage target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            MainPage target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case 0:
                        Intent intent = new Intent(target, EditProfile.class);
                        target.startActivityForResult(intent, 1);
                        break;
                    case Constant.APP_EXIT_CHECK:
                        isExit = false;
                }

            }
        }
    }

    private void showFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            transaction.hide(currentFragment);
            currentFragment = fragment;
            if (!fragment.isAdded()) {
                transaction.add(R.id.fragments_layout, fragment).show(fragment).commit();
            } else {
                transaction.show(fragment).commit();
            }
        }
    }


    //type back button 2times to exit app
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "Press again to exit",
                    Toast.LENGTH_SHORT).show();
            myHandler.sendEmptyMessageDelayed(Constant.APP_EXIT_CHECK, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("local_file", MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("remember", false);
        if (isRemember) {
            String nickname = sharedPreferences.getString("nickName", "");
            nickName.setText(nickname);
        } else {
            nickName.setText("NickName");
        }
    }
}
