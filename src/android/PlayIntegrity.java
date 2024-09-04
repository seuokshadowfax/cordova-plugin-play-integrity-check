package com.example.playintegrity;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.integrity.IntegrityManager;
import com.google.android.play.integrity.IntegrityManagerFactory;
import com.google.android.play.integrity.IntegrityTokenRequest;
import com.google.android.play.integrity.model.IntegrityTokenResponse;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import com.example.utils.NonceGenerator;

public class PlayIntegrity extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getIntegrityToken")) {
            this.getIntegrityToken(callbackContext);
            return true;
        }
        return false;
    }

    private void getIntegrityToken(final CallbackContext callbackContext) {
        String nonce = NonceGenerator.generateNonce(16);
        // Initialize the Integrity Manager
        IntegrityManager integrityManager = IntegrityManagerFactory.create(cordova.getActivity().getApplicationContext());

        // Create the Integrity Token Request
        IntegrityTokenRequest integrityTokenRequest = IntegrityTokenRequest.builder()
                .setNonce(nonce) // Replace with a properly generated nonce
                .build();

        // Request the token
        integrityManager
            .requestIntegrityToken(integrityTokenRequest)
            .addOnCompleteListener(new OnCompleteListener<IntegrityTokenResponse>() {
                @Override
                public void onComplete(Task<IntegrityTokenResponse> task) {
                    if (task.isSuccessful()) {
                        IntegrityTokenResponse integrityTokenResponse = task.getResult();
                        String integrityToken = integrityTokenResponse.token();
                        callbackContext.success(integrityToken);
                    } else {
                        Exception e = task.getException();
                        callbackContext.error("Failed to get integrity token: " + e.getMessage());
                    }
                }
            });
    }
}
