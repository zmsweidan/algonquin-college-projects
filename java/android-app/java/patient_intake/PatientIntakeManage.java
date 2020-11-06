package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import R;

import java.util.ArrayList;

public class PatientIntakeManage extends Activity {

    //result codes
    public static final int DELETE_REQUEST = 1;

    //list view parameters
    ArrayList<String> patientList, typeList;
    ListView listView;
    Adapter messageAdapter;
    Button add;

    //database parameters
    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    //fragment parameters
    FrameLayout frameLayout;
    boolean isTablet = false;
    PatientIntakeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_manage);

        patientList = new ArrayList<>();
        typeList = new ArrayList<>();

        messageAdapter = new Adapter( this );
        listView = findViewById(R.id.listView);
        listView.setAdapter (messageAdapter);

        dbHelper = new PatientIntakeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        queryDatabase();
        handleFragment();

        add = findViewById(R.id.patient_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeManage.this, PatientIntakeForm.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "started patient application form");
            }
        });

    }


    private void queryDatabase(){
        patientList.clear(); typeList.clear();
        cursor = db.rawQuery("select "+dbHelper.KEY_ID+", "+dbHelper.NAME+", "+dbHelper.TYPE+" from "+dbHelper.TABLE_NAME, new String[] {});
        String name, type;

        if (cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex(dbHelper.NAME)); patientList.add(name);
                type = cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)); typeList.add(type);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
    }


    private void handleFragment(){
        //Check if frame layout is loaded
        frameLayout = findViewById(R.id.patient_tablet_frame);
        if (frameLayout != null)
            isTablet = true;

        //Show details of fragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Store patient info in bundle
                Bundle infoToPass = new Bundle();
                cursor = db.rawQuery("select * from "+dbHelper.TABLE_NAME+" where "+dbHelper.KEY_ID+" = ?", new String[] {String.valueOf(messageAdapter.getId(position))});

                infoToPass.putBoolean("isTablet", isTablet);

                infoToPass.putLong("ID", messageAdapter.getId(position));
                infoToPass.putString("name", cursor.getString(cursor.getColumnIndex(dbHelper.NAME)));
                infoToPass.putString("address", cursor.getString(cursor.getColumnIndex(dbHelper.ADDRESS)));
                infoToPass.putString("birthday", cursor.getString(cursor.getColumnIndex(dbHelper.BDAY)));
                infoToPass.putString("phoneNumber", cursor.getString(cursor.getColumnIndex(dbHelper.PHONE)));
                infoToPass.putString("healthCard", cursor.getString(cursor.getColumnIndex(dbHelper.CARD)));
                infoToPass.putString("description", cursor.getString(cursor.getColumnIndex(dbHelper.DESCRIPTION)));
                infoToPass.putString("type", cursor.getString(cursor.getColumnIndex(dbHelper.TYPE)));

                infoToPass.putString("previousSurgery", cursor.getString(cursor.getColumnIndex(dbHelper.SURGERIES)));
                infoToPass.putString("allergies", cursor.getString(cursor.getColumnIndex(dbHelper.ALLERGIES)));
                infoToPass.putString("hadBraces", cursor.getString(cursor.getColumnIndex(dbHelper.BRACES)));
                infoToPass.putString("benefits", cursor.getString(cursor.getColumnIndex(dbHelper.BENEFITS)));
                infoToPass.putString("glassesBought", cursor.getString(cursor.getColumnIndex(dbHelper.GLASSES)));
                infoToPass.putString("glassesStore", cursor.getString(cursor.getColumnIndex(dbHelper.STORE)));


                if (isTablet){//for a tablet
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment  =  new PatientIntakeFragment();
                    fragment.setArguments( infoToPass );
                    ft.replace( R.id.patient_tablet_frame , fragment);
                    ft.commit();

                } else {//for a phone
                    Intent phoneIntent = new Intent (PatientIntakeManage.this, PatientIntakeForm.class);
                    phoneIntent.putExtras(infoToPass);
                    startActivity(phoneIntent);
                    finish();
                }

            }
        });
    }


    private class Adapter extends ArrayAdapter<String> {

        public Adapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return patientList.size();
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = PatientIntakeManage.this.getLayoutInflater();

            View result = null;
            result = inflater.inflate(R.layout.patient_name, null);

            TextView message = result.findViewById(R.id.patient_name_lv); message.setText(patientList.get(position));
            TextView type = result.findViewById(R.id.patient_type_lv); type.setText(typeList.get(position));

            return result;
        }

        public long getId(int position) {
            cursor = db.rawQuery("select * from "+dbHelper.TABLE_NAME, new String[] {});
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndex(dbHelper.KEY_ID));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST) {
            if (resultCode == Activity.RESULT_OK){
                //update database
                Bundle patientInfo = data.getExtras();
                long ID = patientInfo.getLong("ID");
                System.out.println("deleting " + ID);
                deletePatient(ID);
            }
        }
    }

    public void deletePatient(long id){
        db.delete(dbHelper.TABLE_NAME, dbHelper.KEY_ID +"=" +id, null);
        queryDatabase();
        messageAdapter.notifyDataSetChanged();
        if (isTablet)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

}
