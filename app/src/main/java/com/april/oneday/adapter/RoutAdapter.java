package com.april.oneday.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.activity.Routine_Create3;
import com.april.oneday.bean.RoutinesDetailInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ReminderUtis;
import com.april.oneday.utils.WindowMeasureUtils;

import java.util.List;

/**
 * Created by wangtongyu on 2016/4/22.
 */
public class RoutAdapter extends BaseAdapter{

    private static final String TAG = "RoutAdapter";
    List<RoutinesDetailInfo> mList;
    Context mContext;
    public PopupWindow popupWindow;
    private final RoutinesDao dao;
    private String choice;


    /**
     * 构造方法
     */

    public RoutAdapter(Context context, List<RoutinesDetailInfo> list) {
        mList = list;
        mContext = context;
        dao = new RoutinesDao(context);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public RoutinesDetailInfo getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {

        final RoutinesDetailInfo routine2Info = getItem(i);//该条目的具体数据
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_routine2, null);
            viewHolder = new ViewHolder();

            viewHolder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_routine2_item_starttime);
            viewHolder.tvTextTag = (TextView) convertView.findViewById(R.id.tv_routine2_item_texttag);
            viewHolder.tvDuringTime = (TextView) convertView.findViewById(R.id.tv_routine2_item_duration);
            viewHolder.ivTag = (ImageView) convertView.findViewById(R.id.iv_routine2_item_imgtag);
            viewHolder.ivMenu = (LinearLayout) convertView.findViewById(R.id.ll_routine2_item_menu);

            //优化图标配合度
            viewHolder.rl_routine2_background=(RelativeLayout)convertView.findViewById(R.id.rl_routine2_background);

            convertView.setTag(viewHolder);

        } else {
            //复用缓存
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //设置数据
        String detail_start = routine2Info.getDetail_start();
        String detail_end=routine2Info.getDetail_end();
        long longfromStart = ReminderUtis.getLongfromTime(detail_start);
        long longfromEnd = ReminderUtis.getLongfromTime(detail_end);
        Log.v(TAG,longfromStart+"---"+longfromEnd);
        if(longfromEnd>longfromStart){
            long minutes=(longfromEnd-longfromStart)/60000;
            if(minutes/60==0){
                viewHolder.tvDuringTime.setText(minutes+"m");
            }else {
                viewHolder.tvDuringTime.setText((minutes/60)+"h"+(minutes%60)+"m");
            }
        }else {
            long minutes=(longfromEnd-longfromStart)/60000+24*60l;
            viewHolder.tvDuringTime.setText((minutes/60)+"h"+(minutes%60)+"m");
        }

        viewHolder.tvStartTime.setText(detail_start.substring(0,detail_start.lastIndexOf(":")));
        viewHolder.tvTextTag.setText(routine2Info.getIcon_name());
//        viewHolder.tvDuringTime.setText(routine2Info.get());
        final int iconRid = routine2Info.getIconRid();
        viewHolder.ivTag.setImageResource(iconRid);
        int background = ReminderUtis.getBackground(iconRid);
        //Drawable drawable = mContext.getDrawable(background);
        /*viewHolder.ivTag.setBackgroundResource(background);*/
        viewHolder.rl_routine2_background.setBackgroundResource(background);

        LinearLayout menu = viewHolder.ivMenu;
        menu.setTag(i);


        //Buttion点击事件,弹出对话框
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Button","被点击了");
                //Button所在的条目索引
                int position = (int) view.getTag();
                System.out.println("Button所在的条目"+position);

                int[] ints = new int[2];
                view.getLocationOnScreen(ints);
                System.out.println("button"+position+"的位置:"+ints[0]+","+ints[1]);


                View contentView = View.inflate(mContext,R.layout.popwindow_routine2,null);

                TextView tvEdit = (TextView) contentView.findViewById(R.id.tv_poproutine2_edit);
                TextView tvClone = (TextView) contentView.findViewById(R.id.tv_poproutine2_clone);
                TextView tvDelete = (TextView) contentView.findViewById(R.id.tv_poproutine2_delete);
                TextView tvCancle = (TextView) contentView.findViewById(R.id.tv_poproutine2_cancle);

                /**
                 * 编辑按钮点击事件
                 */
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mContext, Routine_Create3.class);
                        intent.putExtra("RoutinesDetailInfo",routine2Info);
                        mContext.startActivity(intent);
                        hidePopWindow();
                    }
                });
                /**
                 * 克隆按钮点击事件
                 */
                tvClone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        //此处需要动态生成
                        int s_id = routine2Info.getS_id();
                        int p_id = routine2Info.getP_id();
                        final String[] choices = dao.getClonedaylist(s_id, p_id);
                        choice=choices[0];
                        builder.setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //执行克隆操作
                                choice = choices[i];
                                Toast.makeText(mContext,"你选择了"+choices[i],Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dao.cloneDetailtoOther(routine2Info,Integer.parseInt(choice.substring(choice.indexOf("day")+3)));
                                dialogInterface.dismiss();
                                hidePopWindow();

                            }
                        });
                        builder.setNegativeButton("取消",null);
                        builder.show();
                    }
                });
                /**
                 * 删除按钮点击事件
                 */
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dao.deleteDetails(routine2Info.getDetail_id());
                        mList.remove(routine2Info);
                        notifyDataSetChanged();
                        hidePopWindow();
                    }
                });
                /**
                 * 取消按钮点击事件
                 */
                tvCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hidePopWindow();
                    }
                });

                //显示新的之前
                hidePopWindow();

                popupWindow = new PopupWindow(contentView, (int) WindowMeasureUtils.px2dp(100,mContext), (int) WindowMeasureUtils.px2dp(160,mContext));

                //获取点击的按钮在屏幕上的位置

                popupWindow.showAtLocation(viewGroup, Gravity.RIGHT | Gravity.TOP,0,ints[1]);

            }
        });


        return convertView;
    }


    /**
     * 隐藏气泡
     */
    public void hidePopWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();//隐藏气泡
            popupWindow = null;
        }
    }



    class ViewHolder {
        TextView tvStartTime, tvTextTag, tvDuringTime;
        ImageView ivTag;//需要同时设定图标和背景
        LinearLayout ivMenu;
        public RelativeLayout rl_routine2_background;
    }

}

