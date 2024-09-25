/* global cordova, module */

module.exports = {
  getIntegrityToken: function(token, successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "PlayIntegrityPlugin", "getIntegrityToken", [token]);
  }
};