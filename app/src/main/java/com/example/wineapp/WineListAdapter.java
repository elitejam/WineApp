package com.example.wineapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    public List<Wine> dataset_;
    private Context context_;
    private WineDetailFragment.OnDetailSelectListener handler_;
    private DataManager dm_;

    // ---------------------------------------------------------------------------------------------
    // Define the widget that goes in the Wine List recycler list view
    // ---------------------------------------------------------------------------------------------
    public class WineListEntry extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {

        // TODO: use a string for now; switch to horizontal linear layout later?
        private Wine wine_;
        private TextView contents_;
        private WineDetailFragment.OnDetailSelectListener handler_;

        WineListEntry(TextView v, WineDetailFragment.OnDetailSelectListener handler) {
            super(v);
            this.contents_ = v;
            this.handler_ = handler;

            // make each wine list entry clickable
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        /* Add OnWineListener to each WineListEntry.
         *
         */
        @Override
        public void onClick(View view) {
            // pass the click event onto the provided handler
//            Log.i("click() = ", Integer.toString(this.getAdapterPosition()));
            this.handler_.onDetailSelected(this.getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (BuildConfig.DEBUG && this.wine_ == null) {
                throw new AssertionError();
            }
            WineListAdapter.this.remove(view, this.wine_.id(), this.getAdapterPosition());
            return false;
        }
    }

    /**
     * Tell this adapter to update the dataset and refresh the recycler list view
     */
    public void refresh() {
        this.dataset_.clear();
        this.dataset_ = this.dm_.selectAll();

        this.notifyDataSetChanged();
    }

    /** Private method to remove a wine entry from the list.
     * @param view A View object handle from the view holder
     * @param position An integer wine id to specify which wine to remove
     */
    public void remove(final View view, final int wine_id, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

        // if you make a final local variable, it will be captured by the alertDialogBuilder closure
        final DataManager dm = this.dm_;
//        Log.i("pos: ", Integer.toString(position));

        alertDialogBuilder.setMessage("Delete this wine?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                HashMap<String, String> q_params = new HashMap<>();
                                q_params.put(DataManager.TABLE_ROW_ID, "" + wine_id);

                                List<Wine> results = dm.find(q_params);
                                if (BuildConfig.DEBUG && results.size() != 1) {
                                    throw new AssertionError();
                                }

                                Toast.makeText(
                                        view.getContext(),
                                        "Wine #" + results.get(0).id() + ": \"" + results.get(0).name() + "\" removed.",
                                        Toast.LENGTH_LONG
                                ).show();

                                dataset_.remove(position);
                                dm.delete(wine_id);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                                notifyDataSetChanged();

                            }
                        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // ---------------------------------------------------------------------------------------------
    // RecyclerView implementation
    // ---------------------------------------------------------------------------------------------
    public WineListAdapter(List<Wine> data, DataManager dm, Context context, WineDetailFragment.OnDetailSelectListener handler) {
        this.dataset_ = data;
        this.context_ = context;
        this.handler_ = handler;
        this.dm_ = dm;
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
        // set wine object
        entry.wine_ = this.dataset_.get(position);
        entry.contents_.setText(this.dataset_.get(position).toString());

        if (position % 2 == 1) {
            entry.contents_.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListRowBackgroundDark));
        } else {
            entry.contents_.setBackgroundColor(this.context_.getResources().getColor(R.color.colorWineListBackgroundLight));
        }

    }

    @Override
    public int getItemCount() {
        return this.dataset_.size();
    }
}
