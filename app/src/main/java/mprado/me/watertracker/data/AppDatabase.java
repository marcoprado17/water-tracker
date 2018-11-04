package mprado.me.watertracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mprado.me.watertracker.data.sensor.Sensor;
import mprado.me.watertracker.data.sensor.SensorDao;

@Database(entities = {Sensor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SensorDao sensorDao();
}
