package com.kingmarco.myclientmodel.Fragments.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kingmarco.myclientmodel.Adapters.ImagesAdapter;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.R;

/** The fragment responsible for show the details of the product*/
public class ProductDetailsFragment extends Fragment {
    //TODO: A BUTTON TO ADD THE ITEM TO A NEW OR OLD CART

    private SetLabelName setLabelName;
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
        product = productData.getParcelable("product");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        viewPager = view.findViewById(R.id.vpImagesProduct);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductCategory = view.findViewById(R.id.txtProductCategory);
        txtProductPrice = view.findViewById(R.id.txtProductPrice);
        txtProductQuantity = view.findViewById(R.id.txtProductQuantity);
        txtProductDetails = view.findViewById(R.id.txtProductDetails);

        /** Create the object to set the image of the product in the view page*/
        ImagesAdapter imagesAdapter = new ImagesAdapter(product.getUrl().toArray(new String[0]),getActivity());
        viewPager.setAdapter(imagesAdapter);

        txtProductName.setText(product.getName());
        txtProductCategory.setText(product.getCategory().toString());
        txtProductPrice.setText(product.getPrice());
        txtProductQuantity.setText(product.getQuantity());
        txtProductDetails.setText(product.getDescription());

        setLabelName.setLabelName("Producto");

        return view;
    }
}