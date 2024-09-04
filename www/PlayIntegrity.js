var exec = require('cordova/exec');

var PlayIntegrity = {
    getIntegrityToken: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'PlayIntegrity', 'getIntegrityToken', []);
    }
};

module.exports = PlayIntegrity;