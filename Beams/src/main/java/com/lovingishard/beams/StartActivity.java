package com.lovingishard.beams;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.Throwables;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.FusiontablesScopes;

import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Collections;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    final String tableId = "1VBr-SPlIaSkjevU8M-Y9NCMtzD_7MOldSPJPM-g";
    final String appName = "lovingishard-beams/1.0";
    final String serviceEmail = "728792558238-lvil13iaq5hrl3jgh18okmf8rk0pe17c@developer.gserviceaccount.com";
    final String p12ResourceName;

        try {

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        InputStream p12File = getResources().openRawResource(R.raw.googleapi_privatekey);
        PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
                SecurityUtils.getPkcs12KeyStore(), p12File, "notasecret",
                "privatekey", "notasecret");
        // service account credential (uncomment setServiceAccountUser for domain-wide delegation)
        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(serviceEmail)
                .setServiceAccountScopes(Collections.singleton(FusiontablesScopes.FUSIONTABLES))
                .setServiceAccountPrivateKey(privateKey)
                .build();

        Fusiontables fusiontables = new Fusiontables.Builder(httpTransport, jsonFactory, credential).setApplicationName(appName).build();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }
    
}
