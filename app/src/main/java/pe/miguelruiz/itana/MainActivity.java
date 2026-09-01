package pe.miguelruiz.itana;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pe.miguelruiz.itana.data.local.seed.PlaceDataSeeder;
import pe.miguelruiz.itana.data.repository.PlaceRepository;
import pe.miguelruiz.itana.ui.places.PlacesActivity;

public class MainActivity extends AppCompatActivity {

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
