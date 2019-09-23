package com.qmul.partymania.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.beans.Moment_Item;
import com.qmul.partymania.dialogs.CommentDialog;
import com.qmul.partymania.dialogs.UserDetailDialog;
import com.qmul.partymania.fragments.Moments;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Square;
import com.qmul.partymania.views.XCImageView;

import java.util.List;

public class MomentItemAdapter extends RecyclerView.Adapter<MomentItemAdapter.MyViewHolder> {
    private List<Moment_Item> mItemInfoList;
    private Context context;
    private Moments moments;
    public MomentItemAdapter(List<Moment_Item> mItemInfoList, Context context,Moments moments ) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
        this.moments =moments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_moment, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
        final Moment_Item moment_item = mItemInfoList.get(position);
        viewHolder.moment_item_nickName.setText(moment_item.getPostManNickname());
        viewHolder.moment_item_content.setText(moment_item.getContent());
        viewHolder.moment_item_likes.setText(moment_item.getLikes() + " likes");
        viewHolder.moment_item_head_photo.setImageBitmap(Square.centerSquareScaleBitmap(BitmapTool.String2Bitmap(moment_item.getPostManHeadphoto()), 200));
        viewHolder.moment_item_photo.setImageBitmap(BitmapTool.String2Bitmap(moment_item.getPostPhoto()));
        viewHolder.moment_item_time.setText(moment_item.getTime());
        viewHolder.moment_item_head_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailDialog userDetailDialog = new UserDetailDialog(context,moment_item.getPostManNickname());
                userDetailDialog.show();
            }
        });

        viewHolder.moment_item_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Report");
                builder.setMessage("You wanna report this?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "You've reported it", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

        viewHolder.moment_item_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.moment_item_likes.setText((moment_item.getLikes() +1) +" likes");
                moments.doLikePlus(moment_item.getPostId(),(moment_item.getLikes() +1));
                viewHolder.moment_item_love.setImageResource(R.drawable.ic_love_selected);
            }
        });

        viewHolder.moment_item_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(context,moment_item.getPostId());
                commentDialog.show();
            }
        });

        viewHolder.moment_item_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(context,moment_item.getPostId());
                commentDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemInfoList == null) {
            return 0;
        }
        return mItemInfoList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView moment_item_nickName, moment_item_content, moment_item_likes, moment_item_time, moment_item_comments;
        ImageView moment_item_option, moment_item_photo, moment_item_love, moment_item_message;
        XCImageView moment_item_head_photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            moment_item_nickName = itemView.findViewById(R.id.moment_item_nickName);
            moment_item_content = itemView.findViewById(R.id.moment_item_content);
            moment_item_likes = itemView.findViewById(R.id.moment_item_likes);
            moment_item_head_photo = itemView.findViewById(R.id.moment_item_head_photo);
            moment_item_photo = itemView.findViewById(R.id.moment_item_photo);
            moment_item_time = itemView.findViewById(R.id.moment_item_date);
            moment_item_comments = itemView.findViewById(R.id.moment_item_comments);
            moment_item_message = itemView.findViewById(R.id.moment_item_message);
            moment_item_love = itemView.findViewById(R.id.moment_item_love);
            moment_item_option = itemView.findViewById(R.id.moment_item_option);
        }
    }


}
