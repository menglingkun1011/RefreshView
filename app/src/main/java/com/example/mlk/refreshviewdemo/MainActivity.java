package com.example.mlk.refreshviewdemo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mlk.refreshviewdemo.utils.PageInfo;
import com.example.mlk.refreshviewdemo.view.RefreshRecy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshRecy refreshRecy;
    private MyAdapter adapter;
    private PageInfo pageInfo = new PageInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshRecy = (RefreshRecy) findViewById(R.id.recyclerView);
        refreshRecy.setLinearLayoutManager(LinearLayout.VERTICAL);

        adapter = new MyAdapter(R.layout.item_recy, null);
        refreshRecy.setAdapter(adapter);

        refreshRecy.setOnRefreshListener(new RefreshRecy.onRefreshListener() {
            @Override
            public void onRefresh() {
                List<String> newList = new ArrayList();
                for (int i = 0; i < 20; i++) {
                    newList.add("标题"+i);
                }

                refreshRecy.downRefreshData(newList);
            }

            @Override
            public void onLoadMore() {
                List<String> newList = new ArrayList();
                if(adapter.getData().size()> 30){
                    refreshRecy.loadMoreData(newList);
                    return;
                }
                for (int i = 0; i < 10; i++) {
                    newList.add("标题"+(adapter.getData().size()+i));
                }
                refreshRecy.loadMoreData(newList);
            }
        },20);
//        refreshRecy.closeLoadMord();
    }


    class MyAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv,item);
        }
    }
}
