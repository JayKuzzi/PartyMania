package com.qmul.partymania.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fashare.stack_layout.StackLayout;
import com.qmul.partymania.R;
import com.qmul.partymania.beans.Matching_Item;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.views.XCImageView;

import java.util.List;

public class MatchingItemAdapter extends StackLayout.Adapter<MatchingItemAdapter.MyViewHolder> {
    private List<Matching_Item> mItemInfoList;
    private Context context;

    public MatchingItemAdapter(List<Matching_Item> mItemInfoList, Context context) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_matching, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Matching_Item matching_item = mItemInfoList.get(position);
        viewHolder.matching_item_photo.setImageBitmap(Square.centerSquareScaleBitmap(BitmapTool.String2Bitmap(matching_item.getHeadphoto()),200));
        viewHolder.matching_item_nickName.setText(matching_item.getNickName());
        if (matching_item.getSex() == 1)
            viewHolder.matching_item_sex.setImageResource(R.drawable.ic_man_x);
        else
            viewHolder.matching_item_sex.setImageResource(R.drawable.ic_woman_x);
        viewHolder.matching_item_freeTime.setText(matching_item.getFreeTime());
        viewHolder.matching_item_sign.setText(matching_item.getSign());
        viewHolder.matching_item_business.setText(matching_item.getBusiness());
        viewHolder.matching_item_hobby.setText(matching_item.getHobby());
        viewHolder.matching_item_email.setText(matching_item.getEmail());


    }

    @Override
    public int getItemCount() {
        if (mItemInfoList == null) {
            return 0;
        }
        return mItemInfoList.size();
    }


    class MyViewHolder extends StackLayout.ViewHolder {
        View item;//最外层子view

        TextView matching_item_nickName, matching_item_age, matching_item_freeTime,
                matching_item_sign, matching_item_business, matching_item_hobby, matching_item_email;

        XCImageView matching_item_photo;
        ImageView matching_item_sex;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            matching_item_nickName = itemView.findViewById(R.id.matching_item_nickname);
            matching_item_sex = itemView.findViewById(R.id.matching_item_sex);
            matching_item_age = itemView.findViewById(R.id.matching_item_age);
            matching_item_freeTime = itemView.findViewById(R.id.matching_item_freeTime);
            matching_item_sign = itemView.findViewById(R.id.matching_item_sign);
            matching_item_business = itemView.findViewById(R.id.matching_item_business);
            matching_item_hobby = itemView.findViewById(R.id.matching_item_hobby);
            matching_item_photo = itemView.findViewById(R.id.matching_item_photo);
            matching_item_email = itemView.findViewById(R.id.matching_item_email);

        }
    }


}
