package com.cattle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cattle.R;
import com.cattle.model.PhotoUpload;
import com.cattle.views.PhotupImageView;

import java.util.List;

/**
 * Created by longjianlin on 15/1/21.
 */
public class SharePhotoAdapter extends BaseAdapter {
    private List<PhotoUpload> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public SharePhotoAdapter(Context context, List<PhotoUpload> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == view) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_grid_photo_share, null);
            holder.photoImageView = (PhotupImageView) view.findViewById(R.id.piv_photo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        PhotoUpload photoUpload = mList.get(position);
        holder.photoImageView.setImageBitmap(photoUpload.getDisplayImage(mContext));
        return view;
    }

    class ViewHolder {
        public PhotupImageView photoImageView;
    }


}
