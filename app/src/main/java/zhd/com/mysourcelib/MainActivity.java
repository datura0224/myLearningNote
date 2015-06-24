package zhd.com.mysourcelib;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zhd.com.mysourcelib.zhd.com.mysourcelib.base.CoustConfig;


public class MainActivity extends ListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListAdapter(new SimpleAdapter(MainActivity.this,
                queryActivities(),
                R.layout.list_item_main,
                new String[]{"title"},
                new int[]{R.id.tv_main_list_item}
                ));
        getListView().setTextFilterEnabled(true);//打开自动匹配，输入文字后自动过滤不匹配的选项


    }

    /*
        查询manifest中添加了过滤器并注册了特定类型的activity的集合
     */
    public ArrayList queryActivities(){
        ArrayList<Map<String,Object>> mapArrayList = new ArrayList<Map<String,Object>>();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(CoustConfig.CATEGORY_UI);
        PackageManager pm = getPackageManager();//通过包管理器，查询注册了特定类型的activity

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        int size = resolveInfos.size();
        for(int i=0; i<size; i++){
            ResolveInfo info = resolveInfos.get(i);

            Map item = new HashMap<String,Object>();
            CharSequence charSeq = info.loadLabel(pm);
            item.put("title",charSeq!=null?charSeq.toString():info.activityInfo.name);//如果manifest里有label就取出lable
            Intent in = activityIntent(
                    info.activityInfo.applicationInfo.packageName,
                    info.activityInfo.name);
            if(in!=null){
                item.put("intent",in);
            }

        mapArrayList.add(item);
        }

        return mapArrayList;
    }

    public Intent activityIntent(String pkg, String pkgName){
        Intent in = new Intent();
        in.setClassName(pkg,pkgName);
        return in;

    }


}
