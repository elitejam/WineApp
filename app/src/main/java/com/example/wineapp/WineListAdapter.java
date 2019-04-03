package com.example.wineapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WineListAdapter extends RecyclerView.Adapter<WineListAdapter.WineListEntry> {
    private List<Wine> dataset_;
    private Context context_;
    private WineDetailFragment.OnDetailSelectListener handler_;

    // ---------------------------------------------------------------------------------------------
    // Define the widget that goes in the Wine List recycler list view
    // ---------------------------------------------------------------------------------------------
    public class WineListEntry extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {
        // TODO: use a string for now; switch to horizontal linear layout later?
        private TextView contents_;
        private WineDetailFragment.OnDetailSelectListener handler_;

        public WineListEntry(TextView v, WineDetailFragment.OnDetailSelectListener handler) {
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
            Toast.makeText(view.getContext(),
                    "LONG CLICK!",
                    Toast.LENGTH_LONG).show();
            remove(view, this.getAdapterPosition());
            return false;
        }


    }


    /* Private method to remove a wine entry from the list.
     * TODO: Need to call delete from db with id.
     * args:
     *     View view: A view from the view holder.
     *     int position: Position to remove from in adapter.
     *
     * returns:
     *     boolean: Always false for now.
     */
    private void remove(final View view, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

        alertDialogBuilder.setMessage("Confirm remove?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(view.getContext(),
                                        "You clicked yes!",
                                        Toast.LENGTH_LONG).show();
                                dataset_.remove(position);
                                notifyItemRemoved(position);
                            }
                        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(view.getContext(),
                        "You clicked no!",
                        Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // ---------------------------------------------------------------------------------------------
    // RecyclerView implementation
    // ---------------------------------------------------------------------------------------------
    public WineListAdapter(List<Wine> data, Context context, WineDetailFragment.OnDetailSelectListener handler) {
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
