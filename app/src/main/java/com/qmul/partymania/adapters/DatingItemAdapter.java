package com.qmul.partymania.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmul.partymania.R;
import com.qmul.partymania.beans.DatingHall_Item;
import com.qmul.partymania.dialogs.CommentDialog;
import com.qmul.partymania.dialogs.UserDetailDialog;
import com.qmul.partymania.fragments.DatingHall;
import com.qmul.partymania.tools.BitmapTool;
import com.qmul.partymania.tools.Square;

import java.util.List;

public class DatingItemAdapter extends RecyclerView.Adapter<DatingItemAdapter.MyViewHolder> {
    private List<DatingHall_Item> mItemInfoList;
    private Context context;
    private DatingHall datingHall;

    public DatingItemAdapter(List<DatingHall_Item> mItemInfoList, Context context, DatingHall datingHall) {
        this.mItemInfoList = mItemInfoList;
        this.context = context;
        this.datingHall = datingHall;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemView = inflater.inflate(R.layout.list_item_partyhall, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
        final DatingHall_Item datingHall_item = mItemInfoList.get(position);
        viewHolder.hall_item_nickName.setText(datingHall_item.getPostManNickname());
        viewHolder.hall_item_content.setText(datingHall_item.getContent());
        viewHolder.hall_item_joined_count.setText(datingHall_item.getJoined_count() + "  Have Joined");
        if (datingHall_item.getType() == 1) {
            viewHolder.hall_item_partyMode.setImageResource(R.drawable.ic_party_mode_selected);
            viewHolder.hall_item_companyMode.setImageResource(R.drawable.ic_company_mode);
        } else {
            viewHolder.hall_item_partyMode.setImageResource(R.drawable.ic_party_mode);
            viewHolder.hall_item_companyMode.setImageResource(R.drawable.ic_company_mode_selected);
        }
        viewHolder.hall_item_head_photo.setImageBitmap(Square.centerSquareScaleBitmap(BitmapTool.String2Bitmap(datingHall_item.getPostManHeadphoto()), 200));

        viewHolder.hall_item_photo.setImageBitmap(BitmapTool.String2Bitmap(datingHall_item.getPostPhoto()));
        viewHolder.hall_item_time.setText(datingHall_item.getTime());

        viewHolder.hall_item_head_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailDialog userDetailDialog = new UserDetailDialog(context,datingHall_item.getPostManNickname());
                userDetailDialog.show();
            }
        });

        viewHolder.hall_item_option.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.hall_item_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.hall_item_joined_count.setText((datingHall_item.getJoined_count() +1) + "  Have Joined");
                datingHall.doLikePlus(datingHall_item.getPostId(),(datingHall_item.getJoined_count() +1));
                viewHolder.hall_item_love.setImageResource(R.drawable.ic_love_selected);
            }
        });

        viewHolder.hall_item_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(context,datingHall_item.getPostId());
                commentDialog.show();
            }
        });

        viewHolder.hall_item_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog commentDialog = new CommentDialog(context,datingHall_item.getPostId());
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
        View item;//最外层子view

        TextView hall_item_nickName, hall_item_content, hall_item_joined_count, hall_item_time, hall_item_comments;

        ImageView hall_item_partyMode, hall_item_companyMode,
                hall_item_head_photo, hall_item_option, hall_item_photo, hall_item_love, hall_item_message;
        ;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;//将itemView赋给item拿去做监听
            hall_item_nickName = itemView.findViewById(R.id.hall_item_nickName);
            hall_item_content = itemView.findViewById(R.id.hall_item_content);
            hall_item_joined_count = itemView.findViewById(R.id.hall_item_likes);
            hall_item_partyMode = itemView.findViewById(R.id.hall_item_party);
            hall_item_companyMode = itemView.findViewById(R.id.hall_item_company);

            hall_item_head_photo = itemView.findViewById(R.id.hall_item_head_photo);
            hall_item_photo = itemView.findViewById(R.id.hall_item_photo);
            hall_item_time = itemView.findViewById(R.id.hall_item_date);
            hall_item_comments = itemView.findViewById(R.id.hall_item_comments);
            hall_item_message = itemView.findViewById(R.id.hall_item_message);
            hall_item_love = itemView.findViewById(R.id.hall_item_love);
            hall_item_option = itemView.findViewById(R.id.hall_item_option);

        }
    }


}
