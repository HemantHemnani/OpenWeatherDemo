package com.weatherapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.weatherapp.databinding.DialogOkCancelBinding;

public class DialogUtil {




    public static Dialog showLoader(Context context/*, boolean textStatus, int currentDoc, int totalDoc*/) {
        final Dialog networkDialogLoader = new Dialog(context, R.style.AppTheme);
        networkDialogLoader.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        networkDialogLoader.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String fromTime = "";
        networkDialogLoader.setContentView(R.layout.progress_loader);
        networkDialogLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        networkDialogLoader.setCancelable(false);
        networkDialogLoader.setCanceledOnTouchOutside(false);


        networkDialogLoader.show();
        return networkDialogLoader;

    }


    public static void hideLoader(final Dialog dialog) {


        dialog.dismiss();
    }





    /*
     * ok/cancel dialog
     * */
    public static Dialog okcancelDialog(Context context, int icon, String title, String msg, String okMessage, String cancelMessage,
                                        boolean isOk, boolean isCancel, selectOkCancelListener listener)
    {
        Dialog dialog = new Dialog(context);

//        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogOkCancelBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_ok_cancel,
                null, false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.setContentView(binding.getRoot());

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if(!TextUtils.isEmpty(msg)) {
            dialog.show();
        }else
        {
            dialog.dismiss();
        }

//        if (icon != 0) {
//            binding.ivIcon.setImageResource(icon);
//        } else {
//            binding.ivIcon.setVisibility(View.GONE);
//        }


        if (TextUtils.isEmpty(title)) {
            binding.tvTitle.setVisibility(View.GONE);
        } else {
            binding.tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            binding.tvMessage.setText(msg);
        }


        if (!TextUtils.isEmpty(okMessage)) {
            binding.btnOk.setText(okMessage);
        }
        if (!TextUtils.isEmpty(cancelMessage)) {
            binding.btnCancel.setText(cancelMessage);
        }

        if (isOk == false) {
            binding.btnOk.setVisibility(View.GONE);
            binding.viewSeparator.setVisibility(View.GONE);
        }

        if (isCancel == false) {
            binding.viewSeparator.setVisibility(View.GONE);
            binding.btnCancel.setVisibility(View.GONE);
        }

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.okListener();
                dialog.dismiss();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelListener();
                dialog.dismiss();
            }
        });

        return dialog;
    }


    /*
     * ok cnacel interface listener
     * */
    public interface selectOkCancelListener {
        public void okListener();

        public void cancelListener();
    }
}
