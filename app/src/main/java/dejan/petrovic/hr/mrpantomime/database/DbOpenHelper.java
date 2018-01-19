package dejan.petrovic.hr.mrpantomime.database;

/*
 * Created by Dejan on 24-Apr-17.
 */

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

class DbOpenHelper extends SQLiteAssetHelper {

    DbOpenHelper(Context context, String DATABASE_NAME, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

}

