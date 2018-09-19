package net.melove.demo.easechat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.melove.demo.easechat.R;

import java.util.List;


/**
 */
public class GroupAdapter extends BaseQuickAdapter<EMGroup, BaseViewHolder> {
    private List<EMGroup> mList;

    public GroupAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    public GroupAdapter(int layoutResId, List<EMGroup> mList) {
        super(layoutResId, mList);
    }

    public void setData(List<EMGroup> list){
        mList.addAll(list);
        this.setNewData(mList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EMGroup group) {
        baseViewHolder.setText(R.id.tv_days_got, group.getGroupName());
        baseViewHolder.setText(R.id.tv_days_time, group.getDescription());
        baseViewHolder.setText(R.id.tv_gold_nums, group.getMemberCount()+"");
    }
}
