package com.kalman_aovid_arges.smstranslation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class SMSGroupAdapter extends RecyclerView.Adapter<SMSGroupAdapter.ViewHolder> {
    private final List<SMSGroup> smsGroups;
    private static OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        SMSGroupAdapter.listener = listener;
    }
    public SMSGroupAdapter(List<SMSGroup> smsGroups) {
        this.smsGroups = smsGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_group_item, parent, false);
        return new ViewHolder(view);
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;
    
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
    
        Canvas canvas = new Canvas(output);
    
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    
        float r = 0;
    
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = (float) bitmap.getHeight() / 2;
        } else {
            r = (float) bitmap.getWidth() / 2;
        }
    
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public Bitmap getDefaultIcon(Context context, int iconResId) {
        // Create a new bitmap with the size of the icon
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
    
        // Draw a random color background
        Paint backgroundPaint = new Paint();
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        backgroundPaint.setColor(color);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
    
        // Draw the icon on top of the background
        Drawable icon = ContextCompat.getDrawable(context, iconResId);
        assert icon != null;
        icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        icon.draw(canvas);
    
        return bitmap;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMSGroup smsGroup = smsGroups.get(position);
        if(smsGroup.getContactName() != null){
            holder.sender.setText(smsGroup.getContactName());
        }else {
            holder.sender.setText(smsGroup.getContactNumber());
        }
        holder.message.setText(String.join(", ", smsGroup.getLastMessageBody()));
        
        // Check if getLastMessageDateTime() is not null before formatting the date
        if (smsGroup.getLastMessageDateTime() != null) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a", Locale.getDefault());
            String lastSentReceivedDateTime = smsGroup.getLastMessageDateTime().format(format);
            holder.lastDate.setText(lastSentReceivedDateTime);
        } else {
            holder.lastDate.setText("No date available");
        }
        // Set the icon to the ImageView
        if(smsGroup.getContactImage() != null){
            Bitmap originalBitmap = smsGroup.getContactImage();
            Bitmap copiedBitmap = getCircularBitmap(originalBitmap.copy(originalBitmap.getConfig(), true));
            holder.icon.setImageBitmap(copiedBitmap);
        } else {
            Bitmap defaultIcon = getCircularBitmap(getDefaultIcon(holder.icon.getContext(), R.drawable.default_icon));
            holder.icon.setImageBitmap(defaultIcon); // Clear the ImageView if there's no contact image
        }
    }


    @Override
    public int getItemCount() {
        return smsGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView icon;
        public TextView sender;
        public TextView message;
        public TextView lastDate;
        public ImageView icon;
        

        public ViewHolder(View itemView) {
            super(itemView);
            lastDate = itemView.findViewById(R.id.lastSentReceivedDateTime);
            icon = itemView.findViewById(R.id.icon);
            sender = itemView.findViewById(R.id.sender);
            message = itemView.findViewById(R.id.message);

            // Set the onClickListener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}