package com.texasgamer.openvrnotif;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

public class PreferencesActivity extends AppCompatPreferenceActivity {

    private FirebaseAnalytics firebaseAnalytics;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else if (preference instanceof RingtonePreference) {
                if (TextUtils.isEmpty(stringValue)) {
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        preference.setSummary(null);
                    } else {
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.action_settings));
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || AboutPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            ((PreferencesActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_general);
            ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_general_prefs), null);

            EditTextPreference p = (EditTextPreference) findPreference(getString(R.string.pref_device_name));
            bindPreferenceSummaryToValue(p);

            if(p.getTitle().toString().equals(getString(R.string.pref_default_device_name))
                    || p.getTitle().toString().isEmpty()) {
                ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_event_empty_device_name), null);
                p.setText(Build.MANUFACTURER + " " + Build.MODEL);
            }

            p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Bundle b = new Bundle();
                    String newValueStr = newValue.toString().equals("true") ? "enabled" : "disabled";
                    b.putString(getString(R.string.analytics_param_new_value), newValueStr);
                    ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_device_name), b);
                    return false;
                }
            });

            findPreference(getString(R.string.pref_start_on_boot)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Bundle b = new Bundle();
                    String newValueStr = newValue.toString().equals("true") ? "enabled" : "disabled";
                    b.putString(getString(R.string.analytics_param_new_value), newValueStr);
                    ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_start_on_boot), b);
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            ((PreferencesActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_notifications);
            ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_notif_prefs), null);

            populatePreferences();
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_pref_notifications, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                return true;
            } else if(id == R.id.enable_all) {
                PreferenceScreen screen = getPreferenceScreen();

                Bundle b = new Bundle();
                b.putLong(getString(R.string.analytics_param_num_apps), screen.getPreferenceCount());
                ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_enable_all), b);

                for(int x = 0; x < screen.getPreferenceCount(); x++) {
                    ((CheckBoxPreference) screen.getPreference(x)).setChecked(true);
                }
            } else if(id == R.id.disable_all) {
                PreferenceScreen screen = getPreferenceScreen();

                Bundle b = new Bundle();
                b.putLong(getString(R.string.analytics_param_num_apps), screen.getPreferenceCount());
                ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_disable_all), b);

                for(int x = 0; x < screen.getPreferenceCount(); x++) {
                    ((CheckBoxPreference) screen.getPreference(x)).setChecked(false);
                }
            }

            return super.onOptionsItemSelected(item);
        }

        private void populatePreferences() {
            PackageManager packageManager = getActivity().getPackageManager();
            List<PackageInfo> allInstalledPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
            PreferenceScreen screen = getPreferenceScreen();
            for(PackageInfo p : allInstalledPackages) {
                CheckBoxPreference pref = new CheckBoxPreference(screen.getContext());
                pref.setKey(getString(R.string.pref_app_notif_base) + "-" + p.packageName);
                pref.setTitle(packageManager.getApplicationLabel(p.applicationInfo));
                pref.setIcon(packageManager.getApplicationIcon(p.applicationInfo));
                pref.setChecked(PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getBoolean(getString(R.string.pref_app_notif_base) + "-" + p.packageName, true));
                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange (Preference preference, Object newValue) {
                        Bundle b = new Bundle();
                        b.putString(getString(R.string.analytics_param_app_name), preference.getTitle().toString());
                        String newValueStr = newValue.toString().equals("true") ? "enabled" : "disabled";
                        b.putString(getString(R.string.analytics_param_new_value), newValueStr);
                        ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_app), b);
                        return true;
                    }
                });
                screen.addPreference(pref);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            setHasOptionsMenu(true);

            ((PreferencesActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_about);
            ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_about_prefs), null);

            try {
                findPreference(getString(R.string.pref_version)).setTitle(getString(R.string.pref_title_version,
                        getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName));
                Preference licenses = findPreference(getString(R.string.pref_licenses));
                licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        ((PreferencesActivity) getActivity()).firebaseAnalytics.logEvent(getString(R.string.analytics_tap_licenses), null);

                        final Dialog licenseDialog = new Dialog(getActivity());
                        licenseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        licenseDialog.setContentView(R.layout.dialog_licenses);
                        WindowManager.LayoutParams licenseParams = licenseDialog.getWindow().getAttributes();
                        licenseParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        licenseDialog.getWindow().setAttributes(licenseParams);
                        licenseDialog.show();

                        WebView licenseWebView = (WebView) licenseDialog.findViewById(R.id.licenseWebView);
                        licenseWebView.loadUrl("file:///android_asset/open_source_licenses.html");
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
