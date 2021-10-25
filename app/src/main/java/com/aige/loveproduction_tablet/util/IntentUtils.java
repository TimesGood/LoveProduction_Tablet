package com.aige.loveproduction_tablet.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.aige.loveproduction_tablet.BuildConfig;

import java.io.File;

/**
 * 对一些页面跳转的一些封装
 */

public class IntentUtils {

    static final String IS_DOCUMENT = "isDoc";
    /**
     * 通过文件后缀名来获取对应的Intent
     *
     * @param context  Context 对象
     * @param file 文件名称
     * @return 对应的Intent 或 null - 不支持的文件类型
     */
    static Intent getFileViewIntent(Context context, File file) {
        String ext = FileViewerUtils.getExtension(file);
        boolean isDocument = false;
        if (TextUtils.isEmpty(ext)) {
            Toast.makeText(context,"无法识别的文件",Toast.LENGTH_SHORT).show();
            return null;
        }
        Intent intent = null;
        if (".jpg".equalsIgnoreCase(ext) || ".jpeg".equalsIgnoreCase(ext) || ".png".equalsIgnoreCase(ext) || ".gif".equalsIgnoreCase(ext)) {
            intent = getImageFileIntent(file,context);
        } else if (".xls".equalsIgnoreCase(ext) || ".xlsx".equalsIgnoreCase(ext) || ".et".equalsIgnoreCase(ext) || ".ett".equalsIgnoreCase(ext)) {
            intent = getExcelFileIntent(file,context);
            isDocument = true;
        } else if (".doc".equalsIgnoreCase(ext) || ".docx".equalsIgnoreCase(ext) || ".wps".equalsIgnoreCase(ext) || ".wpt".equalsIgnoreCase(ext)) {
            intent = getWordFileIntent(file,context);
            isDocument = true;
        } else if (".ppt".equalsIgnoreCase(ext) || ".pptx".equalsIgnoreCase(ext) || ".dps".equalsIgnoreCase(ext) || ".dpt".equalsIgnoreCase(ext)) {
            intent = getPptFileIntent(file,context);
            isDocument = true;
        } else if (".pdf".equalsIgnoreCase(ext)) {
            intent = getPdfFileIntent(file,context);
            isDocument = true;
        } else if (".htm".equalsIgnoreCase(ext) || ".html".equalsIgnoreCase(ext) || ".jsp".equalsIgnoreCase(ext) || ".css".equalsIgnoreCase(ext)) {
            intent = getHtmlFileIntent(file);
        } else if (".txt".equalsIgnoreCase(ext) || ".text".equalsIgnoreCase(ext)) {
            intent = getHtmlFileIntent(file);
        } else if (".mp3".equalsIgnoreCase(ext) || ".wma".equalsIgnoreCase(ext) || ".aar".equalsIgnoreCase(ext) || ".m4a".equalsIgnoreCase(ext)) {
            intent = getAudioFileIntent(file,context);
        } else if (".mp4".equalsIgnoreCase(ext) || ".avi".equalsIgnoreCase(ext) || ".flv".equalsIgnoreCase(ext)) {
            intent = getVideoFileIntent(file,context);
        }
        if (intent != null) {
            intent.putExtra(IS_DOCUMENT, isDocument);
        }
        return intent;
    }

    /**
     * 跳转到Excel选择
     * @param param
     * @param context
     * @return
     */
    static Intent getExcelFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    static Intent getHtmlFileIntent(File param) {
        Uri uri = Uri.parse(param.getAbsolutePath()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param.getAbsolutePath()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * 跳装到图片选择
     * @param param
     * @return
     */
    static Intent getImageFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 跳转到pdf文件选择
     * @param param
     * @return
     */
    static Intent getPdfFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    static Intent getTextFileIntent(File param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param.getAbsolutePath());
            intent.setDataAndType(uri1, "text/plain");
        }
        return intent;
    }

    /**
     * 跳转到音频选择界面
     * @param param
     * @return
     */
    static Intent getAudioFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",param);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * 跳转到视频选择界面
     * @param param
     * @return
     */
    static Intent getVideoFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    static Intent getChmFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    static Intent getWordFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    static Intent getPptFileIntent(File param,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", param);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    static Intent getApkFileIntent(File fileName,Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", fileName);
        intent.setDataAndType(uri, "application/vnd.android");
        return intent;
    }

    /**
     * 根据手机厂商，跳转到不同手机的权限开启页面
     * @param context
     */
    public static void gotoPermission(Context context) {
        String brand = Build.BRAND;//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(context);
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(context);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(context);
        } else {
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }
    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Context context) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context));
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }

    }
    //跳转到应用详情页面
    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));

        return localIntent;
    }
}
