package pe.miguelruiz.itana.data.repository;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.data.local.dao.PlaceDao;
import pe.miguelruiz.itana.data.local.database.AppDatabase;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;

public class PlaceRepository {

    private final PlaceDao placeDao;
    private final ExecutorService executorService;

    public PlaceRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        placeDao = db.placeDao();
        executorService = Executors.newFixedThreadPool(4);
    }

    public void insertPlaces(List<PlaceEntity> places) {
        executorService.execute(() -> placeDao.insertPlaces(places));
    }

    public void insertPlace(PlaceEntity place) {
        executorService.execute(() -> placeDao.insertPlace(place));
    }

    public List<PlaceEntity> getAllPlacesSync() {
        return placeDao.getAllPlaces();
    }

    public PlaceEntity getPlaceByIdSync(int id) {
        return placeDao.getPlaceById(id);
    }

    public int getPlacesCountSync() {
        return placeDao.getPlacesCount();
    }

    public void deleteAllPlaces() {
        executorService.execute(placeDao::deleteAllPlaces);
    }
}
