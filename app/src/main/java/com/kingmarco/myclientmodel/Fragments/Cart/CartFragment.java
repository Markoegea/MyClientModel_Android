package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.CartsAdapter;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.BuildConfig;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/** The fragment that display a recycler view with the carts of the client*/
public class CartFragment extends Fragment {
    
    private SetLabelName setLabelName;
    private RecyclerView rvCarts;
    private CartsAdapter cartsAdapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Testing Purposes delete it and recover this data from Firebase with authentication
        setLabelName = (SetLabelName) getContext();

        ArrayList<Parcelable> products = new ArrayList<>();
        String[] category = new String[]{"category"};
        ArrayList<String> list = new ArrayList<String>(){{ add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Transform_this_image_into_a_dreamlike__fantastical_landscape__Visual_Novel__Colorful__by_Artgerm__En_Seed-1_Steps-30_Guidance-7.5%20(1).jpeg?alt=media&token=cefc0786-f516-426d-897d-f3cee5763bf5");}};
        products.add(new Products(1,10000,4,"Bag","A Super Bag", list,null,new ArrayList<>(Arrays.asList(category))));
        ArrayList<String> list2 = new ArrayList<String>(){{add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Remove_Use_these_settingsEpic_Battle_field__with_dragons__wizards__soldiers_with_mountains__Digital__Seed-0_Steps-30_Guidance-7.5.jpeg?alt=media&token=d8947dff-5ac3-4d7b-81f4-baa0d98aa926");}};
        products.add(new Products(2,20000,2,"Skirt", "A short skirt",list2,null,new ArrayList<>(Arrays.asList(category))));
        ArrayList<String> list3 = new ArrayList<String>(){{add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Anime_dragon_with_a_fierce_expression__breathing_out_smoke___Anime__Infrared__Unreal_Engine_Seed-469113_Steps-25_Guidance-7.5.jpeg?alt=media&token=c6b7813a-9926-4f4e-8a41-2ddc71cdc1f3");}};
        products.add(new Products(3,30000, 1,"T-shirt","A long t-shirt",list3,null,new ArrayList<>(Arrays.asList(category))));;

        ArrayList<Parcelable> promoProduct = new ArrayList<>();
        promoProduct.add(new Promotions(list2.get(0),"Two Bags for 15000",
                new ArrayList<>(){{ add(new Products(1,10000,4,"Bag","A Super Bag", list2,null,new ArrayList<>(Arrays.asList(category))));}},
                15000, LocalDate.of(2023,4,1)));

        promoProduct.add(new Promotions(list.get(0),"Three skirts for 50000",
                new ArrayList<>(){{ add(new Products(2,20000,2,"Skirt", "A short skirt",list,null,new ArrayList<>(Arrays.asList(category))));}},
                50000,LocalDate.of(2023,4,1)));

        promoProduct.add(new Promotions(list2.get(0), "A skirt and a T-shirt for 42500",
                new ArrayList<>(){{ add(new Products(3,30000, 1,"T-shirt","A long t-shirt",list3,null,new ArrayList<>(Arrays.asList(category))));
                    add(new Products(2,20000,2,"Skirt", "A short skirt",list,null,new ArrayList<>(Arrays.asList(category))));}},
                42500,LocalDate.of(2023,4,1)));

        Clients client = new Clients(null,"Marco","Pendragon","3332536","C.C",54,"Male","55476575",0.0,0.0,"Calle X","marko@gmail.com","123");

        ArrayList<Carts> carts = new ArrayList<>();

        carts.add(new Carts(1,products,client,"Tienda Max",60000,"Purchased", client.getDirections(), LocalDate.now(),null));
        carts.add(new Carts(2,promoProduct,client,"Tienda Max",62500,"Purchased", client.getDirections(), LocalDate.now(),null));
        /**Create the adapter and set the database*/
        cartsAdapter = new CartsAdapter(getActivity(),this,R.id.action_cartFragment_to_cartDetailsFragment);
        cartsAdapter.setDatabase(carts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCarts = view.findViewById(R.id.rvCarts);
        rvCarts.setAdapter(cartsAdapter);
        rvCarts.setLayoutManager(new LinearLayoutManager(getContext()));

        setLabelName.setLabelName("Carrito");
        // Inflate the layout for this fragment
        return view;
    }
}