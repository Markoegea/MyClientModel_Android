package com.kingmarco.myclientmodel.Adapters;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;

import java.util.ArrayList;

/**Adapter to be used in a ViewPager element, add multiples images and be able of slide them*/
public class ImagesAdapter extends PagerAdapter {
    private ArrayList<String> grImages;
    private FragmentActivity fragmentActivity;

    public ImagesAdapter(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    public void setImages(ArrayList<String> images) {
        this.grImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return grImages.size();
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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference image = storage.getReferenceFromUrl(grImages.get(position));
        GlideApp.with(fragmentActivity)
                .load(image)
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(250,250));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
