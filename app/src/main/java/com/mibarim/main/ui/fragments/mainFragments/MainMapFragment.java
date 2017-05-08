package com.mibarim.main.ui.fragments.mainFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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
import com.mibarim.main.ui.activities.SuggestRouteActivity;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.models.Address.Location;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.LocalRoute;
import com.mibarim.main.models.enums.LocalRouteTypes;

/*
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hamed on 3/3/2016.
 */
public class MainMapFragment extends Fragment implements OnMapReadyCallback {
    private Context context;

    private MapView mapView;
    protected GoogleMap mMap;

    private View mapViewLayout;
    OnMainMapClickedListener mCallback;

    /*private MarkerOptions blueSrcMarker;
    private MarkerOptions greenSrcMarker;
    private MarkerOptions yellowSrcMarker;


    private MarkerOptions blueDstMarker;
    private MarkerOptions greenDstMarker;
    private MarkerOptions yellowDstMarker;
    private MarkerOptions redDstMarker;

    private boolean isBlueOn = false;
    private boolean isGreenOn = false;
    private boolean isYellowOn = false;
    private boolean isRedOn = false;*/

    private double minLat = 1000;
    private double minLng = 1000;
    private double maxLat = 0;
    private double maxLng = 0;


    List<Marker> srcMarkerList;
    List<Marker> dstMarkerList;
    List<Polyline> routeOverlayList;
    private Marker srcMarker;
    private Marker dstMarker;
    private Marker redSrcMarker;
    private Marker redDstMarker;

    private HashMap<Marker, LocalRoute> routeMarkerMap;
    private LocalRoute selectedLocalRoute;
    private Tracker mTracker;

    public MainMapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        // Obtain the shared Tracker instance.
        BootstrapApplication application = (BootstrapApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        context = getActivity();
        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTracker.setScreenName("MainMapFragment");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("Fragment").setAction("MainMapFragment").build());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapViewLayout = inflater.inflate(R.layout.fragment_map, container,
                false);
        mapView = (MapView) mapViewLayout.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();


        mapView.getMapAsync(this);

        mapView.setClickable(true);


        srcMarkerList = new ArrayList<>();
        dstMarkerList = new ArrayList<>();
        //routeOverlayList = new ArrayList<>();


        /*if (getActivity() instanceof GroupActivity
                || getActivity() instanceof SuggestRouteActivity
                || getActivity() instanceof SuggestGroupActivity) {
            setSourceFlag();
            setDestinationFlag();
        }*/


/*
        mapView.setOnTouchListener(new MapView.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCallback.onMapStartDrag();
                        break;
                    case MotionEvent.ACTION_UP:
                        String lat = String.valueOf(mMap.getCameraPosition().target.latitude);
                        String lng = String.valueOf(mMap.getCameraPosition().target.longitude);
                        SharedPreferences prefs = getActivity().getSharedPreferences(
                                "com.mibarim.main", Context.MODE_PRIVATE);
                        prefs.edit().putString("SrcLatitude", lat).apply();
                        prefs.edit().putString("SrcLongitude", lng).apply();
                        mCallback.onMapStopDrag(lat, lng);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
*/


//        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(context, this);
        //mapView.getOverlays().add(0, mapEventsOverlay);

