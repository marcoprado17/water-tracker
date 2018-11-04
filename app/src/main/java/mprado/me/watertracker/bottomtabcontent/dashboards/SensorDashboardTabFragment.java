package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mprado.me.watertracker.R;

public class SensorDashboardTabFragment extends TabFragment {
    public static final String SENSOR_IDX_KEY = "sensorIdxKey";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sensor_dashboard, container, false);
        TextView textView = view.findViewById(R.id.text);
        textView.setText(getTitle());
        return view;
    }

    @Override
    public String getTitle() {
        return String.format("Sensor %d", getArguments().getInt(SENSOR_IDX_KEY));
    }
}
