package atproj.cyplay.com.asperteamcoach.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import atproj.cyplay.com.asperteamapi.util.CalendarUtil;
import atproj.cyplay.com.asperteamapi.util.UserSettingsUtil;
import atproj.cyplay.com.asperteamcoach.R;
import atproj.cyplay.com.asperteamcoach.formatter.ChartDayAxisFormatter;
import atproj.cyplay.com.asperteamcoach.formatter.ChartHourAxisFormatter;
import atproj.cyplay.com.asperteamcoach.ui.IHistory;
import atproj.cyplay.com.asperteamcoach.ui.activity.base.BaseResourceActivity;
import atproj.cyplay.com.asperteamcoach.ui.customview.StressHistoryFragment;
import atproj.cyplay.com.asperteamcoach.ui.fragment.RmssdHistoryFragment;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by andre on 11-Jun-18.
 */

public class HistoryActivity extends BaseResourceActivity implements IHistory {

    public static final int DAY_PERIOD = 0;
    public static final int WEEK_PERIOD = 1;
    public static final int MONTH_PERIOD = 2;

    @Inject
    UserSettingsUtil userSettings;

    @BindView(R.id.dayButton)
    TextView dayButton;

    @BindView(R.id.weekButton)
    TextView weekButton;

    @BindView(R.id.monthButton)
    TextView monthButton;

    @BindView(R.id.trendImage)
    ImageView trendImage;

    @BindView(R.id.trendStatusText)
    TextView trendStatusText;

    @BindView(R.id.trendText)
    TextView trendText;

    RmssdHistoryFragment rmssdHistoryFragment;
    StressHistoryFragment stressHistoryFragment;

    private int _period = DAY_PERIOD;

    private String _id;
    private float _stressLevelMax;

    private int _startDayShift = 0;
    private int _endDayShift = 1;
    private int _startWeekShift = 0;
    private int _endWeekShift = 0;
    private int _startMonthShift = 0;
    private int _endMonthShift = 0;

