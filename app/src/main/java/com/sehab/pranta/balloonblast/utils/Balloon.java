package com.sehab.pranta.balloonblast.utils;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.sehab.pranta.balloonblast.R;

public class Balloon extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;
    private BalloonListener mListener;
    private boolean mPopped;

    public Balloon(Context context) {
        super(context);
    }


    public Balloon(Context context,int color,int rawHeight) { //rawHeight is absolute pixels and we converted by constructor
        super(context);
        mListener = (BalloonListener) context;
        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);
        int rawWidth = rawHeight/2;
        int dpHeight = PixelHelper.pixelsToDp(rawHeight,context); //getting device independent pixels
        int dpWidth = PixelHelper.pixelsToDp(rawWidth,context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth,dpHeight); //LayoutParams to view objects layout
        setLayoutParams(params);
    }
    //this file updated



    public void releaseBalloon(int screenHeight,int duration){
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight,0f); //starting and ending values
        mAnimator.setInterpolator(new LinearInterpolator()); //how my animation will behave
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(!mPopped){
            mListener.popBalloon(this,false);
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
     setY((float) animation.getAnimatedValue());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mPopped && event.getAction() ==MotionEvent.ACTION_DOWN){
            mListener.popBalloon(this,true);
            mPopped=true;
            mAnimator.cancel();
        }
        return super.onTouchEvent(event);
    }

    public void setPopped(boolean popped) {
        mPopped = popped;
        if(popped){
            mAnimator.cancel();
        }
    }

    public interface BalloonListener{
        void popBalloon(Balloon balloon, boolean userTouch);

    }
}
