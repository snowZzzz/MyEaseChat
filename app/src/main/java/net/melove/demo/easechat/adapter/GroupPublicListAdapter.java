package net.melove.demo.easechat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMGroupInfo;

import net.melove.demo.easechat.R;

import java.util.List;


/**
 */
public class GroupPublicListAdapter extends BaseQuickAdapter<EMGroupInfo, BaseViewHolder> {
    private List<EMGroupInfo> mList;

    public GroupPublicListAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    public GroupPublicListAdapter(int layoutResId, List<EMGroupInfo> mList) {
        super(layoutResId, mList);
    }

    public void setData(List<EMGroupInfo> list){
        mList.addAll(list);
        this.setNewData(mList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final EMGroupInfo group) {
        baseViewHolder.setText(R.id.tv_days_got, group.getGroupName());
    }
}
