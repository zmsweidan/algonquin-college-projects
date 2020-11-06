package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import R;

import java.text.SimpleDateFormat;

public class PatientIntakeForm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_form);

        //Load fragment to phone:
        Bundle infoToPass = getIntent().getExtras();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PatientIntakeFragment mf = new PatientIntakeFragment();
        mf.setArguments( infoToPass );

        ft.replace(  R.id.patient_phone_frame , mf);
        ft.commit();
    }


}
