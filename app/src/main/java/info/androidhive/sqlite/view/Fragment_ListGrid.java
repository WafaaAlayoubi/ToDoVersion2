package info.androidhive.sqlite.view;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.androidhive.sqlite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_ListGrid extends Fragment {


    public Fragment_ListGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__list_grid, container, false);
    }

}
