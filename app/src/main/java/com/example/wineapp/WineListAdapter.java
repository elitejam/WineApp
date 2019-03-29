package com.example.wineapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    private String[] dataset_;
    private Context context_;
    private WineDetailFragment.OnDetailSelectListener handler_;

    // ---------------------------------------------------------------------------------------------
    // Define the widget that goes in the Wine List recycler list view
    // ---------------------------------------------------------------------------------------------
    public static class WineListEntry extends RecyclerView.ViewHolder implements View.OnClickListener {
        // TODO: use a string for now; switch to horizontal linear layout later?
        private TextView contents_;
        private WineDetailFragment.OnDetailSelectListener handler_;

        public WineListEntry(TextView v, WineDetailFragment.OnDetailSelectListener handler) {
            super(v);
            this.contents_ = v;
            this.handler_ = handler;

            // make each wine list entry clickable
            v.setOnClickListener(this);
        }

        /* Add OnWineListener to each WineListEntry.
         *
         */
        @Override
        public void onClick(View view) {
            // pass the click event onto the provided handler
            this.handler_.onDetailSelected(this.getAdapterPosition());
        }
    }

    // ---------------------------------------------------------------------------------------------
    // RecyclerView implementation
    // ---------------------------------------------------------------------------------------------
    public WineListAdapter(String[] data, Context context, WineDetailFragment.OnDetailSelectListener handler) {
        this.dataset_ = data;
        this.context_ = context;
        this.handler_ = handler;
    }

    @Override
    public WineListAdapter.WineListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        // apparently the view has to be created outside the view-holder...
        TextView v = new TextView(parent.getContext());

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        v.setPadding(10, 20, 10, 15);
        v.setLayoutParams(p);
        v.setTypeface(Typeface.MONOSPACE);

        return new WineListEntry(v, this.handler_);
    }

    @Override
    public void onBindViewHolder(WineListEntry entry, int position) {
        // TODO: this will be more complicated when we use more than a TextView
        entry.contents_.setText(this.dataset_[position]);

        if (position % 2 == 1) {
            entry.contents_.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListRowBackgroundDark));
        } else {
            entry.contents_.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListBackgroundLight));
        }
    }

    @Override
    public int getItemCount() {
        return this.dataset_.length;
    }
}
