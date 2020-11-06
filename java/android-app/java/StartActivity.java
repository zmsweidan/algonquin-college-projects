import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import octranspo.AC_OCTranspActivity;
import patient_intake.PatientIntakeStart;
import quiz.MccMainActivity;

public class StartActivity extends Activity {

    private Button studentButton1;
    private Button studentButton2;
    private Button studentButton3;
    private Button studentButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        studentButton1 = findViewById(R.id.student_button01);
        //studentButton2 = findViewById(R.id.student_button02);
        studentButton3 = findViewById(R.id.student_button03);
        studentButton4 = findViewById(R.id.student_button04);

        assignListeners();
    }

    private void assignListeners() {
        studentButton1.setOnClickListener((View e) ->{
            Intent intent = new Intent(this, MccMainActivity.class);
            startActivity(intent);
        });

/*        studentButton2.setOnClickListener((View e) ->{
            //Intent intent = new Intent(this, _);
            //startActivity(intent);
        });*/

        studentButton3.setOnClickListener((View e) ->{
            Intent intent = new Intent(this, PatientIntakeStart.class);
            startActivity(intent);
        });

        studentButton4.setOnClickListener((View e) ->{
            Intent intent = new Intent(this, AC_OCTranspActivity.class);
            startActivity(intent);
        });
    }
}
