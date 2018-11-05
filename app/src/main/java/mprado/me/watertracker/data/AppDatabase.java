package mprado.me.watertracker.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import mprado.me.watertracker.data.rawsensorsample.SensorSample;
import mprado.me.watertracker.data.rawsensorsample.SensorSampleDao;
import mprado.me.watertracker.data.sensor.Sensor;
import mprado.me.watertracker.data.sensor.SensorDao;
import mprado.me.watertracker.data.sensortag.SensorTag;
import mprado.me.watertracker.data.sensortag.SensorTagDao;

@TypeConverters({DataConverters.class})
@Database(entities = {Sensor.class, SensorSample.class, SensorTag.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SensorDao sensorDao();
    public abstract SensorSampleDao rawSensorSampleDao();
    public abstract SensorTagDao sensorTagDao();
}
