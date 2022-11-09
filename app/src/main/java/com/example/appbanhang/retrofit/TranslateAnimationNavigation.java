package com.example.appbanhang.retrofit;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.appbanhang.R;

public class TranslateAnimationNavigation implements View.OnTouchListener{
    private GestureDetector gestureDetector;

    public TranslateAnimationNavigation(Context context, View viewAmination){
        gestureDetector = new GestureDetector(context, new Simpledeclaration(viewAmination));
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public static class Simpledeclaration extends GestureDetector.SimpleOnGestureListener{
        private View viewAnimation;
        public boolean isFinish = true;

        public Simpledeclaration(View viewAnimation) {
            this.viewAnimation = viewAnimation;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (distanceY > 0){
                hideNavigation();
            }
            else{
                showNavigation();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private void hideNavigation() {
            if(viewAnimation == null || viewAnimation.getVisibility() == View.GONE){
                return;
            }
            Animation animationDown = AnimationUtils.loadAnimation(viewAnimation.getContext(), R.anim.move_navigation_down);
            animationDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewAnimation.setVisibility(View.VISIBLE);
                    isFinish = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    viewAnimation.setVisibility(View.GONE);
                    isFinish = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if(isFinish) viewAnimation.startAnimation(animationDown);
        }
        private void showNavigation(){
            if(viewAnimation == null || viewAnimation.getVisibility() == View.VISIBLE){
                return;
            }
            Animation animationUp = AnimationUtils.loadAnimation(viewAnimation.getContext(), R.anim.move_navigation_up);
            animationUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewAnimation.setVisibility(View.VISIBLE);
                    isFinish = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    viewAnimation.getVisibility();
                    isFinish = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if(isFinish) viewAnimation.startAnimation(animationUp);
        }
    }
}
