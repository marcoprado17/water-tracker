package mprado.me.watertracker.data.rawsensorsample;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(primaryKeys = {"date", "sensorId"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorSample {
    @NonNull
    private Date date;
    @NonNull
    private String sensorId;
    private Float flowRate;
}
