package com.example.healthcareapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.healthcareapp.R;


public class NoticeDialogFragment extends DialogFragment {
    public static String TAG = "NoticeDialogFragment";
    private NoticeDialogListener listener;

    public void setListener(NoticeDialogListener noticeDialogListener) {
        listener = noticeDialogListener;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage("¿Desea continuar con la operación?")
                .setPositiveButton("Continuar", (dialog, which) -> {
                    if (listener != null) {
                        listener.onDialogPositiveClick();
                    }
                } )
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    if (listener != null) {
                        listener.onDialogNegativeClick();
                    }
                })
                .create();
    }

}