    public void setStressHistoryFragment(StressHistoryFragment value) {
        stressHistoryFragment = value;
        stressHistoryFragment.setStartTime(CalendarUtil.getDay(_startMonthShift, _startWeekShift, _startDayShift));
        stressHistoryFragment.setEndTime(CalendarUtil.getDay(_endMonthShift, _endWeekShift, _endDayShift));
        stressHistoryFragment.loadByPeriod();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        _id = getIntent().getExtras().get("id").toString();
        _stressLevelMax = Integer.parseInt(getIntent().getExtras().get("stress_level_max").toString());
        _period = Integer.parseInt(getIntent().getExtras().get("period").toString());



        rmssdHistoryFragment = (RmssdHistoryFragment) getFragmentManager().findFragmentById(R.id.rmssdHistoryFragment);
        rmssdHistoryFragment.setListener(rmssdHistoryListener);
        rmssdHistoryFragment.setId(_id);
        rmssdHistoryFragment.setStressLevelMax(_stressLevelMax);
        rmssdHistoryFragment.setXAxisValueFormatter(new ChartHourAxisFormatter());

        updateTitle(_period);
        updateButtons(_period);
        updateFormatter(_period);
        updateTrendText(_period);
        updateShifts(_period);
        updateHistoryData();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected RmssdHistoryFragment.OnRmssdHistoryListener rmssdHistoryListener = new RmssdHistoryFragment.OnRmssdHistoryListener() {
        @Override
        public void onTrend(float stressShift) {
            if (stressShift > 0) {
                trendImage.setImageResource(R.drawable.ic_more_stressed);
                trendStatusText.setText(R.string.more_stressed);
                trendStatusText.setTextColor(getResources().getColor(R.color.colorMoreStressed));
            } else if (stressShift < 0) {
                trendImage.setImageResource(R.drawable.ic_more_relaxed);
                trendStatusText.setText(R.string.more_relaxed);
                trendStatusText.setTextColor(getResources().getColor(R.color.colorMoreRelaxed));
            } else {
                trendImage.setImageResource(R.drawable.ic_stable);
                trendStatusText.setText(R.string.stable);
                trendStatusText.setTextColor(getResources().getColor(R.color.colorStable));
            }
        }
    };

    private void updateTitle(int period) {
        if (_period == DAY_PERIOD)
            setTitle(R.string.activity_day_history);
        else if (_period == WEEK_PERIOD)
            setTitle(R.string.activity_week_history);
        else if (_period == MONTH_PERIOD)
            setTitle(R.string.activity_month_history);
    }

    private void updateButtons(int period) {
        dayButton.setTextColor(getResources().getColor(R.color.colorDateChooserTextNotActive));
        weekButton.setTextColor(getResources().getColor(R.color.colorDateChooserTextNotActive));
        monthButton.setTextColor(getResources().getColor(R.color.colorDateChooserTextNotActive));

        dayButton.setBackgroundResource(R.drawable.background_date_chooser_not_active);
        weekButton.setBackgroundResource(R.drawable.background_date_chooser_not_active);
        monthButton.setBackgroundResource(R.drawable.background_date_chooser_not_active);

        switch (period) {
            case DAY_PERIOD:
                dayButton.setTextColor(getResources().getColor(R.color.colorDateChooserText));
                dayButton.setBackgroundResource(R.drawable.background_date_chooser);
                break;
            case WEEK_PERIOD:
                weekButton.setTextColor(getResources().getColor(R.color.colorDateChooserText));
                weekButton.setBackgroundResource(R.drawable.background_date_chooser);
                break;
            case MONTH_PERIOD:
                monthButton.setTextColor(getResources().getColor(R.color.colorDateChooserText));
                monthButton.setBackgroundResource(R.drawable.background_date_chooser);
                break;
        }
    }

    private void updateFormatter(int period) {
        switch (period) {
            case DAY_PERIOD:
                rmssdHistoryFragment.setXAxisValueFormatter(new ChartHourAxisFormatter());
                break;
            case WEEK_PERIOD:
                rmssdHistoryFragment.setXAxisValueFormatter(new ChartDayAxisFormatter());
                break;
            case MONTH_PERIOD:
                rmssdHistoryFragment.setXAxisValueFormatter(new ChartDayAxisFormatter());
                break;
        }
    }

    private void updateTrendText(int period) {
        switch (period) {
            case DAY_PERIOD:
                trendText.setText(R.string.trend_of_the_day);
                break;
            case WEEK_PERIOD:
                trendText.setText(R.string.trend_of_the_week);
                break;
            case MONTH_PERIOD:
                trendText.setText(R.string.trend_of_the_month);
                break;
        }
    }

    private void updateShifts(int period) {
        _endDayShift = 0;
        _startWeekShift = 0;
        _startMonthShift = 0;

        switch (period) {
            case DAY_PERIOD:
                _endDayShift = 1;
                break;
            case WEEK_PERIOD:
                _startWeekShift = -1;
                break;
            case MONTH_PERIOD:
                _startMonthShift = -1;
                break;
        }
    }

    private void updateHistoryData() {
        String startDay = CalendarUtil.getDay(_startMonthShift, _startWeekShift, _startDayShift);
        String endDay = CalendarUtil.getDay(_endMonthShift, _endWeekShift, _endDayShift);

        rmssdHistoryFragment.setStartTime(startDay);
        rmssdHistoryFragment.setEndTime(endDay);

        if (_period == DAY_PERIOD) {
            rmssdHistoryFragment.setMinimum(8);
            rmssdHistoryFragment.setMaximum(20);
        } else if (_period == WEEK_PERIOD) {
            rmssdHistoryFragment.setMinimum(CalendarUtil.dateToDayUnix(startDay));
            rmssdHistoryFragment.setMaximum(CalendarUtil.dateToDayUnix(endDay));
        } else if (_period == MONTH_PERIOD) {
            rmssdHistoryFragment.setMinimum(CalendarUtil.dateToDayUnix(startDay));
            rmssdHistoryFragment.setMaximum(CalendarUtil.dateToDayUnix(endDay));
        }

        rmssdHistoryFragment.loadByPeriod();

        if (stressHistoryFragment != null) {
            stressHistoryFragment.setStartTime(startDay);
            stressHistoryFragment.setEndTime(endDay);
            stressHistoryFragment.loadByPeriod();
        }
    }

    @OnClick(R.id.dayButton)
    public void onClickDayButton() {
        _period = DAY_PERIOD;

        updateTitle(_period);
        updateButtons(_period);
        updateFormatter(_period);
        updateTrendText(_period);
        updateShifts(_period);
        updateHistoryData();
    }

    @OnClick(R.id.weekButton)
    public void onClickWeekButton() {
        _period = WEEK_PERIOD;

        updateTitle(_period);
        updateButtons(_period);
        updateFormatter(_period);
        updateTrendText(_period);
        updateShifts(_period);
        updateHistoryData();
    }

    @OnClick(R.id.monthButton)
    public void onClickMonthButton() {
        _period = MONTH_PERIOD;

        updateTitle(_period);
        updateButtons(_period);
        updateFormatter(_period);
        updateTrendText(_period);
        updateShifts(_period);
        updateHistoryData();
    }
}
