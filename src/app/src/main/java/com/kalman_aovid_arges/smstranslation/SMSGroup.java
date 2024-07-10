package com.kalman_aovid_arges.smstranslation;
import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class SMSGroup {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String contactName;
    private String contactNumber;    
    private int unreadMessagesCount;
    private String lastMessageBody;
    private String sender;

    @TypeConverters(Converters.class)
    private Bitmap contactImage;

    @TypeConverters(Converters.class)
    private LocalDateTime lastMessageDateTime;

    @TypeConverters(Converters.class)
    private List<SMSMessage> messages;
   
    public SMSGroup(String contactName, String contactNumber, Bitmap contactImage, int unreadMessagesCount, String messageBody, LocalDateTime lastMessageDateTime, List<SMSMessage> messages, String sender) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactImage = contactImage;
        this.unreadMessagesCount = unreadMessagesCount;
        this.lastMessageBody = messageBody;
        this.lastMessageDateTime = lastMessageDateTime;
        this.messages = messages;
        this.sender = sender;
    }

    public SMSGroup() {
    }

    // getters

    public int getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public Bitmap getContactImage() {
        return contactImage;
    }
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }
    public String getLastMessageBody() {
        return lastMessageBody;
    }
    public LocalDateTime getLastMessageDateTime() {
        return lastMessageDateTime;
    }
    public List<SMSMessage> getMessages() {
        return messages;
    }
    public String getSender() {
        return sender;
    }

    // setters
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }
    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }
    public void setLastMessageBody(String messageBody) {
        this.lastMessageBody = messageBody;
    }
    public void setLastMessageDateTime(LocalDateTime lastMessageDateTime) {
        this.lastMessageDateTime = lastMessageDateTime;
    }
    public void setMessages(List<SMSMessage> messages) {
        this.messages = messages;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

}
