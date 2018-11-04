package mprado.me.watertracker.data.sensortag;

import android.arch.persistence.room.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(primaryKeys = {"name", "sensorId"})
@Getter
@Setter
public class SensorTag {
    private String name;
    private String sensorId;
}
