package com.qmul.partymania.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Permissions;
import com.qmul.partymania.tools.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private EditText account, password;
    private TextView sign_up;
    private Button sign_in;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog loginDialog;
    private String loginResult;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }


    private void init() {
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.go_sign_up_page);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        sign_inButtonEnable();
    }

    private void sign_inButtonEnable() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (account.getText().length() == 0 || password.getText().length() == 0) {
                    sign_in.setEnabled(false);
                    sign_in.setBackgroundColor(Color.parseColor("#9BCBF7"));
                } else {
                    sign_in.setEnabled(true);
                    sign_in.setBackgroundColor(Color.parseColor("#3897F0"));
                }
            }
        };
        account.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                if (!Permissions.checkNetwork(SignIn.this)) {
                    Toast toast = Toast.makeText(SignIn.this, "Network not connected", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else {
                    sign_in.setEnabled(false);
                    sign_in.setBackgroundColor(Color.parseColor("#9BCBF7"));
                    loginDialog = new ProgressDialog(this);
                    loginDialog.setTitle("Sign in");
                    loginDialog.setMessage("Authenticating user, please wait.");
                    loginDialog.setCancelable(false);
                    loginDialog.show();
                    new Thread(new loginThread()).start();
                }
                break;
            case R.id.go_sign_up_page:
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                break;
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<SignIn> mTarget;

        public MyHandler(SignIn target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            SignIn target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.sign_in.setEnabled(true);
                        target.sign_in.setBackgroundColor(Color.parseColor("#3897F0"));
                        target.loginDialog.dismiss();
                        Toast.makeText(target, "Server Closed, Email me: 200826311@qq.com", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.LOGIN_FAIL:
                        target.sign_in.setEnabled(true);
                        target.sign_in.setBackgroundColor(Color.parseColor("#3897F0"));
                        target.loginDialog.dismiss();
                        Toast.makeText(target, "Wrong account or password", Toast.LENGTH_SHORT).show();
                        break;
                    case Constant.LOGIN_SUCCESS:
                        target.sharedPreferences = target.getSharedPreferences("local_file", MODE_PRIVATE);
                        target.editor = target.sharedPreferences.edit();
                        target.editor.putBoolean("remember", true);
                        JSONArray myJsonArray = null;
                        try {
                            myJsonArray = new JSONArray(target.loginResult);
                            JSONObject object = myJsonArray.getJSONObject(0);
                            System.out.println(object.getString("userId"));
                            target.editor.putInt("userId", object.getInt("userId"));
                            target.editor.putString("userName", object.getString("userName"));
                            target.editor.putString("nickName", object.getString("nickName"));
                            target.editor.putString("freeTime", object.getString("freeTime"));
                            target.editor.putInt("sex", object.getInt("sex"));
                            target.editor.putString("sign", object.getString("sign"));
                            target.editor.putString("business", object.getString("business"));
                            target.editor.putString("hobby", object.getString("hobby"));
                            target.editor.putString("age", object.getString("age"));
                            target.editor.putString("headphoto", object.getString("headphoto"));
                            target.editor.putString("email", object.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        target.editor.commit();
                        target.loginDialog.dismiss();
                        Toast.makeText(target, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(target, MainPage.class);
                        target.startActivity(intent);
                        target.finish();
                        break;
                }
            }
        }
    }

    private class loginThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String loginPath = "http://" + Constant.MY_SERVER_IP + "/PartyMania/Login" + "?username=" + account.getText().toString() + "&password=" + password.getText().toString();
            Message message = new Message();
            loginResult = WebService.executeHttpGet(loginPath);
            System.out.println(loginResult);
            if (loginResult == null ) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else if (loginResult.trim().equals("400")) {
                message.what = Constant.LOGIN_FAIL;
            } else {
                message.what = Constant.LOGIN_SUCCESS;
            }
            myHandler.sendMessage(message);
        }
    }
}


