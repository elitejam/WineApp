package com.example.wineapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    private String[] dataset_;
    private Context context_;

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

    public WineListAdapter(String[] data, Context context) {
        this.dataset_ = data;
        this.context_ = context;
    }

    @Override
    public WineListAdapter.WineListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        // apparently the view has to be created outside the viewholder...
        TextView v = new TextView(parent.getContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        v.setPadding(10, 20, 10, 15);
        v.setLayoutParams(p);
        v.setTypeface(Typeface.MONOSPACE);

        return new WineListEntry(v);
    }

    @Override
    public void onBindViewHolder(WineListEntry entry, int position) {
        entry.contents.setText(this.dataset_[position]);

        if (position % 2 == 1) {
            entry.contents.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListRowBackgroundDark));
        } else {
            entry.contents.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListBackgroundLight));
        }
    }

    @Override
    public int getItemCount() {
        return this.dataset_.length;
    }
}
