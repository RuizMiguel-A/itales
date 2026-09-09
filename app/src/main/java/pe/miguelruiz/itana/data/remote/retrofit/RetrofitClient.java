package pe.miguelruiz.itana.data.remote.retrofit;

import pe.miguelruiz.itana.data.remote.api.ItanesApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Reemplazar con la URL base real proporcionada
    private static final String BASE_URL = "https://raw.githubusercontent.com/RuizMiguel-A/itales/master/api/";
    private static Retrofit retrofit = null;

    public static ItanesApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ItanesApiService.class);
    }
}
