package com.example.chatbot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class dialog extends AppCompatDialogFragment {

    String msg;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.imagedialog, null);

        ImageView image=(ImageView)view.findViewById(R.id.imageView);
        image.setImageResource(R.drawable.alert);
        TextView tv=(TextView)view.findViewById(R.id.textView);
        tv.setText(msg);
        builder.setView(view).setTitle("");

        return builder.create();
    }

    public void setMessage(String s){
        msg=s;
    }

}
