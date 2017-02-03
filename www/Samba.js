/*global cordova, module*/

module.exports = {
    test: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "test", [string]);
    }
};