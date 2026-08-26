package pe.miguelruiz.itana.data.local.seed;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.data.local.entity.PlaceEntity;
import pe.miguelruiz.itana.data.repository.PlaceRepository;

public class PlaceDataSeeder {

    private static final String TAG = "PlaceDataSeeder";
    private final PlaceRepository repository;
    private final ExecutorService executorService;

    public PlaceDataSeeder(PlaceRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void seed() {
        executorService.execute(() -> {
            int count = repository.getPlacesCountSync();
            if (count == 0) {
                List<PlaceEntity> initialPlaces = getInitialPlaces();
                repository.insertPlaces(initialPlaces);
                Log.d(TAG, "PlaceDataSeeder: 5 lugares disponibles en Room");
            } else {
                Log.d(TAG, "PlaceDataSeeder: La base de datos ya contiene registros. Registros actuales: " + count);
            }
        });
    }

    private List<PlaceEntity> getInitialPlaces() {
        List<PlaceEntity> places = new ArrayList<>();

        places.add(new PlaceEntity(
                1,
                "Monasterio de Santa Catalina de Siena",
                "Histórico convento virreinal y ciudadela de sillar.",
                "El Monasterio de Santa Catalina es un complejo turístico y religioso ubicado en el centro histórico de Arequipa. Fundado en 1579, es una pequeña ciudadela de sillar blanco y rosado que cuenta con calles estrechas, claustros, plazas y coloridos jardines de estilo colonial.",
                "Calle Santa Catalina 301, Arequipa",
                -16.395224,
                -71.536856,
                "https://ejemplo.com/imagenes/santa-catalina.jpg",
                1,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                2,
                "Cañón del Colca",
                "Uno de los cañones más profundos del mundo y hábitat del cóndor.",
                "El Cañón del Colca es un impresionante destino natural ubicado en la provincia de Caylloma. Es famoso por ser uno de los cañones más profundos del mundo y ofrece paisajes espectaculares, terrazas agrícolas preincas y es el lugar ideal para el avistamiento del majestuoso cóndor andino.",
                "Valle del Colca, Provincia de Caylloma, Arequipa",
                -15.606619,
                -71.872425,
                "https://ejemplo.com/imagenes/canon-del-colca.jpg",
                2,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                3,
                "Centro Histórico",
                "Casco antiguo de Arequipa, Patrimonio Cultural de la Humanidad.",
                "El Centro Histórico de Arequipa destaca por su hermosa arquitectura colonial construida casi en su totalidad con sillar, una piedra volcánica blanca. Su núcleo central es la Plaza de Armas, rodeada por imponentes arquerías y la espectacular Catedral basílica.",
                "Plaza de Armas, Centro Histórico, Arequipa",
                -16.398803,
                -71.536961,
                "https://ejemplo.com/imagenes/centro-historico.jpg",
                3,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                4,
                "Misti",
                "Icónico estratovolcán activo y símbolo de la Ciudad Blanca.",
                "El Misti es un estratovolcán imponente que se eleva a más de 5,800 metros sobre el nivel del mar. Con su forma cónica perfecta y su cima ocasionalmente nevada, es el guardián y el símbolo geográfico más representativo de la ciudad de Arequipa.",
                "Volcán Misti, Reserva Nacional de Salinas y Aguada Blanca, Arequipa",
                -16.294444,
                -71.409167,
                "https://ejemplo.com/imagenes/volcan-misti.jpg",
                4,
                "2026-08-25"
        ));

        places.add(new PlaceEntity(
                5,
                "Mirador de Yanahuara",
                "Mirador tradicional con arcos de sillar y vista a los volcanes.",
                "El Mirador de Yanahuara, construido en el siglo XIX, está compuesto por una serie de hermosos arcos de sillar en los que se encuentran grabadas frases de ilustres pensadores arequipeños. Ofrece una de las mejores vistas panorámicas de la ciudad y de sus tres volcanes: Misti, Chachani y Pichu Pichu.",
                "Calle Miguel Grau s/n, Distrito de Yanahuara, Arequipa",
                -16.388831,
                -71.542247,
                "https://blog.incarail.com/es/mirador-de-yanahuara-arequipa-guia/",
                5,
                "2026-08-25"
        ));

        return places;
    }
}
