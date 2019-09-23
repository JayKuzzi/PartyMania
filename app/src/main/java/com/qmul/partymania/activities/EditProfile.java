package com.qmul.partymania.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.dialogs.BusinessChooseDialog;
import com.qmul.partymania.dialogs.FreeTimeChooseDialog;
import com.qmul.partymania.dialogs.HobbyChooseDialog;
import com.qmul.partymania.dialogs.SignChooseDialog;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.tools.WebService;
import com.qmul.partymania.views.XCImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {


    private TextView btnCancel, btnDone, freeTime, business, sign, hobby, userName;
    private EditText nickName;
    private MyHandler myHandler = new MyHandler(this);
    private ProgressDialog editProfileDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String postHeadphoto;
    private XCImageView xcImageView;
    private Bitmap choosePhoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("local_file", MODE_PRIVATE);
        btnCancel = findViewById(R.id.edit_profile_cancel);
        btnDone = findViewById(R.id.edit_profile_done);
        userName = findViewById(R.id.edit_profile_username);
        nickName = findViewById(R.id.edit_profile_nickname);
        freeTime = findViewById(R.id.edit_profile_freeTime);
        business = findViewById(R.id.edit_profile_business);
        xcImageView = findViewById(R.id.change_photo);
        sign = findViewById(R.id.edit_profile_sign);
        hobby = findViewById(R.id.edit_profile_hobby);
        btnDone.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        freeTime.setOnClickListener(this);
        business.setOnClickListener(this);
        sign.setOnClickListener(this);
        hobby.setOnClickListener(this);
        xcImageView.setOnClickListener(this);
        String currentHead = sharedPreferences.getString("headphoto", "");
        if(!currentHead.equals("")){
            byte[] bytes = Base64.decode(currentHead, Base64.DEFAULT);
            xcImageView.setImageBitmap(Square.centerSquareScaleBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),200));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile_cancel:
                finish();
                break;
            case R.id.edit_profile_done:
                if (nickName.getText().toString().equals("") || freeTime.getText().toString().equals("")
                        || business.getText().toString().equals("") || sign.getText().toString().equals("")
                        || hobby.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Complete", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnDone.setEnabled(false);
                editProfileDialog = new ProgressDialog(this);
                editProfileDialog.setTitle("Edit Profile");
                editProfileDialog.setMessage("Editing user, please wait.");
                editProfileDialog.setCancelable(false);
                editProfileDialog.show();
                new Thread(new editProfileThread()).start();
                break;
            case R.id.edit_profile_freeTime:
                final FreeTimeChooseDialog freeTimeChooseDialog = new FreeTimeChooseDialog(this);
                freeTimeChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        freeTimeChooseDialog.dismiss();
                        freeTime.setText(freeTimeChooseDialog.getToast1() + " - " + freeTimeChooseDialog.getToast2());
                    }
                });
                freeTimeChooseDialog.show();
                break;
            case R.id.edit_profile_business:
                final BusinessChooseDialog businessChooseDialog = new BusinessChooseDialog(this);
                businessChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        businessChooseDialog.dismiss();
                        business.setText(businessChooseDialog.getToast());
                    }
                });
                businessChooseDialog.show();
                break;
            case R.id.edit_profile_sign:
                final SignChooseDialog signChooseDialog = new SignChooseDialog(this);
                signChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signChooseDialog.dismiss();
                        sign.setText(signChooseDialog.getToast());
                    }
                });
                signChooseDialog.show();
                break;
            case R.id.edit_profile_hobby:
                final HobbyChooseDialog hobbyChooseDialog = new HobbyChooseDialog(this);
                hobbyChooseDialog.getOk().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hobbyChooseDialog.dismiss();
                        hobby.setText(hobbyChooseDialog.getToast());
                    }
                });
                hobbyChooseDialog.show();
                break;

            case R.id.change_photo:
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
                choosePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                xcImageView.setImageBitmap(Square.centerSquareScaleBitmap(choosePhoto,200));
                postHeadphoto = BitmapTool.Bitmap2String(choosePhoto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static class MyHandler extends Handler {
        private final WeakReference<EditProfile> mTarget;

        public MyHandler(EditProfile target) {
            mTarget = new WeakReference<>(target);
        }

        @Override
        public void handleMessage(Message msg) {
            EditProfile target = mTarget.get();
            if (target != null) {
                switch (msg.what) {
                    case Constant.SERVER_NOT_RETURN:
                        target.editProfileDialog.dismiss();
                        Toast.makeText(target, "Server exception", Toast.LENGTH_SHORT).show();
                        target.btnDone.setEnabled(true);
                        break;
                    case Constant.COMPLETE_SUCCESS:
                        target.editor = target.sharedPreferences.edit();
                        target.editor.putString("nickName", target.nickName.getText().toString());
                        target.editor.putString("freeTime", target.freeTime.getText().toString());
                        target.editor.putString("sign", target.sign.getText().toString());
                        target.editor.putString("business", target.business.getText().toString());
                        target.editor.putString("hobby", target.hobby.getText().toString());
                        target.editor.putString("headphoto", target.postHeadphoto);
                        target.editor.commit();
                        target.editProfileDialog.dismiss();
                        Toast.makeText(target, "Edit success", Toast.LENGTH_SHORT).show();
                        target.finish();
                        break;
                    case Constant.COMPLETE_FAIL:
                        target.editProfileDialog.dismiss();
                        Toast.makeText(target, "Edit failure", Toast.LENGTH_SHORT).show();
                        target.btnDone.setEnabled(true);
                        break;
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String currentNickName = sharedPreferences.getString("nickName", "");
        String currentFreeTime = sharedPreferences.getString("freeTime", "");
        String currentSign = sharedPreferences.getString("sign", "");
        String currentHobby = sharedPreferences.getString("hobby", "");
        String currentBusiness = sharedPreferences.getString("business", "");

        freeTime.setText(currentFreeTime);
        business.setText(currentBusiness);
        hobby.setText(currentHobby);
        sign.setText(currentSign);
        nickName.setText(currentNickName);

        boolean isRemember = sharedPreferences.getBoolean("remember", false);
        if (isRemember) {
            String username = sharedPreferences.getString("userName", "");
            userName.setText(username);
        } else {
            userName.setText("Error");
        }
    }

    private class editProfileThread implements Runnable {
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
                boolean isRemember = sharedPreferences.getBoolean("remember", false);
                int currentUserId = 0;
                if (isRemember) {
                    currentUserId = sharedPreferences.getInt("userId", -1);
                } else {
                    System.out.println("No UserId in local_file");
                }
                object.put("userId", currentUserId);
                object.put("nickname", nickName.getText().toString());
                object.put("freetime", freeTime.getText().toString());
                object.put("sign", sign.getText().toString());
                object.put("business", business.getText().toString());
                object.put("hobby", hobby.getText().toString());
                object.put("headphoto", postHeadphoto);
                String result = WebService.executeHttpPost(object, "CompleteInfo");
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
