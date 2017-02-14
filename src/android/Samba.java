package org.apache.cordova.samba;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jcifs.Config;
import jcifs.smb.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Samba extends CordovaPlugin {
    private NtlmPasswordAuthentication auth;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("auth")) {
            Config.setProperty("jcifs.smb.lmCompatibility", "2");
            this.auth = new NtlmPasswordAuthentication(null, args.getString(0), args.getString(1));
            callbackContext.success();
            return true;
        }
        if (action.equals("getFiles")) {
            JSONArray files = this.getFiles(args.getString(0));
            callbackContext.success(files.toString());
            return true;
        } else if (action.equals("downloadFile")) {
            String path = this.downloadFile(args.getString(0), args.getString(1));
            callbackContext.success(path);
            return true;
        }
        return false;
    }

    private String downloadFile(String path, String target) {
        try {
            SmbFile file = new SmbFile(path, this.auth);
            SmbFileInputStream in = new SmbFileInputStream(file);
            FileOutputStream out = new FileOutputStream(new File(target), false);

            byte[] b = new byte[8192];
            int i;
            while ((i = in.read(b)) > 0) {
                out.write(b, 0, i);
            }

            in.close();
            out.close();
            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private JSONArray getFiles(String path) throws JSONException {
        try {
            JSONArray result = new JSONArray();
            SmbFile dir = new SmbFile(path, this.auth);
            SmbFile[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                JSONObject object = new JSONObject();
                object.put("name", files[i].getName());
                object.put("path", files[i].getPath());
                object.put("createTime", (int)(files[i].createTime() / 1000));
                object.put("size", files[i].getContentLength());
                result.put(object);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            JSONArray result = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("error", e.getMessage());
            result.put(object);
            return result;
        }
    }
}
