package com.oltranz.opay.utilities.updator.logic;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.oltranz.opay.utilities.updator.apiclient.UpDateConfig;
import com.oltranz.opay.utilities.updator.updatemodels.DeviceApp;

import net.erdfelt.android.apk.AndroidApk;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import okhttp3.ResponseBody;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/15/2017.
 */

public class DirManager {
    private Context context;

    public DirManager(Context context) {
        this.context = context;
    }

    public boolean isUpdateDirAvailable()throws Exception {
        String appName = context.getPackageName().replace(".","/");
        appName += "/updates/";
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(mFile.exists())
            return true;
        else {
            boolean isCreated = mFile.mkdir();
            if(isCreated)
                return true;
            else
                throw new Exception("Directory: "+mFile.getAbsolutePath()+" couldn't be created");
        }
    }

    public boolean isFileAvailable()throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        return files.length > 0;
    }

    public boolean isEmptyUpdateDir()throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        Log.d("UpdateDir", "Update file number is: "+files.length);
        if(files.length > 1){
            try{
                boolean isDeleted = deleteJunk();
                if(isDeleted)
                    Log.d("DeleteJunk", "Junk file are deleted");
                else
                    Log.d("DeleteJunk", "Junk file failed to be deleted");

            }catch (Exception e){
                e.printStackTrace();
                Log.d("DeleteJunk", "Error deleting junk: "+e.getMessage());
            }
        }
        boolean isEmpty = files.length <= 0;
        return isEmpty;
    }

    public void deleteAllFile()throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if(files.length <= 0)
            return;
        int failures = 0;
        int numberOfFiles = files.length;
        for(File f : files){
            if(!f.delete())
                failures ++;
        }

        Log.d("DeleteAll", "Files: "+failures+" out of: "+numberOfFiles+" Failed to be deleted");
    }

    public void deleteOldFile()throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if(files.length <= 0 || files.length == 1)
            return;
        int failures = 0;
        int numberOfFiles = files.length;
        for(int i = 1; i <= files.length; i++){
            File f = files[i];
            if(!f.delete())
                failures ++;
        }

        Log.d("DeleteAll", "Files: "+failures+" out of: "+numberOfFiles+" Failed to be deleted");
    }
    public boolean isUpdateRequired(DeviceApp deviceApp)throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if(files.length <= 0)
            return true;

        File updateFile = files[0];
        AndroidApk apkFile = new AndroidApk(updateFile);
        if(apkFile.getPackageName().equals(deviceApp.getPackageInfo())){
            return !(apkFile.getAppVersionCode().equals(deviceApp.getVersionCode()) &&
                    apkFile.getAppVersion().equals(deviceApp.getVersionName()));
        }else{
            boolean isDeleted = updateFile.delete();
            if(isDeleted)
                throw new Exception("Packages inconsistent, Intrusion deleted");
            else
                throw new Exception("Packages inconsistent, Intrusion not deleted");
        }
    }

    public File getUpdateFile(DeviceApp deviceApp) throws Exception {
        try{
            File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
            if(!mFile.exists())
                if(!isUpdateDirAvailable())
                    throw new Exception("Update directory is missing");

            if(!isUpdateRequired(deviceApp))
                throw new Exception("No update required");

            File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            if(files.length <= 0)
                throw new Exception("No update file found");
            return files[0];
        }catch (Exception e){
            throw new Exception("Error: "+e.getMessage());
        }
    }

    public String getUpdateFilePath(DeviceApp deviceApp) throws Exception {
        try{
            if(!isUpdateRequired(deviceApp))
                throw new Exception("No update required");

            File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
            if(!mFile.exists())
                if(!isUpdateDirAvailable())
                    throw new Exception("Update directory is missing");
            File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            if(files.length <= 0)
                throw new Exception("No update file found");
            return files[0].getAbsolutePath();
        }catch (Exception e){
            throw new Exception("Error: "+e.getMessage());
        }
    }
    
    public boolean deleteJunk()throws Exception {
        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),UpDateConfig.APP_DESC+"_updates");
        if(!mFile.exists())
            if(!isUpdateDirAvailable())
                throw new Exception("Update directory is missing");
        File[] files = mFile.listFiles((FileFilter) FileFileFilter.FILE);
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        if(files.length <= 0 || files.length == 1)
            throw new Exception("No update file found");
        int failures = 0;
        int numberOfFiles = files.length;
        for (int i = 1; i >= numberOfFiles; i++){
            File junkFile = files[i];
            boolean isDeleted = junkFile.delete();
            if(!isDeleted)
                failures ++;
        }

        if(failures > 0)
            throw new Exception("System delete junk exit with errors. "+failures+" files out of "+numberOfFiles+" are not deleted");
        return true;
    }

    public boolean saveTheUpdate(ResponseBody body)throws Exception {
        try {
            File updateFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), UpDateConfig.APP_DESC+"_updates"+ File.separator+"applicationUpdate_"+new Date().getTime()+".apk");
            if(updateFile.exists())
                return true;

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
                outputStream = new FileOutputStream(updateFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();

                double kilobytes = (updateFile.length() / 1024);
                double megabytes = (kilobytes / 1024);

                Log.d("Updater", "File to downloaded: "+megabytes+"Mbs Name: "+updateFile.getAbsolutePath());
                return true;
            } catch (IOException e) {
                throw new Exception(e.getMessage());
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }catch (Exception e){
                    throw new Exception(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

}
