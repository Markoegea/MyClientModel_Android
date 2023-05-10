package com.kingmarco.myclientmodel.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.kingmarco.myclientmodel.R;
import com.kingmarco.myclientmodel.Threads.ImageThreads;

/**Adapter to be used in a ViewPager element, add multiples images and be able of slide them*/
public class ImagesAdapter extends PagerAdapter {

    private String[] images;
    private FragmentActivity fragmentActivity;

    public ImagesAdapter(String[] images, FragmentActivity fragmentActivity) {
        this.images = images;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**Instantiate the ImageView object, allocate it in the ViewPager,
     *and create a Thread to bring the image and set it to the ImageView*/
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        new ImageThreads(images[position],imageView,fragmentActivity).start();
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(250,250));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
