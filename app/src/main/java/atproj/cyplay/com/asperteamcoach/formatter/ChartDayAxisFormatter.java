package atproj.cyplay.com.asperteamcoach.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

import atproj.cyplay.com.asperteamapi.util.CalendarUtil;

/**
 * Created by andre on 12-Jun-18.
 */

public class ChartDayAxisFormatter implements IAxisValueFormatter {

    private DecimalFormat _format;

    public ChartDayAxisFormatter() {
        _format = new DecimalFormat("00");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String date = CalendarUtil.dayUnixToDate((int)value);
        return date.substring(8, 10);
    }
}
