package tadakazu1972.activityrecorder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        mView.findViewById(R.id.btn1).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction1();
            }
        });
        mView.findViewById(R.id.btn2).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction2();
            }
        });
        mView.findViewById(R.id.btn3).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("食事・飲食");
                Toast.makeText(mActivity, "食事・飲食を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        mView.findViewById(R.id.btn4).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction4();
            }
        });
        mView.findViewById(R.id.btn5).setOnClickListener(new OnClickListener(){
           @Override
            public void onClick(View v){
               selectAction5();
           }
        });
        mView.findViewById(R.id.btn6).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
               selectAction6();
           }
        });
        mView.findViewById(R.id.btn7).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction7();
            }
        });
        mView.findViewById(R.id.btn8).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("家庭での学習活動");
                Toast.makeText(mActivity, "家庭での学習活動を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        mView.findViewById(R.id.btn9).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction9();
            }
        });
        mView.findViewById(R.id.btn10).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction10();
            }
        });
        mView.findViewById(R.id.btn11).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction11();
            }
        });
        mView.findViewById(R.id.btn12).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction12();
            }
        });
        mView.findViewById(R.id.btn13).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction13();
            }
        });
        mView.findViewById(R.id.btn14).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction14();
            }
        });
        mView.findViewById(R.id.btn15).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction15();
            }
        });
        mView.findViewById(R.id.btn16).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                setData("受診・診療");
                Toast.makeText(mActivity, "受診・診療を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        mView.findViewById(R.id.btn17).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction17();
            }
        });
        mView.findViewById(R.id.btn18).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                selectAction18();
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

    private void selectAction1(){
        final CharSequence[] actions = {"起床","就寝"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction2(){
        final CharSequence[] actions = {"昼寝・うたた寝","休息","お茶・おやつ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction4(){
        final CharSequence[] actions = {"身じたく","洗面","入浴"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction5(){
        final CharSequence[] actions = {"テレビ・ラジオの視聴","新聞・雑誌の購読"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction6(){
        final CharSequence[] actions = {"読書","音楽・ビデオ鑑賞","楽器演奏","その他の趣味・習い事"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction7(){
        final CharSequence[] actions = {"炊事","掃除","洗濯","ごみ捨て","その他の家事","庭作業","育児・家族の世話","子どもと遊ぶ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction9(){
        final CharSequence[] actions = {"買い物(歩行)","買い物(自転車)","犬の散歩","歩行","自転車","バス・電車","車・バイク"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction10(){
        final CharSequence[] actions = {"通勤・通学(歩行)","通勤・通学(自転車)","通勤・通学(バス・電車)","通勤・通学(車・バイク)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction11(){
        final CharSequence[] actions = {"座業","立ち仕事","農作業"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction12(){
        final CharSequence[] actions = {"授業","体育授業","学校行事","部・クラブ活動"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction13(){
        final CharSequence[] actions = {"SNS","知人と会う","会食"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction14(){
        final CharSequence[] actions = {"地域活動","PTA活動","冠婚葬祭","ボランティア"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction15(){
        final CharSequence[] actions = {"観光","映画・美術鑑賞","娯楽施設","スポーツ観戦","その他の趣味","学習塾","習い事"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction18(){
        final CharSequence[] actions = {"スマホ・PC・タブレット","ゲーム","その他"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }

    private void selectAction17(){
        final CharSequence[] actions = {"体操・ヨガ","散歩","速歩","ランニング","その他のスポーツ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("活動を選択してください");
        builder.setItems(actions, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                setData(actions[which].toString());
                Toast.makeText(mActivity, actions[which].toString()+"を入力しました。", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //何もしない
            }
        });
        builder.setCancelable(true);
        builder.create();
        builder.show();
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
            Cursor curCSV = db.rawQuery("SELECT * FROM records order by date desc", null);
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
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"okazaki@sports.osaka-cu.ac.jp"});
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
