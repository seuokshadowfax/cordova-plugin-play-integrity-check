package com.android.playintegrity.PlayIntegrityPlugin;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenResponse;
import com.google.android.play.core.integrity.IntegrityTokenRequest;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import java.security.SecureRandom;
import android.util.Base64;

public class PlayIntegrityPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getIntegrityToken")) {
            this.getIntegrityToken(callbackContext);
            return true;
        }
        return false;
    }

    private void getIntegrityToken(final CallbackContext callbackContext) {
        IntegrityManager integrityManager = IntegrityManagerFactory.create(getContext());
        Task<IntegrityTokenResponse> integrityTokenResponseTask = integrityManager.requestIntegrityToken(
            IntegrityTokenRequest.builder().setNonce(getNonce()).build());

        integrityTokenResponseTask.addOnSuccessListener(response -> {
            String integrityToken = response.token();
            callbackContext.success(integrityToken);
        });

        integrityTokenResponseTask.addOnFailureListener(e -> {
            callbackContext.error("Integrity check failed: " + e.getMessage());
        });
    }

    private String getNonce() {
        // Create a nonce for the request, should be unique per request
        return Base64.encodeToString(UUID.randomUUID().toString().getBytes(), Base64.DEFAULT);
    }

    private Context getContext() {
        return this.cordova.getActivity().getApplicationContext();
    }
}
