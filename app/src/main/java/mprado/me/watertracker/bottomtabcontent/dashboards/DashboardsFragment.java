package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import mprado.me.watertracker.MainActivity;
import mprado.me.watertracker.R;
import mprado.me.watertracker.data.sensor.Sensor;

public class DashboardsFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private ContentLoadingProgressBar mContentLoadingProgressBar;

    private List<AsyncTask<?, ?, ?>> mAsyncTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAsyncTasks = new LinkedList<>();

        View view = inflater.inflate(R.layout.dashboards, container, false);
        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.pager);
        mContentLoadingProgressBar = view.findViewById(R.id.content_loading_progress_bar);
        mAsyncTasks.add(new InitTabsAsyncTask().execute());
        return view;
    }

    private class InitTabsAsyncTask extends AsyncTask<Void, Void, List<Sensor>> {
        @Override
        protected List<Sensor> doInBackground(Void... voids) {
            return MainActivity.db.sensorDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Sensor> sensors) {
            mAdapter = new PagerAdapter(getChildFragmentManager(), sensors);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mContentLoadingProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        for (AsyncTask<?, ?, ?> asyncTask : mAsyncTasks) {
            if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                asyncTask.cancel(true);
            }
        }
    }
}
