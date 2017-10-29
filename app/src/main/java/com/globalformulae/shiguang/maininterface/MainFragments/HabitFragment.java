package com.globalformulae.shiguang.maininterface.MainFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.globalformulae.shiguang.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitFragment extends Fragment {

    @BindView(R.id.anim)
    ImageView anim;

    public HabitFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_habit, container, false);
        ButterKnife.bind(this,view);
        //Glide.with(this).load(R.drawable.pgy).into(anim);
        return view;
    }

}
