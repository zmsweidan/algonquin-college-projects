package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import R;

import java.util.ArrayList;
import java.util.Calendar;

public class PatientIntakeStatistics extends Activity {

    ArrayList<Integer> patientAge;
    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    TextView minAge;
    TextView maxAge;
    TextView avgAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_statistics);

        //run query for patient age
        dbHelper = new PatientIntakeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("select "+ dbHelper.BDAY + " from "+dbHelper.TABLE_NAME, new String[] {});

        patientAge =  new ArrayList<>();
        String birthday;
        int day, month, year, age;

        //find ages from database
        if (cursor.moveToFirst()){
            do {
                birthday = cursor.getString(cursor.getColumnIndex(dbHelper.BDAY)).replaceAll("\\s+","");
                String[] birthdaySplit = birthday.split("/");
                day = Integer.valueOf(birthdaySplit[0].replaceFirst("^0+(?!$)", ""));
                month = Integer.valueOf(birthdaySplit[1].replaceFirst("^0+(?!$)", ""));
                year = Integer.valueOf(birthdaySplit[2]);
                age = getAge(day, month, year);
                patientAge.add(age);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        //calculate statistics
        int min = Integer.MAX_VALUE;
        int max = 0;
        int avg = 0;
        for (int currentAge: patientAge){
            min = Math.min(min, currentAge);
            max = Math.max(max, currentAge);
            avg += currentAge;
        }
        if (patientAge.size() != 0)
            avg = avg/patientAge.size();
        else min = 0;

        //set statistics
        minAge = findViewById(R.id.patient_minAge); minAge.setText(String.valueOf(min));
        maxAge = findViewById(R.id.patient_maxAge); maxAge.setText(String.valueOf(max));
        avgAge = findViewById(R.id.patient_avgAge); avgAge.setText(String.valueOf(avg));


    }

    private int getAge(int day, int month, int year){
        Calendar birthday = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        birthday.set(year, month, day);

        int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR))
            age--;

        return age;
    }
}
