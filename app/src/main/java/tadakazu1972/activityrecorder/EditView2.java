package tadakazu1972.activityrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by tadakazu on 2016/06/01.
 */
public class EditView2 extends DialogFragment {
    private MainActivity mActivity;

    public static EditView2 newInstance(String _id, String date, String activity){
        EditView2 instance = new EditView2();
        Bundle arguments = new Bundle();
        arguments.putString("_id", _id);
        arguments.putString("date", date);
        arguments.putString("activity", activity);
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if (activity instanceof MainActivity == false){
            throw new UnsupportedOperationException("WARNING! Call from not MainActivity");
        }
        mActivity = (MainActivity)activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mActivity = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String _id = getArguments().getString("_id");
        final String date = getArguments().getString("date");
        final String activity = getArguments().getString("activity");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.editview2, null);
        final EditText editDate = (EditText)view.findViewById(R.id.editDate);
        editDate.setText(date);
        final EditText editActivity = (EditText)view.findViewById(R.id.editActivity);
        editActivity.setText(activity);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("修正データを入力し、修正を押してください。");
        alert.setView(view);
        alert.setPositiveButton("修正", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                String date2 = editDate.getText().toString();
                String activity2 = editActivity.getText().toString();
                mActivity.mDBHelper.update(mActivity.db, _id, date2, activity2);
                Toast.makeText(mActivity, "データを修正しました。", Toast.LENGTH_LONG).show();
                //修正したことを確認させる意味でリストを再び生成して表示させる
                mActivity.showEditView();
                dismiss();
            }
        });
        alert.setNeutralButton("削除", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                mActivity.mDBHelper.delete(mActivity.db, _id);
                //修正したことを確認させる意味でリストを再び生成して表示させる
                mActivity.showEditView();
                dismiss();
            }
        });
        alert.setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //リスト表示
                mActivity.showEditView();
                dismiss();
            }
        });
        alert.setCancelable(false);
        return alert.create();
    }
}
