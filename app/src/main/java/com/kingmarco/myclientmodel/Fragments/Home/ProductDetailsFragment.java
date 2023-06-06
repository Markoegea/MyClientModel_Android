package com.kingmarco.myclientmodel.Fragments.Home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kingmarco.myclientmodel.Adapters.ImagesAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;

/** The fragment responsible for show the details of the product*/
public class ProductDetailsFragment extends Fragment implements GetFireStoreDB {

    private SetLabelName setLabelName;
    private View contentView;
    private ViewPager viewPager;
    private TextView txtProductName, txtProductCategory, txtProductPrice,
            txtProductAvailable, txtProductDetails;
    private EditText edtProductQuantity;
    private CardView cvAddToCart;
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
        long stockId = productData.getLong("stock");
        product = (Products) StockHolder.getSingleStockItem(StockType.PRODUCT,stockId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_product_details, container, false);
        setViews(contentView);
        setInformation();
        setListener();
        setLabelName.setLabelName("Producto");
        return contentView;
    }

    private void setViews(View view){
        viewPager = view.findViewById(R.id.vpImagesProduct);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductCategory = view.findViewById(R.id.txtProductCategory);
        txtProductPrice = view.findViewById(R.id.txtProductPrice);
        txtProductAvailable = view.findViewById(R.id.txtProductAvailable);
        txtProductDetails = view.findViewById(R.id.txtProductDetails);
        edtProductQuantity = view.findViewById(R.id.edtProductQuantity);
        cvAddToCart = view.findViewById(R.id.cvAddToCart);
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
        txtProductAvailable.setText(String.valueOf(product.getQuantity()));
        txtProductDetails.setText(product.getDescription());

        /** Create the object to set the image of the product in the view page*/
        ImagesAdapter imagesAdapter = new ImagesAdapter(getActivity());
        imagesAdapter.setImages(product.getUrl());
        viewPager.setAdapter(imagesAdapter);
    }

    private void setListener(){
        cvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SyncAuthDB.getInstance().isLogin()){
                    onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                    return;
                }
                if (ClientHolder.getYouClient() == null){
                    onCompleteFireStoreRequest(SnackBarsInfo.DISABLE_ERROR);
                    return;
                }
                addToCart();
            }
        });
    }

    private void addToCart() {
        String text = edtProductQuantity.getText().toString();
        try{
            int quantity = Integer.parseInt(text);
            SyncFireStoreDB.uploadCartItemsRequest(product, StockType.PRODUCT,quantity,this,this);
        } catch (NumberFormatException e){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
        }
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),false);
    }
}