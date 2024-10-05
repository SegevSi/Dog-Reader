package bagrut.project.dogreader;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scan_button = findViewById(R.id.btnCapture);

        Button scan_stats_button = findViewById(R.id.stats);
        Button capture_w_service = findViewById(R.id.const_capture);

        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move  = new Intent(MainActivity.this , MainActivity3.class);
                startActivity(move );
            }
        });
        scan_stats_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move  = new Intent(MainActivity.this , MainActivity2.class);
                startActivity(move );
            }
        });

        capture_w_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move  = new Intent(MainActivity.this , ContinuousCaptureActivity.class);
                startActivity(move );
            }
        });
    }


}


