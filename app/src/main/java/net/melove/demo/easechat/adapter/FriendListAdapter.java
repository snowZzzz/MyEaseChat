package net.melove.demo.easechat.adapter;

import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMGroup;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.bean.FriendBean;

import java.util.List;


/**
 */
public class FriendListAdapter extends BaseQuickAdapter<FriendBean, BaseViewHolder> {
    private List<FriendBean> mList;

    public FriendListAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    public FriendListAdapter(int layoutResId, List<FriendBean> mList) {
        super(layoutResId, mList);
    }

    public void setData(List<FriendBean> list){
        mList.addAll(list);
        this.setNewData(mList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final FriendBean group) {
        baseViewHolder.setText(R.id.tv_days_got, group.getUserName());
        baseViewHolder.setChecked(R.id.rb_select, group.isSelect());
        baseViewHolder.setOnCheckedChangeListener(R.id.rb_select, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    group.setSelect(isChecked);
            }
        });
    }
}
