package atproj.cyplay.com.asperteamcoach.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by andre on 12-Jun-18.
 */

public class ChartHourAxisFormatter implements IAxisValueFormatter {

    private DecimalFormat _format;

    public ChartHourAxisFormatter() {
        _format = new DecimalFormat("00");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return _format.format(value) + "";
    }
}
