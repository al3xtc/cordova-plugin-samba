/*global cordova, module*/

module.exports = {
    auth: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "auth", [string]);
    },
    getFiles: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "getFiles", [string]);
    }
};