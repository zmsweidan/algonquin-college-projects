package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import R;

public class PatientIntakeStart extends AppCompatActivity {

    //class variables
    Button form;
    Button importXml;
    Button patients;
    Button stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_start);

        form = findViewById(R.id.PatientIntakeStart_form);
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakeForm.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "started patient application form");
            }
        });

        importXml = findViewById(R.id.PatientIntakeStart_import);
        importXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakeImport.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "started patient import");
            }
        });

        patients = findViewById(R.id.PatientIntakeStart_manage);
        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakeManage.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "patient list viewed");
            }
        });

        stats = findViewById(R.id.PatientIntakeStart_stats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statsIntent = new Intent(PatientIntakeStart.this, PatientIntakeStatistics.class);
                startActivity(statsIntent);
                Log.i("PatientIntakeStart", "patient statistic viewed");
            }
        });
    }

    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.patient_intake_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){

        switch(mi.getItemId()){
            case R.id.zaid_about:

                Dialog about = new Dialog(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(R.layout.patient_custom_dialog);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return true;
    }
}
