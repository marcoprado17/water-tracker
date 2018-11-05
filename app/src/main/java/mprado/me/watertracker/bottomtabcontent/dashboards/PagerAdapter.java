package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mprado.me.watertracker.MainActivity;
import mprado.me.watertracker.data.sensor.Sensor;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<TabFragment> tabFragments;

    public PagerAdapter(FragmentManager fm, List<Sensor> sensors) {
        super(fm);

        tabFragments = new ArrayList<>();

        for(Sensor sensor : sensors){
            tabFragments.add(getNewSensorDashboardFragment(sensor.getId()));
        }
        tabFragments.add(new TotalSensorsDashboardTabFragment());
    }

    private SensorDashboardTabFragment getNewSensorDashboardFragment(String sensorId) {
        SensorDashboardTabFragment sensorDashboardTabFragment = new SensorDashboardTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SensorDashboardTabFragment.SENSOR_ID_KEY, sensorId);
        sensorDashboardTabFragment.setArguments(bundle);
        return sensorDashboardTabFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabFragments.get(position).getTitle();
    }
}
