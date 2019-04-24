package com.maestromaster.foursquaremapexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maestromaster.foursquaremapexample.items.Category;
import com.maestromaster.foursquaremapexample.items.Venue;

import java.util.ArrayList;
import java.util.List;

import static com.maestromaster.foursquaremapexample.MapsActivity.TAG;

public class MyPageAdapter extends PagerAdapter {

    private List<Venue> models = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;

    public MyPageAdapter(List<Venue> models, Context context) {
        this.models.addAll(models);

        this.context = context;
    }

    @Override
    public int getCount() {
        // Log.d(TAG,"MODELS SIZE = "+models.size());
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Venue venue = models.get(position);

        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        final com.makeramen.roundedimageview.RoundedImageView imageView = view.findViewById(R.id.image);

        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);

        String descr = venue.getLocation().getAddress();

        if (venue.getCategories().size() > 0) {
            Category category = venue.getCategories().get(0);
            String name = category.getName();




            /*if(name.contains("Спорт"))  imageView.setBackgroundResource(R.drawable.sport);
            if(name.contains("Здание"))  imageView.setBackgroundResource(R.drawable.build);
            if(name.contains("Парк"))  imageView.setBackgroundResource(R.drawable.park);
           // if(name.contains("Спорт"))  imageView.setBackgroundResource(R.drawable.sport);
           // if(name.contains("Спорт"))  imageView.setBackgroundResource(R.drawable.sport);
            descr  = descr+ ", " + name;*/


        }

        LinearLayout rel = view.findViewById(R.id.rel);


        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        rel.setOnTouchListener(new MyTouchListener(rel, context, new ZoomInterface() {
            @Override
            public void increase() {
                Log.d("TAG", "increase");

                ViewGroup.LayoutParams params = imageView.getLayoutParams();

                int baseHeight = params.height;

                int newH = baseHeight + 400;
                params.height = newH;
                imageView.setLayoutParams(params);

                Animation slide_up = AnimationUtils.loadAnimation(context,
                        R.anim.scale_up);
                imageView.setVisibility(View.VISIBLE);
                slide_up.setDuration(200);
                imageView.startAnimation(slide_up);

            }

            @Override
            public void reduce() {
                Animation scale_down = AnimationUtils.loadAnimation(context,
                        R.anim.scale_down);
                //  imageView.setVisibility(View.GONE);
                scale_down.setDuration(150);
                scale_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();

                        int baseHeight = params.height;

                        int newH = baseHeight - 400;
                        params.height = newH;
                        imageView.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(scale_down);


            }
        }));

        imageView.setOnTouchListener(new MyTouchListener(rel, context, new ZoomInterface() {
            @Override
            public void increase() {
                Log.d("TAG", "increase");
                //imageView.setVisibility(View.VISIBLE);

            }

            @Override
            public void reduce() {
                Log.d("TAG", "reduce");
                Animation scale_down = AnimationUtils.loadAnimation(context,
                        R.anim.scale_down);
                //  imageView.setVisibility(View.GONE);
                scale_down.setDuration(150);
                scale_down.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();

                        int baseHeight = params.height;

                        int newH = baseHeight - 400;
                        params.height = newH;
                        imageView.setLayoutParams(params);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(scale_down);


            }
        }));


        description.setText(descr);

        title.setText(venue.getName());


        container.addView(view, 0);


        return view;
    }


    private void showContent() {

    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public Venue getVenue(int position) {
        if (position >= models.size()) position = models.size() - 1;
        if (position < 0) position = 0;
        return models.get(position);
    }


    private View mCurrentView;


    // Saving current active item
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "SET CURRENT ITEM");
        if (object == null) Log.d(TAG, "SET CURRENT ITEM null");
        else Log.d(TAG, "SET CURRENT ITEM ok");
        mCurrentView = (View) object;
    }

    public View getCurrentItem() {
        return mCurrentView;
    }

}
