package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ProgressBar;

import R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatientIntakeImport extends Activity {

    //layout variables
    EditText xmlText;
    Button downloadButton;
    ProgressBar progressBar;

    //database variables
    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_import);

        dbHelper = new PatientIntakeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        xmlText = findViewById(R.id.patient_xml);
        progressBar = findViewById(R.id.PatientIntakeImport_progress);
        downloadButton = findViewById(R.id.PatientIntakeImport_download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadXML myBackgroundThread = new downloadXML();
                myBackgroundThread.execute(xmlText.getText().toString());


            }

        });
    }

    class downloadXML extends AsyncTask<String, Integer, String> {

        String name, address, birthday, phoneNumber, healthCard, description;
        String previousSurgery, allergies;
        String glassesBought, glassesStore;
        String benefits, hadBraces;

        String patientType;
        String snackbarString;

        @Override
        protected String doInBackground(String... args) {

            try {

                for(String siteUrl: args) {
                    //start connection
                    URL url = new URL(siteUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream iStream = urlConnection.getInputStream();

                    //create XML pull parser
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(iStream, "UTF-8");
                    publishProgress(25);

                    int type;
                    //While you're not at the end of the document:
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        //Are you currently at a Start Tag?
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("Patient")) {

                                patientType = parser.getAttributeValue(null, "type"); cv.put(PatientIntakeDatabaseHelper.TYPE, patientType);
                                parser.nextTag(); name = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.NAME, name);
                                parser.nextTag(); address = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.ADDRESS, address);
                                parser.nextTag(); birthday = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.BDAY, birthday);
                                parser.nextTag(); phoneNumber = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.PHONE, phoneNumber);
                                parser.nextTag(); healthCard = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.CARD, healthCard);
                                parser.nextTag(); description = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.DESCRIPTION, description);
                                previousSurgery = allergies = glassesBought = glassesStore = benefits = hadBraces = "null";

                                switch(patientType){
                                    case "Doctor":
                                        parser.nextTag(); previousSurgery = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.SURGERIES, previousSurgery);
                                        parser.nextTag(); allergies = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.ALLERGIES, allergies);
                                        break;
                                    case "Optometrist":
                                        parser.nextTag(); glassesBought = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.GLASSES, glassesBought);
                                        parser.nextTag(); glassesStore = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.STORE, glassesStore);
                                        break;
                                    case "Dentist":
                                        parser.nextTag(); benefits = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.BENEFITS, benefits);
                                        parser.nextTag(); hadBraces = parser.nextText(); cv.put(PatientIntakeDatabaseHelper.BRACES, hadBraces);
                                        break;
                                }

                                Log.i("XML Download - Adding", name + ", Type: " + patientType);
                                db.insert(PatientIntakeDatabaseHelper.TABLE_NAME, null, cv);
                                cv.clear();
                                publishProgress(50);
                            }
                        }
                        parser.next();
                    }
                }
                publishProgress(100);
                snackbarString = "Patients succesfully imported";

            } catch (Exception mfe) {
                Log.e("Error", mfe.getMessage());
                snackbarString = "Error: XML file could not be downloaded";
            }
            //Send a string to the GUI thread through onPostExecute
            return "Finished";

        }

        //when GUI is ready, update the objects
        @Override
        public void onProgressUpdate(Integer ... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        //gui thread
        @Override
        public void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            Snackbar bar = Snackbar.make(findViewById(R.id.patient_import_label), snackbarString, Snackbar.LENGTH_LONG);
            bar.show();
        }

    }


}
