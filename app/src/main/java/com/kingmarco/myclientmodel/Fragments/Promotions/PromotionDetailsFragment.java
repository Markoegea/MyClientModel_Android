package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;


/** The fragment responsible to show the detail of the promotion*/
public class PromotionDetailsFragment extends Fragment {
    //TODO: A BUTTON TO ADD THE PROMOTION TO A NEW OR OLD CART
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private SetLabelName setLabelName;
    private ImageView ivPromotion;
    private TextView txtPromotionName, txtPromotionDate, txtPromotionPrice;
    private RecyclerView rvPromotedProducts;
    private Promotions promotions;
    private StockAdapter stockAdapter;
    private ListenerRegistration listenerRegistration;

    public PromotionDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle data = getArguments();
        if (data == null) {return;}
        promotions = data.getParcelable("promotion");
        if (promotions.getProducts() == null){return;}
        stockAdapter = new StockAdapter(getActivity(),this);
        stockAdapter.setActionId(R.id.action_promotionDetailsFragment_to_productDetailsFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_details, container, false);
        setViews(view);
        setInformation();
        setLabelName.setLabelName("Detalles Promocion");
        // Inflate the layout for this fragment
        return view;
    }

    private void setViews(View view){
        ivPromotion = view.findViewById(R.id.ivPromotion);
        txtPromotionName = view.findViewById(R.id.txtPromotionName);
        txtPromotionDate = view.findViewById(R.id.txtPromotionDate);
        txtPromotionPrice = view.findViewById(R.id.txtPromotionPrice);
        rvPromotedProducts = view.findViewById(R.id.rvPromotedProducts);
    }

    private void setInformation(){
        /**Organize the information and set the adapter to the recycler view*/
        if (promotions.getUrl() != null){
            StorageReference gsReference = storage.getReferenceFromUrl(promotions.getUrl().get(0));
            GlideApp.with(getActivity())
                    .load(gsReference)
                    .apply(RequestOptions.centerCropTransform())
                    .into(ivPromotion);
        }
        txtPromotionName.setText(promotions.getName());
        txtPromotionDate.setText(promotions.getFinishDate().getDateFormat());
        String price = "$ "+ new DecimalFormat("###,###,###").format(promotions.getPrice())+" COP" ;
        txtPromotionPrice.setText(price);

        rvPromotedProducts.setAdapter(stockAdapter);
        rvPromotedProducts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        listenerRegistration = SyncFireStoreDB.newListenerRegistration(stockAdapter, Products.class,"Productos");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }
}