package com.globalformulae.shiguang.maininterface.MainFragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.globalformulae.shiguang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TimerFragment extends Fragment {

    @BindView(R.id.iv_sun)
    ImageView sunIV;
    @BindView(R.id.iv_soil)
    ImageView soilIV;
    @BindView(R.id.watering)
    ImageView wateringIV;
    @BindView(R.id.water_btn)
    Button waterBTN;
    private OnFragmentInteractionListener mListener;

    public TimerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimerFragment newInstance(String param1, String param2) {
        TimerFragment fragment = new TimerFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_timer, container, false);
        ButterKnife.bind(this,view);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.sun_anim);

        sunIV.setAnimation(animation);

        AnimationDrawable animationDrawable=(AnimationDrawable)soilIV.getBackground();
        animationDrawable.start();
        return view;
    }

    @OnClick(R.id.water_btn)
    void waterClick(){
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.watering);
        animation1.setFillAfter(false);
        wateringIV.startAnimation(animation1);

        wateringIV.setBackgroundResource(R.drawable.watering_anim);
        wateringIV.setVisibility(View.VISIBLE);
        final AnimationDrawable animationDrawable= (AnimationDrawable) wateringIV.getBackground();
        animationDrawable.start();
        wateringIV.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                wateringIV.setVisibility(View.GONE);

            }
        },4500);

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String str) {
        if (mListener != null) {
            mListener.onFragmentInteraction(str);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String str);
    }
    abstract class MyAnimationDrawable extends AnimationDrawable {
        Handler finishHandler;      // 判断结束的Handler
        public MyAnimationDrawable() {
        }
        @Override
        public void start() {
            super.start();
            /**
             * 首先用父类的start()
             * 然后启动线程，来调用onAnimationEnd()
             */
            finishHandler = new Handler();
            finishHandler.postDelayed(
                    new Runnable() {
                        public void run() {
                            onAnimationEnd();
                        }
                    }, 4500);
        }
        /**
         * 这个方法获得动画的持续时间（之后调用onAnimationEnd()）
         */
        public int getTotalDuration() {
            int durationTime = 0;
            for (int i = 0; i < this.getNumberOfFrames(); i++) {
                durationTime += this.getDuration(i);
            }
            return durationTime;
        }
        /**
         * 结束时调用的方法，一定要实现
         */
        abstract void onAnimationEnd();
    }
}
