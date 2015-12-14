package hu.android.ait.americanabroad;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


public class MessageFragment extends DialogFragment {

    public static final String TAG = "dialog";

    String msg;
    String title;

    static MessageFragment newInstance(String t, String s) {
        MessageFragment f = new MessageFragment();

        Bundle args = new Bundle();
        args.putString("msg", s);
        args.putString("title", t);
        f.setArguments(args);

        return f;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = getArguments().getString("msg");
        title = getArguments().getString("title");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View v = inflater.inflate(R.layout.dialog_message, null);

        TextView tvMessage = (TextView) v.findViewById(R.id.tvMessage);
        TextView tvFragmentTitle = (TextView) v.findViewById(R.id.tvFragmentTitle);

        tvMessage.setText(msg);
        tvFragmentTitle.setText(title);

        builder.setView(v)

                // Add action buttons
                .setPositiveButton((R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ///
                    }
                });
        return builder.create();
    }


}
