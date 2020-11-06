package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import R;

import java.text.SimpleDateFormat;

public class PatientIntakeFragment extends Fragment {

    //mode check
    boolean isTablet, editMode = false;
    String name, address, birthday, phoneNumber, healthCard, description, type;
    String previousSurgery, allergies, glassesBought, glassesStore, benefits, hadBraces;
    long ID;

    //layout parameters
    RadioGroup patientTypeRadio;
    int selectedRadioButton;
    Button submitButton, saveButton, deleteButton;
    LinearLayout editGroup;
    GridLayout doctorGrid, dentistGrid, optmoteristGrid;

    //database parameters
    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ContentValues cv;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        //parameters pssed for edit mode
        Bundle patientInfo = getArguments();
        if (patientInfo != null) {
            editMode = true;
            isTablet = patientInfo.getBoolean("isTablet");

            ID =  patientInfo.getLong("ID");
            name = patientInfo.getString("name");
            birthday = patientInfo.getString("birthday");
            address = patientInfo.getString("address");
            phoneNumber = patientInfo.getString("phoneNumber");
            healthCard = patientInfo.getString("healthCard");
            description = patientInfo.getString("description");
            type = patientInfo.getString("type");

            previousSurgery = patientInfo.getString("previousSurgery");
            allergies = patientInfo.getString("allergies");
            benefits = patientInfo.getString("benefits");
            hadBraces = patientInfo.getString("hadBraces");
            glassesBought = patientInfo.getString("glassesBought");
            glassesStore = patientInfo.getString("glassesStore");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate gui
        View gui = inflater.inflate(R.layout.patient_form, null);

        //instantiate layout objects
        doctorGrid = gui.findViewById(R.id.doctor_grid);
        dentistGrid = gui.findViewById(R.id.dentist_grid);
        optmoteristGrid = gui.findViewById(R.id.optometrist_grid);
        submitButton = gui.findViewById(R.id.PatientIntakeForm_submit);
        editGroup = gui.findViewById(R.id.PatientIntakeForm_edit);
        saveButton = gui.findViewById(R.id.PatientIntakeForm_save);
        deleteButton = gui.findViewById(R.id.PatientIntakeForm_delete);

        //add listener to radio group
        patientTypeRadio = gui.findViewById(R.id.patient_type);
        patientTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (editMode)
                    editGroup.setVisibility(View.VISIBLE);
                else
                    submitButton.setVisibility(View.VISIBLE);

                switch(checkedId) {
                    case R.id.doctor:
                        doctorGrid.setVisibility(View.VISIBLE);
                        dentistGrid.setVisibility(View.GONE);
                        optmoteristGrid.setVisibility(View.GONE);
                        selectedRadioButton=1;
                        break;
                    case R.id.dentist:
                        doctorGrid.setVisibility(View.GONE);
                        dentistGrid.setVisibility(View.VISIBLE);
                        optmoteristGrid.setVisibility(View.GONE);
                        selectedRadioButton=2;
                        break;
                    case R.id.optometrist:
                        doctorGrid.setVisibility(View.GONE);
                        dentistGrid.setVisibility(View.GONE);
                        optmoteristGrid.setVisibility(View.VISIBLE);
                        selectedRadioButton=3;
                        break;
                }
                //reset values
                ((EditText)gui.findViewById(R.id.patient_surgeries)).setText("");
                ((EditText)gui.findViewById(R.id.patient_allergies)).setText("");
                ((CheckBox)gui.findViewById(R.id.patient_braces)).setChecked(false);
                ((EditText)gui.findViewById(R.id.patient_benefits)).setText("");
                ((EditText)gui.findViewById(R.id.patient_glassesdate)).setText("");
                ((EditText)gui.findViewById(R.id.patient_store)).setText("");

            }
        });

        //initialise database
        dbHelper = new PatientIntakeDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        //add listener to submit/save button
        if (editMode) submitButton = saveButton;
        submitButton.setOnClickListener(click -> {

            String message = "Error: please add all required information";
            boolean validGeneral = false;
            boolean validSpecial = false;

            String name = ((EditText)gui.findViewById(R.id.patient_name)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.NAME, name);
            String address = ((EditText)gui.findViewById(R.id.patient_address)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.ADDRESS, address);
            String birthday = ((EditText)gui.findViewById(R.id.patient_birthday)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.BDAY, birthday);
            String phoneNumber = ((EditText)gui.findViewById(R.id.patient_phone)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.PHONE, phoneNumber);
            String healthCard = ((EditText)gui.findViewById(R.id.patient_card)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.CARD, healthCard);
            String description = ((EditText)gui.findViewById(R.id.patient_description)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.DESCRIPTION, description);

            //validate general patient info
            if (!name.isEmpty() && !address.isEmpty() && !birthday.isEmpty() && !phoneNumber.isEmpty() && !healthCard.isEmpty() && !description.isEmpty())
                validGeneral = true;

            //validate date
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.parse(birthday);
            } catch (Exception e){
                validGeneral = false;
            }

            if (selectedRadioButton == 1) {
                cv.put(PatientIntakeDatabaseHelper.TYPE, "Doctor");
                String previousSurgery = ((EditText)gui.findViewById(R.id.patient_surgeries)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.SURGERIES, previousSurgery);
                String allergies = ((EditText)gui.findViewById(R.id.patient_allergies)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.ALLERGIES, allergies);
                if (!previousSurgery.isEmpty() && !allergies.isEmpty()) validSpecial = true;
            }
            if (selectedRadioButton == 2) {
                cv.put(PatientIntakeDatabaseHelper.TYPE, "Dentist");
                String benefits = ((EditText)gui.findViewById(R.id.patient_benefits)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.BENEFITS, benefits);
                String hadBraces = ((CheckBox)gui.findViewById(R.id.patient_braces)).isChecked()?"Yes":"No"; cv.put(PatientIntakeDatabaseHelper.BRACES, hadBraces);
                if (!benefits.isEmpty() && !hadBraces.isEmpty()) validSpecial = true;
            }
            if (selectedRadioButton == 3) {
                cv.put(PatientIntakeDatabaseHelper.TYPE, "Optometrist");
                String glassesBought = ((EditText)gui.findViewById(R.id.patient_glassesdate)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.GLASSES, glassesBought);
                String glassesStore = ((EditText)gui.findViewById(R.id.patient_store)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.STORE, glassesStore);
                if (!glassesBought.isEmpty() && !glassesStore.isEmpty()) validSpecial = true;
            }

            //If entered data is all valid
            if (validGeneral & validSpecial){
                if (editMode){
                    db.update(PatientIntakeDatabaseHelper.TABLE_NAME, cv, "id="+ID, null);
                    cv.clear();
                    Log.i("Patient Form - updated", name);
                    message = "Patient " + name + " successfully updated";
                } else {
                    db.insert(PatientIntakeDatabaseHelper.TABLE_NAME, null, cv);
                    cv.clear();
                    Log.i("Patient Form - added", name);
                    message = name + " successfully added";
                }
            }

            //display status message
            Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
            toast.show();

            //if operation is successfull
            if (validGeneral & validSpecial)
                startActivity(new Intent(getActivity(), PatientIntakeManage.class));

        });

        //add listen to delete button
        deleteButton.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure you want to remove this patient?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (isTablet){
                        ((PatientIntakeManage) getActivity()).deletePatient(ID);
                    } else {
                        db.delete(dbHelper.TABLE_NAME, dbHelper.KEY_ID +"=" +ID, null);
                        db.close();
                        startActivity( new Intent(getActivity(), PatientIntakeManage.class));
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { }
            });
            builder.create().show();
        });

        //add parameters for edit mode
        if (editMode){
            ((EditText)gui.findViewById(R.id.patient_name)).setText(name);
            ((EditText)gui.findViewById(R.id.patient_birthday)).setText(birthday);
            ((EditText)gui.findViewById(R.id.patient_address)).setText(address);
            ((EditText)gui.findViewById(R.id.patient_phone)).setText(phoneNumber);
            ((EditText)gui.findViewById(R.id.patient_card)).setText(healthCard);
            ((EditText)gui.findViewById(R.id.patient_description)).setText(description);

            switch (type){
                case "Doctor":
                    patientTypeRadio.check(R.id.doctor);
                    ((EditText)gui.findViewById(R.id.patient_surgeries)).setText(previousSurgery);
                    ((EditText)gui.findViewById(R.id.patient_allergies)).setText(allergies);
                    break;
                case "Dentist":
                    patientTypeRadio.check(R.id.dentist);
                    if (hadBraces.equals("Yes")) ((CheckBox)gui.findViewById(R.id.patient_braces)).setChecked(true);
                    ((EditText)gui.findViewById(R.id.patient_benefits)).setText(benefits);
                    break;
                case "Optometrist":
                    patientTypeRadio.check(R.id.optometrist);
                    ((EditText)gui.findViewById(R.id.patient_glassesdate)).setText(glassesBought);
                    ((EditText)gui.findViewById(R.id.patient_store)).setText(glassesStore);
                    break;
            }
        }

        //disable title if landscape and edit mode
        if (isTablet && editMode)
            ((TextView)gui.findViewById(R.id.patient_form_label)).setVisibility(View.GONE);

        return gui;
    }

}