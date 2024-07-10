package com.kalman_aovid_arges.smstranslation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SMSMessageAdapter extends RecyclerView.Adapter<SMSMessageAdapter.ViewHolder> {
    private List<SMSMessage> SMSMessages;
    private final RecyclerView recyclerView;

    public SMSMessageAdapter(List<SMSMessage> messages, RecyclerView recyclerView) {
        this.SMSMessages = messages;
        this.recyclerView = recyclerView;
    }

    public void addMessage(SMSMessage message) {
        SMSMessages.add(message);
        notifyItemInserted(SMSMessages.size() - 1);
        recyclerView.scrollToPosition(SMSMessages.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMSMessage smsMessage = SMSMessages.get(position);

        // Set the message and timestamp
        holder.message.setText(smsMessage.getMsgBody());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm a", Locale.getDefault());
        String formattedDateTime = smsMessage.getSentReceivedDateTime().format(formatter);

               
        holder.timestamp.setText(formattedDateTime);

        // Check the type of the message and adjust the layout accordingly
        if (smsMessage.getType() == 1) {
            // If the message is received, align it to the left, add margin to the right, add padding, and set the text color to black
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.message.getLayoutParams();
            params.horizontalBias = 0f;
            params.rightMargin = 50;
            params.bottomMargin = 15;
            holder.message.setLayoutParams(params);
            holder.message.setPadding(15, 20, 15, 20);
            holder.message.setBackgroundResource(R.drawable.bg_received_message);
            holder.message.setTextColor(Color.BLACK);


        } else {
            // If the message is sent, align it to the right, add margin to the left, add padding, and set the text color to black
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.message.getLayoutParams();
            params.horizontalBias = 1f;
            params.leftMargin = 50;
            params.bottomMargin = 15;
            holder.message.setLayoutParams(params);
            holder.message.setPadding(15, 20, 15, 20);
            holder.message.setBackgroundResource(R.drawable.bg_sent_message);
            holder.message.setTextColor(Color.BLACK);

            // Add left margin to the timestamp
            ConstraintLayout.LayoutParams timestampParams = (ConstraintLayout.LayoutParams) holder.timestamp.getLayoutParams();
            timestampParams.leftMargin = 50;
            holder.timestamp.setLayoutParams(timestampParams);

        }
        // Scroll to the bottom of the list
        if (position == SMSMessages.size() - 1) {
            holder.itemView.post(() -> {
                // Call smooth scroll
                ((RecyclerView) holder.itemView.getParent()).smoothScrollToPosition(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return SMSMessages.size();
    }

    public void setMessages(List<SMSMessage> Messages) {
        this.SMSMessages = Messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView message;
        public TextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}