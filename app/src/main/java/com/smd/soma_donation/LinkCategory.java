package com.smd.soma_donation;

/**
 * Created by YS on 2016-07-23.
 */
public class LinkCategory {
    public static final String RANKING = "ranking";
    public static final String MONTHLY = "monthly";

    public static boolean isRanking(String ranking) {
        return RANKING.equals(ranking.toLowerCase());
    }

    public static boolean isMonthly(String monthly) {
        return MONTHLY.equals(monthly.toLowerCase());
    }
}
