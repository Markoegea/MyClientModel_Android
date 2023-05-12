package com.kingmarco.myclientmodel.Fragments.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kingmarco.myclientmodel.Adapters.ImagesAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;

/** The fragment responsible for show the details of the product*/
public class ProductDetailsFragment extends Fragment {
    //TODO: A BUTTON TO ADD THE ITEM TO A NEW OR OLD CART
    private SetLabelName setLabelName;
    private View contentView;
    private ViewPager viewPager;
    private TextView txtProductName, txtProductCategory, txtProductPrice, txtProductQuantity, txtProductDetails;
    private Products product;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle productData = getArguments();
        if (productData == null) {return;}
        product = productData.getParcelable("product");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_product_details, container, false);
        setViews(contentView);
        setInformation();
        setLabelName.setLabelName("Producto");
        return contentView;
    }

    private void setViews(View view){
        viewPager = view.findViewById(R.id.vpImagesProduct);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductCategory = view.findViewById(R.id.txtProductCategory);
        txtProductPrice = view.findViewById(R.id.txtProductPrice);
        txtProductQuantity = view.findViewById(R.id.txtProductQuantity);
        txtProductDetails = view.findViewById(R.id.txtProductDetails);
    }

    private void setInformation(){
        txtProductName.setText(product.getName());
        StringBuilder categories = new StringBuilder();
        for (String category : product.getCategory()){
            categories.append(category).append(", ");
        }
        txtProductCategory.setText(categories);
        String price = "$ "+ new DecimalFormat("###,###,###").format(product.getPrice())+" COP" ;
        txtProductPrice.setText(price);
        txtProductQuantity.setText(String.valueOf(product.getQuantity()));
        txtProductDetails.setText(product.getDescription());

        /** Create the object to set the image of the product in the view page*/
        ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity());
        imagesAdapter.setImages(product.getUrl());
        viewPager.setAdapter(imagesAdapter);
    }
}