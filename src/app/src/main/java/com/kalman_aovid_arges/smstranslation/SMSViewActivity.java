package com.kalman_aovid_arges.smstranslation;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SMSViewActivity extends AppCompatActivity {
    private SMSGroup smsGroup;
    private SMSMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsview);
        
        String contactName = getIntent().getStringExtra("contactName");
        String contactNumber = getIntent().getStringExtra("contactNumber");
        String sender = getIntent().getStringExtra("sender");
        Serializable data = getIntent().getSerializableExtra("messages");
        List<SMSMessage> messages;
        if (data instanceof List<?>) {
            List<?> rawMessages = (List<?>) data;
            if (!rawMessages.isEmpty() && rawMessages.get(0) instanceof SMSMessage) {
                messages = (List<SMSMessage>) rawMessages;
            } else {
                messages = new ArrayList<>(); // or handle error
            }
        } else {
            messages = new ArrayList<>(); // or handle error
        }

        smsGroup = new SMSGroup(contactName, contactNumber, null, 0, "", null, messages, sender);

        RecyclerView recyclerView = findViewById(R.id.smsMessagesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SMSMessageAdapter(smsGroup.getMessages(), recyclerView);
        recyclerView.setAdapter(adapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(smsGroup.getContactName());
            actionBar.setSubtitle(smsGroup.getContactNumber());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        MenuItem item = menu.findItem(R.id.action_switch);
        View actionView = item.getActionView();
        assert actionView != null;
        SwitchCompat translationSwitch = actionView.findViewById(R.id.translationSwitch);

        ProgressBar progress = findViewById(R.id.progressBar2);
        progress.setVisibility(View.INVISIBLE);

        translationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            new Thread(() -> {
                runOnUiThread(() -> {
                    progress.setVisibility(View.VISIBLE);
                    progress.setIndeterminate(false); // Set ProgressBar to determinate mode
                });
                List<SMSMessage> messages;
                if (isChecked) {
                    Log.d("SmsViewFragment", "Switch is on");
                    // The switch is on, show translated messages
                    List<SMSMessage> translatedMessages = new ArrayList<>();

                    AppDatabase db = AppDatabase.getInstance(getApplicationContext().getApplicationContext());
                    List<SMSGroup> smsGroups = db.smsGroupDao().getAll();

                    SMSGroup smsGroup1 = null;
                    for (SMSGroup g : smsGroups) {
                        if (smsGroup.getSender().equals(g.getSender())) {
                            smsGroup1 = smsGroup;
                            break;
                        }
                    }
                    LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    String targetLanguagePref = sharedPreferences.getString("language", "english");

                    String targetLanguageCode;
                    if (targetLanguagePref.equals("hebrew")) {
                        targetLanguageCode = TranslateLanguage.HEBREW;
                    } else {
                        targetLanguageCode = TranslateLanguage.ENGLISH;
                    }

                    assert smsGroup1 != null;
                    CountDownLatch latch = new CountDownLatch(smsGroup1.getMessages().size());
                    final int totalMessages = smsGroup1.getMessages().size();
                    runOnUiThread(() -> progress.setMax(totalMessages));

                    AtomicInteger translatedMessagesCount = new AtomicInteger();


                    for (SMSMessage message : smsGroup1.getMessages()) {
                        if (!message.getIsTranslated()) {
                            languageIdentifier.identifyLanguage(message.getMsgBody())
                                    .addOnSuccessListener(languageCode -> {
                                        if (!languageCode.equals("und")) {
                                            TranslatorOptions options = new TranslatorOptions.Builder()
                                                    .setSourceLanguage(Objects.requireNonNull(TranslateLanguage.fromLanguageTag(languageCode)))
                                                    .setTargetLanguage(targetLanguageCode)
                                                    .build();

                                            Translator translator = Translation.getClient(options);
                                            translator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SMSViewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            translator.translate(message.getMsgBody())
                                                    .addOnSuccessListener(translatedText -> {
                                                        message.setTranslatedMsgBody(translatedText);
                                                        message.setIsTranslated(true);

                                                        latch.countDown();
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                                                        latch.countDown();
                                                    });
                                        } else {
                                            latch.countDown();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        e.printStackTrace();
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                                        latch.countDown();
                                    });
                        } else {
                            latch.countDown();
                        }
                        runOnUiThread(() -> {
                            progress.setProgress(translatedMessagesCount.incrementAndGet()); // Increment and update progress
                        });
                    }

                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT ).show();
                        e.printStackTrace();
                    }

                    // Update the UI here

                    for (SMSMessage message : smsGroup1.getMessages()) {
                        Log.d("SmsViewFragment", "Message: " + message.getMsgBody());
                        Log.d("SmsViewFragment", "Is Translated: " + message.getIsTranslated());
                        Log.d("SmsViewFragment", "Translated Message: " + message.getTranslatedMsgBody());
                        if(message.getIsTranslated()){
                            translatedMessages.add(new SMSMessage(message.getTranslatedMsgBody(), message.getType(), message.getSentReceivedDateTime()));
                        }else {
                            translatedMessages.add(new SMSMessage(message.getMsgBody(), message.getType(), message.getSentReceivedDateTime()));
                        }

                    }

                    messages = translatedMessages;
                } else {
                    Log.d("SmsViewFragment", "Switch is off");
                    messages = smsGroup.getMessages();
                }

                final List<SMSMessage> finalMessages = messages;
                runOnUiThread(() -> {
                    adapter.setMessages(finalMessages);
                    adapter.notifyDataSetChanged();
                    progress.setVisibility(View.INVISIBLE);
                });
            }).start();
        });

        return true;
    }



}