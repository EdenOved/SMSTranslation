package com.kalman_aovid_arges.smstranslation;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.net.Uri;
import android.database.Cursor;
import android.content.Context;
import android.provider.ContactsContract;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Comparator;
import java.util.Objects;
import android.Manifest;


public class HomeFragment extends Fragment {
    private AppDatabase db;
    private static final int PERMISSIONS_REQUEST_READ_SMS = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.smsGroupRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "db-sms").build();

        List<SMSGroup> smsGroups = null;

        // Check if the READ_SMS permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            smsGroups = fetchAndGroupSMS();
            // If not, request the permission
        } else {
             ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        }

        // Fetch SMS messages, group them, and set the adapter
        saveSMSGroupsToDB(smsGroups);

        SMSGroupAdapter adapter = new SMSGroupAdapter(smsGroups);
        recyclerView.setAdapter(adapter);

        // Add item click listener
        List<SMSGroup> finalSmsGroups = smsGroups;
        adapter.setOnItemClickListener(new SMSGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SMSGroup selectedGroup = finalSmsGroups.get(position);
                
                Intent intent = new Intent(HomeFragment.this.getActivity(), SMSViewActivity.class);
                intent.putExtra("id", selectedGroup.getId());
                intent.putExtra("contactName", selectedGroup.getContactName());
                intent.putExtra("contactNumber", selectedGroup.getContactNumber());
                intent.putExtra("sender", selectedGroup.getSender());
                intent.putExtra("messages", (Serializable) selectedGroup.getMessages());
                startActivity(intent);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Reset the ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setTitle(R.string.app_name);
            actionBar.setSubtitle("");
        }
    }

    private List<SMSGroup> fetchAndGroupSMS() {
        List<SMSGroup> smsGroups = new ArrayList<>();
        Map<String, List<SMSMessage>> smsMap = new HashMap<>();

        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, "date ASC");

        if (cursor != null && cursor.moveToFirst()) {
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");
            int dateIndex = cursor.getColumnIndex("date");
            int typeIndex = cursor.getColumnIndex("type");

            if (addressIndex == -1 || bodyIndex == -1 || dateIndex == -1 || typeIndex == -1) {
                // One of the columns doesn't exist
                return smsGroups;
            }

            do {
                String address = cursor.getString(addressIndex);
                String body = cursor.getString(bodyIndex);
                long dateMillis = cursor.getLong(dateIndex);
                int type = cursor.getInt(typeIndex);

                LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateMillis), ZoneId.systemDefault());

                if (!smsMap.containsKey(address)) {
                    smsMap.put(address, new ArrayList<>());
                }
                Objects.requireNonNull(smsMap.get(address)).add(new SMSMessage(body, type, date));
            } while (cursor.moveToNext());

            cursor.close();
        }


        for (Map.Entry<String, List<SMSMessage>> entry : smsMap.entrySet()) {
            List<SMSMessage> messages = entry.getValue();
            SMSMessage lastMessage = messages.get(messages.size() - 1); // Assuming the last message is at the end of the list

            String contactNumber = entry.getKey();
            String contactName = getContactName(requireActivity(), contactNumber);
            Bitmap contactImage = getContactImage(requireActivity(), contactNumber);
            int unreadMessagesCount = 0; // You need to count the unread messages in the list
            String lastMessageBody = lastMessage.getMsgBody();
            LocalDateTime lastMessageDateTime = lastMessage.getSentReceivedDateTime();

            smsGroups.add(new SMSGroup(contactName, contactNumber, contactImage, unreadMessagesCount, lastMessageBody, lastMessageDateTime, messages, contactNumber));
        }

        smsGroups.sort(new Comparator<SMSGroup>() {
            @Override
            public int compare(SMSGroup o1, SMSGroup o2) {
                return o2.getLastMessageDateTime().compareTo(o1.getLastMessageDateTime());
            }
        });

        return smsGroups;
    }

    private String getContactName(Context context, String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            return null;
        } else {
            String contactName = null;
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
            return contactName;
        }
    }

    public Bitmap getContactImage(Context context, String contactNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactNumber));
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.PHOTO_URI}, null, null, null);

        if (cursor == null) {
            return null; // or return a specific default image
        }

        String imageUri = null;
        int columnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_URI);
        if (columnIndex != -1 && cursor.moveToFirst()) {
            imageUri = cursor.getString(columnIndex);
        }
        cursor.close();

        if (imageUri == null) {
            return null; // or return a specific default image
        }

        try {
            InputStream input = context.getContentResolver().openInputStream(Uri.parse(imageUri));
            if (input == null) {
                return null; // or return a specific default image
            }
            return BitmapFactory.decodeStream(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null; // or return a specific default image
        }
    }

    private void saveSMSGroupsToDB(List<SMSGroup> smsGroups) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(smsGroups != null) {
                    for (SMSGroup group : smsGroups) {
                        db.smsGroupDao().insertAll(group);
                    }
                }
            }
        }).start();
    }

}