package com.qmul.partymania.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.adapters.VPAdapterForPost;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.NowTime;
import com.qmul.partymania.tools.WebService;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static com.qmul.partymania.tools.BitmapTool.Bitmap2String;

public class NewPost extends AppCompatActivity implements View.OnClickListener {


    private TextView btnCancel, btnShare;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] tabTittle = {"Moments", "PartyHall"};
    private VPAdapterForPost vpAdapterForPost;
    private int currentPostMode;
    private String postContent, postPhoto, time, postCost;
    private int postLikes = 100, postType = 3;
    private Bitmap photo;
    private SharedPreferences sharedPreferences;

    private ImageView imageView;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog addPostDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        init();
        viewSettings();
    }


    private void init() {
        sharedPreferences = getSharedPreferences("local_file", MODE_PRIVATE);
        btnCancel = findViewById(R.id.post_cancel);
        btnShare = findViewById(R.id.post_share);
        tabLayout = findViewById(R.id.post_tabLayout);
        viewPager = findViewById(R.id.post_viewPager);
        imageView = findViewById(R.id.post_upload_photo);
        imageView.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void viewSettings() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText("Moments"));
        tabLayout.addTab(tabLayout.newTab().setText("PartyHall"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentPostMode = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(vpAdapterForPost = new VPAdapterForPost(getSupportFragmentManager(), tabTittle));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                currentPostMode = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_cancel:
                finish();
                break;
            case R.id.post_share:
                if (currentPostMode == 0) {
                    if (vpAdapterForPost.getShareToMoment().getDesc().getText().toString().equals("")) {
                        Toast.makeText(this, "Please Complete Description", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    postType = 3;
                    postContent = vpAdapterForPost.getShareToMoment().getDesc().getText().toString();

                }
                if (currentPostMode == 1) {
                    if (vpAdapterForPost.getShareToHall().getDesc().getText().toString().equals("")
                            || vpAdapterForPost.getShareToHall().getCost().getText().toString().equals("")
                            || vpAdapterForPost.getShareToHall().getTime().getText().toString().equals("")
                            || vpAdapterForPost.getShareToHall().getType().getText().toString().equals("")) {
                        Toast.makeText(this, "Please Complete Form", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println(vpAdapterForPost.getShareToHall().getType().getText().toString());
                    if (vpAdapterForPost.getShareToHall().getType().getText().equals("Party Type"))
                        postType = 1;
                    if (vpAdapterForPost.getShareToHall().getType().getText().toString().equals("Company Type"))
                        postType = 2;
                    postContent = vpAdapterForPost.getShareToHall().getDesc().getText().toString();
                    time = vpAdapterForPost.getShareToHall().getTime().getText().toString();
                    postCost = vpAdapterForPost.getShareToHall().getCost().getText().toString();
                }
                btnShare.setEnabled(false);
                addPostDialog = new ProgressDialog(this);
                addPostDialog.setTitle("New Post");
                addPostDialog.setMessage("Uploading post, please wait.");
                addPostDialog.setCancelable(false);
                addPostDialog.show();
                new Thread(new addPostThread()).start();
                break;
            case R.id.post_upload_photo:
                if (ContextCompat.checkSelfPermission(this, Manifest.
                        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.
                            permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                }
                break;
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, 1);
                } else {
                    Toast.makeText(this, "No access to album", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        else {
            try {
                Uri uri = data.getData();
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<NewPost> mTarget;

        public MyHandler(NewPost target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            NewPost target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.addPostDialog.dismiss();
                        Toast.makeText(target, "Server exception", Toast.LENGTH_SHORT).show();
                        target.btnShare.setEnabled(true);
                        break;
                    case Constant.POST_SUCCESS:
                        target.addPostDialog.dismiss();
                        Toast.makeText(target, "Post success", Toast.LENGTH_SHORT).show();
                        target.finish();
                        break;
                    case Constant.POST_FAIL:
                        target.addPostDialog.dismiss();
                        Toast.makeText(target, "Post failure", Toast.LENGTH_SHORT).show();
                        target.btnShare.setEnabled(true);
                        break;
                }

            }
        }
    }


    private class addPostThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage();
            JSONObject object = new JSONObject();
            try {
                int currentUserId = sharedPreferences.getInt("userId", -1);
                object.put("postId", (Math.random() * 900000 + 100000));
                object.put("postManId", currentUserId);
                postPhoto = Bitmap2String(photo);
                object.put("photo", postPhoto);
                object.put("likes", postLikes);
                if (postType == 3)
                    object.put("content", postContent);
                else
                    object.put("content", postContent + " Time:" +time + "  "+ postCost);
                object.put("time", NowTime.getNowDate());
                object.put("type", postType);
                System.out.println(object);
                String result = WebService.executeHttpPost(object, "AddPost");
                if (result == null||result.equals("")) {
                    message.what = Constant.SERVER_NOT_RETURN;
                } else {
                    message.what = Integer.parseInt(result);
                }
                myHandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
