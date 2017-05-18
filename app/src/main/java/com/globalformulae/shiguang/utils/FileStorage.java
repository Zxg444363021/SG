package com.globalformulae.shiguang.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.UUID;

/**
 * Created by Fsh on 2016/12/28.
 */

public class FileStorage {
    private File cropIconDir;
    private File picDir;

    public FileStorage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File external = Environment.getExternalStorageDirectory();
            String rootDir = "/" + "icons";
            cropIconDir = new File(external, rootDir + "/crop");
            if (!cropIconDir.exists()) {
                cropIconDir.mkdirs();
            }
            picDir = new File(external, rootDir + "/pic");
            if (!picDir.exists()) {
                picDir.mkdirs();
                picDir.mkdirs();
            }
        } else {
            Log.e("FileStorage", "1");
        }
    }

    public File createCropFile() {
        String fileName = "";
        if (cropIconDir != null) {
            fileName = UUID.randomUUID().toString() + ".png";
        } else {
            Log.e("FileStorage", "2");
        }
        return new File(cropIconDir, fileName);
    }

    public File createPicFile() {
        String fileName = "";
        if (picDir != null) {
            fileName = UUID.randomUUID().toString() + ".png";
        } else {
            Log.e("FileStorage", "3");
        }
        return new File(picDir, fileName);
    }

}
