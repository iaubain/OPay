package com.oltranz.opay.utilities.updator.updtservice;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.oltranz.opay.utilities.updator.loaders.UpdateApiLoader;
import com.oltranz.opay.utilities.updator.logic.DeviceProvider;
import com.oltranz.opay.utilities.updator.logic.DirManager;
import com.oltranz.opay.utilities.updator.updatemodels.TokenModel;

import java.io.File;
import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class UpdateService extends IntentService implements UpdateApiLoader.OnUpdateApiLoader {
    public static final String ACTION_CHECK_UPDATES = "com.oltranz.pf.n_payfuel_engen.utilities.updator.UpdateService.action.ACTION_CHECK_UPDATES";
    public static final String ACTION_TRIGGER = "com.oltranz.pf.n_payfuel_engen.utilities.updator.UpdateService.action.TRIGGER_UPDATES";

    public UpdateService() {
        super("UpdateService");
    }
    private UpdateApiLoader updateApiLoader;
    private DirManager dirManager;

    private boolean isUpdateDialogShowing = false;
    private Dialog dialog;

    private Thread.UncaughtExceptionHandler onRuntimeError= new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            //Try starting the Activity again
            Log.e("Error", "Thread: "+thread.getName()+" and error: "+ex.getMessage() );
        }};
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startCheckUpdateAction(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_CHECK_UPDATES);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHECK_UPDATES.equals(action)) {
                handleCheckUpdates();
            }
        }
    }

    /**
     * Handle action handleCheckUpdates in the provided background thread with the provided
     * parameters.
     */
    private void handleCheckUpdates() {
        Thread.setDefaultUncaughtExceptionHandler(onRuntimeError);
        updateApiLoader = new UpdateApiLoader(UpdateService.this, UpdateService.this);
        dirManager = new DirManager(UpdateService.this);

        //ToDO Remember to remove this toast
        //Toast.makeText(UpdateService.this, "Current application version code: "+ BuildConfig.VERSION_CODE, Toast.LENGTH_SHORT).show();

        updateApiLoader.checkUpdate(new DeviceProvider(UpdateService.this).genDevice());
    }

    @Override
    public void onCheckUpdate(boolean isDone, Object object) {
        if(!isDone){
            Log.d("CheckUpdate", (String) object);
            return;
        }
        try {
            TokenModel tokenModel = (TokenModel) object;
            if(!dirManager.isEmptyUpdateDir()){
               try {
                   dirManager.deleteOldFile();
                   if(!dirManager.isUpdateRequired(new DeviceProvider(UpdateService.this).genDevice())){
                       updateApiLoader.downloadUpdates(tokenModel);
                   }else{
                       //trigger update
                       triggerUpdate();
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }else{
                updateApiLoader.downloadUpdates(tokenModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadUpdate(boolean isDone, Object object) {
        if(!isDone){
            Log.d("Download", (String) object);
            return;
        }
        try {
            if(!dirManager.isFileAvailable()){
                Log.d("LocalFile", "Local update apk file is missing");
                return;
            }
            if(!dirManager.isUpdateRequired(new DeviceProvider(UpdateService.this).genDevice())){
                Log.d("LocalFile", "No update is required");
                try {
                    dirManager.deleteAllFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
            //trigger auto update
            triggerUpdate();
        }catch (Exception e){
            Log.d("LocalFile", e.getMessage());
        }
    }

    private void triggerUpdate() throws IOException {
        try{
            if(isUpdateDialogShowing){
                Log.d("Updater", "Update dialog is showing");
                return;
            }
            File updateFile = dirManager.getUpdateFile(new DeviceProvider(UpdateService.this).genDevice());
            if(updateFile.length() <= 0){
                Log.d("LocalFile", "Bad update file");
                return;
            }
            double kilobytes = (updateFile.length() / 1024);
            double megabytes = (kilobytes / 1024);
            Log.d("Updater", "Updating file with: "+megabytes+"Mbs from path: "+updateFile.getAbsolutePath());

            try {
                if(!dirManager.isUpdateRequired(new DeviceProvider(UpdateService.this).genDevice())){
                    Log.d("Updater", "No update required");
                    return;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(ACTION_TRIGGER);
            intent.setAction(ACTION_TRIGGER);
            LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(intent);

            isUpdateDialogShowing = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
