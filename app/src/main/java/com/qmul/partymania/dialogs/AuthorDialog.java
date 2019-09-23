package com.qmul.partymania.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Constant;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.tools.WebService;
import com.qmul.partymania.views.XCImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


public class AuthorDialog extends Dialog {

    public AuthorDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_author);
        setCanceledOnTouchOutside(false);
    }
}