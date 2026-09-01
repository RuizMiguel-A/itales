package pe.miguelruiz.itana.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pe.miguelruiz.itana.data.local.dao.FavoriteDao;
import pe.miguelruiz.itana.data.local.dao.PlaceDao;
import pe.miguelruiz.itana.data.local.entity.FavoriteEntity;
import pe.miguelruiz.itana.data.local.entity.PlaceEntity;

@Database(entities = {PlaceEntity.class, FavoriteEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;
    private static final String DATABASE_NAME = "itanes_database";

    public abstract PlaceDao placeDao();
    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}
