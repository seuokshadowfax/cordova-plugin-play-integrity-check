package com.android.playintegrity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.play.core.integrity.IntegrityManager;
import com.google.android.play.core.integrity.IntegrityManagerFactory;
import com.google.android.play.core.integrity.IntegrityTokenResponse;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class PlayIntegrityPlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("getIntegrityToken")) {
            // Call Play Integrity API here
            IntegrityManager integrityManager = IntegrityManagerFactory.create(cordova.getActivity().getApplicationContext());

            Task<IntegrityTokenResponse> integrityTokenResponse = integrityManager.requestIntegrityToken(/* Request Parameters */);

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
}
