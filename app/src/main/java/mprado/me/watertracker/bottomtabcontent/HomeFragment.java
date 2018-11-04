package mprado.me.watertracker.bottomtabcontent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import mprado.me.watertracker.MainActivity;
import mprado.me.watertracker.R;
import mprado.me.watertracker.data.sensor.Sensor;

public class HomeFragment extends Fragment {
    private static final String LAST_SENSORS_SYNC_KEY = "lastSensorsSyncKey";
    private static final String LAST_SENSORS_DATA_SYNC_KEY = "lastSensorsDataSyncKey";

    private Button mSyncSensorsButton;
    private Button mCleanAllDataButton;
    private TextView mNOfSensorsTextView;
    private TextView mLastSensorsSyncTextView;
    private TextView mLastSensorsDataSyncTextView;
    private LinearLayout mContentLinearLayout;
    private ContentLoadingProgressBar mContentLoadingProgressBar;

    private List<AsyncTask<?, ?, ?>> mAsyncTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        mAsyncTasks = new LinkedList<>();

        mSyncSensorsButton = rootView.findViewById(R.id.sync_sensors_button);
        mSyncSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new SyncSensorsAsyncTask().execute());
            }
        });

        mCleanAllDataButton = rootView.findViewById(R.id.clean_all_data_button);
        mCleanAllDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new CleanAllDataAsyncTask().execute());
            }
        });

        mNOfSensorsTextView = rootView.findViewById(R.id.n_of_sensors_text_view);
        mAsyncTasks.add(new FetchAndFillViewDataAsyncTask().execute());

        mLastSensorsSyncTextView = rootView.findViewById(R.id.last_sensors_sync_text_view);
        mLastSensorsDataSyncTextView = rootView.findViewById(R.id.last_sensors_data_sync_text_view);

        mContentLinearLayout = rootView.findViewById(R.id.content_linear_layout);
        mContentLoadingProgressBar = rootView.findViewById(R.id.content_loading_progress_bar);

        return rootView;
    }

    private class SyncSensorsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mSyncSensorsButton.setText(R.string.syncronizing);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: Obter o dado real
            Sensor[] sensors = new Sensor[]{
                    Sensor.builder().id("a").build(),
                    Sensor.builder().id("b").build(),
                    Sensor.builder().id("c").build()
            };
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainActivity.db.sensorDao().insertAll(sensors);
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LAST_SENSORS_SYNC_KEY, Calendar.getInstance().getTime().toLocaleString());
            editor.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reloadThisFragment();
        }
    }

    private class FetchAndFillViewDataAsyncTask extends AsyncTask<Void, Void, List<Sensor>> {
        @Override
        protected List<Sensor> doInBackground(Void... voids) {
            List<Sensor> sensors = MainActivity.db.sensorDao().getAll();
            return sensors;
        }

        @Override
        protected void onPostExecute(List<Sensor> sensors) {
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            String lastSensorsSync = preferences.getString(LAST_SENSORS_SYNC_KEY, "-");
            String lastSensorsDataSync = preferences.getString(LAST_SENSORS_DATA_SYNC_KEY, "-");
            mNOfSensorsTextView.setText(String.format(getString(R.string.n_of_sensors), sensors.size()));
            mLastSensorsSyncTextView.setText(String.format(getString(R.string.last_sensors_sync), lastSensorsSync));
            mLastSensorsDataSyncTextView.setText(String.format(getString(R.string.last_sensors_data_sync), lastSensorsDataSync));
            mContentLinearLayout.setVisibility(View.VISIBLE);
            mContentLoadingProgressBar.setVisibility(View.GONE);
        }
    }

    private class CleanAllDataAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mCleanAllDataButton.setText(R.string.cleaning);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainActivity.db.clearAllTables();

            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(LAST_SENSORS_SYNC_KEY);
            editor.remove(LAST_SENSORS_DATA_SYNC_KEY);
            editor.commit();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reloadThisFragment();
        }
    }

    private void reloadThisFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, new HomeFragment())
                .commit();
    }

    @Override
    public void onStop() {
        super.onStop();

        for(AsyncTask<?, ?, ?> asyncTask: mAsyncTasks) {
            if(asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING){
                asyncTask.cancel(true);
            }
        }
    }
}
