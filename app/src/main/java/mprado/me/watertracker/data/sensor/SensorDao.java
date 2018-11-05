package mprado.me.watertracker.data.sensor;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SensorDao {
    @Query("SELECT * FROM Sensor")
    List<Sensor> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Sensor... sensors);
}
