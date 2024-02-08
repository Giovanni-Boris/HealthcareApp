package com.example.healthcareapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class NoticeDialogFragment extends DialogFragment {
    public static String TAG = "NoticeDialogFragment";
    private NoticeDialogListener listener;
    private String message = "¿Desea continuar con la operación?";

    public void setListener(NoticeDialogListener noticeDialogListener) {
        listener = noticeDialogListener;
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
        void onDismiss();
        void onCancel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage(message)
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
                .setOnDismissListener(dialog -> {
                    if (listener != null) {
                        listener.onDismiss();
                    }
                })
                .setOnCancelListener(dialogInterface -> {
                    if (listener != null){
                        listener.onCancel();
                    }
                })
                .create();
    }

    public void setMessage(String m){
        message = m;
    }
}