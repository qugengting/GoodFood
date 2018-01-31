package com.common.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuruibin on 2017/11/10.
 * 描述：BaseAdapter抽象类
 */

public abstract class AbstractAdapter<E, T extends BaseHolder> extends BaseAdapter {

    protected List<E> data = new ArrayList<E>();
    protected Context context;

    public AbstractAdapter() {
    }

    public AbstractAdapter(Context context, List<E> list) {
        this.context = context;
        this.data = list;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * 增加全部list
     *
     * @param data
     */
    public void addDatas(List<E> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 增加某个item
     *
     * @param data
     */
    public void addData(E data) {
        this.data.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 删除某项item
     *
     * @param position
     */
    public void delItem(int position) {
        if (data != null && data.size() > position) {
            data.remove(position);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 删除全部
     */
    public void delAll() {
        data.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T holder;
        if (convertView == null) {
            //getView()的抽象方法，返回值为int
            convertView = getItemView(getView());
            //通过泛型自动转换holder类型，由于butterknife需要在holder里面绑定控件，所以不能写成同一个holder，而需要自动转换。
            holder = getHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (T) convertView.getTag();
        }
        //逻辑处理的抽象方法
        bindEvent(holder,position);
        return convertView;
    }
    //三个抽象方法
    protected abstract T getHolder(View v);

    protected abstract int getView();

    protected abstract void bindEvent(T holder,int position);

    protected View getItemView(int id) {
        return LayoutInflater.from(context).inflate(id, null);
    }

}
