package com.example.chatbot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class chat_rec extends RecyclerView.ViewHolder {

    public ImageView botProfileImage;
    TextView leftText, rightText;
    public chat_rec(View itemView){
        super(itemView);

        leftText=(TextView)itemView.findViewById(R.id.leftText);
        rightText=(TextView)itemView.findViewById(R.id.rightText);
        botProfileImage=(ImageView)itemView.findViewById(R.id.botProfileImage);

    }
}
