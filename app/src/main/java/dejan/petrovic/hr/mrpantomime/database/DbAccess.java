package dejan.petrovic.hr.mrpantomime.database;

/*
 * Created by Dejan on 24-Apr-17.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dejan.mrpantomime.R;

import java.util.ArrayList;
import java.util.List;

public class DbAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DbAccess instance;

    /**
     * Private constructor to avoid object creation from outside classes.
     *
     */
    private DbAccess(Context context) {
        this.openHelper = new DbOpenHelper(context, context.getResources().getString(R.string.table_name), 1);
    }

    /**
     * Return a singleton instance of DbAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DbAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DbAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all words from the database.
     *
     * @return a List of words
     */
    public List<String> getWords() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM words", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();

        }
        cursor.close();
        return list;
    }
}
