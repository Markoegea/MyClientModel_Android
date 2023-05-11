package com.kingmarco.myclientmodel.Fragments.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.DeleteThis;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**The fragment responsible to set or update the location information of the client*/
public class ChangeLocationDataFragment extends Fragment implements OnMapReadyCallback,
        OnMapsSdkInitializedCallback, GetFireStoreDB {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference clientDB = db.collection("Clientes");
    private static final int MAPS_LAYOUT = 1;
    private static final int WRITE_LAYOUT = 2;
    private SetLabelName setLabelName;
    private Clients client;
    private RelativeLayout mapsLayout;
    private LinearLayout writeLayout;
    //Maps View
    private MapView mapView;
    private GoogleMap googleMap;
    private EditText edtCountry, edtCity, edtAddress, edtLocation;
    private FloatingActionButton btnUpload;
    private RadioGroup radioGroup;
    private Double longitude, latitude;
    private View contentView;

    /**The activity of request permissions of the user to access location*/
    private final ActivityResultLauncher<String[]> getLocationPermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                        if (result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)&&
                                result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)){
                            requestPermissions();
                        }
                }
            }
    );

    public ChangeLocationDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle data = getArguments();
        if (data != null){
            client = getArguments().getParcelable("client");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_change_location_data, container, false);

        /**Initialize the mapview*/
        MapsInitializer.initialize(getContext(), MapsInitializer.Renderer.LATEST, this);
        mapView = contentView.findViewById(R.id.mpClient);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        setMapsViews(contentView);

        setLabelName.setLabelName("Ubicación");
        // Inflate the layout for this fragment
        return contentView;
    }

    //Maps Functions
    private void setMapsViews(View view) {
        btnUpload = view.findViewById(R.id.btnUpdate);

        radioGroup = view.findViewById(R.id.radioMaps);

        mapsLayout = view.findViewById(R.id.rlWriteMap);
        writeLayout = view.findViewById(R.id.llWriteDirections);
        writeLayout.setVisibility(View.VISIBLE);
        setOnClickListeners(WRITE_LAYOUT);

        edtLocation = view.findViewById(R.id.edtLocation);
        edtCountry = view.findViewById(R.id.edtCountry);
        edtCity = view.findViewById(R.id.edtCity);
        edtAddress = view.findViewById(R.id.edtAddress);
        setTextViews();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioWriteDirections:
                        writeLayout.setVisibility(View.VISIBLE);
                        mapsLayout.setVisibility(View.GONE);
                        setOnClickListeners(WRITE_LAYOUT);
                        break;
                    case R.id.radioWriteMaps:
                        writeLayout.setVisibility(View.GONE);
                        mapsLayout.setVisibility(View.VISIBLE);
                        requestPermissions();
                        setOnClickListeners(MAPS_LAYOUT);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setOnClickListeners(int type) {
        switch (type) {
            case MAPS_LAYOUT:
                btnUpload.setOnClickListener(this::onMapUpdateClick);
                break;
            case WRITE_LAYOUT:
                btnUpload.setOnClickListener(this::onWriteUpdateClick);
                break;
            default:
                break;
        }
    }

    private void setTextViews(){
        if (client.getDirections() == null) {return;}
        edtLocation.setText(client.getDirections());

        String[] locationInfo = client.getDirections().replaceAll("\\s","").split(",");
        if (locationInfo.length < 3){return;}
        edtAddress.setText(locationInfo[0]);
        edtCity.setText(locationInfo[1]);

        StringBuilder country = new StringBuilder();
        for (int i = 2; i < locationInfo.length; i++){
            country.append(locationInfo[i]);
        }
        edtCountry.setText(country);
    }

    /**Update the location if the all fields are correct using the map*/
    private void onMapUpdateClick(View view) {
        if (latitude == null || longitude == null || edtLocation.getText().toString().isEmpty()){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return;
        }
        client.setLatitude(latitude);
        client.setLongitude(longitude);
        client.setDirections(edtLocation.getText().toString());
        uploadClient();
    }

    /**Update the location if the all fields are correct using the edit texts*/
    private void onWriteUpdateClick(View view) {
        if (edtCountry.getText().toString().isEmpty()
                || edtCity.getText().toString().isEmpty()
                || edtAddress.getText().toString().isEmpty()) {
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return;
        }
        client.setLatitude(null);
        client.setLongitude(null);
        String direction = edtAddress.getText().toString() + ", " +
                edtCity.getText().toString() + ", " +
                edtCountry.getText().toString();
        client.setDirections(direction);
        uploadClient();
    }

    private void uploadClient(){
        DocumentReference documentReference = clientDB.document(client.getId());
        documentReference.set(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    onCompleteFireStoreRequest(SnackBarsInfo.UPDATE_SUCCESS);
                } else{
                    onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
                }
            }
        });
    }

    /**Move the camara to Colombia, and set the listener to add the marker*/
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = null;
        if (client.getLongitude() != null && client.getLatitude() != null ){
            latLng = new LatLng(client.getLatitude(), client.getLongitude());
            updateLocation(latLng);
        }else{
            latLng = new LatLng(4.570868, -74.297333);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setOnMapClickListener(this::updateLocation);
    }

    /**Request the permission to access to the location if there are no permission
     * Otherwise It will do nothing, or ask nicely to the user to granted the permission*/
    private void requestPermissions(){
        LatLng latLng = null;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Permiso de Ubicacion Requerido")
                        .setMessage("Puedes ubicar mas rapido tu casa con el uso de Google Maps")
                        .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getContext().getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No quiero",null)
                        .create()
                        .show();
            } else {
                getLocationPermissions.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION});
            }
        } else {
            if (googleMap == null){return;}

            this.googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location != null){
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                updateLocation(latLng);
            }
        }
    }

    /**Set the marker in the new location, and get the name of the country, city and direction*/
    private void updateLocation(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.clear();
        googleMap.addMarker(markerOptions);

        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
            if(addresses.size() > 0){
                Address address = addresses.get(0);
                longitude = address.getLongitude();
                latitude = address.getLatitude();
                edtLocation.setText(address.getAddressLine(0));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null){
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null){
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null){
            mapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null){
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null){
            mapView.onLowMemory();
        }
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),true);
    }
}