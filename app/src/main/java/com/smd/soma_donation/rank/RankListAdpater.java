package com.smd.soma_donation.rank;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smd.soma_donation.R;
import com.smd.soma_donation.retrofit.format.DTORankItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sue on 2016-03-19.
 */
public class RankListAdpater extends RecyclerView.Adapter<RankListAdpater.ViewHolder>
{
    Context context;
    List<DTORankItem> items = new LinkedList<>();
    int item_layout;

    public RankListAdpater(Context context, List<DTORankItem> items, int item_layout) {
        this.context = context;
        if (items != null) {
            this.items = items;
        }
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DTORankItem item = items.get(position);

        Log.d("TEST", "실제 아이템: " + item.getContentTitle());
        holder.id = item.getContentId();
        // TODO getContentRank 로 변경
        holder.rank.setText(position + 1 + "");
        holder.title.setText(item.getContentTitle());
        holder.likeCount.setText("" + item.getLikeCount());
        holder.viewCount.setText("" + item.getViewCount());

        if (item.isLike() == 1) {
            holder.likeLayout.setBackgroundResource(R.drawable.round_full);
            holder.likeText.setTextColor(context.getResources().getColor(R.color.reviewColor));
            holder.likeCount.setTextColor(context.getResources().getColor(R.color.reviewColor));
        } else {
            holder.likeLayout.setBackgroundResource(R.drawable.round_edge);
            holder.likeText.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.likeCount.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        holder.rankItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 뷰 누를경우 상세 보기로 이동
                Intent intent = new Intent(context, DetailDonation.class);
                intent.putExtra("detail_id", item.getContentId());
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
        TextView rank, title, likeCount, viewCount, likeText;
        LinearLayout rankItem;
        LinearLayout likeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rankItem = (LinearLayout) itemView.findViewById(R.id.rank_item);
            rank = (TextView) itemView.findViewById(R.id.rank);
            title = (TextView) itemView.findViewById(R.id.title);
            likeCount = (TextView) itemView.findViewById(R.id.likeCount);
            viewCount = (TextView) itemView.findViewById(R.id.viewCount);
            likeLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);
            likeText = (TextView) itemView.findViewById(R.id.like_text);
        }
    }
}