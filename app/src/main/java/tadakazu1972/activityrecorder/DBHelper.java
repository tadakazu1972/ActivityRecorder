package tadakazu1972.activityrecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tadakazu on 2016/05/27.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "myrecord.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //Create Table
        sqLiteDatabase.execSQL("create table records(_id integer primary key autoincrement,unixtime integer,date text,activity text)");

        //Init
        sqLiteDatabase.execSQL("insert into records(unixtime,date,activity) values('0123456789','2016/01/01 00:00','初期化テストデータ')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2){
        //delete
        sqLiteDatabase.execSQL("drop table if exist records");
        //create
        onCreate(sqLiteDatabase);
    }

    public void insert(SQLiteDatabase db,long unixtime,String date,String s){
        ContentValues cv = new ContentValues();
        cv.put("unixtime", unixtime);
        cv.put("date", date);
        cv.put("activity", s);
        db.insert("records", null, cv);
    }

    public void update(SQLiteDatabase db, String _id, String date, String activity){
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("activity", activity);
        db.update("records", cv, "_id = " +_id, null);
    }

    public void delete(SQLiteDatabase db, String _id){
        db.delete("records", "_id = " + _id, null);
    }
}
