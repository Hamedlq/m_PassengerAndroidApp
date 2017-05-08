package com.mibarim.main.receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.mibarim.main.R;
import com.mibarim.main.core.Constants;
import com.mibarim.main.models.NotificationModel;
import com.mibarim.main.services.RouteRequestService;
import com.mibarim.main.ui.activities.MainActivity;
import com.mibarim.main.util.DynamicJsonConverter;
import com.mibarim.main.util.SafeAsyncTask;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 5/7/2016.
 */
public class PeriodicReceiver extends BroadcastReceiver {
    final static String TAG = "PeriodicReceiver";
    final static String ROUTEACTIVITY = "com.mibarim.main.activities.RouteActivity";
    private NotificationModel res;
    private String Mobile;
    private Context _context;

//    @Inject
//    RouteRequestService routeRequestService;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            _context = context;
            Log.d(TAG, "period got");
            SharedPreferences prefs = context.getSharedPreferences(
                    "com.mibarim.main", Context.MODE_PRIVATE);
            Mobile = prefs.getString("UserMobile", "");
//            Log.d(TAG, "mobile"+Mobile);
//            if (CheckNetworkConnection.isNetworkAvailable(_context)) {
            getNotifies();
//            }
            Log.d(TAG, "check service");

        } catch (Exception e) {
            Log.e(TAG, "Something Bad Happened Exception", e);
        }
    }

    private void getNotifies() {
        new SafeAsyncTask<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                RestAdapter rest = new RestAdapter.Builder()
                        .setEndpoint(Constants.Http.URL_BASE)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setConverter(new DynamicJsonConverter())
                        .build();
                RouteRequestService routeRequestService = new RouteRequestService(rest);
                res = routeRequestService.notify(Mobile);
                return true;
            }


            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                Log.d(TAG, "Exception" + e.toString());
            }

            @Override
            protected void onSuccess(final Boolean state) throws Exception {
                super.onSuccess(state);
                showNotification(res);
            }
        }.execute();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(NotificationModel res) {
        Log.d(TAG, "here route "+res.IsNewRouteSuggest+" msg "+res.IsNewMessage);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (res.IsNewRouteSuggest) {
            NotificationManager notificationManager = (NotificationManager) _context.getSystemService(_context.NOTIFICATION_SERVICE);
            Intent routeActivityIntent = new Intent(_context, MainActivity.class);
            routeActivityIntent.putExtra("theRouteId", res.SuggestRouteRequestId.intValue());
            PendingIntent displayIntent = PendingIntent.getActivity(
                    _context, res.SuggestRouteRequestId.intValue(),
                    routeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Log.d(TAG, res.SuggestRouteRequestId.toString());
            //set notification
            Notification notification;
            notification = new Notification.Builder(_context)
                    .setContentTitle("پیشنهاد جدید")
                    .setContentText("شما هم مسیر جدیدی در پیشنهادات خود دارید")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(displayIntent)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .build();
            //---sets the notification to trigger---
            notificationManager.notify(res.SuggestRouteRequestId.intValue(), notification);
            Log.d(TAG, "notif sent");
        }
        if (res.IsNewMessage) {
            NotificationManager notificationManager = (NotificationManager) _context.getSystemService(_context.NOTIFICATION_SERVICE);
            Intent routeActivityIntent = new Intent(_context, MainActivity.class);
            routeActivityIntent.putExtra("theRouteId", res.MessageRouteRequestId.intValue());
            PendingIntent displayIntent = PendingIntent.getActivity(
                    _context, res.MessageRouteRequestId.intValue(),
                    routeActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //set notification
            Notification notification;
            notification = new Notification.Builder(_context)
                    .setContentTitle("پیغام جدید")
                    .setContentText("پیغام جدیدی در گروه هم مسیری شما ارسال شده است")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(displayIntent)
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .build();
            //---sets the notification to trigger---
            notificationManager.notify(res.MessageRouteRequestId.intValue(), notification);
            Log.d(TAG, "notif sent");

        }
    }

}