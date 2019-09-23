package com.qmul.partymania.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.qmul.partymania.R;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.NowTime;
import com.qmul.partymania.tools.Permissions;
import com.qmul.partymania.tools.WebService;
import com.qmul.partymania.views.SexBoxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private EditText account, password, email;
    private TextView birthday;
    private Button sign_up;
    private SexBoxView sexBoxView;
    private int calculated_age;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog checkNameDialog;
    private ProgressDialog signUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        email = findViewById(R.id.e_mail);
        birthday = findViewById(R.id.birthday);
        TextView sign_in = findViewById(R.id.back_sign_in_page);
        sign_up = findViewById(R.id.sign_up);
        sexBoxView = findViewById(R.id.sex);
        birthday.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        sign_in.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday:
                new TimePickerBuilder(SignUp.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String year = String.valueOf(date.getYear());
                        if (year.charAt(0) == '1')
                            year = "20" + year.substring(1);
                        else
                            year = "19" + year;
                        birthday.setText(date.getDate() + "/" + (date.getMonth() + 1) + "/" + year);
                        calculated_age = Integer.parseInt(NowTime.getSysYear()) - Integer.parseInt(year);
                    }
                }).build().show();
                break;
            case R.id.sign_up:
                if (!Permissions.checkNetwork(this)) {
                    Toast toast = Toast.makeText(SignUp.this, "Network not connected", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                if (account.getText().toString().equals("") ||
                        password.getText().toString().equals("") ||
                        birthday.getText().toString().equals("Birthday") ||
                        email.getText().toString().equals("")) {
                    Toast.makeText(SignUp.this, "Please complete the form", Toast.LENGTH_SHORT).show();
                } else if (account.getText().toString().length() < 2) {
                    Toast.makeText(SignUp.this, "At least two characters account", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length() < 6) {
                    Toast.makeText(SignUp.this, "At least two characters password", Toast.LENGTH_SHORT).show();
                } else if (sexBoxView.getStatus() == 0) {
                    Toast.makeText(SignUp.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else {
                    sign_up.setEnabled(false);
                    sign_up.setBackgroundColor(Color.parseColor("#9BCBF7"));
                    checkNameDialog = new ProgressDialog(this);
                    checkNameDialog.setTitle("Sign up");
                    checkNameDialog.setMessage("Checking your username, please wait.");
                    checkNameDialog.setCancelable(false);
                    checkNameDialog.show();
                    new Thread(new checkNameThread()).start();
                }
                break;
            case R.id.back_sign_in_page:
                finish();
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<SignUp> mTarget;

        public MyHandler(SignUp target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            SignUp target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.checkNameDialog.dismiss();
                        if (target.signUpDialog.isShowing())
                            target.signUpDialog.dismiss();
                        Toast.makeText(target, "Server exception", Toast.LENGTH_SHORT).show();
                        target.sign_up.setEnabled(true);
                        target.sign_up.setBackgroundColor(Color.parseColor("#3897F0"));
                        break;
                    case Constant.NAMEEXISTED:
                        target.checkNameDialog.dismiss();
                        Toast.makeText(target, "Username already exists", Toast.LENGTH_SHORT).show();
                        target.sign_up.setEnabled(true);
                        target.sign_up.setBackgroundColor(Color.parseColor("#3897F0"));
                        break;
                    case Constant.NAMENOTEXISTED:
                        Toast.makeText(target, "Username can be use", Toast.LENGTH_SHORT).show();
                        target.checkNameDialog.dismiss();
                        target.SignUp();
                        break;
                    case Constant.SERVER_NOT_RETURN_FOR_REG:
                        target.signUpDialog.dismiss();
                        Toast.makeText(target, "Server exception", Toast.LENGTH_SHORT).show();
                        target.sign_up.setBackgroundColor(Color.parseColor("#3897F0"));
                        target.sign_up.setEnabled(true);
                        break;
                    case Constant.REG_SUCCESS:
                        target.signUpDialog.dismiss();
                        Toast.makeText(target, "Registered successfully", Toast.LENGTH_SHORT).show();
                        target.finish();
                        break;
                    case Constant.REG_FAIL:
                        target.signUpDialog.dismiss();
                        Toast.makeText(target, "Database add failed", Toast.LENGTH_SHORT).show();
                        target.sign_up.setEnabled(true);
                        target.sign_up.setBackgroundColor(Color.parseColor("#3897F0"));
                        break;
                }

            }
        }
    }

    private void SignUp() {
        signUpDialog = new ProgressDialog(this);
        signUpDialog.setTitle("Sign up");
        signUpDialog.setMessage("Adding User,please wait.");
        signUpDialog.show();
        new Thread(new signUpThread()).start();
    }


    private class checkNameThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String checkNamePath = "http://" + Constant.MY_SERVER_IP
                    + "/PartyMania/CheckUserName" + "?username=" + account.getText().toString();
            Message message = new Message();
            String checkNameResult = WebService.executeHttpGet(checkNamePath);
            if (checkNameResult == null || checkNameResult.equals("")) {
                message.what = Constant.SERVER_NOT_RETURN;
            } else {
                message.what = Integer.parseInt(checkNameResult.trim());
            }
            myHandler.sendMessage(message);
        }
    }

    private class signUpThread implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = myHandler.obtainMessage();
            JSONObject object = new JSONObject();
            try {
                object.put("userId", (int) (Math.random() * 90000 + 10000));
                object.put("userName", account.getText().toString());
                object.put("sex", sexBoxView.getStatus());
                object.put("age", calculated_age);
                object.put("password", password.getText().toString());
                object.put("email", email.getText().toString());
                String result = WebService.executeHttpPost(object, "SignUp");
                if (result == null || result.equals("")) {
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
