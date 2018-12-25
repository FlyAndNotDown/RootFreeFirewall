package com.nuaa.is.rootfreefirewall.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.model.SandboxSms;

import java.util.List;

public class MessageSandboxAdapter extends BaseAdapter {

    // ViewHolder
    private static class ViewHolder {
        TextView phoneNumberTextView;
        TextView dangeroursDegreeTextView;
        TextView deliverTimeTextView;
        TextView messageContentTextView;
    }

    // 数据
    private List<SandboxSms> datas;
    // 上下文
    private Context context;

    // 构造
    public MessageSandboxAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<SandboxSms> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_message__message_sandbox_list_item, null);
            viewHolder.phoneNumberTextView = (TextView) convertView.findViewById(R.id.fragment_message__message_sandbox_list_item__phone_number_text_view);
            viewHolder.dangeroursDegreeTextView = (TextView) convertView.findViewById(R.id.fragment_message__message_sandbox_list_item__dangerous_degree_text_view);
            viewHolder.deliverTimeTextView = (TextView) convertView.findViewById(R.id.fragment_message__message_sandbox_list_item__deliver_time_text_view);
            viewHolder.messageContentTextView = (TextView) convertView.findViewById(R.id.fragment_message__message_sandbox_list_item__message_content_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.phoneNumberTextView.setText(datas.get(position).getPhoneNumer());
        viewHolder.dangeroursDegreeTextView.setText(datas.get(position).getDangerousDegree());
        viewHolder.deliverTimeTextView.setText(datas.get(position).getDate());
        viewHolder.messageContentTextView.setText(datas.get(position).getContent());

        return convertView;
    }
}
