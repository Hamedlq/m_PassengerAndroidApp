package com.mibarim.main.ui.fragments.addRouteFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mibarim.main.BootstrapApplication;
import com.mibarim.main.R;
import com.mibarim.main.adapters.LocationAdapter;
import com.mibarim.main.core.LocationService;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.CityLocation;
import com.mibarim.main.models.enums.AddRouteStates;
import com.mibarim.main.ui.activities.AddMapActivity;
import com.mibarim.main.util.Toaster;

/*import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;*/

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hamed on 3/3/2016.
 */
public class AddMapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    protected GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //private MyLocationNewOverlay mLocationOverlay;
    //private ScaleBarOverlay mScaleBarOverlay;
    private Context context;
    private View mapViewLayout;
    List<Marker> markerList;
    private Marker srcMarker;
    private Marker dstMarker;
    List<Polyline> routeOverlayList;
    private double minLat = 1000;
    private double minLng = 1000;
    private double maxLat = 0;
    private double maxLng = 0;
    /*String srcLat;
    String srcLng;
    String dstLat;
    String dstLng;*/

    OnMapClickedListener mCallback;

    public AddMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BootstrapApplication.component().inject(this);
        context = getActivity();
        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        ButterKnife.bind(this, getView());
//    }

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

        markerList = new ArrayList<>();


        /*MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(context, this);
        mapView.getOverlays().add(0, mapEventsOverlay);

        mLocationOverlay.enableMyLocation();

*/
        return mapViewLayout;
    }


    private void doAction() {
        String srcLat=mCallback.getSrcLatLng().Lat;
        String srcLng=mCallback.getSrcLatLng().Lng;
        String dstLat=mCallback.getDstLatLng().Lat;
        String dstLng=mCallback.getDstLatLng().Lng;
        switch (mCallback.getRouteStates()) {
            case SelectEventOriginState:
            case SelectOriginState:
            case SelectGoHomeState:
            case SelectReturnWorkState:
                String theSrcLat = String.valueOf(mMap.getCameraPosition().target.latitude);
                String theSrcLng = String.valueOf(mMap.getCameraPosition().target.longitude);
                if (!theSrcLat.equals(srcLat) && !theSrcLng.equals(srcLng)) {
                    srcLat = theSrcLat;
                    srcLng = theSrcLng;
                    SharedPreferences prefs = getActivity().getSharedPreferences(
                            "com.mibarim.main", Context.MODE_PRIVATE);
                    prefs.edit().putString("SrcLatitude", srcLat).apply();
                    prefs.edit().putString("SrcLongitude", srcLng).apply();
                    mCallback.setSrcLatLng(srcLat, srcLng);
                    mCallback.onMapStopDrag(srcLat, srcLng);
                }
                break;
            case SelectEventDestinationState:
            case SelectDestinationState:
            case SelectGoWorkState:
            case SelectReturnHomeState:
                String theDstLat = String.valueOf(mMap.getCameraPosition().target.latitude);
                String theDstLng = String.valueOf(mMap.getCameraPosition().target.longitude);
                if (!theDstLat.equals(dstLat) && !theDstLng.equals(dstLng)) {
                    dstLat = theDstLat;
                    dstLng = theDstLng;
                    mCallback.setDstLatLng(dstLat, dstLng);
                    mCallback.onMapStopDrag(dstLat, dstLng);
                }
                break;
            default:
                break;
        }
    }

    public void SourceEventState() {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        if (dstMarker != null) {
            dstMarker.remove();
        }
        double lat = Double.valueOf(((AddMapActivity) getActivity()).getEvent().Latitude);
        double lng = Double.valueOf(((AddMapActivity) getActivity()).getEvent().Longitude);
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_to)));
        LatLng startPoint = new LatLng(lat, lng - 0.01);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 14.0f));
        mCallback.setSrcLatLng(String.valueOf(startPoint.latitude), String.valueOf(startPoint.longitude));
    }

    public void DestinationState() {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        if (dstMarker != null) {
            dstMarker.remove();
        }
        double lat = Double.valueOf(mCallback.getSrcLatLng().Lat);
        double lng = Double.valueOf(mCallback.getSrcLatLng().Lng);
        LatLng startPoint = new LatLng(lat, lng);
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));

        LatLng dstPoint = new LatLng(lat, lng - 0.01);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(dstPoint));
    }

    public void DestinationState(String dstLat, String dstLng) {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        if (dstMarker != null) {
            dstMarker.remove();
        }
        double lat = Double.valueOf(mCallback.getSrcLatLng().Lat);
        double lng = Double.valueOf(mCallback.getSrcLatLng().Lng);
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));
        double dstLatitude = Double.valueOf(dstLat);
        double dstLongitude = Double.valueOf(dstLng);
        LatLng dstPoint = new LatLng(dstLatitude, dstLongitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(dstPoint));

    }

    public void DestinationEventState() {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        if (dstMarker != null) {
            dstMarker.remove();
        }
        double lat = Double.valueOf(((AddMapActivity) getActivity()).getEvent().Latitude);
        double lng = Double.valueOf(((AddMapActivity) getActivity()).getEvent().Longitude);
        LatLng startPoint = new LatLng(lat, lng);
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));
        double dstlat = Double.valueOf(mCallback.getDstLatLng().Lat);
        double dstlng = Double.valueOf(mCallback.getDstLatLng().Lng);
        LatLng dstPoint = new LatLng(dstlat, dstlng);
        if(lat==dstlat){
            dstPoint = new LatLng(dstlat, dstlng- 0.01);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dstPoint, 14.0f));
    }

