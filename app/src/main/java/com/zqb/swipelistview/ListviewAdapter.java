package com.zqb.swipelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zqb on 2016/7/12.
 */
public class ListviewAdapter extends BaseAdapter {

    private SwipeListview mlistview;
    private Context mContext;
    private ArrayList<String>mList;
    public ListviewAdapter(Context context, ArrayList<String>list,SwipeListview listview)
    {
        mContext=context;
        mList=list;
        mlistview=listview;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null)
        {
//            convertView = LayoutInflater.from(mContext)
//                    .inflate(R.layout.list_item,parent,false);
            convertView=View.inflate(mContext,R.layout.list_item,null);
            holder=new ViewHolder();
            holder.tv= (TextView) convertView.findViewById(R.id.tv);
            holder.delete= (TextView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(mList.get(position).toString());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
                mlistview.turnToNormal();
            }
        });
        return convertView;
    }
    class ViewHolder
    {
        private TextView tv;
        private TextView delete;
    }
}
