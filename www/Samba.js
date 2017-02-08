/*global cordova, module*/

module.exports = {
    auth: function (array, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "auth", array);
    },
    getFiles: function (string, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "getFiles", [string]);
    },
    downloadFile: function (array, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Samba", "downloadFile", array);
    }
};
