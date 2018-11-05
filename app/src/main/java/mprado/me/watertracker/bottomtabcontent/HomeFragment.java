package mprado.me.watertracker.bottomtabcontent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import mprado.me.watertracker.MainActivity;
import mprado.me.watertracker.R;
import mprado.me.watertracker.data.rawsensorsample.SensorSample;
import mprado.me.watertracker.data.sensor.Sensor;

public class HomeFragment extends Fragment {
    public static class FakeRawSensorSampleGenerator {
        private static final Date INITIAL_DATE = new GregorianCalendar(2018, 6, 1).getTime();
        private static final Date FINAL_DATE = Calendar.getInstance().getTime();

        private static final double MIN_FLOW_RATE = 0.0;
        private static final double MAX_FLOW_RATE = 10.0;

        private static final double FLOW_RATE_ABS_DELTA = 1.0;

        private static final double P_BE_ZERO_IF_NON_ZERO = 0.05;
        private static final double P_BE_ZERO_IF_ZERO = 0.98;

        private static final int MIN_SECONDS_SHIFT = 30;
        private static final int MAX_SECONDS_SHIFT = 720;

        public static List<SensorSample> getRawSensorSamples(List<Sensor> sensors) {
            List<SensorSample> sensorSamples = new LinkedList<>();

            for (Sensor sensor : sensors) {
                double currentFlowRate = MIN_FLOW_RATE + Math.random() * (MAX_FLOW_RATE - MIN_FLOW_RATE);
                Date currentDate = INITIAL_DATE;

                Boolean isZeroFilter = Math.random() > 0.5;

                while (currentDate.before(FINAL_DATE)) {
                    currentFlowRate = currentFlowRate + (-FLOW_RATE_ABS_DELTA + Math.random() * 2 * FLOW_RATE_ABS_DELTA);
                    currentFlowRate = Math.min(currentFlowRate, MAX_FLOW_RATE);
                    currentFlowRate = Math.max(currentFlowRate, MIN_FLOW_RATE);

                    if (isZeroFilter == false) {
                        isZeroFilter = Math.random() <= P_BE_ZERO_IF_NON_ZERO ? true : false;
                    } else {
                        isZeroFilter = Math.random() <= P_BE_ZERO_IF_ZERO ? true : false;
                    }

                    sensorSamples.add(SensorSample
                            .builder()
                            .date(currentDate)
                            .flowRate(isZeroFilter ? 0 : (float) currentFlowRate)
                            .sensorId(sensor.getId())
                            .build()
                    );

                    Calendar c = Calendar.getInstance();
                    c.setTime(currentDate);
                    c.add(Calendar.SECOND, (int) (MIN_SECONDS_SHIFT + Math.random() * (MAX_SECONDS_SHIFT - MIN_SECONDS_SHIFT)));
                    currentDate = c.getTime();
                }
            }

            return sensorSamples;
        }

    }

    private static final String LAST_SENSORS_SYNC_KEY = "lastSensorsSyncKey";
    private static final String LAST_SENSORS_SAMPLE_SYNC_KEY = "lastSensorsDataSyncKey";

    private Button mFakeSyncSensorsButton;
    private Button mFakeSyncSensorsSamplesButton;
    private Button mSyncSensorsButton;
    private Button mSyncSensorsSamplesButton;
    private Button mCleanAllDataButton;
    private TextView mNOfSensorsTextView;
    private TextView mLastSensorsSyncTextView;
    private TextView mLastSensorsSamplesSyncTextView;
    private LinearLayout mContentLinearLayout;
    private ContentLoadingProgressBar mContentLoadingProgressBar;

    private List<AsyncTask<?, ?, ?>> mAsyncTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        mAsyncTasks = new LinkedList<>();

        mFakeSyncSensorsButton = rootView.findViewById(R.id.fake_sync_sensors_button);
        mFakeSyncSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new FakeSyncSensorsAsyncTask().execute());
            }
        });

        mSyncSensorsButton = rootView.findViewById(R.id.sync_sensors_button);
        mSyncSensorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new SyncSensorsAsyncTask().execute());
            }
        });

        mFakeSyncSensorsSamplesButton = rootView.findViewById(R.id.fake_sync_sensors_samples_button);
        mFakeSyncSensorsSamplesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new FakeSyncSensorsSampleAsyncTask().execute());
            }
        });

        mSyncSensorsSamplesButton = rootView.findViewById(R.id.sync_sensors_samples_button);
        mSyncSensorsSamplesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncTasks.add(new SyncSensorsSampleAsyncTask().execute());
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
        mLastSensorsSamplesSyncTextView = rootView.findViewById(R.id.last_sensors_samples_sync_text_view);

        mContentLinearLayout = rootView.findViewById(R.id.content_linear_layout);
        mContentLoadingProgressBar = rootView.findViewById(R.id.content_loading_progress_bar);

        return rootView;
    }

    private class FakeSyncSensorsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mFakeSyncSensorsButton.setText(R.string.syncronizing);
        }

        @Override
        protected Void doInBackground(Void... voids) {
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

    private class SyncSensorsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mSyncSensorsButton.setText(R.string.syncronizing);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: Implementar
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

    private class FakeSyncSensorsSampleAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mFakeSyncSensorsSamplesButton.setText(R.string.syncronizing);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<SensorSample> sensorSamples = FakeRawSensorSampleGenerator.getRawSensorSamples(MainActivity.db.sensorDao().getAll());
            for(SensorSample sensorSample : sensorSamples) {
                Log.d("MPRADO", String.format("%s, %s, %s", sensorSample.getSensorId(), sensorSample.getDate(), sensorSample.getFlowRate()));
            }
            MainActivity.db.sensorSampleDao().insertAll(sensorSamples);
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LAST_SENSORS_SAMPLE_SYNC_KEY, Calendar.getInstance().getTime().toLocaleString());
            editor.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reloadThisFragment();
        }
    }

    private class SyncSensorsSampleAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mSyncSensorsSamplesButton.setText(R.string.syncronizing);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: Implementar
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LAST_SENSORS_SAMPLE_SYNC_KEY, Calendar.getInstance().getTime().toLocaleString());
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
            String lastSensorsDataSync = preferences.getString(LAST_SENSORS_SAMPLE_SYNC_KEY, "-");
            mNOfSensorsTextView.setText(String.format(getString(R.string.n_of_sensors), sensors.size()));
            mLastSensorsSyncTextView.setText(String.format(getString(R.string.last_sensors_sync), lastSensorsSync));
            mLastSensorsSamplesSyncTextView.setText(String.format(getString(R.string.last_sensors_data_sync), lastSensorsDataSync));
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
            editor.remove(LAST_SENSORS_SAMPLE_SYNC_KEY);
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

        for (AsyncTask<?, ?, ?> asyncTask : mAsyncTasks) {
            if (asyncTask != null && asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                asyncTask.cancel(true);
            }
        }
    }
}
