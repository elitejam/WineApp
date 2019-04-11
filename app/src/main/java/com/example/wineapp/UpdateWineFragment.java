package com.example.wineapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateWineFragment.OnUpdateWineListener} interface
 * to handle interaction events.
 * Use the {@link UpdateWineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateWineFragment extends Fragment {

    private TextView wineName;
    private RatingBar newRating;
    private Button updateButton;

    private OnUpdateWineListener mListener;

    public UpdateWineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Wine wine.
     * @return A new instance of fragment UpdateWineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateWineFragment newInstance(Wine wine) {
        UpdateWineFragment fragment = new UpdateWineFragment();

        Bundle args = new Bundle();
        args.putParcelable("wine", wine);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_wine, container, false);
        wineName = rootView.findViewById(R.id.wineName);
        newRating = rootView.findViewById(R.id.updateRating);
        updateButton = rootView.findViewById(R.id.updateButton);

        Bundle bundle = this.getArguments();

        // Make sure a bundle is received.
        if (bundle != null) {
            final Wine wine = bundle.getParcelable("wine");
            wineName.setText(wine.name());
            final int id = wine.id();

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataManager dm = ((MainActivity) getActivity()).dm;
                    HashMap<String, String> props = new HashMap<>();
                    String rating = Double.toString(newRating.getRating());

                    props.put("rating", rating);
                    dm.update(id, props);
                    getFragmentManager().popBackStack();
                    ((MainActivity) getActivity()).loadWineListLayout();

                    Toast.makeText(
                            getContext(),
                            "UPDATING WINE: " + wine.name() + " " + rating + " stars beep boop...",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // TODO: Not sure how dangerous this is...
//        if (context instanceof OnUpdateWineListener) {
//            mListener = (OnUpdateWineListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnWineUpdateListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUpdateWineListener {
        // TODO: Update argument type and name
        void onUpdateWine(Wine wine);
    }
}
