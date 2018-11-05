package mprado.me.watertracker.bottomtabcontent.dashboards;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import mprado.me.watertracker.MainActivity;
import mprado.me.watertracker.R;
import mprado.me.watertracker.data.rawsensorsample.SensorSample;


public class SensorDashboardTabFragment extends TabFragment {
    public static final String SENSOR_ID_KEY = "sensorIdxKey";

    public static final int N_HOURS = 24;

    private View mRootView;
    private LineChart mChart;
    private TextView mChartTitleTextView;

    private List<AsyncTask<?, ?, ?>> mAsyncTasks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAsyncTasks = new LinkedList<>();

        View mRootView = inflater.inflate(R.layout.sensor_dashboard, container, false);
        mAsyncTasks.add(new InitChartAsyncTask().execute(N_HOURS));

        mChart = mRootView.findViewById(R.id.chart);

        mChartTitleTextView = mRootView.findViewById(R.id.chart_title_text_view);
        mChartTitleTextView.setText(String.format("Vazão do sensor '%s' em L/s nas últimas %s horas", getArguments().getString(SENSOR_ID_KEY), N_HOURS));

        return mRootView;
    }

    @Override
    public String getTitle() {
        return String.format("Sensor %s", getArguments().getString(SENSOR_ID_KEY));
    }

    private class InitChartAsyncTask extends AsyncTask<Integer, Void, List<SensorSample>> {
        @Override
        protected List<SensorSample> doInBackground(Integer... params) {
            int nHours = params[0];
            Calendar c = Calendar.getInstance();
            c.add(Calendar.HOUR, -nHours);
            Log.d("MPRADO", String.format("c.getTime(): %s", c.getTime()));
            return MainActivity.db.sensorSampleDao().getAllOrderedByDate(getArguments().getString(SENSOR_ID_KEY), c.getTime());
        }

        @Override
        protected void onPostExecute(List<SensorSample> sensorSamples) {
            Log.d("MPRADO", String.format("sensors.size(): %s", sensorSamples.size()));

            List<Entry> entries = new ArrayList<Entry>();

            for (SensorSample sensorSample : sensorSamples) {
                // turn your data into Entry objects
                entries.add(new Entry(sensorSample.getDate().getTime(), sensorSample.getFlowRate()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Label");

            LineData lineData = new LineData(dataSet);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setLabelCount(2);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar c = Calendar.getInstance();
                    Log.d("MPRADO", String.format("value: %s", value));
                    Log.d("MPRADO", String.format("(long)value: %s", (long)value));
                    c.setTimeInMillis((long)value);
                    Log.d("MPRADO", String.format("c.getTime(): %s", c.getTime()));
                    return formatter.format(c.getTime());
                }
            });

            mChart.setData(lineData);
            mChart.invalidate();
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
