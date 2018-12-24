package com.nuaa.is.rootfreefirewall.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Telephony;

import com.nuaa.is.rootfreefirewall.R;

/**
 * Message木块 Fragment
 */
public class MessageFragment extends Fragment {

    private String defaultSmsApplication;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        this.requestToBeDefaultSmsApplication();
    }

    @Override
    public void onDestroy() {

    }

    // 请求成为默认短信应用
    private void requestToBeDefaultSmsApplication() {
        // 查询当前的默认短信应用并且将报名存储起来
        this.defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getActivity());
        // 向用户请求将本应用设置成默认短信应用
        Intent intent = new Intent(getActivity(), Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
        startActivity(intent);
    }

    // 还原成原来的短信应用
    private void recoverDefaultSmsApplication() {
        Intent intent = new Intent(getActivity(), Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, this.defaultSmsApplication);
        startActivity(intent);
    }

}
