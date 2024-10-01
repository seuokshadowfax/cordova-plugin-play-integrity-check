/* global cordova, module */

module.exports = {
  getIntegrityToken: function(successCallback, errorCallback) {
      cordova.exec(successCallback, errorCallback, "PlayIntegrityPlugin", "getIntegrityToken", []);
  }
};