/*
    private void DriveRouteState() {
        drawDrivePath();
    }*/

    public void drawDrivePath(){
        List<PathPoint> pathPointList = mCallback.getRecommendPathPointList();
        int selectedPath = mCallback.getSelectedPathPoint();
        setSourceFlag();
        setDestinationFlag();
        drawPath(pathPointList, selectedPath);
        LatLng startPoint = new LatLng(Double.parseDouble(mCallback.getSrcLatLng().Lat), Double.parseDouble(mCallback.getSrcLatLng().Lng));
        zoomToBoundry();
    }

    public void MoveMap(String latitude, String longitude) {
        double lat = Double.valueOf(latitude);
        double lng = Double.valueOf(longitude);
        LatLng startPoint = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(startPoint));
        switch (mCallback.getRouteStates()) {
            case SelectOriginState:
            case SelectEventOriginState:
            case SelectGoHomeState:
            case SelectReturnWorkState:
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        "com.mibarim.main", Context.MODE_PRIVATE);
                prefs.edit().putString("SrcLatitude", latitude).apply();
                prefs.edit().putString("SrcLongitude", longitude).apply();
                break;
            case SelectEventDestinationState:
            case SelectDestinationState:
            case SelectGoWorkState:
            case SelectReturnHomeState:
                break;
        }
    }

    public void setMapPoints(List<CityLocation> cityLocationList) {
        markerList.removeAll(markerList);
        markerList = new ArrayList<>();
        for (CityLocation cl : cityLocationList) {
            Bitmap p = writeOnDrawable(R.mipmap.ic_bold, cl.ShortName);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(cl.CityLocationPoint.Lat), Double.parseDouble(cl.CityLocationPoint.Lng)))
                    .title(cl.FullName)
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(p)));
            markerList.add(marker);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        switch (mCallback.getRouteStates()) {
            case SelectGoHomeState:
            case SelectOriginState:

            case SelectReturnWorkState:
                // Get the location
                Location location = LocationService.getLocationManager(context).getLocation();
                LatLng startPoint;
                SharedPreferences prefs = getActivity().getSharedPreferences(
                        "com.mibarim.main", Context.MODE_PRIVATE);
                String latitude = prefs.getString("SrcLatitude", "35.717110");
                String longitude = prefs.getString("SrcLongitude", "51.426830");
                if (location != null) {
                    startPoint = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    Double lat = Double.valueOf(latitude);
                    Double lng = Double.valueOf(longitude);
                    startPoint = new LatLng(lat, lng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 14.0f));
                mCallback.setSrcLatLng(String.valueOf(startPoint.latitude), String.valueOf(startPoint.longitude));
                //SourceState();
                break;
            case SelectEventOriginState:
                SourceEventState();
                break;
            case SelectGoWorkState:
            case SelectReturnHomeState:
                DestinationState();
                break;
            case SelectDestinationState:
                DestinationState();
                break;
            case SelectEventDestinationState:
                DestinationEventState();
                break;
            case SelectDriveRouteState:
                drawDrivePath();
                //DriveRouteState();
                break;
            default:
                break;
        }


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                doAction();
            }
        });


    }


    // Container Activity must implement this interface
    public interface OnMapClickedListener {
        public AddRouteStates getRouteStates();

        public void onMapStartDrag();

        public void onMapStopDrag(String latitude, String longitude);

        public void setSrcLatLng(String latitude, String longitude);

        public void setDstLatLng(String latitude, String longitude);

        public LocationPoint getSrcLatLng();

        public LocationPoint getDstLatLng();

        public List<PathPoint> getRecommendPathPointList();

        public int getSelectedPathPoint();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMapClickedListener) activity;
        } catch (ClassCastException e) {
        }

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

    private void setSourceFlag() {
        if (srcMarker != null) {
            srcMarker.remove();
        }
        srcMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(mCallback.getSrcLatLng().Lat), Double.parseDouble(mCallback.getSrcLatLng().Lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_from)));
        setMinMaxValues(Double.parseDouble(mCallback.getSrcLatLng().Lat), Double.parseDouble(mCallback.getSrcLatLng().Lng));
    }

    private void setDestinationFlag() {
        if (dstMarker != null) {
            dstMarker.remove();
        }
        dstMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.parseDouble(mCallback.getDstLatLng().Lat), Double.parseDouble(mCallback.getDstLatLng().Lng)))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.ic_to)));

        setMinMaxValues(Double.parseDouble(mCallback.getDstLatLng().Lat), Double.parseDouble(mCallback.getDstLatLng().Lng));
    }


    private void zoomToBoundry() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(new LatLng(maxLat,maxLng));
        builder.include(new LatLng(minLat,minLng));

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }

    public void drawPath(List<PathPoint> pathPointList, int selected) {
        int cou = 1;
        Polyline routeOverlay;
        routeOverlayList = new ArrayList<>();
        for (PathPoint pathPoint : pathPointList) {
            if (pathPoint != null && pathPoint.metadata != null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                if (cou == selected) {
                    polylineOptions.width(10);
                } else {
                    polylineOptions.width(5);
                }
                polylineOptions.color(getColor(cou));
                List<LatLng> points = new ArrayList<>();
                for (LocationPoint location : pathPoint.path) {
                    if (location != null) {
                        points.add(new LatLng(Double.parseDouble(location.Lat), Double.parseDouble(location.Lng)));
                    }
                }
                polylineOptions.addAll(points);
                routeOverlay=mMap.addPolyline(polylineOptions);
                routeOverlayList.add(routeOverlay);
            }
            cou++;
        }
    }

    private int getColor(int cou) {
        switch (cou) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.RED;
            case 5:
                return Color.BLACK;
            default:
                return Color.WHITE;
        }

    }

    public void ClearRoute() {
        mMap.clear();
    }

    public void reDrawRoutePaths() {
        ClearRoute();
        /*List<PathPoint> pathPointList = mCallback.getRecommendPathPointList();
        int selectedPath = mCallback.getSelectedPathPoint();
        drawPath(pathPointList, selectedPath);*/
        drawDrivePath();
    }

    public Bitmap writeOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        int MY_DIP_VALUE = 15; //5dp
        int pixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, getResources().getDisplayMetrics());
        paint.setTextSize(pixel);

        Canvas canvas = new Canvas(bm);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, (canvas.getWidth() - bounds.width()) / 2, 7 * (canvas.getHeight() - bounds.height()) / 16, paint);

        return bm;
    }

}


