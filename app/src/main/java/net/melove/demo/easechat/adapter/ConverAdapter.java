package net.melove.demo.easechat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.melove.demo.easechat.R;

import java.util.List;


/**
 */
public class ConverAdapter extends BaseQuickAdapter<EMConversation, BaseViewHolder> {
    private List<EMConversation> mList;

    public ConverAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    public ConverAdapter(int layoutResId, List<EMConversation> mList) {
        super(layoutResId, mList);
    }

    public void setData(List<EMConversation> list){
        mList.addAll(list);
        this.setNewData(mList);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EMConversation conversation) {
        String username = conversation.conversationId();
        EMMessage lastMessage = conversation.getLastMessage();
        EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
        baseViewHolder.setText(R.id.tv_days_got, username);
        baseViewHolder.setText(R.id.tv_days_time, body.getMessage());
        baseViewHolder.setText(R.id.tv_gold_nums, lastMessage.getMsgTime()+"");
        baseViewHolder.setText(R.id.tv_days_unread_num, conversation.getUnreadMsgCount()+"");
    }
}
