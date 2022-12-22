package com.example.myfirstapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.RemoteViews;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Widget extends AppWidgetProvider {
    static final String ACTION_OHAB = "com.example.myfirstapp.Widget.ACTION_OHAB";
    static final String ACTION_SPOTIFY = "android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION";  //google clock alarm sin intent som starter spotify avspilling //todo finn ut av alarmen sin ".action.TIME_TICK"

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);


        //todo loop som fikser dette gjennom valgte preferences fra WidgetSettings
        views.setOnClickPendingIntent(R.id.kitchenUP, pendingIntentCreator(context, "Kitchen", "100", 1));
        views.setOnClickPendingIntent(R.id.kitchenDOWN, pendingIntentCreator(context, "Kitchen", "0", 2));
        views.setOnClickPendingIntent(R.id.stueUP, pendingIntentCreator(context, "Stue", "100", 3));
        views.setOnClickPendingIntent(R.id.stueDOWN, pendingIntentCreator(context, "Stue", "0", 4));
        views.setOnClickPendingIntent(R.id.sovUP, pendingIntentCreator(context, "Soverom", "100", 5));
        views.setOnClickPendingIntent(R.id.sovDOWN, pendingIntentCreator(context, "Soverom", "0", 6));

        views.setOnClickPendingIntent(R.id.allONOFF, pendingIntentCreator(context, "dimtime", "1", 0)); //todo
        views.setOnClickPendingIntent(R.id.allUP, pendingIntentCreator(context, "dimtime", "1", 0)); //todo
        views.setOnClickPendingIntent(R.id.allDOWN, pendingIntentCreator(context, "dimtime", "1", 0)); //todo

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    //todo: status updates på onclick? (openhab returnerer status:x uansett request.)
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_SPOTIFY)){
            OpenHabControl ohab = new OpenHabControl(context.getCacheDir());
            ohab.itemControl("Stue", "1");
            ohab.itemControl("Soverom", "20");
        }

        Log.i("hansiintent", intent.getAction());
        if (intent.getAction().equals(ACTION_OHAB)) {
            if(intent.getStringExtra("itemName").equals("dimtime")){
                OpenHabControl ohab = new OpenHabControl(context.getCacheDir());
                ohab.itemControl("Stue", "1");
                ohab.itemControl("Soverom", "20");
            }else{
                Log.i("ACTION_OHAB.extras", intent.getStringExtra("itemName") + " + " + intent.getStringExtra("body"));
                OpenHabControl ohab = new OpenHabControl(context.getCacheDir());
                ohab.itemControl(intent.getStringExtra("itemName"), intent.getStringExtra("body"));
            }
            }


    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    static PendingIntent pendingIntentCreator(Context context, String itemName, String body, int reqCode){
        Intent intent = new Intent(context, Widget.class);
        intent.setAction(ACTION_OHAB);    //todo arg
        intent.putExtra("itemName", itemName);
        intent.putExtra("body", body);
        Log.i("ACTION_OHAB.extrasTEST", intent.getStringExtra("itemName") + " + " + intent.getStringExtra("body"));
        return PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_IMMUTABLE);
}


}