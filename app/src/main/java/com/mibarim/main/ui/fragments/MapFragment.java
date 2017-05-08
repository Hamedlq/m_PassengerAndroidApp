package com.mibarim.main.ui.fragments;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.Route.SuggestBriefRouteModel;
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.models.Address.Location;

import java.util.ArrayList;
import java.util.List;

/*
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
*/

/**
 * Created by Hamed on 3/3/2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    protected GoogleMap mMap; // Might be null if Google Play services APK is not available.

    /*
    private MyLocationNewOverlay mLocationOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
*/
    private Context context;

    private Marker srcMarker;
    private Marker blueSrcMarker;
    private Marker greenSrcMarker;
    private Marker yellowSrcMarker;
    private Marker redSrcMarker;

    private Marker dstMarker;
    private Marker blueDstMarker;
    private Marker greenDstMarker;
    private Marker yellowDstMarker;
    private Marker redDstMarker;


    private boolean isBlueOn = false;
    private boolean isGreenOn = false;
    private boolean isYellowOn = false;
    private boolean isRedOn = false;

    private double minLat = 1000;
    private double minLng = 1000;
    private double maxLat = 0;
    private double maxLng = 0;
    private View mapViewLayout;
    Polyline routeOverlay;
    List<Polyline> routeOverlayList;

    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapViewLayout = inflater.inflate(R.layout.fragment_map, container,
                false);
        mapView = (MapView) mapViewLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);

        mapView.setClickable(true);
        return mapViewLayout;
    }


    private void setSourceFlag() {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        Location srcPoint = new Location();
         if (getActivity() instanceof SuggestRouteActivity) {
            srcPoint = ((SuggestRouteActivity) getActivity()).getRouteSource();
        }
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));
        setMinMaxValues(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng));
    }

    private void setDestinationFlag() {
        if (dstMarker != null) {
            dstMarker.remove();
        }
        Location dstPoint = new Location();
         if (getActivity() instanceof SuggestRouteActivity) {
            dstPoint = ((SuggestRouteActivity) getActivity()).getRouteDestination();
        }
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_to)));

        setMinMaxValues(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng));
        zoomToBoundry();
    }

    private void setMinMaxValues(double lat, double lng) {
        if (minLat > lat) {
            minLat = lat;
        }
        if (minLng > lng) {
            minLng = lng;
        }
        if (maxLat < lat) {
            maxLat = lat;
        }
        if (maxLng < lng) {
            maxLng = lng;
        }
    }


    public void setRoute(String srcLat, String srcLng, String dstLat, String dstLng, PathPoint pathRoute, int color) {
        setMinMaxValues(Double.parseDouble(srcLat), Double.parseDouble(srcLng));
        setMinMaxValues(Double.parseDouble(dstLat), Double.parseDouble(dstLng));
        if (redSrcMarker != null) {
            redSrcMarker.remove();
        }
        redSrcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(srcLat), Double.parseDouble(srcLng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_red_o)));
        if (redDstMarker != null) {
            redDstMarker.remove();
        }
        redDstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(dstLat), Double.parseDouble(dstLng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_red_d)));
        drawPath(pathRoute);
        zoomToBoundry();

    }

    private void drawPath(PathPoint pathRoute){
        if(routeOverlay!=null){
            routeOverlay.remove();
        }
        routeOverlayList = new ArrayList<Polyline>();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        List<LatLng> points = new ArrayList<>();
        if (pathRoute.path.size() > 0) {
            for (LocationPoint location : pathRoute.path) {
                if (location != null) {
                    points.add(new LatLng(Double.parseDouble(location.Lat), Double.parseDouble(location.Lng)));
                }
            }
            polylineOptions.addAll(points);
            routeOverlay = mMap.addPolyline(polylineOptions);
            routeOverlayList.add(routeOverlay);
        }
    }
    private void zoomToBoundry() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(new LatLng(maxLat, maxLng));
        builder.include(new LatLng(minLat, minLng));

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }


    public void setRouteSuggests(SuggestBriefRouteModel selectedItem, int position) {
        mMap.clear();

        Location srcPoint = new Location();
        srcPoint.lat = selectedItem.SelfRouteModel.SrcLatitude;
        srcPoint.lng = selectedItem.SelfRouteModel.SrcLongitude;
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));
        setMinMaxValues(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng));

        Location dstPoint = new Location();
        dstPoint.lat = selectedItem.SelfRouteModel.DstLatitude;
        dstPoint.lng = selectedItem.SelfRouteModel.DstLongitude;
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_to)));
        setMinMaxValues(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng));

        srcPoint.lat = selectedItem.SuggestRouteModel.SrcLatitude;
        srcPoint.lng = selectedItem.SuggestRouteModel.SrcLongitude;
        redSrcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_red_o)));
        setMinMaxValues(Double.parseDouble(srcPoint.lat), Double.parseDouble(srcPoint.lng));

        dstPoint.lat = selectedItem.SuggestRouteModel.DstLatitude;
        dstPoint.lng = selectedItem.SuggestRouteModel.DstLongitude;
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_red_d)));
        setMinMaxValues(Double.parseDouble(dstPoint.lat), Double.parseDouble(dstPoint.lng));

        zoomToBoundry();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (getActivity() instanceof SuggestRouteActivity) {
            setSourceFlag();
            setDestinationFlag();
        }

    }
}
