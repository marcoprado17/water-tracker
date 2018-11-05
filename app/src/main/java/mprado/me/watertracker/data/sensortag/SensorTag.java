package mprado.me.watertracker.data.sensortag;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"name", "sensorId"})
@Getter
@Setter
public class SensorTag {
    @NonNull
    private String name;
    @NonNull
    private String sensorId;
}
