/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.coffeeshot.toolbox.level;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Holder for Settings, About, etc.
 */
public class SettingsFragment extends PreferenceFragment
{

  private static final String TAG = "SettingsFragment";

  private static final String KEY_TYPE = "type";

  private static final String KEY_VERSION = "version";
  private static final String KEY_OPEN_SOURCE = "open_source";
  private static final String KEY_SITE_LINK = "website";

  public static SettingsFragment newInstance(@SettingsActivity.SettingsType int type)
  {
    Bundle args = new Bundle();
    args.putInt(KEY_TYPE, type);
    SettingsFragment fragment = new SettingsFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public SettingsFragment()
  {
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    int type = getArguments().getInt(KEY_TYPE);
    if (type == SettingsActivity.TYPE_ABOUT)
    {
      addPreferencesFromResource(R.xml.about);

      Preference licensePreference = findPreference(KEY_OPEN_SOURCE);
      licensePreference.setIntent(new Intent(getActivity().getApplicationContext(),
          LicenseActivity.class));

      Preference linkPreference = findPreference(KEY_SITE_LINK);
      linkPreference.setIntent(new Intent(Intent.ACTION_VIEW).setData(
          Uri.parse(getString(R.string.website_url))));

      loadVersion(getActivity());
    }
    else if (type == SettingsActivity.TYPE_SETTINGS)
    {
      addPreferencesFromResource(R.xml.settings);
    }
    else
    {
      throw new IllegalStateException("SettingsFragment type " + type + " is unknown.");
    }
  }

  @Override
  public void onResume()
  {
    super.onResume();
  }

  private void loadVersion(Context context)
  {
    PackageManager pm = context.getPackageManager();

    PackageInfo info;
    String versionName;
    try
    {
      info = pm.getPackageInfo(getActivity().getPackageName(), 0);
      versionName = info.versionName;
    }
    catch (PackageManager.NameNotFoundException e)
    {
      Log.e(TAG, "Could not load package info.", e);
      versionName = "";
    }

    Preference version = findPreference(KEY_VERSION);
    version.setSummary(versionName);
  }
}
