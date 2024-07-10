package com.kalman_aovid_arges.smstranslation;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SMSMessage implements Serializable {
    private String msgBody;
    private int type;
    private LocalDateTime sentReceivedDateTime;
    private String translatedMsgBody;
    private boolean isTranslated;

    public SMSMessage(){
        this.msgBody = "";
        this.type = 3;
        this.sentReceivedDateTime = LocalDateTime.now();
    }
    
    public SMSMessage(String msgBody, int type, LocalDateTime sentReceivedDateTime){
        this.msgBody = msgBody;
        this.type = type;
        this.sentReceivedDateTime = sentReceivedDateTime;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public int getType(){
        return type;
    }
    public boolean getIsTranslated() {
        return isTranslated;
    }
    public String getTranslatedMsgBody() {
        return translatedMsgBody;
    }

    public LocalDateTime getSentReceivedDateTime() {
        return sentReceivedDateTime;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSentReceivedDateTime(LocalDateTime sentReceivedDateTime) {
        this.sentReceivedDateTime = sentReceivedDateTime;
    }
    public void setIsTranslated(boolean isTranslated) {
        this.isTranslated = isTranslated;
    }
    public void setTranslatedMsgBody(String translatedMsgBody) {
        this.translatedMsgBody = translatedMsgBody;
    }
}
