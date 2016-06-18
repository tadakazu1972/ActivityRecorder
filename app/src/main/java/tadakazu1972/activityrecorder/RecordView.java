package tadakazu1972.activityrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by tadakazu on 2016/05/27.
 */
public class RecordView extends DialogFragment {
    private MainActivity mActivity;
    private ListView mListView;

    public static RecordView newInstance(){
        RecordView instance = new RecordView();
        return instance;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof MainActivity == false){
            throw new UnsupportedOperationException("WARNING! Call from not MainActivity");
        }
        mActivity = (MainActivity)activity;
        mListView = mActivity.mListView;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mActivity = null;
        mListView = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String order;
        order = "select * from records order by _id desc";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"date","activity"};
        int[] to = {R.id.text_date,R.id.text_activity};
        mActivity.mAdapter = new SimpleCursorAdapter(mActivity,R.layout.record_view,c,from,to,0);
        mListView.setAdapter(mActivity.mAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("活動記録データ");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setNegativeButton("閉じる", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });
        this.setCancelable(false);
        return builder.create();
    }


}
