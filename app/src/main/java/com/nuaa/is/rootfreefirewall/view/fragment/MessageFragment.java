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

    // request code
    private static final int REQUEST_CODE__CHANGE_DEFAULT_SMS_APPLICATION = 0xf5;

    // 是否是默认短信应用
    private boolean isDefaultSmsApplication;

    // 默认短信应用
    private String defaultSmsApplication;
    // SmsReceiver
    private SmsReceiver smsReceiver;

    // UI 组件
    private Button updateMessageAbortDatabaseButton;
    private Button startMessageAbortServiceButton;
    private ListView messageSandboxListView;

    // 按钮 enable 状态
    private boolean updateMessageAbortDatabaseButtonEnable = true;
    private boolean startMessageAbortServiceButtonEnable = true;

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

    @Override
    public void onResume() {
        super.onResume();

//        // 更新按钮 enable 状态
        this.updateButtonEnableStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 根据 REQUEST_CODE 进行判断
        switch (requestCode) {
            case REQUEST_CODE__CHANGE_DEFAULT_SMS_APPLICATION:
                if (resultCode == getActivity().RESULT_OK) {
                    this.isDefaultSmsApplication = true;
                    // 注册广播接收器
                    registerBroadcastReceiver();
                    // 改变按钮 enable 状态，改变文字
                    startMessageAbortServiceButtonEnable = false;
                    startMessageAbortServiceButton.setText(R.string.fragment_message__start_message_abort_service_button__disabled_text);
                } else {
                    this.isDefaultSmsApplication = false;
                    // 改变按钮 enable 状态
                    startMessageAbortServiceButtonEnable = true;
                }
                // 更新按钮状态
                this.updateButtonEnableStatus();
                break;
            default:
                break;
        }
    }

    // 更新按钮状态
    private void updateButtonEnableStatus() {
        this.startMessageAbortServiceButton.setEnabled(this.startMessageAbortServiceButtonEnable);
        this.updateMessageAbortDatabaseButton.setEnabled(this.updateMessageAbortDatabaseButtonEnable);
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
        // 开启短信拦截服务按钮
        this.startMessageAbortServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开启服务
                startMessageAbortService();
                // 改变 enable 状态
                startMessageAbortServiceButtonEnable = false;
                updateButtonEnableStatus();
            }
        });
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

    // 开启短信拦截服务
    private void startMessageAbortService() {
        // Toast 提示
        Toast.makeText(
                getActivity(),
                getString(R.string.toast__change_default_sms_application),
                Toast.LENGTH_LONG
        ).show();

        // 保存原来的默认短信应用信息
        this.defaultSmsApplication = Telephony.Sms.getDefaultSmsPackage(getActivity());
        // 更新状态
        this.isDefaultSmsApplication = this.defaultSmsApplication.equals(getActivity().getPackageName());

        if (this.isDefaultSmsApplication) {
            // 如果已经是默认应用了
            this.onActivityResult(REQUEST_CODE__CHANGE_DEFAULT_SMS_APPLICATION, getActivity().RESULT_OK, new Intent());
        } else {
            // 如果不是默认应用，向用户请求将本应用设置成默认短信应用
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getActivity().getPackageName());
            startActivityForResult(intent, REQUEST_CODE__CHANGE_DEFAULT_SMS_APPLICATION);
        }
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
