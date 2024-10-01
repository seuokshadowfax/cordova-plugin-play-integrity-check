package com.android.playintegrity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenResponse;
import com.google.android.play.core.integrity.IntegrityTokenRequest;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        IntegrityManager integrityManager = IntegrityManagerFactory.create(cordova.getActivity().getApplicationContext());
        Task<IntegrityTokenResponse> integrityTokenResponseTask = integrityManager.requestIntegrityToken(
            IntegrityTokenRequest.builder().setNonce(getNonce()).build());

        integrityTokenResponseTask.addOnSuccessListener(response -> {
            String integrityToken = response.token();

            JSONObject integrityData = getIntegrityVerifyData(integrityToken);
            callbackContext.success(integrityData);
        });

        integrityTokenResponseTask.addOnFailureListener(e -> {
            callbackContext.error("Integrity check failed: " + e.getMessage());
        });
    }

    private String getNonce() {
        // Create a nonce for the request, should be unique per request
        byte[] nonceBytes = new byte[16]; // 16 bytes
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(nonceBytes);

        return Base64.encodeToString(nonceBytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    private String getIntegrityVerifyData(token) {
        String integrityToken = token;

        try {
            // Split the JWT token into three parts: header, payload, and signature
            String[] tokenParts = integrityToken.split("\\.");

            if (tokenParts.length == 3) {
                // Decode the payload part (second part of the JWT)
                String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));

                // Parse the payload into JSON
                JSONObject payloadJson = new JSONObject(payload);

                // Display the decoded payload
                System.out.println("Decoded Payload: " + payloadJson.toString(4));

                // You can now access the integrity data from the JSON object
                JSONObject appIntegrity = payloadJson.getJSONObject("appIntegrity");
                JSONObject deviceIntegrity = payloadJson.getJSONObject("deviceIntegrity");

                // Check App Integrity
                String appRecognitionVerdict = appIntegrity.getString("appRecognitionVerdict");
                System.out.println("App Recognition Verdict: " + appRecognitionVerdict);

                // Check Device Integrity
                String deviceRecognitionVerdict = deviceIntegrity.getJSONArray("deviceRecognitionVerdict").getString(0);
                System.out.println("Device Recognition Verdict: " + deviceRecognitionVerdict);

                JSONObject integrityData = new JSONObject();
                integrityData.put("appIntegrity", appIntegrity);
                integrityData.put("deviceIntegrity", deviceIntegrity);

                return integrityData.toString(4);
            } else {
                System.out.println("Invalid token format.");
                return "Invalid token format.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error decoding token: " + e.getMessage();
        }
    }
}
