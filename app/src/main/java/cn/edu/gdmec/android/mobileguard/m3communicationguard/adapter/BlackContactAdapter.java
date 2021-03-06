package cn.edu.gdmec.android.mobileguard.m3communicationguard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;

/**
 * Created by LYB on 2017/11/2.
 */

public class BlackContactAdapter extends BaseAdapter {
    private BlackContactCallback callback;
    private List<BlackContactInfo> contactInfos;
    private Context context;
    private BlackNumberDao dao;

    class ViewHolder{
        TextView mNameTV;
        TextView mModeTV;
        TextView mTypeTV;
        View mContactImgv;
        View mDeleteView;
    }

    public BlackContactAdapter(List<BlackContactInfo> systemContacts,
                               Context context) {
        super();
        this.contactInfos=systemContacts;
        this.context=context;
        dao=new BlackNumberDao(context);
    }


    public void notifyDataSetChanged() {
    }

    public void setCallBack(BlackContactCallback callback) {
        this.callback=callback;

    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return contactInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            view=View.inflate(context, R.layout.item_list_blackcontact,null);
            holder=new ViewHolder();
            holder.mNameTV=(TextView)view.findViewById(R.id.tv_black_name);
            holder.mModeTV=(TextView)view.findViewById(R.id.tv_black_mode);
            holder.mTypeTV=(TextView)view.findViewById(R.id.tv_black_type);
            holder.mContactImgv=view.findViewById(R.id.view_black_icon);
            holder.mDeleteView=view.findViewById(R.id.view_black_delete);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(i).contactName+"("
                +contactInfos.get(i).phoneNumber+")");
        holder.mModeTV.setText(contactInfos.get(i).getModeString(
                contactInfos.get(i).mode));
        holder.mTypeTV.setText(contactInfos.get(i).blackType);
        holder.mNameTV.setTextColor(context.getResources().getColor(
                R.color.bright_purple));
        holder.mModeTV.setTextColor(context.getResources().getColor(
                R.color.bright_purple));
        holder.mTypeTV.setTextColor(context.getResources().getColor(
                R.color.bright_purple));
        holder.mContactImgv.setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean delete=dao.detele(contactInfos.get(i));
                if(delete){
                    contactInfos.remove(contactInfos.get(i));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    if (dao.getTotalNumber()==0){
                        callback.DataSizeChanged();
                    }
                }else {
                    Toast.makeText(context,"删除失败!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public interface BlackContactCallback{
        void DataSizeChanged();
    }
}
