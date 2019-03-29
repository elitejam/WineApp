package com.example.wineapp;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

// this is a linearlayout manager that lets you disable scrolling during runtime
public class LinearLayoutManagerScrollEnable extends LinearLayoutManager {
    private boolean scroll_enable_ = true;

    public LinearLayoutManagerScrollEnable(Context context) {
        super(context);
    }

    public void setScrollEnable(boolean enable) {
        this.scroll_enable_ = enable;
    }

    @Override
    public boolean canScrollVertically() {
        return this.scroll_enable_ && super.canScrollVertically();
    }
}
