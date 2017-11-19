package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by asd on 2017/11/11.
 */

public class AppInfoParser {
    public static List<AppInfo>getAppInfos(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS + PackageManager.GET_SIGNATURES+PackageManager.GET_ACTIVITIES);
        List<AppInfo> appinfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo : packInfos) {
            AppInfo appinfo = new AppInfo();
            String packname = packInfo.packageName;
            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.appSize = appSize;
            String version = packInfo.versionName;
            appinfo.version = version;
            appinfo.InstallTime = new Date(packInfo.firstInstallTime).toLocaleString();
            try {
                //PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
                byte[] ss = packInfo.signatures[0].toByteArray();
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(
                        new ByteArrayInputStream(ss));
                if (cert!=null){
                    appinfo.signature=cert.getIssuerDN().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //PackageInfo packinfo1 = null;
            /*try {
                //PackageInfo packinfo1 = pm.getPackageInfo(packname, PackageManager.GET_PERMISSIONS);
                if (packInfo.requestedPermissions!=null){
                    for (String pio : packInfo.requestedPermissions){
                        appinfo.permissions= appinfo.permissions+pio+"\n";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            StringBuilder sb = new StringBuilder();
            if (packInfo.requestedPermissions != null) {
                for (String per:packInfo.requestedPermissions) {
                    sb.append(per + "\n");
                }
                appinfo.permissions = sb.toString();
            }
           //获取活动
            appinfo.appActivities = Arrays.toString(packInfo.activities);

            int flags = packInfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) !=0) {
                appinfo.isInRoom=false;
            }else{
                appinfo.isInRoom=true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM&flags)!=0){
                appinfo.isUserApp=false;
            }else {

                appinfo.isUserApp=true;
            }
            appinfos.add(appinfo);
            appinfo=null;
        }
        return appinfos;
    }
}
