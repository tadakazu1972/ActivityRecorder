package tadakazu1972.activityrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by tadakazu on 2016/06/01.
 */
public class EditView extends DialogFragment {
    private MainActivity mActivity;
    private ListView mListView;

    public static EditView newInstance(){
        EditView instance = new EditView();
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
        order = "select * from records order by date desc";
        Cursor c = mActivity.db.rawQuery(order, null);
        String[] from = {"date","activity"};
        int[] to = {R.id.text_date,R.id.text_activity};
        mActivity.mAdapter = new SimpleCursorAdapter(mActivity,R.layout.record_view,c,from,to,0);
        mListView.setAdapter(mActivity.mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                ListView listView = (ListView)parent;
                Cursor i = (Cursor)listView.getItemAtPosition(position);
                String _id = i.getString(i.getColumnIndex("_id"));
                String date = i.getString(i.getColumnIndex("date"));
                String activity = i.getString(i.getColumnIndex("activity"));
                EditView2 instance = EditView2.newInstance(_id, date, activity);
                instance.setCancelable(false);
                instance.show(getFragmentManager(), "3");
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修正するデータをタップしてください");
        ViewGroup parent = (ViewGroup)mListView.getParent();
        if ( parent!=null) {
            parent.removeView(mListView);
        }
        builder.setView(mListView);
        builder.setNegativeButton("閉じる", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dismiss();
            }
        });
        this.setCancelable(false);
        return builder.create();
    }
}
