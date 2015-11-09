package com.awrtechnologies.valor.valorfireplace.activities;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by m004 on 13/07/15.
 */
public class ViewTouchListener implements View.OnTouchListener {

    float currentposX;
    float currentposY;
    float currentViewposX;
    float currentViewposY;

    float movpoxX;
    float movposY;
    float diffXpos;
    float diffYpos;
    ImageView imagcenterbackground;
    ImageView imgCenter;
    float widthImgBackground;
    float heightImgBackground;
    Context context;


    public ViewTouchListener(Context context,ImageView imagcenterbackground, ImageView imgCenter, float widthImgBackground, float heightImgBackground) {
        this.imagcenterbackground = imagcenterbackground;
        this.imgCenter = imgCenter;
        this.widthImgBackground = widthImgBackground;
        this.heightImgBackground = heightImgBackground;
        this.context=context;


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                currentposX = event.getX();
                currentposY = event.getY();


                currentViewposX = ViewHelper.getX(imagcenterbackground);
                currentViewposY = ViewHelper.getY(imagcenterbackground);


                break;
            case MotionEvent.ACTION_UP:
                float diffX = 0;
                float diffY = 0;

                if (currentViewposX <= 5) {
                    diffX = currentViewposX * -1;
                } else if (currentViewposX >= widthImgBackground - imagcenterbackground.getWidth()) {
                    diffX = (widthImgBackground - imagcenterbackground.getWidth()) - currentViewposX;
                }


                if (ViewHelper.getY(imagcenterbackground) <= 5) {
                    diffY = currentViewposY * -1;
                } else if (currentViewposY >= heightImgBackground - imagcenterbackground.getHeight()) {
                    diffY = (heightImgBackground - imagcenterbackground.getHeight()) - currentViewposY;
                }

                AnimatorSet animatorSet = new AnimatorSet();
                Collection<Animator> animators = new ArrayList<>();

                if (diffX != 0) {
                    animators.add(ObjectAnimator.ofFloat(imagcenterbackground, "x", currentViewposX, currentViewposX + diffX));
                    animators.add(ObjectAnimator.ofFloat(imgCenter, "x", currentViewposX, currentViewposX + diffX));
                }
                if (diffY != 0) {
                    animators.add(ObjectAnimator.ofFloat(imagcenterbackground, "y", currentViewposY, currentViewposY + diffY));
                    animators.add(ObjectAnimator.ofFloat(imgCenter, "y", currentViewposY, currentViewposY + diffY));
                }
                if (animators.size() > 0) {
                    animatorSet.playTogether(animators);
                    animatorSet.setDuration(100);
                    animatorSet.start();
                }

                //ViewHelper.setTranslationX(imagcenterbackground, diffX);
                //  ViewHelper.setTranslationY(imagcenterbackground, diffY);

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                movpoxX = event.getX();
                movposY = event.getY();

                diffXpos = movpoxX - currentposX;
                diffYpos = movposY - currentposY;

                ViewHelper.setX(imagcenterbackground, currentViewposX + diffXpos);
                ViewHelper.setY(imagcenterbackground, currentViewposY + diffYpos);
                ViewHelper.setX(imgCenter, currentViewposX + diffXpos);
                ViewHelper.setY(imgCenter, currentViewposY + diffYpos);


                currentViewposX = currentViewposX + diffXpos;
                currentViewposY = currentViewposY + diffYpos;

                movpoxX = event.getX();
                movposY = event.getY();


                break;

            case MotionEvent.ACTION_CANCEL:
//                         diffX=0;
//                         diffY=0;
//                        if(ViewHelper.getX(imagcenterbackground)<=5)
//                        {
//                            diffX=ViewHelper.getX(imagcenterbackground)*-1;
//                        }
//                        else if(imagcenterbackground.getX()>=widthImgBackground-imagcenterbackground.getWidth())
//                        {
//                            diffX=diffXpos+(widthImgBackground-imagcenterbackground.getWidth());
//                        }
//                        if(ViewHelper.getY(imagcenterbackground)<=5)
//                        {
//                            diffY=ViewHelper.getX(imagcenterbackground)*-1;
//                        }
//                        else if(imagcenterbackground.getY() >=heightImgBackground-imagcenterbackground.getHeight())
//                        {
//                            diffY=diffYpos+(heightImgBackground-imagcenterbackground.getHeight());
//                        }
//                        ViewHelper.setTranslationX(imagcenterbackground, diffX);
//                        ViewHelper.setTranslationY(imagcenterbackground, diffY);
                break;
        }
        return true;
    }
}
