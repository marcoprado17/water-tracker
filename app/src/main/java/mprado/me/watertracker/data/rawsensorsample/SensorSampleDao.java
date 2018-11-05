package mprado.me.watertracker.data.rawsensorsample;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface SensorSampleDao {
    @Query("SELECT * FROM SensorSample WHERE sensorId = :sensorId ORDER BY date")
    List<SensorSample> getAllUnnormalizedOrderedByDate(String sensorId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SensorSample> sensorSamples);

    @Update
    void updateAll(List<SensorSample> sensorSamples);
}