        //mMap.setMyLocationEnabled(true);
        //initScreen();
        return mapViewLayout;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        //        // Get the location
        android.location.Location location = LocationService.getLocationManager(getActivity()).getLocation();
        LatLng startPoint;
        SharedPreferences prefs;
        String latitude =  "35.717110";
        String longitude = "51.426830";
        if (getActivity() != null) {
            prefs = getActivity().getSharedPreferences(
                    "com.mibarim.main", Context.MODE_PRIVATE);
            latitude = prefs.getString("SrcLatitude", "35.717110");
            longitude = prefs.getString("SrcLongitude", "51.426830");
        }
        if (location != null) {
            startPoint = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            Double lat = Double.valueOf(latitude);
            Double lng = Double.valueOf(longitude);
            startPoint = new LatLng(lat, lng);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 14.0f));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                String lat = String.valueOf(mMap.getCameraPosition().target.latitude);
                String lng = String.valueOf(mMap.getCameraPosition().target.longitude);
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        "com.mibarim.main", Context.MODE_PRIVATE);
                prefs.edit().putString("SrcLatitude", lat).apply();
                prefs.edit().putString("SrcLongitude", lng).apply();
                mCallback.onMapStopDrag(lat, lng);
            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedLocalRoute = routeMarkerMap.get(marker);
                if (selectedLocalRoute != null) {
                    mCallback.onMarkerClicked(selectedLocalRoute);
                    showSelectedRoute(selectedLocalRoute);
                    setMinMaxValues(Double.parseDouble(selectedLocalRoute.SrcPoint.Lat), Double.parseDouble(selectedLocalRoute.SrcPoint.Lng));
                    setMinMaxValues(Double.parseDouble(selectedLocalRoute.DstPoint.Lat), Double.parseDouble(selectedLocalRoute.DstPoint.Lng));
                    zoomToBoundry();
                    marker.hideInfoWindow();
                }
                return false;
            }
        });

    }


    // Container Activity must implement this interface
    public interface OnMainMapClickedListener {
        public void onMapStartDrag();

        public void onMapStopDrag(String latitude, String longitude);

        public void onMarkerClicked(LocalRoute localRoute);

        //void setCityLocations(String latitude, String longitude);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMainMapClickedListener) activity;
        } catch (ClassCastException e) {
        }

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


    private void zoomToBoundry() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(new LatLng(maxLat, maxLng));
        builder.include(new LatLng(minLat, minLng));

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }

//    @Override
//    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
//        //intelligentStateSelector(geoPoint);
//        switch (((NewRouteDelActivity) getActivity()).getSrcDstStateSelector()) {
//            case "SOURCE_SELECTED":
//                mapView.getOverlays().remove(srcMarker);
//                srcMarker.setPosition(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
//                srcMarker.setIcon(ContextCompat.getDrawable(context, R.mipmap.ic_from));
//                srcMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
//                mapView.getOverlays().add(0, srcMarker);
//                mapView.invalidate();
//                isSrcMarkerSet = true;
//                ((NewRouteDelActivity) getActivity()).routeRequest.SrcLatitude = String.valueOf(geoPoint.getLatitude());
//                ((NewRouteDelActivity) getActivity()).routeRequest.SrcLongitude = String.valueOf(geoPoint.getLongitude());
////                Toaster.showLong(getActivity(), "Longitude: " + geoPoint.getLongitude() + " Latitude: " + geoPoint.getLatitude());
//                break;
//            case "DESTINATION_SELECTED":
//                mapView.getOverlays().remove(dstMarker);
//                dstMarker.setPosition(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
//                dstMarker.setIcon(ContextCompat.getDrawable(context, R.mipmap.ic_to));
//                dstMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
//                mapView.getOverlays().add(0, dstMarker);
//                mapView.invalidate();
//                ((NewRouteDelActivity) getActivity()).routeRequest.DstLatitude = String.valueOf(geoPoint.getLatitude());
//                ((NewRouteDelActivity) getActivity()).routeRequest.DstLongitude = String.valueOf(geoPoint.getLongitude());
////                Toaster.showLong(getActivity(), "Longitude: " + geoPoint.getLongitude() + " Latitude: " + geoPoint.getLatitude());
//                break;
//        }
//
//        return true;
//    }

    //    private void intelligentStateSelector(GeoPoint geoPoint) {
