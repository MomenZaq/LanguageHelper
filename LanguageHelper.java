package yallashoot.shoot.yalla.com.yallashoot.newapp.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.core.view.ViewCompat;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import yallashoot.shoot.yalla.com.yallashoot.newapp.core.repositoryHelper.SharedPreferencesHelper;
import yallashoot.shoot.yalla.com.yallashoot.newapp.screens.main.MainActivity;

@Singleton
public class LanguageHelper {
    Context context;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    public LanguageHelper(Context context, SharedPreferencesHelper sharedPreferencesHelper) {
        this.context = context;
        this.sharedPreferencesHelper = sharedPreferencesHelper;
    }

    public void setDirection(View v) {

        int direction = View.LAYOUT_DIRECTION_LTR;

        if (isRTL()) {
            direction = View.LAYOUT_DIRECTION_RTL;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            v.setLayoutDirection(direction);
        } else {
            ViewCompat.setLayoutDirection(v, direction);
        }


    }

    public Context wrapContext(Context context) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        String language = getCurrentLocal();

        Locale locale = new Locale(language);
        Locale.setDefault(locale);


        //Update your config with the Locale i. e. saved in SharedPreferences
        Configuration config = context.getResources().getConfiguration();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocale(locale);
        config.setLayoutDirection(locale);
//            return context.createConfigurationContext(config);
//        } else {

        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(locale);
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return context.createConfigurationContext(config);
//        }


//        }
//        return null;

    }

    public void overrideLocal(Context context) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            String language = getCurrentLocal();

            Locale locale = new Locale(language);
            Locale.setDefault(locale);


            //Update your config with the Locale i. e. saved in SharedPreferences
            Configuration config = context.getResources().getConfiguration();
            config.setLocale(new Locale(language));


            //Update your config with the Locale i. e. saved in SharedPreferences
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
//            } else {
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLayoutDirection(locale);
            }
//            }


            // override the locale on the given context (Activity, Fragment, etc...)
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

            // override the locale on the application context
            if (context != context.getApplicationContext()) {
                context.getApplicationContext().getResources().updateConfiguration(config, context.getApplicationContext().getResources().getDisplayMetrics());
            }
        } catch (Exception e) {
        }


//        }
//        return null;

    }

    public void setNewLanguage(String currentLanguage, Activity activity) {
        if (sharedPreferencesHelper.getSelectedLanguage().equals(currentLanguage) ||
                (sharedPreferencesHelper.getSelectedLanguage().equals("") && getSystemLanguage().equals(currentLanguage))) {
//        same language
            return;
        }

        sharedPreferencesHelper.setSelectedLanguage(currentLanguage);
        try {
            restartApplication(activity);
        } catch (Exception e) {

            try {

                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(context,
                        android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                context.startActivity(intent, bundle);
            } catch (Exception e2) {
            }
        }

    }

    public void restartApplication(final Activity activity) {


        // Systems at 29/Q and later don't allow relaunch, but System.exit(0) on
        // all supported systems will relaunch ... but by killing the process, then
        // restarting the process with the back stack intact. We must make sure that
        // the launch activity is the only thing in the back stack before exiting.
        final PackageManager pm = activity.getPackageManager();
        final Intent intent = pm.getLaunchIntentForPackage(activity.getPackageName());
        activity.finishAffinity(); // Finishes all activities.
        activity.startActivity(intent);    // Start the launch activity
        System.exit(0);    // System finishes and automatically relaunches us.

    }

    public boolean isRTL() {
        if (sharedPreferencesHelper.getSelectedLanguage().equals("")) {

            return getSystemLanguage().equals("ar");

        } else {
            return sharedPreferencesHelper.getSelectedLanguage().equals("ar");
        }
    }

    public String getCurrentLocal() {
        return isRTL() ? "ar" : "en";
    }

    public String getSystemLanguage() {
        return ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0).getLanguage();


    }

}
