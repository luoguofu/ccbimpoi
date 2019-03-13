
package com.weqia.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LHelper {

    public LHelper() {
    }

    public void json(String json) {
        if (TextUtils.isEmpty(json)) {
            L.e("Empty/Null json content");
        } else {
            try {
                if (json.startsWith("{") || json.startsWith("[")) {
                    String message;
                    if (json.startsWith("{")) {
                        JSONObject e1 = new JSONObject(json);
                        message = e1.toString(4);
                        log(message, new Object[0]);
                        return;
                    }

                    if (json.startsWith("[")) {
                        JSONArray e = new JSONArray(json);
                        message = e.toString(4);
                        log(message, new Object[0]);
                    }
                } else {
                    L.e("not json show real:" + json);
                }
            } catch (Exception e) {
                L.e("not json show real:" + json);
            }

        }
    }

    private synchronized void log(String msg, Object... args) {
        String message = this.createMessage(msg, args);
        this.log1();
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= 4000) {
            this.log2(message);
            this.log3();
        } else {
            for (int i = 0; i < length; i += 4000) {
                int count = Math.min(length - i, 4000);
                this.log2(new String(bytes, i, count));
            }
            this.log3();
        }
    }

    private void log1() {
        L.i("╔════════════════════════════════════════════════════════════════════════════════════════");
    }


    private void log3() {
        L.i("╚════════════════════════════════════════════════════════════════════════════════════════");
    }


    private void log2(String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        String[] arr$ = lines;
        int len$ = lines.length;
        for (int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            L.i("║ " + line);
        }
    }

    private String createMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }


}
