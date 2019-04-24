package com.maestromaster.foursquaremapexample;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class MyTouchListener implements View.OnTouchListener {

    private GestureDetector mDetector;

    View view;
    Context context;
    ZoomInterface zoomInterface;

    int minimumH  = 0;


    public MyTouchListener(View view, Context context, ZoomInterface zoomInterface) {
        this.view = view;
        this.context = context;
        this.zoomInterface = zoomInterface;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        minimumH = params.height;

        mDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    // In the SimpleOnGestureListener subclass you should override
    // onDown and any other gesture that you want to detect.
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        private final float minDY = 0;
        private int vector = 1;
        private int counter = 0;
        private final int maxCount = 2;



        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
              Log.d("TAG", "onScroll: distanceX "+(int)distanceX+"   distanceY "+(int)distanceY);
             // Log.d("TAG", "onScroll: distanceY "+distanceY);

            int newVector = (int)(distanceY/Math.abs(distanceY));
            counter++;
            if(counter==maxCount){
                vector = newVector;
                counter=0;
            }
            Log.d("TAG","vector = "+vector+" newVector ="+newVector+" counter="+counter);

            if(Math.abs(distanceY)>Math.abs(distanceX) && newVector == vector && Math.abs(distanceY)>minDY){
                Log.d("TAG","RESIZE");


               /* ViewGroup.LayoutParams params = view.getLayoutParams();

                int baseHeight = params.height;

                int newH = baseHeight+(int)(2.5*distanceY);

                if(newH>minimumH) {

                    params.height = newH;
                }*/

              //  Log.d("TAG", "onFling: baseHeight="+baseHeight+" newHeigth = "+newH);

              //  view.setLayoutParams(params);
            }

            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
         //   Log.d("TAG", "onFling: ");

            Log.d("TAG", "onFling: velocityY="+velocityY);

            Log.d("TAG", "onFling: event1="+event1.getY());
            Log.d("TAG", "onFling: event2="+event2.getY());

            if(zoomInterface !=null){

                if(vector == 1 ){
                    zoomInterface.increase();
                }
                if(vector == -1){
                    zoomInterface.reduce();
                }
            }


            /*new Thread(){
                @Override
                public void run() {

                    while((int)Math.abs(lastDY)>1) {

                        h.post(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("TAG", "lastDY=" + lastDY);
                                ViewGroup.LayoutParams params = view.getLayoutParams();
                                int baseHeight = params.height;
                                int newH = baseHeight + (int) (lastDY);
                                lastDY = lastDY / zatuhanie;

                                params.height = newH;
                            }
                        });
                    }
                }
            }.start();*/

            return true;
        }

       // Handler h = new Handler();



    }




}
