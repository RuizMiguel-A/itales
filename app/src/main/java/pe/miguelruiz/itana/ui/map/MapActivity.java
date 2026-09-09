package pe.miguelruiz.itana.ui.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.maps.Style;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.R;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;
import pe.miguelruiz.itana.data.repository.PlaceRepository;
import pe.miguelruiz.itana.ui.detail.PlaceDetailActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private TextView textPlaceName;
    private PlaceRepository repository;
    private PlaceEntity currentPlace;
    
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize MapLibre before onCreate
        MapLibre.getInstance(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        textPlaceName = findViewById(R.id.textMapPlaceName);
        
        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.buttonZoomIn).setOnClickListener(v -> {
            if (mapLibreMap != null) {
                double currentZoom = mapLibreMap.getCameraPosition().zoom;
                if (currentZoom < 19) {
                    mapLibreMap.animateCamera(CameraUpdateFactory.zoomIn());
                }
            }
        });

        findViewById(R.id.buttonZoomOut).setOnClickListener(v -> {
            if (mapLibreMap != null) {
                double currentZoom = mapLibreMap.getCameraPosition().zoom;
                if (currentZoom > 3) {
                    mapLibreMap.animateCamera(CameraUpdateFactory.zoomOut());
                }
            }
        });
        
        mapView.onCreate(savedInstanceState);
        
        repository = new PlaceRepository(getApplication());
        
        int placeId = getIntent().getIntExtra(PlaceDetailActivity.EXTRA_PLACE_ID, -1);
        if (placeId == -1) {
            Toast.makeText(this, R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPlaceData(placeId);
    }

    private void loadPlaceData(int placeId) {
        executorService.execute(() -> {
            PlaceEntity place = repository.getPlaceByIdSync(placeId);
            mainHandler.post(() -> {
                if (place != null) {
                    currentPlace = place;
                    textPlaceName.setText(place.getName());
                    mapView.getMapAsync(this);
                } else {
                    Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    @Override
    public void onMapReady(@NonNull MapLibreMap mapLibreMap) {
        this.mapLibreMap = mapLibreMap;
        
        mapLibreMap.setMinZoomPreference(3);
        mapLibreMap.setMaxZoomPreference(19);
        
        mapLibreMap.setStyle(new Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty"), style -> {
            if (currentPlace != null) {
                setupMarkerAndCamera();
            }
        });
    }

    private void setupMarkerAndCamera() {
        double lat = currentPlace.getLatitude();
        double lng = currentPlace.getLongitude();

        if (isValidCoordinate(lat, lng)) {
            LatLng position = new LatLng(lat, lng);
            
            // Add marker
            mapLibreMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(currentPlace.getName()));

            // Center camera
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position)
                    .zoom(15)
                    .build();
            
            mapLibreMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
        } else {
            Toast.makeText(this, R.string.error_invalid_coordinates, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidCoordinate(double lat, double lng) {
        return lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180;
    }

    // Lifecycle methods for MapView
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        mapView.onDestroy();
    }
}