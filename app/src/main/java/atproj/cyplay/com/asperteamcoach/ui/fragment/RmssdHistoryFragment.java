package atproj.cyplay.com.asperteamcoach.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.domain.interactor.StressInteractor;
import atproj.cyplay.com.asperteamapi.domain.interactor.callback.ResourceRequestCallback;
import atproj.cyplay.com.asperteamapi.model.AvgStress;
import atproj.cyplay.com.asperteamapi.model.exception.BaseException;
import atproj.cyplay.com.asperteamapi.util.CalendarUtil;
import atproj.cyplay.com.asperteamapi.util.LinearRegression;
import atproj.cyplay.com.asperteamapi.util.MesuarementUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.ui.fragment.base.BaseResourceFragment;
import butterknife.BindView;

/**
 * Created by andre on 11-Jun-18.
 */

public class RmssdHistoryFragment extends BaseResourceFragment {

    @Inject
    StressInteractor stressInteractor;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    private boolean _created = false;

    private String _id;
    private float _stressLevelMax;

    private String _startTime;
    private String _endTime;

    private Integer _mininmum;
    private Integer _maximum;
    private IAxisValueFormatter _xAxisValueFormatter;
    private OnRmssdHistoryListener _listener;

    public void setId(String value) {
        _id = value;
    }

    public void setStressLevelMax(float value) {
        _stressLevelMax = value;
    }

    public void setStartTime(String value) {
        _startTime = value;
    }

    public void setEndTime(String value) {
        _endTime = value;
    }

    public void setMinimum(int value) {
        _mininmum = value;
        if (_created)
            updateMinimum();
    }

    private void updateMinimum() {
        lineChart.getXAxis().setAxisMinimum(_mininmum);
    }

    public void setMaximum(int value) {
        _maximum = value;
        if (_created)
            updateMaximum();
    }

    private void updateMaximum() {
        lineChart.getXAxis().setAxisMaximum(_maximum);
    }

    public void setXAxisValueFormatter(IAxisValueFormatter formatter) {
        _xAxisValueFormatter = formatter;
        if (_created)
            updateXAxisValueFormatter();
    }

    private void updateXAxisValueFormatter() {
        lineChart.getXAxis().setValueFormatter(_xAxisValueFormatter);

    }

    public void setListener(OnRmssdHistoryListener value) {
        _listener = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rmssd_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _created = true;
        if (_mininmum != null)
            updateMinimum();
        if (_maximum != null)
            updateMaximum();
        if (_xAxisValueFormatter != null)
            updateXAxisValueFormatter();

        if (_startTime != null)
            getRmssds(_id, _startTime, _endTime);

        initLineChart();
    }

    private void initLineChart() {
        lineChart.setBackgroundColor(getResources().getColor(R.color.colorChartBackground));
        lineChart.setPadding(0, 0, 0, 0);
        lineChart.setViewPortOffsets(
                MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 20),
                MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 5),
                MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 20),
                MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 20));
        //lineChart.setDrawGridBackground(false);

        Legend l  = lineChart.getLegend();
        l.setEnabled(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        initXAxis();
        initYAxis();
    }

    private void initXAxis() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(7, true);
    }

    private void initYAxis() {
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(100f);
        yAxisLeft.setLabelCount(10);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(100f);
        yAxisRight.setLabelCount(10);
    }

    public void loadByPeriod() {
        if (_created)
            getRmssds(_id, _startTime, _endTime);
    }

    protected void getRmssds(String userId, String startTime, String endTime) {
        showPreloader();
        stressInteractor.getAvgRmmssd(userId, startTime, endTime, new ResourceRequestCallback<List<AvgStress>>() {
            @Override
            public void onSucess(List<AvgStress> stresses) {
                hidePreloader();
                if (_listener != null)
                    updateTrend(stresses);
                drawChart(stresses);
            }

            @Override
            public void onError(BaseException e) {
                hidePreloader();
            }
        });
    }

    private void updateTrend(List<AvgStress> stresses) {
        List<Double> xSrc = new ArrayList<>();
        List<Double> ySrc = new ArrayList<>();
        for (int i = 0; i < stresses.size(); i++) {
            double x = 0;
            if (stresses.get(i).getHour() != null)
                x = stresses.get(i).getHour();
            else
                x = CalendarUtil.dateToDayUnix(stresses.get(i).getDate());
            xSrc.add(x);

            double y = stresses.get(i).getAvgStress();
            ySrc.add(y);
        }

        List<Double> linearRegressionY = LinearRegression.calc(xSrc, ySrc, 10, 20);


        float stressShift = (float)(linearRegressionY.get(1) - linearRegressionY.get(0));
        _listener.onTrend(stressShift);
    }

    protected void drawChart(List<AvgStress> stresses) {
        // draw main chart
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < stresses.size(); i++) {
            Entry entry = new Entry();
            if (stresses.get(i).getHour() != null) {
                entry = new Entry(stresses.get(i).getHour(), stresses.get(i).getAvgStress());
            } else if (stresses.get(i).getDate() != null) {
                int value = CalendarUtil.dateToDayUnix(stresses.get(i).getDate());
                entry = new Entry(value, stresses.get(i).getAvgStress());
                entries.add(entry);
            }
            entries.add(entry);
        }

        if (entries.size() == 0)
            entries.add(new Entry(0.0f, 0.0f));

        LineDataSet dataSet = new LineDataSet(entries, "Stress level");
		dataSet.setDrawValues(false);
        dataSet.setColor(getResources().getColor(R.color.colorChartLine));
        dataSet.setLineWidth(MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 1.1f));
        dataSet.setCircleColor(getResources().getColor(R.color.colorChartLine));
        dataSet.setCircleColorHole(getResources().getColor(R.color.colorChartLine));
        dataSet.setCircleRadius(MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 1.6f));
        //LineData data = new LineData(dataSet);
        //lineChart.setData(data);

        // draw max level
        ArrayList<Entry> maxLevelEntries = new ArrayList<>();
        maxLevelEntries.add(new Entry(0, _stressLevelMax));
        maxLevelEntries.add(new Entry(100000000000l, _stressLevelMax));
        LineDataSet maxLevelDataSet = new LineDataSet(maxLevelEntries, "Max stress level");
		maxLevelDataSet.setDrawValues(false);
        maxLevelDataSet.setColor(getResources().getColor(R.color.colorChartStressLevel));
        maxLevelDataSet.setLineWidth(MesuarementUtil.dpToPixel(getActivity().getApplicationContext(), 1.5f));
        //LineData maxLevelData = new LineData(maxLevelDataSet);
		
		

        List<ILineDataSet> lines = new ArrayList<>();
        lines.add(dataSet);
        lines.add(maxLevelDataSet);

        lineChart.setData(new LineData(lines));

        lineChart.invalidate();
    }

    public interface OnRmssdHistoryListener {
        void onTrend(float stressShift);
    }
}
