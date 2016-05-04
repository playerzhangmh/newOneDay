package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;

public class Routine_Create4 extends Activity {


    //初始化background,icon,tagname数组
    int[] background={R.drawable.routine3_bg1,R.drawable.routine3_bg2,R.drawable.routine3_bg3,
            R.drawable.routine3_bg4,R.drawable.routine3_bg5,R.drawable.routine3_bg3,
            R.drawable.routine3_bg2,R.drawable.routine3_bg1,R.drawable.routine3_bg5,
            R.drawable.routine3_bg4,R.drawable.routine3_bg2,R.drawable.routine3_bg5,};

    int[] icon={R.drawable.routine3_tag_exercise,R.drawable.routine3_tag_outdoor,R.drawable.routine3_tag_date
            ,R.drawable.routine3_tag_bike,R.drawable.routine3_tag_clear,R.drawable.routine3_tag_ktv
            ,R.drawable.routine3_tag_tv,R.drawable.routine3_tag_internet,R.drawable.routine3_tag_work
            ,R.drawable.routine3_tag_training,R.drawable.routine3_tag_sleep,R.drawable.routine3_tag_test};

    String[] tagname={"跑步","户外","约会"
            ,"骑行","家务","K歌"
            ,"TV","上网","工作"
            ,"健身","休息","会议"};

    private ListView lv_routine4_taglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏,此处有坑
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.routine4_tag);


        lv_routine4_taglist = (ListView)findViewById(R.id.lv_routine4_taglist);
        MyListAdapter myListAdapter = new MyListAdapter();
        lv_routine4_taglist.setAdapter(myListAdapter);
        lv_routine4_taglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Routine4","onIntemClick");
                Intent intent = new Intent();
                intent.putExtra("Routine4",tagname[i]+","+icon[i]);
                setResult(100,intent);
                finish();
            }
        });

    }


    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View conview, ViewGroup viewGroup) {

            View view = View.inflate(Routine_Create4.this, R.layout.routine4_tag_item, null);
            ImageView ib_routine4_icon=(ImageView) view.findViewById(R.id.ib_routine4_icon);
            TextView tv_routine4_tagname=(TextView) view.findViewById(R.id.tv_routine4_tagname);
            RelativeLayout rl_routine4_background=(RelativeLayout)view.findViewById(R.id.rl_routine4_background);

            ib_routine4_icon.setImageResource(icon[position]);
            tv_routine4_tagname.setText(tagname[position]);
            rl_routine4_background.setBackgroundResource(background[position]);
            return view;
        }
    }


    //back键
    public void routine4_back(View v){

        finish();
    }
}
