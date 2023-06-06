package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.StockObserver;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;


/** The fragment responsible to show the detail of the promotion*/
public class PromotionDetailsFragment extends Fragment implements GetFireStoreDB, StockObserver {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private SetLabelName setLabelName;
    private ImageView ivPromotion;
    private TextView txtPromotionName, txtPromotionDate, txtPromotionPrice;
    private EditText edtProductQuantity;
    private RecyclerView rvPromotedProducts;
    private CardView cvAddToCart;
    private View contentView;
    private Promotions promotions;
    private StockAdapter stockAdapter;

    public PromotionDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle data = getArguments();
        if (data == null) {return;}
        long stockId = data.getLong("stock");
        promotions = (Promotions) StockHolder.getSingleStockItem(StockType.PROMOTION,stockId);
        if (promotions == null){return;}
        stockAdapter = new StockAdapter(getActivity(),this);
        stockAdapter.setActionId(R.id.action_promotionDetailsFragment_to_productDetailsFragment);
        onVariableChange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_promotion_details, container, false);
        setViews(contentView);
        setInformation();
        setListener();
        setLabelName.setLabelName("Detalles Promocion");
        // Inflate the layout for this fragment
        return contentView;
    }

    private void setViews(View view){
        ivPromotion = view.findViewById(R.id.ivPromotion);
        txtPromotionName = view.findViewById(R.id.txtPromotionName);
        txtPromotionDate = view.findViewById(R.id.txtPromotionDate);
        txtPromotionPrice = view.findViewById(R.id.txtPromotionPrice);
        edtProductQuantity = view.findViewById(R.id.edtProductQuantity);
        cvAddToCart = view.findViewById(R.id.cvAddToCart);
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
        txtPromotionDate.setText(promotions.getFinishDate().toString());
        String price = "$ "+ new DecimalFormat("###,###,###").format(promotions.getPrice())+" COP" ;
        txtPromotionPrice.setText(price);

        rvPromotedProducts.setAdapter(stockAdapter);
        rvPromotedProducts.setLayoutManager(new LinearLayoutManager(getContext()));
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
            SyncFireStoreDB.uploadCartItemsRequest(promotions,StockType.PROMOTION,quantity,this,this);
        } catch (NumberFormatException e){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StockHolder.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StockHolder.removeObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        StockHolder.removeObserver(this);
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),false);
    }

    @Override
    public void onVariableChange() {
        stockAdapter.setDatabase(StockHolder.getList(StockType.PRODUCT));
    }
}