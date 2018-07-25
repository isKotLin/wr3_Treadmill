/*
 * Copyright (C) 2013 YIXIA.COM
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vov.vitamio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;

import io.vov.vitamio.utils.ContextUtils;


/**
 * Inspect this class before using any other Vitamio classes.
 * <p/>
 * Don't modify this class, or the full Vitamio library may be broken.
 */
public class Vitamio {
    private static String vitamioPackage;
    private static String vitamioLibraryPath;

    /**
     * Check if Vitamio is initialized at this device
     *
     * @param ctx Android Context
     * @return true if the Vitamio has been initialized.
     */
    @SuppressLint("NewApi")
    public static boolean isInitialized(Context ctx) {
        vitamioPackage = ctx.getPackageName();

        if (VERSION.SDK_INT > 23) {
            vitamioLibraryPath = ctx.getApplicationInfo().nativeLibraryDir + "/";
        } else if (VERSION.SDK_INT == 22) {
            if (ctx.getApplicationContext().getPackageResourcePath().startsWith("/data/app") ) {
                Log.e("vitamio", "22");
                vitamioLibraryPath = ContextUtils.getDataDir(ctx) + "lib/";
            } else {
                Log.e("vitamio", "system");
                vitamioLibraryPath = "/system/app/WrPlayer/lib/";
            }
        } else if (VERSION.SDK_INT == 19) {
            if (ctx.getApplicationContext().getPackageResourcePath().startsWith("/data/app")) {
                Log.e("vitamio", "17");
                vitamioLibraryPath = ContextUtils.getDataDir(ctx) + "lib/";
            } else {
                vitamioLibraryPath = "/system/" + "lib/";
            }
        }else {
            vitamioLibraryPath = ContextUtils.getDataDir(ctx) + "lib/";
        }
        return true;
    }

    public static String getVitamioPackage() {
        return vitamioPackage;
    }


    public static final String getLibraryPath() {
        return vitamioLibraryPath;
    }

}
