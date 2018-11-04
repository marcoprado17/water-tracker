package mprado.me.watertracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TotalSensorsDashboardTabFragment extends TabFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.total_sensors_dashboard, container, false);
    }

    @Override
    public String getTitle() {
        return "Total";
    }
}
