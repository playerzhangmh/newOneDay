package com.april.oneday.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.view.RoundImageView;

import java.util.List;
import java.util.Map;

/**
 * Created by wangtongyu on 2016/4/18.
 */
public class TimeLineAdapter extends BaseAdapter {
    MainActivity mActivity;
    List<MediaInfo> list;
    static final int TYPE_TEXT = 0;
    static final int TYPE_SINGLE_PHOTO = 1;
    static final int TYPE_TWO_PHOTOS = 2;
    static final int TYPE_THREE_PHOTOS = 3;

    private List<Map<String, Bitmap>> bitmapList;

    public TimeLineAdapter(List<MediaInfo> list,List<Map<String,Bitmap>>  bitmapList,MainActivity mActivity) {
        this.list = list;
        this.mActivity = mActivity;
        this.bitmapList = bitmapList;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MediaInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 返回item布局的类型总数
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        System.out.println("getViewTypeCount:" + super.getViewTypeCount());
        return 4;
    }

    /**
     * 返回某个item的布局的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        MediaInfo mediaInfo = getItem(position);

        int type = getItemViewType(position);

        TextHolder textHolder = null;
        SinglePhotoHolder singlePhotoHolder = null;
        TwoPhotosHolder twoPhotosHolder = null;
        ThreePhotosHolder threePhotosHolder = null;


        //当前没有缓存
        if (convertView == null) {

            switch (type) {
                case TYPE_TEXT:

                    convertView = View.inflate(mActivity, R.layout.item_text, null);
                    textHolder = new TextHolder();

                    textHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    textHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    convertView.setTag(textHolder);

                    break;
                case TYPE_SINGLE_PHOTO:

                    convertView = View.inflate(mActivity, R.layout.item_single_img, null);
                    singlePhotoHolder = new SinglePhotoHolder();

                    singlePhotoHolder.ivPhoto = (RoundImageView) convertView.findViewById(R.id.img);
                    singlePhotoHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    singlePhotoHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    convertView.setTag(singlePhotoHolder);

                    break;

                case TYPE_TWO_PHOTOS:

                    convertView = View.inflate(mActivity, R.layout.item_two_imgs, null);
                    twoPhotosHolder = new TwoPhotosHolder();

                    twoPhotosHolder.ivPhoto_1 = (RoundImageView) convertView.findViewById(R.id.img_1);
                    twoPhotosHolder.ivPhoto_2 = (RoundImageView) convertView.findViewById(R.id.img_2);
                    twoPhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    twoPhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                    convertView.setTag(twoPhotosHolder);
                    break;
                case TYPE_THREE_PHOTOS:

                    convertView = View.inflate(mActivity, R.layout.item_three_imgs, null);
                    threePhotosHolder = new ThreePhotosHolder();

                    threePhotosHolder.ivPhoto_1 = (RoundImageView) convertView.findViewById(R.id.img_1);
                    threePhotosHolder.ivPhoto_2 = (RoundImageView) convertView.findViewById(R.id.img_2);
                    threePhotosHolder.ivPhoto_3 = (RoundImageView) convertView.findViewById(R.id.img_3);
                    threePhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    threePhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                    convertView.setTag(threePhotosHolder);
                    break;
            }

        } else {//复用converView缓存
            switch (type) {
                case TYPE_TEXT:
                    if(convertView.getTag() instanceof TextHolder){
                        textHolder = (TextHolder) convertView.getTag();
                    }else {
                        //缓存类型不匹配，重新加载
                        convertView = View.inflate(mActivity, R.layout.item_text, null);
                        textHolder = new TextHolder();

                        textHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        textHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                        convertView.setTag(textHolder);
                    }


                    break;

                case TYPE_SINGLE_PHOTO:
                    if(convertView.getTag() instanceof SinglePhotoHolder){
                        singlePhotoHolder = (SinglePhotoHolder) convertView.getTag();
                    }else{
                        convertView = View.inflate(mActivity, R.layout.item_single_img, null);
                        singlePhotoHolder = new SinglePhotoHolder();

                        singlePhotoHolder.ivPhoto = (RoundImageView) convertView.findViewById(R.id.img);
                        singlePhotoHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        singlePhotoHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                        convertView.setTag(singlePhotoHolder);
                    }

                    break;

                case TYPE_TWO_PHOTOS:

                    if(convertView.getTag() instanceof TwoPhotosHolder){
                        twoPhotosHolder = (TwoPhotosHolder) convertView.getTag();
                    }else {
                        convertView = View.inflate(mActivity, R.layout.item_two_imgs, null);
                        twoPhotosHolder = new TwoPhotosHolder();

                        twoPhotosHolder.ivPhoto_1 = (RoundImageView) convertView.findViewById(R.id.img_1);
                        twoPhotosHolder.ivPhoto_2 = (RoundImageView) convertView.findViewById(R.id.img_2);
                        twoPhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        twoPhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                        convertView.setTag(twoPhotosHolder);
                    }


                    break;

                case TYPE_THREE_PHOTOS:
                    if(convertView.getTag() instanceof ThreePhotosHolder){
                        threePhotosHolder = (ThreePhotosHolder) convertView.getTag();
                    }else{
                        convertView = View.inflate(mActivity, R.layout.item_three_imgs, null);
                        threePhotosHolder = new ThreePhotosHolder();

                        threePhotosHolder.ivPhoto_1 = (RoundImageView) convertView.findViewById(R.id.img_1);
                        threePhotosHolder.ivPhoto_2 = (RoundImageView) convertView.findViewById(R.id.img_2);
                        threePhotosHolder.ivPhoto_3 = (RoundImageView) convertView.findViewById(R.id.img_3);
                        threePhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        threePhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                        convertView.setTag(threePhotosHolder);
                    }


                    break;

            }
        }

        //不论是否复用了缓存,都需要重新设置值
        switch (type) {
            case TYPE_TEXT:
                textHolder.tvContent.setText(mediaInfo.getDesc());
                textHolder.tvTime.setText(mediaInfo.getDate());
                break;

            case TYPE_SINGLE_PHOTO:
                singlePhotoHolder.tvContent.setText(mediaInfo.getDesc());
                singlePhotoHolder.tvTime.setText(mediaInfo.getDate());

                Map<String, Bitmap> map = bitmapList.get(position);
                if (map.size()==1){
                    Bitmap bitmap = map.get("pic1");
                    singlePhotoHolder.ivPhoto.setImageBitmap(bitmap);
                }



                break;

            case TYPE_TWO_PHOTOS:

                twoPhotosHolder.tvContent.setText(mediaInfo.getDesc());
                twoPhotosHolder.tvTime.setText(mediaInfo.getDate());

                Map<String, Bitmap> map2 = bitmapList.get(position);
                if (map2.size()==2){
                    twoPhotosHolder.ivPhoto_1.setImageBitmap(map2.get("pic1"));
                    twoPhotosHolder.ivPhoto_2.setImageBitmap(map2.get("pic2"));
                }
                break;

            case TYPE_THREE_PHOTOS:

                threePhotosHolder.tvContent.setText(mediaInfo.getDesc());
                threePhotosHolder.tvTime.setText(mediaInfo.getDate());
                Map<String, Bitmap> map3 = bitmapList.get(position);
                if (map3.size()==3){
                    threePhotosHolder.ivPhoto_1.setImageBitmap(map3.get("pic1"));
                    threePhotosHolder.ivPhoto_2.setImageBitmap(map3.get("pic2"));
                    threePhotosHolder.ivPhoto_3.setImageBitmap(bitmapList.get(position).get("pic3"));
                }



                break;
        }


        return convertView;


    }
}


/**
 * ViewHolder基类
 */
class BaseViewHolder {
    TextView tvContent, tvTime;
}

/**
 * 纯文本
 */
class TextHolder extends BaseViewHolder {

}

/**
 * 单张图片
 */
class SinglePhotoHolder extends BaseViewHolder {
    RoundImageView ivPhoto;
}

/**
 * 两张图片
 */
class TwoPhotosHolder extends BaseViewHolder {
    RoundImageView ivPhoto_1, ivPhoto_2;
}

/**
 * 三张图片
 */
class ThreePhotosHolder extends BaseViewHolder {
    RoundImageView ivPhoto_1, ivPhoto_2, ivPhoto_3;
}

