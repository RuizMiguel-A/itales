package pe.miguelruiz.itana.data.repository;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelruiz.itana.data.local.dao.FavoriteDao;
import pe.miguelruiz.itana.data.local.database.AppDatabase;
import pe.miguelruiz.itana.data.local.entity.FavoriteEntity;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;
    private final ExecutorService executorService;

    public FavoriteRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        favoriteDao = db.favoriteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertFavorite(FavoriteEntity favorite) {
        executorService.execute(() -> favoriteDao.insertFavorite(favorite));
    }

    public void deleteFavorite(int placeId) {
        executorService.execute(() -> favoriteDao.deleteFavorite(placeId));
    }

    public boolean isFavoriteSync(int placeId) {
        return favoriteDao.isFavorite(placeId);
    }
}
