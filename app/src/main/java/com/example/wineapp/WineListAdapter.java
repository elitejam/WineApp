package com.example.wineapp;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    private String[] dataset_;

    // ---------------------------------------------------------------------------------------------
    // Define the widget that goes in the Wine List recycler list view
    // ---------------------------------------------------------------------------------------------
    public static class WineListEntry extends RecyclerView.ViewHolder {
        // TODO: use a string for now; switch to horizontal linear layout later?
        public TextView contents;

        public WineListEntry(TextView v) {
            super(v);
            this.contents = v;
        }
    }

    public WineListAdapter(String[] data) {
        this.dataset_ = data;
    }

    @Override
    public WineListAdapter.WineListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        // apparently the view has to be created outside the viewholder...
        TextView v = new TextView(parent.getContext());
        return new WineListEntry(v);
    }

    @Override
    public void onBindViewHolder(WineListEntry entry, int position) {
        entry.contents.setText(this.dataset_[position]);
    }

    @Override
    public int getItemCount() {
        return this.dataset_.length;
    }
}
