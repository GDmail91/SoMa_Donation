package com.smd.soma_donation.review;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.format.DTOReviewItem;

import java.util.List;

/**
 * Created by sue on 2016-03-19.
 */
public class ReviewListAdpater extends RecyclerView.Adapter<ReviewListAdpater.ViewHolder>
{
    Context context;
    List<DTOReviewItem> items;
    int item_layout;

    public ReviewListAdpater(Context context, List<DTOReviewItem> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DTOReviewItem item = items.get(position);

        Log.d("TEST", "실제 아이템: "+item.getReview_id());
        holder.id = item.getReview_id();
        holder.calender.setImageResource(android.R.drawable.ic_menu_today);
        holder.reviewNumber.setText(""+item.getReview_id());
        holder.reviewTitle.setText(item.getReview_title());
        holder.reviewDonate.setText(""+item.getDonation());
        holder.reviewDate.setText(item.getPost_date().split("T")[0]);

        holder.reviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 뷰 누를경우 상세 보기로 이동
                Intent intent = new Intent(context, ReviewDonation.class);
                intent.putExtra("review_id", item.getReview_id());
                context.startActivity(intent);
            }
        });

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
        TextView reviewNumber, reviewTitle, reviewDonate, reviewDate;
        ImageView calender;
        LinearLayout reviewItem;

        public ViewHolder(View itemView) {
            super(itemView);
            calender = (ImageView) itemView.findViewById(R.id.calender);
            reviewItem = (LinearLayout) itemView.findViewById(R.id.review_item);
            reviewNumber = (TextView) itemView.findViewById(R.id.review_number);
            reviewTitle = (TextView) itemView.findViewById(R.id.review_title);
            reviewDonate = (TextView) itemView.findViewById(R.id.review_donate);
            reviewDate = (TextView) itemView.findViewById(R.id.review_date);
        }
    }
}