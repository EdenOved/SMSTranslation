package com.kalman_aovid_arges.smstranslation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Bitmap fromByteArray(byte[] value) {
        return value == null ? null : BitmapFactory.decodeByteArray(value, 0, value.length);
    }

    @TypeConverter
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static LocalDateTime fromString(String value) {
        return value == null ? null : LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @TypeConverter
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @TypeConverter
    public static List<SMSMessage> fromJson(String value) {
        return new Gson().fromJson(value, new TypeToken<List<SMSMessage>>() {}.getType());
    }

    @TypeConverter
    public static String fromList(List<SMSMessage> list) {
        return new Gson().toJson(list);
    }
}
