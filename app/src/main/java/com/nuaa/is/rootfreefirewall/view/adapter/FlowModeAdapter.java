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
 * 流量模式适配器
 */
public class FlowModeAdapter extends BaseAdapter {

    // ViewHolder
    private static class ViewHolder {
        TextView textView;
    }

    // 数据
    private List<Boolean> datas = new ArrayList<>();
    // 上下文
    Context context;

    // 构造
    public FlowModeAdapter(Context context) {
        this.context = context;
    }

    // 设置数据
    public void setDatas(List<Boolean> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_network_config__flow_mode_item, null);
            viewHolder.textView = (TextView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(
                datas.get(position) ?
                        context.getString(R.string.activity_network_config__flow_mode__spy) :
                        context.getString(R.string.activity_network_config__flow_mode__kill)
        );

        return convertView;
    }

}
