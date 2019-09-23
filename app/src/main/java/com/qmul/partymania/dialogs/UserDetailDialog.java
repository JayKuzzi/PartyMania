package com.qmul.partymania.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.fragments.Moments;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.tools.WebService;
import com.qmul.partymania.views.XCImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by bb on 2017/3/14.
 */

public class UserDetailDialog extends Dialog {
    private String clickedNickName;
    private String queryUserResult;
    private int currentId;
    private int friendsId;
    private ImageView cancel;
    private ProgressDialog queryUserInfoDialog,addFriendDialog;
    private MyHandler myHandler;
    private TextView nick, age, free, hobby, business, sign, email, addToFriend;
    private ImageView sex;
    private XCImageView head;

    public UserDetailDialog(Context context, String clickedNickName) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_user_detail);
        setCanceledOnTouchOutside(false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("local_file", getContext().MODE_PRIVATE);
        currentId = sharedPreferences.getInt("userId", -1);
        initView();
        initData();
        this.clickedNickName = clickedNickName;
        myHandler = new MyHandler(this);
    }


    private void initView() {
        nick = findViewById(R.id.user_item_nickname);
        age = findViewById(R.id.user_item_age);
        free = findViewById(R.id.user_item_freeTime);
        hobby = findViewById(R.id.user_item_hobby);
        business = findViewById(R.id.user_item_business);
        sign = findViewById(R.id.user_item_sign);
        email = findViewById(R.id.user_item_email);
        addToFriend = findViewById(R.id.user_item_addToFriend);
        sex = findViewById(R.id.user_item_sex);
        head = findViewById(R.id.user_item_head);
        cancel = findViewById(R.id.user_detail_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                System.out.println(clickedNickName);
            }
        });
        addToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendDialog = new ProgressDialog(getContext());
                addFriendDialog.setTitle("Adding");
                addFriendDialog.setMessage("Adding followed one, please wait.");
                addFriendDialog.setCancelable(false);
                addFriendDialog.show();
                new Thread(new addFriendThread()).start();
            }
        });
    }

    private void initData() {
        queryUserInfoDialog = new ProgressDialog(getContext());
        queryUserInfoDialog.setTitle("Loding");
        queryUserInfoDialog.setMessage("Loading User Info, please wait.");
        queryUserInfoDialog.setCancelable(false);
        queryUserInfoDialog.show();
        new Thread(new queryUserInfoThread()).start();
    }


    private static class MyHandler extends Handler {
        private final WeakReference<UserDetailDialog> mTarget;

        public MyHandler(UserDetailDialog target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            UserDetailDialog target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.queryUserInfoDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_USER_NULL:
                        target.queryUserInfoDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server return null", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.QUERY_USER_SUCCESS:
                        Toast.makeText(target.getContext(), "Query successful", Toast.LENGTH_SHORT).show();
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.queryUserResult);
                            for (int i = 0; i < myJsonArray.length(); i++) {
                                JSONObject object = myJsonArray.getJSONObject(i);
                                target.friendsId =object.getInt("userId");
                                target.nick.setText(object.getString("nickName"));
                                target.age.setText(object.getString("age"));
                                target.free.setText(object.getString("freeTime"));
                                target.sign.setText(object.getString("sign"));
                                target.business.setText(object.getString("business"));
                                target.email.setText(object.getString("email"));
                                target.hobby.setText(object.getString("hobby"));
                                if (object.getInt("sex") == 1)
                                    target.sex.setImageResource(R.drawable.ic_man_x);
                                else
                                    target.sex.setImageResource(R.drawable.ic_woman_x);
                                target.head.setImageBitmap(Square.centerSquareScaleBitmap(BitmapTool.String2Bitmap(object.getString("headphoto")), 200));
                            }
                            target.queryUserInfoDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.initView();
                        break;
                    case Constant.ADD_RELATION_FAIL:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "Server exception", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.ADD_RELATION_SUCCESS:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "Add successful", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.RELATION_EXIST:
                        target.addFriendDialog.dismiss();
                        Toast.makeText(target.getContext(), "You have followed him, do not add again", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        }
    }

    private class queryUserInfoThread implements Runnable {
        @Override
        public void run() {
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/QueryUserByNickname" + "?nickname=" + clickedNickName;
            Message message = new Message();
            queryUserResult = WebService.executeHttpGet(path);
            if (queryUserResult == null || queryUserResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (queryUserResult.trim().equals("33")) {
                message.what = Constant.QUERY_USER_NULL;
            } else {
                message.what = Constant.QUERY_USER_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }

    private class addFriendThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String path = "http://" + Constant.MY_SERVER_IP + "/PartyMania/AddRelation" + "?myId=" + currentId +"&friendsId="+ friendsId;
            Message message = new Message();
            queryUserResult = WebService.executeHttpGet(path);
            if (queryUserResult == null || queryUserResult.equals("")) {
                message.what = Constant.ADD_RELATION_FAIL;
            } else if (queryUserResult.trim().equals("22")) {
                message.what = Constant.ADD_RELATION_FAIL;
            }else if (queryUserResult.trim().equals("35")) {
                message.what = Constant.RELATION_EXIST;
            } else {
                message.what = Constant.ADD_RELATION_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }
}
