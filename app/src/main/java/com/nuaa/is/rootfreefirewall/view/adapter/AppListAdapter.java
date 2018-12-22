package com.nuaa.is.rootfreefirewall.view.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.model.AppInfo;

import java.util.List;

/**
 * App 列表适配器
 */
public class AppListAdapter extends BaseAdapter {

    // ViewHolder
    private static class ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;
        TextView appPackageTextView;
        CheckBox appFlowAllowCheckBox;
    }

    // 数据
    private List<AppInfo> datas;
    // 上下文
    private Context context;

    public AppListAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<AppInfo> datas) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_network__app_list_item, null);
            viewHolder.appIconImageView = (ImageView) convertView.findViewById(R.id.fragment_network__app_list_item__app_icon_image_view);
            viewHolder.appNameTextView = (TextView) convertView.findViewById(R.id.fragment_network__app_list_item__app_name_text_view);
            viewHolder.appPackageTextView = (TextView) convertView.findViewById(R.id.fragment_network__app_list_item__app_package_name_text_view);
            viewHolder.appFlowAllowCheckBox = (CheckBox) convertView.findViewById(R.id.fragment_network__app_list_item__app_flow_allow_check_box);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.appIconImageView.setImageDrawable(
                datas.get(position).getIcon()
        );
        viewHolder.appNameTextView.setText(
                datas.get(position).getName()
        );
        viewHolder.appPackageTextView.setText(
                datas.get(position).getPackageName()
        );
        viewHolder.appFlowAllowCheckBox.setChecked(
                datas.get(position).isFlowAllow()
        );

        return convertView;
    }
}
