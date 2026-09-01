package pe.miguelruiz.itana.ui.detail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.R;
import pe.miguelruiz.itana.data.local.entity.FavoriteEntity;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;
import pe.miguelruiz.itana.data.repository.FavoriteRepository;
import pe.miguelruiz.itana.data.repository.PlaceRepository;

public class PlaceDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PLACE_ID = "pe.miguelruiz.itana.EXTRA_PLACE_ID";

    private TextView textName, textShortDesc, textDesc, textAddress, textCoordinates;
    private ImageView imagePlace;
    private Button buttonFavorite;
    private PlaceRepository placeRepository;
    private FavoriteRepository favoriteRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isFavorite = false;
    private int placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        initViews();
        placeRepository = new PlaceRepository(getApplication());
        favoriteRepository = new FavoriteRepository(getApplication());

        placeId = getIntent().getIntExtra(EXTRA_PLACE_ID, -1);
        if (placeId == -1) {
            Toast.makeText(this, R.string.error_invalid_id, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPlaceDetails(placeId);
        checkFavoriteStatus();
        setupFavoriteButton();
    }

    private void initViews() {
        textName = findViewById(R.id.textDetailName);
        textShortDesc = findViewById(R.id.textDetailShortDescription);
        textDesc = findViewById(R.id.textDetailDescription);
        textAddress = findViewById(R.id.textDetailAddress);
        textCoordinates = findViewById(R.id.textDetailCoordinates);
        imagePlace = findViewById(R.id.imagePlaceDetail);
        buttonFavorite = findViewById(R.id.buttonFavorite);
    }

    private void loadPlaceDetails(int placeId) {
        executorService.execute(() -> {
            PlaceEntity place = placeRepository.getPlaceByIdSync(placeId);
            mainHandler.post(() -> {
                if (place != null) {
                    displayPlace(place);
                } else {
                    Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void displayPlace(PlaceEntity place) {
        textName.setText(place.getName());
        textShortDesc.setText(place.getShortDescription());
        textDesc.setText(place.getDescription());
        textAddress.setText(place.getAddress());
        
        String coords = String.format(Locale.getDefault(), "Lat: %.6f, Lon: %.6f", 
                place.getLatitude(), place.getLongitude());
        textCoordinates.setText(coords);

        Glide.with(this)
                .load(place.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(imagePlace);
    }

    private void checkFavoriteStatus() {
        executorService.execute(() -> {
            isFavorite = favoriteRepository.isFavoriteSync(placeId);
            mainHandler.post(this::updateFavoriteButtonUI);
        });
    }

    private void setupFavoriteButton() {
        buttonFavorite.setOnClickListener(v -> {
            if (isFavorite) {
                favoriteRepository.deleteFavorite(placeId);
                isFavorite = false;
                updateFavoriteButtonUI();
            } else {
                String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                FavoriteEntity favorite = new FavoriteEntity(placeId, createdAt);
                favoriteRepository.insertFavorite(favorite);
                isFavorite = true;
                updateFavoriteButtonUI();
            }
        });
    }

    private void updateFavoriteButtonUI() {
        if (isFavorite) {
            buttonFavorite.setText(R.string.button_favorite_remove);
        } else {
            buttonFavorite.setText(R.string.button_favorite_add);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}