//        if (isSrcMarkerSet) {
//            Location loc1 = new Location("");
//            loc1.setLatitude(geoPoint.getLatitude());
//            loc1.setLongitude(geoPoint.getLongitude());
//
//            Location loc2 = new Location("");
//            loc2.setLatitude(srcMarker.getPosition().getLatitude());
//            loc2.setLongitude(srcMarker.getPosition().getLongitude());
//
//            float distanceInMeters = loc1.distanceTo(loc2);
//            if (distanceInMeters > 1000) {
//                ((NewRouteDelActivity) getActivity()).setSrcDstStateSelector("DESTINATION_SELECTED");
//            }
//        }
//
//    }
//
//    @Override
//    public boolean longPressHelper(GeoPoint geoPoint) {
//        return false;
//    }
    public void setMapLocalRoutes(List<LocalRoute> localRouteList) {
        if (mMap != null) {
            mMap.clear();
            routeOverlayList = new ArrayList<Polyline>();
            //PathOverlay routeOverlay;
            routeMarkerMap = new HashMap<Marker, LocalRoute>();
            srcMarkerList = new ArrayList<>();
            dstMarkerList = new ArrayList<>();
            for (LocalRoute localRoute : localRouteList) {

                if (localRoute.LocalRouteType == LocalRouteTypes.Driver) {
                    srcMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)))
                            .title(localRoute.RouteStartTime)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_car_start)));
                } else {
                    srcMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)))
                            .title(localRoute.RouteStartTime)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.mipmap.ic_pass_start)));
                }
                routeMarkerMap.put(srcMarker, localRoute);

/*
            dstMarker =  mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)))
                    .title(localRoute.RouteStartTime)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.ic_to)));
*/
/*            if (localRoute.PathRoute.path.size() > 0) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(5);
                List<LatLng> points = new ArrayList<>();
                for (LocationPoint location : localRoute.PathRoute.path) {
                    if (location != null) {
                        points.add(new LatLng(Double.parseDouble(location.Lat), Double.parseDouble(location.Lng)));
                        //routeOverlay.add(new GeoPoint(Double.parseDouble(location.Lat), Double.parseDouble(location.Lng)));
                    }
                }
                polylineOptions.addAll(points);
                routeOverlay = mMap.addPolyline(polylineOptions);
                routeOverlayList.add(routeOverlay);
            }*/
            }
            if (selectedLocalRoute != null) {
                showSelectedRoute(selectedLocalRoute);
            }
//        mapView.getOverlays().addAll(0, srcMarkerList);
//        mapView.getOverlays().addAll(0, dstMarkerList);
//        mapView.getOverlays().addAll(0, routeOverlayList);
//        mapView.invalidate();
        }
    }

    private void showSelectedRoute(LocalRoute localRoute) {
        Polyline routeOverlay;
        routeOverlayList = new ArrayList<Polyline>();
        if (localRoute.LocalRouteType == LocalRouteTypes.Driver) {
            srcMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)))
                    .title(localRoute.RouteStartTime)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.ic_car_start)));
        } else {
            srcMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)))
                    .title(localRoute.RouteStartTime)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.ic_pass_start)));
        }
        routeMarkerMap.put(srcMarker, localRoute);
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(localRoute.DstPoint.Lat), Double.parseDouble(localRoute.DstPoint.Lng)))
                .title(localRoute.RouteStartTime)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_to)));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(5);
        List<LatLng> points = new ArrayList<>();
        if (localRoute.PathRoute.path.size() > 0) {
            for (LocationPoint location : localRoute.PathRoute.path) {
                if (location != null) {
                    points.add(new LatLng(Double.parseDouble(location.Lat), Double.parseDouble(location.Lng)));
                }
            }
            polylineOptions.addAll(points);
            routeOverlay = mMap.addPolyline(polylineOptions);
            routeOverlayList.add(routeOverlay);
        } else {
            points.add(new LatLng(Double.parseDouble(localRoute.SrcPoint.Lat), Double.parseDouble(localRoute.SrcPoint.Lng)));
            points.add(new LatLng(Double.parseDouble(localRoute.DstPoint.Lat), Double.parseDouble(localRoute.DstPoint.Lng)));
            polylineOptions.addAll(points);
            routeOverlay = mMap.addPolyline(polylineOptions);
            routeOverlayList.add(routeOverlay);
        }
    }


    public void MoveMap(String latitude, String longitude) {
        double lat = Double.valueOf(latitude);
        double lng = Double.valueOf(longitude);
        LatLng startPoint = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 14.0f));
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            if (isVisibleToUser) {
                ((MainActivity) getActivity()).showActionBar();
                ((MainActivity)getActivity()).showMenu();
                ((MainActivity)getActivity()).showRouteListOnMap();
            } else {
                ((MainActivity)getActivity()).hideMenu();
            }
        }
    }
*/

}
