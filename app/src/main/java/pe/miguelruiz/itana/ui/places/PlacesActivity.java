package pe.miguelruiz.itana.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.R;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;
import pe.miguelruiz.itana.data.repository.PlaceRepository;
import pe.miguelruiz.itana.ui.detail.PlaceDetailActivity;

public class PlacesActivity extends AppCompatActivity {

    private PlaceAdapter adapter;
    private PlaceRepository repository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        initViews();
        loadData();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter();
        adapter.setOnPlaceClickListener(place -> {
            Intent intent = new Intent(PlacesActivity.this, PlaceDetailActivity.class);
            intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_ID, place.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        
        repository = new PlaceRepository(getApplication());
    }

    private void loadData() {
        executorService.execute(() -> {
            List<PlaceEntity> places = repository.getAllPlacesSync();
            mainHandler.post(() -> adapter.setPlaces(places));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}