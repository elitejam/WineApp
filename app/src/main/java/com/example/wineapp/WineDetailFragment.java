package com.example.wineapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WineDetailFragment.OnDetailSelectListener} interface
 * to handle interaction events.
 * Use the {@link WineDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WineDetailFragment extends Fragment {
    public WineDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WineDetailFragment.
     */
    public static WineDetailFragment newInstance(Wine info) {
        WineDetailFragment fragment = new WineDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable("wine", info);

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
        View rootView = inflater.inflate(R.layout.fragment_wine_detail, container, false);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            TextView info = rootView.findViewById(R.id.info);
            Wine w = bundle.getParcelable("wine");
            info.setText(w.toString());

            // add Wine object to delete button
            rootView.findViewById(R.id.wineDetailDeleteButton).setTag(w);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public interface OnDetailSelectListener {
        void onDetailSelected(int wine_id);
    }
}
