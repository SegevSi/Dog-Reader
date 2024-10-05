package bagrut.project.dogreader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PieChartView extends LinearLayout {
    private PieChart pieChart;

    public PieChartView(Context context) {
        super(context);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);

        pieChart = new PieChart(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        pieChart.setLayoutParams(layoutParams);
        this.addView(pieChart);

        // Customize chart appearance
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelColor(getResources().getColor(android.R.color.black));

        // Configure legend
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(12f);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

        pieChart.invalidate(); // refresh
    }

    public void setData(Map<String, Integer> data) {
        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Classes");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh
    }


}
