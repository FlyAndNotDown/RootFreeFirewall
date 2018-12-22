package com.nuaa.is.rootfreefirewall.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.model.AppInfo;
import com.nuaa.is.rootfreefirewall.service.FirewallVpnService;
import com.nuaa.is.rootfreefirewall.util.AppUtil;
import com.nuaa.is.rootfreefirewall.view.activity.NetworkConfigActivity;
import com.nuaa.is.rootfreefirewall.view.adapter.AppListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Network模块 Fragment
 */
public class NetworkFragment extends Fragment {

    // TAG
    private static final String TAG = "RFF-NetworkFragment";

    // Service & Activity 请求码
    private static final int REQUEST_CODE__FIREWALL_VPN_SERVICE = 0xf0;
    private static final int REQUEST_CODE__NETWORK_CONFIG_ACTIVITY = 0xf1;

    // 等待 Vpn 启动状态
    private boolean waittingVpnStart;

    // UI组件
    private Button startFirewallButton;
    private Button configFirewallButton;
    private ListView appListView;

    // App 信息
    private List<AppInfo> appInfos;
    // App 适配器
    private AppListAdapter appListAdapter;

    // 配置信息
    public static final boolean DEFAULT_IS_TCP_FLOW_MODE_SPY = true;
    public static final boolean DEFAULT_IS_UDP_FLOW_MODE_SPY = true;
    public boolean isTcpFlowModeSpy;
    public boolean isUdpFlowModeSpy;

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 如果收到了服务已经运行的广播，则刚更新 flag
            if (FirewallVpnService.BROADCAST_VPN_STATE.equals(intent.getAction()) &&
                    intent.getBooleanExtra("running", false)) {
                waittingVpnStart = false;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        // 设置按钮状态
        this.setButtonEnableStatus(!waittingVpnStart && !FirewallVpnService.isRunning());

        // 更新配置
        this.updateConfig();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_network, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 更新配置
        this.updateConfig();
        // 载入 App 信息
        this.loadAppInfos();
        // 获取UI组件
        this.getUIComponent();
        // 设置适配器
        this.setAdapter();
        // 添加组件监听事件
        this.addComponentListener();
        // 注册广播接收器
        this.registerBroadcastReceiver();

        this.waittingVpnStart = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果是启动VpnInterface的请求
        switch (requestCode) {
            case REQUEST_CODE__FIREWALL_VPN_SERVICE:
                if (resultCode == getActivity().RESULT_OK) {
                    // 开启服务
                    Intent intent = new Intent(getActivity(), FirewallVpnService.class);
                    intent.putExtra("isTcpFlowModeSpy", this.isTcpFlowModeSpy);
                    intent.putExtra("isUdpFlowModeSpy", this.isUdpFlowModeSpy);
                    getActivity().startService(intent);
                    this.waittingVpnStart = true;

                    // 调整启动 VPN 按钮状态
                    this.setButtonEnableStatus(false);
                }
                break;
            case REQUEST_CODE__NETWORK_CONFIG_ACTIVITY:
                this.updateConfig();
                break;
            default:
                break;
        }
    }

    // 注册广播接收器函数
    private void registerBroadcastReceiver() {
        // VpnState 广播
        LocalBroadcastManager
                .getInstance(getActivity())
                .registerReceiver(vpnStateReceiver, new IntentFilter(FirewallVpnService.BROADCAST_VPN_STATE));
    }

    // 获取UI组件函数
    private void getUIComponent() {
        this.startFirewallButton = getActivity().findViewById(R.id.fragment_network__start_firewall_button);
        this.configFirewallButton = getActivity().findViewById(R.id.fragment_network__config_firewall_button);
        this.appListView = getActivity().findViewById(R.id.fragment_network__app_list_view);
    }

    // 添加组件监听事件函数
    private void addComponentListener() {
        // 开启防火墙监听事件
        this.startFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 准备VpnService
                prepareVpnService();
            }
        });

        // 配置防火墙监听事件
        this.configFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NetworkConfigActivity.class);
                intent.putExtra("isTcpFlowModeSpy", isTcpFlowModeSpy);
                intent.putExtra("isUdpFlowModeSpy", isUdpFlowModeSpy);
                // 开启配置 Activity
                startActivityForResult(intent, REQUEST_CODE__NETWORK_CONFIG_ACTIVITY);
            }
        });

        // listView
        this.appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                appInfos.get(position).setFlowAllow(
                        !appInfos.get(position).isFlowAllow()
                );
                appListAdapter.notifyDataSetChanged();
            }
        });
    }

    // 载入 App 信息
    private void loadAppInfos() {
        this.appInfos = AppUtil.getUserAppInfos(getActivity());
    }

    // 设置适配器
    private void setAdapter() {
        this.appListAdapter = new AppListAdapter(getActivity());
        this.appListView.setAdapter(this.appListAdapter);
        this.appListAdapter.setDatas(this.appInfos);
    }

    // 准备VpnService函数
    private void prepareVpnService() {
        // 让用户确认是否允许使用VpnService的权限
        Intent intent = VpnService.prepare(getActivity());

        if (intent != null) {
            // 如果用户没有同意，则询问用户是否允许，并且在用户同意之后启动VpnInterface
            startActivityForResult(intent, REQUEST_CODE__FIREWALL_VPN_SERVICE);
        } else {
            // 如果用户已经确认过了，则直接开启VpnInterface
            onActivityResult(REQUEST_CODE__FIREWALL_VPN_SERVICE, getActivity().RESULT_OK, null);
        }
    }

    // 设置启动防火墙按钮状态
    private void setButtonEnableStatus(boolean enable) {
        // 设置激活状态
        this.startFirewallButton.setEnabled(enable);
        this.configFirewallButton.setEnabled(enable);
        // 设置文字
        this.startFirewallButton.setText(
                enable ? getString(R.string.fragment_network__start_firewall_button__enable__text) :
                        getString(R.string.fragment_network__start_firewall_button__disabled__text)
        );
    }

    // 更新配置
    private void updateConfig() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        this.isTcpFlowModeSpy = sharedPreferences.getBoolean("isTcpFlowModeSpy", DEFAULT_IS_TCP_FLOW_MODE_SPY);
        this.isUdpFlowModeSpy = sharedPreferences.getBoolean("isUdpFlowModeSpy", DEFAULT_IS_UDP_FLOW_MODE_SPY);
    }

}
