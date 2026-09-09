package pe.miguelruiz.itana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import pe.miguelruiz.itana.data.local.seed.PlaceDataSeeder;
import pe.miguelruiz.itana.data.remote.api.ItanesApiService;
import pe.miguelruiz.itana.data.remote.dto.PlaceRemoteDto;
import pe.miguelruiz.itana.data.remote.retrofit.RetrofitClient;
import pe.miguelruiz.itana.data.repository.PlaceRepository;
import pe.miguelruiz.itana.ui.places.PlacesActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ITANES_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initDataSeeder();
        initViews();
        testRetrofit();
    }

    private void testRetrofit() {
        ItanesApiService apiService = RetrofitClient.getApiService();
        Call<List<PlaceRemoteDto>> call = apiService.getPlaces();

        call.enqueue(new Callback<List<PlaceRemoteDto>>() {
            @Override
            public void onResponse(Call<List<PlaceRemoteDto>> call, Response<List<PlaceRemoteDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlaceRemoteDto> places = response.body();
                    Log.d(TAG, "Respuesta recibida correctamente");
                    Log.d(TAG, "Total lugares: " + places.size());
                    for (PlaceRemoteDto place : places) {
                        Log.d(TAG, place.getId() + " - " + place.getName());
                    }
                } else {
                    Log.e(TAG, "Respuesta HTTP no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceRemoteDto>> call, Throwable t) {
                Log.e(TAG, "Error de conexión o red: " + t.getMessage());
            }
        });
    }

    private void initViews() {
        Button buttonExplore = findViewById(R.id.buttonExplore);
        buttonExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
            startActivity(intent);
        });
    }

    private void initDataSeeder() {
        PlaceRepository repository = new PlaceRepository(getApplication());
        PlaceDataSeeder seeder = new PlaceDataSeeder(repository);
        seeder.seed();
    }
}
