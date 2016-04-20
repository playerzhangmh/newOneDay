package com.april.oneday.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.utils.CommenUtils;
import com.april.oneday.utils.MyBitmapUtils;

import java.util.List;

/**
 * Created by wangtongyu on 2016/4/18.
 */
public class TimeLineAdapter extends BaseAdapter {
    Context context;
    List<MediaInfo> list;
    static final int TYPE_TEXT = 0;
    static final int TYPE_SINGLE_PHOTO = 1;
    static final int TYPE_TWO_PHOTOS = 2;
    static final int TYPE_THREE_PHOTOS = 3;

    public TimeLineAdapter(List<MediaInfo> list, Context context) {
        this.list = list;
        this.context = context;
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

                    convertView = View.inflate(context, R.layout.item_text, null);
                    textHolder = new TextHolder();

                    textHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    textHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    convertView.setTag(textHolder);

                    break;
                case TYPE_SINGLE_PHOTO:

                    convertView = View.inflate(context, R.layout.item_single_img, null);
                    singlePhotoHolder = new SinglePhotoHolder();

                    singlePhotoHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.img);
                    singlePhotoHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    singlePhotoHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                    convertView.setTag(singlePhotoHolder);

                    break;

                case TYPE_TWO_PHOTOS:

                    convertView = View.inflate(context, R.layout.item_two_imgs, null);
                    twoPhotosHolder = new TwoPhotosHolder();

                    twoPhotosHolder.ivPhoto_1 = (ImageView) convertView.findViewById(R.id.img_1);
                    twoPhotosHolder.ivPhoto_2 = (ImageView) convertView.findViewById(R.id.img_2);
                    twoPhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    twoPhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                    convertView.setTag(twoPhotosHolder);
                    break;
                case TYPE_THREE_PHOTOS:

                    convertView = View.inflate(context, R.layout.item_three_imgs, null);
                    threePhotosHolder = new ThreePhotosHolder();

                    threePhotosHolder.ivPhoto_1 = (ImageView) convertView.findViewById(R.id.img_1);
                    threePhotosHolder.ivPhoto_2 = (ImageView) convertView.findViewById(R.id.img_2);
                    threePhotosHolder.ivPhoto_3 = (ImageView) convertView.findViewById(R.id.img_3);
                    threePhotosHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    threePhotosHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);

                    convertView.setTag(threePhotosHolder);
                    break;
            }

        } else {//复用converView缓存
            switch (type) {
                case TYPE_TEXT:
                        textHolder = (TextHolder) convertView.getTag();
                    break;

                case TYPE_SINGLE_PHOTO:
                        singlePhotoHolder = (SinglePhotoHolder) convertView.getTag();
                    break;

                case TYPE_TWO_PHOTOS:
                        twoPhotosHolder = (TwoPhotosHolder) convertView.getTag();
                    break;

                case TYPE_THREE_PHOTOS:
                        threePhotosHolder = (ThreePhotosHolder) convertView.getTag();
                    break;
            }
        }

        //不论是否复用了缓存,都需要重新设置值
        switch (type) {
            case TYPE_TEXT:
                    System.out.println("纯文本复用缓存了");
                    textHolder.tvContent.setText(mediaInfo.getDesc());
                    textHolder.tvTime.setText(mediaInfo.getDate());
                break;

            case TYPE_SINGLE_PHOTO:
                    System.out.println("单张图片复用缓存了");
                    singlePhotoHolder.tvContent.setText(mediaInfo.getDesc());
                    singlePhotoHolder.tvTime.setText(mediaInfo.getDate());
                    //设置的图片需要经过压缩
                    Bitmap bm = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic1()), 80, 80);
                    singlePhotoHolder.ivPhoto.setImageBitmap(bm);

                break;

            case TYPE_TWO_PHOTOS:

                    System.out.println("两张图片复用缓存了");
                    twoPhotosHolder.tvContent.setText(mediaInfo.getDesc());
                    twoPhotosHolder.tvTime.setText(mediaInfo.getDate());
                    Bitmap bitmap1 = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic1()), 50, 50);
                    Bitmap bitmap2 = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic2()), 50, 50);
                    twoPhotosHolder.ivPhoto_1.setImageBitmap(bitmap1);
                    twoPhotosHolder.ivPhoto_2.setImageBitmap(bitmap2);


                break;

            case TYPE_THREE_PHOTOS:

                    System.out.println("三张图片复用缓存了");
                    threePhotosHolder.tvContent.setText(mediaInfo.getDesc());
                    threePhotosHolder.tvTime.setText(mediaInfo.getDate());

                    Bitmap bm1 = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic1()), 30, 30);
                    Bitmap bm2 = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic2()), 30, 30);
                    Bitmap bm3 = MyBitmapUtils.decodeSampledBitmapFromSd(
                            CommenUtils.getPathByName(mediaInfo.getPic3()), 30, 30);
                    threePhotosHolder.ivPhoto_1.setImageBitmap(bm1);
                    threePhotosHolder.ivPhoto_2.setImageBitmap(bm2);
                    threePhotosHolder.ivPhoto_3.setImageBitmap(bm3);


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
    ImageView ivPhoto;
}

/**
 * 两张图片
 */
class TwoPhotosHolder extends BaseViewHolder {
    ImageView ivPhoto_1, ivPhoto_2;
}

/**
 * 三张图片
 */
class ThreePhotosHolder extends BaseViewHolder {
    ImageView ivPhoto_1, ivPhoto_2, ivPhoto_3;
}

