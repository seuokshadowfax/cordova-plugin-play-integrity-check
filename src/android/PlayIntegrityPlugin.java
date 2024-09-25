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
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("getIntegrityToken")) {
            String nonce = generateNonce();
            // Call Play Integrity API here
            IntegrityManager integrityManager = IntegrityManagerFactory.create(cordova.getActivity().getApplicationContext());

            IntegrityTokenRequest request = IntegrityTokenRequest.builder().setNonce(nonce).build();

            Task<IntegrityTokenResponse> integrityTokenResponse = integrityManager.requestIntegrityToken(request);

            integrityTokenResponse.addOnSuccessListener(new OnSuccessListener<IntegrityTokenResponse>() {
                @Override
                public void onSuccess(IntegrityTokenResponse response) {
                    callbackContext.success(response.token());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    callbackContext.error(e.getMessage());
                }
            });

            return true;
        }
        return false;
    }

    private String generateNonce() {
        byte[] nonceBytes = new byte[16]; // 16 bytes
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(nonceBytes);

        return Base64.encodeToString(nonceBytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }
}
