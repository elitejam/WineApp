package com.example.wineapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddWineFragment.OnAddWineListener} interface
 * to handle interaction events.
 * Use the {@link AddWineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWineFragment extends Fragment {

    private static final String TAG = "AddWineFragment";

    private OnAddWineListener mListener;

    private EditText nameEditText;
    private EditText yearEditText;
    private EditText brandEditText;
    private EditText costEditText;
    private EditText grapeEditText;
    private RatingBar ratingRatingBar;
    private Button addWineButton;

    public AddWineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddWineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddWineFragment newInstance() {
        AddWineFragment fragment = new AddWineFragment();
        Bundle args = new Bundle();
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
        View rootView =  inflater.inflate(R.layout.fragment_add_wine, container, false);

        addWineButton = rootView.findViewById(R.id.addWineButton);
        nameEditText = rootView.findViewById(R.id.name);
//        yearEditText = rootView.findViewById(R.id.brand);
//        yearEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        brandEditText = rootView.findViewById(R.id.brand);
        costEditText = rootView.findViewById(R.id.cost);
        costEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        ratingRatingBar = rootView.findViewById(R.id.rating);
        addWineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                Log.d(TAG, name);
                String year = yearEditText.getText().toString();
                String rating = String.valueOf(ratingRatingBar.getRating());
                Toast.makeText(
                        getActivity(),
                        "ADDING WINE: " + name + " " + year + " " + rating + " stars beep boop...",
                        Toast.LENGTH_SHORT
                ).show();
                Wine newWine = new Wine(
                        1,
                        name,
                        "Yellowtail",
                        Wine.Color.RED,
                        5.22,
                        "Concord",
                        9.0
                );
                DataManager dm = ((MainActivity)getActivity()).dm;
                dm.insertWine(newWine);
                dm.printWineList(dm.selectAll());
                getFragmentManager().popBackStack();
                ((MainActivity)getActivity()).loadWineListLayout();
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAddWine();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddWineListener) {
            mListener = (OnAddWineListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setRetainInstance(true);
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
    public interface OnAddWineListener {
        // TODO: Update argument type and name
        void onAddWine();
    }

    public void showData (Cursor c) {
        while (c.moveToNext()){

            Log.i("LOLOLOLOL" + c.getString(0), c.getString(1) + ", " + c.getString(2) + ", "
                    + c.getString(3) + ", " + c.getString(4) + ", " + c.getString(5));
        }
    }
}
