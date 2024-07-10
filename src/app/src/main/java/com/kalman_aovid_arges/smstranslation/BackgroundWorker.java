package com.kalman_aovid_arges.smstranslation;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class BackgroundWorker extends Worker {
    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isBackgroundTranslationEnabled = preferences.getBoolean("background_translation", false);

        if(isBackgroundTranslationEnabled){
            translateAllMessages();
        }

        return Result.success();
    }
    void translateAllMessages(){
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<SMSGroup> smsGroups = db.smsGroupDao().getAll();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String targetLanguagePref = sharedPreferences.getString("language", "english");

        String targetLanguageCode;
        if (targetLanguagePref.equals("hebrew")) {
            targetLanguageCode = TranslateLanguage.HEBREW;
        } else {
            targetLanguageCode = TranslateLanguage.ENGLISH;
        }

        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();


        for (SMSGroup g: smsGroups){
            CountDownLatch latch = new CountDownLatch(g.getMessages().size());
            for (SMSMessage message : g.getMessages()) {
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
                                        }
                                    });
                                    translator.translate(message.getMsgBody())
                                            .addOnSuccessListener(translatedText -> {
                                                message.setTranslatedMsgBody(translatedText);
                                                message.setIsTranslated(true);
                                                latch.countDown();
                                            })
                                            .addOnFailureListener(e -> {
                                                latch.countDown();
                                            });
                                } else {
                                    latch.countDown();
                                }
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                latch.countDown();
                            });
                } else {
                    latch.countDown();
                }
            }

        }

        db.smsGroupDao().insertAll((SMSGroup) smsGroups);
    }
}
