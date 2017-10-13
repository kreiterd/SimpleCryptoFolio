package danielkreiter.simplecryptofolio.UI;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * PieChartValueFormatter : Defines the format of the values in the pie chart
 */
public class PieChartValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    /**
     * Instantiates a new PieChartValueFormatter.
     */
    public PieChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    /**
     * returns the formatted value
     *
     * @param value
     * @param entry
     * @param dataSetIndex
     * @param viewPortHandler
     * @return the formatted value
     */
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " €";
    }
}