/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.fmradio.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;

import com.android.fmradio.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Edit favorite station name and frequency, caller should implement
 * EditFavoriteListener
 */
public class FmFavoriteEditDialog extends DialogFragment {
    private static final String STATION_NAME = "station_name";
    private static final String STATION_FREQ = "station_freq";
    private EditFavoriteListener mListener = null;
    private EditText mStationNameEditor = null;

    /**
     * Create edit favorite dialog instance, caller should implement edit
     * favorite listener
     *
     * @param stationName The station name
     * @param stationFreq The station frequency
     * @return edit favorite dialog
     */
    public static FmFavoriteEditDialog newInstance(String stationName, int stationFreq) {
        FmFavoriteEditDialog fragment = new FmFavoriteEditDialog();
        Bundle args = new Bundle(2);
        args.putString(STATION_NAME, stationName);
        args.putInt(STATION_FREQ, stationFreq);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Edit favorite listener
     */
    public interface EditFavoriteListener {
        /**
         * Edit favorite station name and station frequency
         */
        void editFavorite(int stationFreq, String name);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EditFavoriteListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String stationName = getArguments().getString(STATION_NAME);
        final int stationFreq = getArguments().getInt(STATION_FREQ);
        View v = View.inflate(getActivity(), R.layout.editstation, null);
        mStationNameEditor = (EditText) v.findViewById(
                R.id.dlg_edit_station_name_text);

        if (null == stationName || "".equals(stationName.trim())) {
            stationName = "";
        }

        mStationNameEditor.requestFocus();
        mStationNameEditor.requestFocusFromTouch();
        // Edit
        mStationNameEditor.setText(stationName);
        Editable text = mStationNameEditor.getText();
        Selection.setSelection(text, text.length());
        return new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.rename)
                .setView(v)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String newName;
                    if (mStationNameEditor.getText() == null) { newName = ""; } else { newName = mStationNameEditor.getText().toString().trim(); }
                    mListener.editFavorite(stationFreq, newName);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

}
