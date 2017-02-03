package org.apache.cordova.samba;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jcifs.smb.*;
import java.net.MalformedURLException;

public class Samba extends CordovaPlugin {
    private NtlmPasswordAuthentication auth;
    private String host;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("auth")) {
            this.auth = new NtlmPasswordAuthentication(null, args.getString(1), args.getString(2));
            this.host = args.getString(0);
            callbackContext.success();
            return true;
        }
        if (action.equals("getFiles")) {
            JSONArray files = this.getFiles(args.getString(0));
            callbackContext.success(files);
            return true;
        }
        return false;
    }

    private JSONArray getFiles(String path) {
        String share = "smb://" + this.host + "/" + path + "/";
        JSONArray result = new JSONArray();
        try {
            SmbFile dir = new SmbFile(share, this.auth);
            SmbFile[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                JSONObject object = new JSONObject();
                object.put("name", files[i].getName());
                object.put("size", files[i].getContentLength());
                result.put(object);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        return result;
    }
}