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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.message.SmsReceiver;
import com.nuaa.is.rootfreefirewall.model.Phone;
import com.nuaa.is.rootfreefirewall.model.SandboxSms;
import com.nuaa.is.rootfreefirewall.util.SmsAbortDatabaseUtil;
import com.nuaa.is.rootfreefirewall.view.RFFApplication;
import com.nuaa.is.rootfreefirewall.view.adapter.MessageSandboxAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    // 短信拦截数据库
    private List<Phone> phones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 载入短信拦截数据库
        this.loadSmsAbortDatabase();
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
        this.saveSmsAbortDatabase();
        this.recoverDefaultSmsApplication();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 更新按钮 enable 状态
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

    // 载入短信拦截数据库
    private void loadSmsAbortDatabase() {
        this.phones = SmsAbortDatabaseUtil.getPhoneList(getActivity());
    }

    // 存储短信拦截数据库
    private void saveSmsAbortDatabase() {
        SmsAbortDatabaseUtil.save(getActivity(), this.phones);
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
                updateMessageAbortDatabaseButtonEnable = false;
                updateButtonEnableStatus();
            }
        });

        // 更新短信拦截数据库按钮
        this.updateMessageAbortDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新数据库
                startMessageAbortServiceButtonEnable = false;
                updateMessageAbortDatabaseButtonEnable = false;
                updateButtonEnableStatus();

                // 开始更新
                SmsAbortDatabaseUtil.getAbortDatabaseByNetwork(getActivity(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startMessageAbortServiceButtonEnable = true;
                                updateMessageAbortDatabaseButtonEnable = true;
                                updateButtonEnableStatus();

                                // 提示
                                Toast.makeText(getActivity(), "更新失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 建立JSON对象
                        JSONObject jsonObject = JSON.parseObject(response.body().string());

                        // 拆分
                        List<Phone> networkPhones = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("numbers");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);
                            networkPhones.add(new Phone(
                                    object.getString("number"),
                                    object.getInteger("blockNum")
                            ));
                        }

                        // 合并两者
                        for (int i = 0; i < networkPhones.size(); i++) {
                            boolean found = false;
                            for (int j = 0; j < phones.size(); j++) {
                                if (networkPhones.get(i).getNumber().equals(phones.get(j).getNumber())) {
                                    found = true;
                                    if (networkPhones.get(i).getBlockNum() != phones.get(j).getBlockNum()) {
                                        phones.get(j).setBlockNum(networkPhones.get(i).getBlockNum());
                                    }
                                }
                            }
                            if (!found) {
                                phones.add(new Phone(
                                        networkPhones.get(i).getNumber(),
                                        networkPhones.get(i).getBlockNum()
                                ));
                            }
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startMessageAbortServiceButtonEnable = true;
                                updateMessageAbortDatabaseButtonEnable = true;
                                updateButtonEnableStatus();

                                // 提示
                                Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
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
        RFFApplication rffApplication = (RFFApplication) getActivity().getApplication();
        rffApplication.setPhones(this.phones);
        rffApplication.setMessageSandboxAdapter(this.messageSandboxAdapter);
        rffApplication.setSandboxSmsList(this.sandboxSmsList);
        rffApplication.setWarningNum(20);
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
