package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private TabFragment[] tabFragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

        tabFragments = new TabFragment[]{
                getNewSensorDashboardFragment(0),
                getNewSensorDashboardFragment(1),
                getNewSensorDashboardFragment(2),
                new TotalSensorsDashboardTabFragment()
        };
    }

    private SensorDashboardTabFragment getNewSensorDashboardFragment(int sensorIdx) {
        SensorDashboardTabFragment sensorDashboardTabFragment = new SensorDashboardTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SensorDashboardTabFragment.SENSOR_IDX_KEY, sensorIdx);
        sensorDashboardTabFragment.setArguments(bundle);
        return sensorDashboardTabFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments[position];
    }

    @Override
    public int getCount() {
        return tabFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabFragments[position].getTitle();
    }
}
