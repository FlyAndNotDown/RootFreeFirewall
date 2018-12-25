package com.nuaa.is.rootfreefirewall.view.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.message.SmsReceiver;
import com.nuaa.is.rootfreefirewall.model.SandboxSms;
import com.nuaa.is.rootfreefirewall.view.adapter.MessageSandboxAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Message木块 Fragment
 */
public class MessageFragment extends Fragment {

    // Action
    private static final String ACTION_SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";

    // 默认短信应用
    private String defaultSmsApplication;
    // SmsReceiver
    private SmsReceiver smsReceiver;

    // UI 组件
    private Button updateMessageAbortDatabaseButton;
    private Button startMessageAbortServiceButton;
    private ListView messageSandboxListView;

    // 沙箱短信信息
    private List<SandboxSms> sandboxSmsList;
    // 适配器
    private MessageSandboxAdapter messageSandboxAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化数据
        this.initDatas();
        // 请求变更默认短信应用
        this.requestToBeDefaultSmsApplication();
        // 注册广播接收器
        this.registerBroadcastReceiver();
        // 获取 UI 组件
        this.getUIComponent();
        // 设置适配器
        this.setAdapter();
        // 添加组件监听事件
        this.addComponentListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.recoverDefaultSmsApplication();
    }

    // 初始化数据
    private void initDatas() {
        this.sandboxSmsList = new ArrayList<>();
    }

    // 获取 UI 组件
    private void getUIComponent() {
        this.updateMessageAbortDatabaseButton = getActivity().findViewById(R.id.fragment_message__update_message_abort_database_button);
        this.startMessageAbortServiceButton = getActivity().findViewById(R.id.fragment_message__start_message_abort_service_button);
        this.messageSandboxListView = getView().findViewById(R.id.fragment_message__message_sandbox_list_view);
    }

    // 添加组件监听事件
    private void addComponentListener() {

    }

    // 设置适配器
    private void setAdapter() {
        this.messageSandboxAdapter = new MessageSandboxAdapter(getActivity());
        this.messageSandboxListView.setAdapter(this.messageSandboxAdapter);
        this.messageSandboxAdapter.setDatas(this.sandboxSmsList);
    }

    // 注册广播接收器
    private void registerBroadcastReceiver() {
        this.smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageFragment.ACTION_SMS_DELIVER);
        getActivity().registerReceiver(this.smsReceiver, intentFilter);
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
