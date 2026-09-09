package pe.miguelruiz.itana.data.remote.api;

import java.util.List;

import pe.miguelruiz.itana.data.remote.dto.PlaceRemoteDto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ItanesApiService {
    @GET("places.json")
    Call<List<PlaceRemoteDto>> getPlaces();
}
