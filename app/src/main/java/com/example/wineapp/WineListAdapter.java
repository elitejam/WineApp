package com.example.wineapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    private List<Wine> dataset_;
    private WineDetailFragment.OnDetailSelectListener handler_;
    private DataManager dm_;

    // ---------------------------------------------------------------------------------------------
    // Define the widget that goes in the Wine List recycler list view
    // ---------------------------------------------------------------------------------------------
    public class WineListEntry extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {

        private Wine wine_;
        private View contents_;
        private WineDetailFragment.OnDetailSelectListener handler_;

        WineListEntry(View v, WineDetailFragment.OnDetailSelectListener handler) {
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
    private void remove(final View view, final int wine_id, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

        // if you make a final local variable, it will be captured by the alertDialogBuilder closure
        final DataManager dm = this.dm_;

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
    public WineListAdapter(List<Wine> data, DataManager dm, WineDetailFragment.OnDetailSelectListener handler) {
        this.dataset_ = data;
        this.handler_ = handler;
        this.dm_ = dm;
    }

    @Override
    public WineListAdapter.WineListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        // use the WineCardFragment
        View wineCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_wine_list_card, parent, false);
        return new WineListEntry(wineCard, this.handler_);
    }

    @Override
    public void onBindViewHolder(WineListEntry entry, int position) {
        // set wine object
        entry.wine_ = this.dataset_.get(position);

        if (BuildConfig.DEBUG && entry.wine_ == null) {
            throw new AssertionError();
        }

        TextView name = entry.contents_.findViewById(R.id.wineCard_name);
        TextView brand = entry.contents_.findViewById(R.id.wineCard_brand);
        TextView cost = entry.contents_.findViewById(R.id.wineCard_cost);
        RatingBar rating = entry.contents_.findViewById(R.id.wineCard_rating);

        if (entry.wine_.name() == null)     name.setText(R.string.null_name);
        else                                name.setText(entry.wine_.name());

        if (entry.wine_.brand() == null)   brand.setText(R.string.null_brand);
        else                                brand.setText(entry.wine_.brand());

        cost.setText(String.format(Locale.ENGLISH, "$%.2f", entry.wine_.cost()));
        rating.setRating((float) (entry.wine_.rating()));
    }

    @Override
    public int getItemCount() {
        return this.dataset_.size();
    }
}
