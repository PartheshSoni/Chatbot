package com.example.chatbot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static android.widget.ListPopupWindow.WRAP_CONTENT;


public class messagelist extends AppCompatActivity implements AIListener {
    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<ChatMessage, chat_rec> adapter;
    Boolean flagFab = true;

    private AIService aiService;

    FirebaseApp firebaseApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist);

        //getActionBar().hide();
        //firebaseApp.initializeApp(this);

        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        editText=(EditText)findViewById(R.id.editText);
        addBtn=(RelativeLayout)findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        final AIConfiguration config = new AIConfiguration("889436a23b9844eca7f96ad6df964992",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIService aiService = AIService.getService(this, config);

        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(config);
        final AIRequest aiRequest = new AIRequest();



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editText.getText().toString().trim();

                if(!message.equals("")){
                    ChatMessage chatMessage=new ChatMessage(message, "user");
                    ref.child("chat").push().setValue(chatMessage);

                    aiRequest.setQuery(message);

                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {
                                Result result=aiResponse.getResult();
                                String reply=result.getFulfillment().getSpeech();
                                ChatMessage chatMessage=new ChatMessage();

                                if(reply.charAt(reply.length()-1)=='#') {
                                    openEDialog("#");

                                    chatMessage=new ChatMessage(reply.substring(0, reply.length()-1), "bot");
                                }

                                else if(reply.charAt(reply.length()-1)=='*') {
                                    openEDialog("*");

                                    chatMessage=new ChatMessage(reply.substring(0, reply.length()-1), "bot");
                                }

                                else if(reply.charAt(reply.length()-1)=='@'){
                                    openEDialog("@");
                                    //openDialog(reply.substring(0, reply.length()-1));
                                    chatMessage=new ChatMessage(reply.substring(0, reply.length()-1), "bot");
                                }

                                else if(reply.charAt(reply.length()-1)=='$'){
                                    openEDialog("$");
                                    //openDialog(reply.substring(0, reply.length()-1));
                                    chatMessage=new ChatMessage(reply.substring(0, reply.length()-1), "bot");
                                }

                                else if(reply.charAt(reply.length()-1)=='%'){

                                    openDialog(reply.substring(0, reply.length()-1));
                                    chatMessage=new ChatMessage(reply.substring(0, reply.length()-1), "bot");
                                }

                                else{
                                    chatMessage=new ChatMessage(reply, "bot");
                                    MediaPlayer ring= MediaPlayer.create(messagelist.this,R.raw.unconvinced);
                                    ring.start();
                                }

                                ref.child("chat").push().setValue(chatMessage);
                            }
                        }

                        private void openDialog(String s) {
                            final dialog dialogi=new dialog();
                            MediaPlayer ring= MediaPlayer.create(messagelist.this,R.raw.unsure);
                            ring.start();
                            dialogi.setMessage(s);
                            dialogi.show(getSupportFragmentManager(), "dialog");
                            new CountDownTimer(3000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub

                                    dialogi.dismiss();
                                }
                            }.start();
                        }

                        public void openEDialog(String s) {
                            final Dialog dialog=new Dialog(messagelist.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialogforimage);

                            ImageView image=(ImageView)dialog.findViewById(R.id.imageView2);

                            MediaPlayer ring= MediaPlayer.create(messagelist.this,R.raw.openended);
                            ring.start();

                            if(s.equals("#"))
                                image.setImageResource(R.drawable.night);

                            else if(s.equals("*"))
                                image.setImageResource(R.drawable.morning);
                            else if(s.equals("@"))
                                image.setImageResource(R.drawable.afternoon);
                            else if(s.equals("$"))
                                image.setImageResource(R.drawable.evening);


                            dialog.show();
                            Window window=dialog.getWindow();
                            window.setLayout(WRAP_CONTENT, WRAP_CONTENT);


                            new CountDownTimer(3000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onFinish() {
                                    // TODO Auto-generated method stub

                                    dialog.dismiss();
                                }
                            }.start();
                        }
                    }.execute(aiRequest);
                }
                else{
                    aiService.startListening();
                }
                editText.setText("");
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ImageView fab_img=(ImageView)findViewById(R.id.fab_img);
                Bitmap img1= BitmapFactory.decodeResource(getResources(), R.drawable.ic_send_white_24dp);
                Bitmap img2=BitmapFactory.decodeResource(getResources(), R.drawable.ic_mic_white_24dp);

                if(s.toString().trim().length()!=0&&flagFab){
                    ImageViewAnimatedChange(getApplicationContext(), fab_img, img1);
                    flagFab=false;
                }
                else if(s.toString().trim().length()==0)
                {
                    ImageViewAnimatedChange(getApplicationContext(), fab_img, img2);
                    flagFab=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        adapter=new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(ChatMessage.class, R.layout.item_message_sent, chat_rec.class, ref.child("chat")) {

            @Override
            protected void populateViewHolder(chat_rec viewHolder, ChatMessage model, int position) {
                if (model.getMsgUser().equals("user")) {
                    viewHolder.rightText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.botProfileImage.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.GONE);
                } else {
                    // if(model.getMsgText().charAt(model.getMsgText().length()-1)=='#')
                    // openDialog(("\n\n"+model.getMsgText().substring(0, model.getMsgText().length()-1)));
                    String displayStr="";

                    viewHolder.leftText.setText(model.getMsgText());
                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.botProfileImage.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);
                }


            }



        };


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount=adapter.getItemCount();
                int lastVisiblePosition=linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastVisiblePosition==-1||(positionStart>=(msgCount-1)&&lastVisiblePosition==(positionStart-1))){
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });




        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {

        Result result1=response.getResult();

        String message=result1.getResolvedQuery();
        ChatMessage chatMessage=new ChatMessage(message, "user");
        ref.child("chat").push().setValue(chatMessage);


        String reply=result1.getFulfillment().getSpeech();
        ChatMessage chatMessage1=new ChatMessage(reply, "bot");
        ref.child("chat").push().setValue(chatMessage1);
    }

    @Override
    public void onError(ai.api.model.AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
