package com.example.chatbot;

public class ChatMessage {

    private String msgText;
    private String msgUser;


    public ChatMessage(String msgtext, String msgUser){
        this.msgText=msgtext;
        this.msgUser=msgUser;
    }

    public ChatMessage(){}

    public String getMsgText(){
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }
}
