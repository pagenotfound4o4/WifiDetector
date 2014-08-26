package cn.edu.zju.eagle.wifidetector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.zju.eagle.wifidetector.R;

public class WiFiInfoAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private List<? extends Map<String, ?>> dataList;
    private int groupLayoutId, childLayoutId;

    public WiFiInfoAdapter(Context context, int groupLayoutId, int childLayoutId,
                           List<? extends Map<String, ?>> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = data;
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
    }

    public void setDataSet(List<? extends Map<String, ?>> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((List)dataList.get(groupPosition).get("child")).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((List)dataList.get(groupPosition).get("child")).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;

        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = inflater.inflate(groupLayoutId, parent, false);
            holder.ssid = (TextView)convertView.findViewById(R.id.group_ssid);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder)convertView.getTag();
        }
        holder.ssid.setText(dataList.get(groupPosition).get("ssid").toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;

        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = inflater.inflate(childLayoutId, parent, false);
            holder.title = (TextView)convertView.findViewById(R.id.child_title);
            holder.content = (TextView)convertView.findViewById(R.id.child_content);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder)convertView.getTag();
        }
        System.out.println("11111");
        List<Map<String, Object>> child = (List)dataList.get(groupPosition).get("child");
        System.out.println("22222");
        holder.title.setText(child.get(childPosition).get("title").toString());
        holder.content.setText(child.get(childPosition).get("content").toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupViewHolder {
        TextView ssid;
    }

    class ChildViewHolder {
        TextView title;
        TextView content;
    }
}
