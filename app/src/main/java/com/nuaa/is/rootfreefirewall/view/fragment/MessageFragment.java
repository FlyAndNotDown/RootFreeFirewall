package com.nuaa.is.rootfreefirewall.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nuaa.is.rootfreefirewall.R;

/**
 * Message木块 Fragment
 */
public class MessageFragment extends Fragment {

    // 默认短信应用
    private String defaultSmsApplication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 请求变更默认短信应用
        this.requestToBeDefaultSmsApplication();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.recoverDefaultSmsApplication();
    }

    // 请求成为默认短信应用
    private void requestToBeDefaultSmsApplication() {
        // Toast 提示
        Toast.makeText(
                getActivity(),
                getString(R.string.toast__change_default_sms_application),
                Toast.LENGTH_LONG
        ).show();

        // 保存原来的默认短信应用信息
        this.defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getActivity());

        // 向用户请求将本应用设置成默认短信应用
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
        startActivity(intent);
    }

    // 还原成原来的短信应用
    private void recoverDefaultSmsApplication() {
        // Toast 提示
        Toast.makeText(
                getActivity(),
                getString(R.string.toast__recover_default_sms_application),
                Toast.LENGTH_LONG
        ).show();

        // 想用户请求将默认短信应用还原成原来的应用
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.defaultSmsApplication);
        startActivity(intent);
    }

}
