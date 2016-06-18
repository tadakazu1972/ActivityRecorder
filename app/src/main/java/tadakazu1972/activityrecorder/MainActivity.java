package tadakazu1972.activityrecorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    protected MainActivity mActivity = null;
    protected View mView = null;
    protected ListView mListView = null;
    protected DBHelper mDBHelper = null;
    protected SQLiteDatabase db = null;
    protected Cursor mCursor = null;
    protected SimpleCursorAdapter mAdapter = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mView = this.getWindow().getDecorView();
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //permission (for Android 6.0)
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission  = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ( writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        //Set buttons
        mView.findViewById(R.id.btnData).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                mActivity.showRecordView();
            }
        });
        mView.findViewById(R.id.btnEdit).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                mActivity.showEditView();
            }
        });
        mView.findViewById(R.id.btnExport).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                exportDB();
            }
        });
        mView.findViewById(R.id.btnAwake).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("起床");
            }
        });
        mView.findViewById(R.id.btnNap).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("昼寝");
            }
        });
        mView.findViewById(R.id.btnSleep).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("就寝");
            }
        });
        mView.findViewById(R.id.btnWalk1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("歩く");
            }
        });
        mView.findViewById(R.id.btnWalk2).setOnClickListener(new OnClickListener(){
           @Override
            public void onClick(View v){
               setData("小走り");
           }
        });
        mView.findViewById(R.id.btnRun).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
               setData("走る");
           }
        });
        mView.findViewById(R.id.btnSit).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("座る");
            }
        });
        mView.findViewById(R.id.btnStand).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("立つ");
            }
        });
        mView.findViewById(R.id.btnLie).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("寝転ぶ");
            }
        });
        mView.findViewById(R.id.btnEat1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("食事:軽め");
            }
        });
        mView.findViewById(R.id.btnEat2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("食事:普通");
            }
        });
        mView.findViewById(R.id.btnEat3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("食事:多い");
            }
        });
        mView.findViewById(R.id.btnBicycle).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("自転車");
            }
        });
        mView.findViewById(R.id.btnBike).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("バイク");
            }
        });
        mView.findViewById(R.id.btnCar).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("車");
            }
        });
        mView.findViewById(R.id.btnSports1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("運動:軽め");
            }
        });
        mView.findViewById(R.id.btnSports2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("運動:普通");
            }
        });
        mView.findViewById(R.id.btnSports3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("運動:激しい");
            }
        });
        //listview
        mListView = new ListView(this);
        //db
        mDBHelper = new DBHelper(this);
        db = mDBHelper.getWritableDatabase();
    }

    @Override
    public void onStart(){
        super.onStart();
        this.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void showRecordView(){
        RecordView mRecordView = RecordView.newInstance();
        mRecordView.setCancelable(false);
        mRecordView.show(getFragmentManager(), "1");
    }

    public void showEditView(){
        EditView mEditView = EditView.newInstance();
        mEditView.setCancelable(false);
        mEditView.show(getFragmentManager(), "2");
    }

    public void setData(String s){
        Long nowTime = System.currentTimeMillis(); // 現在日時を取得 UNIX time
        DateFormat yyyymmddhhmm = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //ミリ秒から String の yyyy/MM/dd HH:mm への変換
        String date = yyyymmddhhmm.format(new Date(nowTime));
        mDBHelper.insert(db, nowTime , date, s);
    }

    private void exportDB() {
        //保存するフォルダを作成
        final String SAVE_DIR = "/activityrecorder/";
        File savefile = new File(Environment.getExternalStorageDirectory().getPath() + SAVE_DIR);
        try {
            if(!savefile.exists()){
                savefile.mkdir();
            }
        } catch(SecurityException e){
            e.printStackTrace();
            throw e;
        }

        /*
        //外部ストレージ確認
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //マウントされていてread/write可能
            Log.v("tag","外部ストレージ:マウントされていてread/write可能");
        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            //マウントされているがread権限しかない
            Log.v("tag","マウントされているがread権限しかない");
        } else {
            //マウントされていない
            Log.v("tag","マウントされていない");
        }
        */

        File file = new File(savefile, "myrecords.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM records", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            Log.v("tag","csvファイル生成");
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

        //メール添付
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//ファイル読み込む権限付与
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tadakazu1972@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "データ送信");
        intent.putExtra(Intent.EXTRA_TEXT, "データお送りします。");
        //ファイル添付
        File sendFile = new File(Environment.getExternalStorageDirectory().getPath()+ SAVE_DIR +"myrecords.csv");
        Uri attachments = Uri.fromFile(sendFile);
        Log.v("tag", Environment.getExternalStorageDirectory().getPath()+SAVE_DIR+"myrecords.csv");
        intent.putExtra(Intent.EXTRA_STREAM, attachments);
        try {
            startActivity(Intent.createChooser(intent, "メールアプリを選択"));
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(this, "メールアプリが見つかりません", Toast.LENGTH_LONG).show();
        }
    }
}
