package com.smd.soma_donation.rank;

/**
 * Created by YS on 2016-07-13.
 */
public class RankItem {
    private int id;
    private int rank;
    private String title;
    private int likeCount;
    private int viewCount;

    public RankItem(int id, int rank, String title, int likeCount, int viewCount) {
        this.id = id;
        this.rank = rank;
        this.title = title;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
