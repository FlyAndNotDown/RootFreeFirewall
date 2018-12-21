package com.nuaa.is.rootfreefirewall.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nuaa.is.rootfreefirewall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * NetworkConfigActivity - FirewallModeSpinner 适配器
 */
public class NetworkConfigActivityFirewallModeSpinnerAdapter extends BaseAdapter {

    // 数据
    private List<String> datas = new ArrayList<>();
    // Context
    Context context;

    // 构造
    public NetworkConfigActivityFirewallModeSpinnerAdapter(Context context) {
        this.context = context;
    }

    // 设置数据
    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_network_config__firewall_mode_item, null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(datas.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
    }

}
