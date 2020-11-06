package patient_intake;
/**
 * Author: Zaid Sweidan
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class PatientIntakeDatabaseHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "Patients.db";
    static int VERSION_NUM = 1;

    static final String TABLE_NAME = "patients";
    static final String KEY_ID = "id", NAME = "name", ADDRESS = "address", BDAY = "birthday", PHONE = "phone", CARD = "card", DESCRIPTION = "description", TYPE = "type";
    static final String SURGERIES = "surgeries", ALLERGIES = "allergies", BRACES = "braces", BENEFITS = "benefits", GLASSES = "glasses", STORE = "store";

    public PatientIntakeDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " ( " + KEY_ID + " integer primary key autoincrement, "
                        + NAME + " text NOT NULL, " + ADDRESS + " text NOT NULL, " + BDAY + " date NOT NULL, " + PHONE + " text NOT NULL, " + CARD + " text NOT NULL, " + DESCRIPTION + " text NOT NULL, " + TYPE + " text NOT NULL, "
                        + SURGERIES + " text, " + ALLERGIES + " text, "
                        + BRACES + " text, " + BENEFITS + " text, "
                        + GLASSES + " date, " + STORE + " text "
                        + ");"
        );
        Log.i("ChatDatabaseHelper", "Calling onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.i("ChatDatabaseHelper", "Calling onDowngrade, oldVersion=" + oldVer + " newVersion=" + newVer);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
