package bagrut.project.dogreader;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.HashMap;
import java.util.Map;


public class MainActivity2 extends Activity {


    private PieChartView pie_chart ;
    private static DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button back_button = findViewById(R.id.clear);
        this.pie_chart = findViewById(R.id.pie_chart);
        dbHandler = new DBHandler(this);
        set_chart();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteAllData(); ;
                set_chart();

            }
        });

    }
    private void set_chart()
    {



        Map<String, Integer> classData = new HashMap<>();
        classData.put("angry"+"("+dbHandler.count("angry")+")", dbHandler.count("angry"));
        classData.put("happy"+"("+dbHandler.count("happy")+")", dbHandler.count("happy"));
        classData.put("relaxed"+"("+dbHandler.count("relaxed")+")", dbHandler.count("relaxed"));
        classData.put("sad"+"("+dbHandler.count("sad")+")", dbHandler.count("sad"));

        this.pie_chart.setData(classData);


    }
}









