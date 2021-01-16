package project.meet;

public class MessageObject {
    private String msg;
    private boolean sentByCurrentUser;


    public MessageObject(String message, boolean sentByCurrentUser){
        this.msg=message;
        this.sentByCurrentUser=sentByCurrentUser;
    }

    public String getMsg(){
        return msg;
    }

    public boolean getSentByCurrentUser(){
        return sentByCurrentUser;
    }
}


