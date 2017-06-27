package com.globalformulae.shiguang.maininterface.MainFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globalformulae.shiguang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitFragment extends Fragment {


    public HabitFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_habit, container, false);
    }

}
