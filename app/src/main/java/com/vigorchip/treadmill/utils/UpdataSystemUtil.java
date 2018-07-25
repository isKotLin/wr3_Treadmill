package com.vigorchip.treadmill.utils;

import android.content.Context;
import android.os.RecoverySystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdataSystemUtil {

    public static final String CHCHE_PARTITION = "/cache/";

    public static final String DEFAULT_PACKAGE_NAME = "update.zip";

    public static void updataSystem(Context context, File otaPackageFile) {
        try {
            boolean b = copyFile(otaPackageFile, new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME));
            if (b) {
                installPackage(context, new File(CHCHE_PARTITION + DEFAULT_PACKAGE_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean copyFile(File f1, File f2) throws Exception {
        int length = 2097152;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return true;
            } else
                out.write(buffer, 0, ins);
        }
    }

    public static void installPackage(Context context, File packageFile) {
        try {
            RecoverySystem.installPackage(context, packageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
