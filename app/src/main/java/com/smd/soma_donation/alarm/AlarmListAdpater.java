package com.smd.soma_donation.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.smd.soma_donation.R;
import com.smd.soma_donation.rank.DetailDonation;
import com.smd.soma_donation.retrofit.format.DTOAlarmItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sue on 2016-03-19.
 */
public class AlarmListAdpater extends RecyclerView.Adapter<AlarmListAdpater.ViewHolder>
{
    Context context;
    List<DTOAlarmItem> items = new LinkedList<>();
    int item_layout;

    public AlarmListAdpater(Context context, List<DTOAlarmItem> items, int item_layout) {
        this.context = context;
        if (items != null) {
            this.items = items;
        }
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DTOAlarmItem item = items.get(position);

        Log.d("TEST", "실제 아이템: " + item.getContentTitle());
        holder.id = item.getAlarmId();
        // TODO getContentRank 로 변경
        holder.title.setText(item.getContentTitle());
        holder.description.setText(item.getDescription());
        holder.calender.setImageResource(android.R.drawable.ic_menu_today);
        holder.alarmDate.setText(item.getAlarmDate().split("T")[0]);
        if (item.isCheck() == 1) {
            holder.alarmItemLayout.setBackgroundResource(R.color.reviewColor);
        } else {
            holder.alarmItemLayout.setBackgroundResource(R.color.reviewColorUnCheck);
        }

        holder.alarmItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 뷰 누를경우 상세 보기로 이동
                Intent intent = new Intent(context, DetailDonation.class);
                intent.putExtra("detail_id", item.getAlarmContentId());
                intent.putExtra("alarm_id", item.getAlarmId());
                context.startActivity(intent);
            }
        });

        holder.loadThumbNailmage(item.getContentImg());

    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void clear() {
        this.items.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        int id;
        RelativeLayout alarmItemLayout;
        TextView title, description, alarmDate;
        ImageView alarmImg, calender;
        boolean isCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            alarmItemLayout = (RelativeLayout) itemView.findViewById(R.id.alarm_item);
            title = (TextView) itemView.findViewById(R.id.alarm_title);
            description = (TextView) itemView.findViewById(R.id.alarm_description);
            alarmImg = (ImageView) itemView.findViewById(R.id.alarm_img);
            alarmDate = (TextView) itemView.findViewById(R.id.alarm_date);
            calender = (ImageView) itemView.findViewById(R.id.calender);
        }

        private void loadThumbNailmage(String content_img) {
            String baseUrl = context.getResources().getString(R.string.baseURL);
            String imgUrl = baseUrl + "/ranking/" + id + "/image?content_img=" + content_img;
            Glide.with(context).load(imgUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(alarmImg) {
                @Override
                protected void setResource(Bitmap resource) {
                    if (resource != null)
                        alarmImg.setImageBitmap(resource);
                }
            });
        }
    }
}