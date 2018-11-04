package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mprado.me.watertracker.R;

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
