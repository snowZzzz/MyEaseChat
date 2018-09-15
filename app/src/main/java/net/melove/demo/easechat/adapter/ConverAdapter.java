package net.melove.demo.easechat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.melove.demo.easechat.R;


/**
 */
public class ConverAdapter extends BaseQuickAdapter<EMConversation, BaseViewHolder> {
    public ConverAdapter(int layoutResId) {
        super(layoutResId, null);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, EMConversation conversation) {
        String username = conversation.conversationId();
        EMMessage lastMessage = conversation.getLastMessage();
        EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
        baseViewHolder.setText(R.id.tv_days_got, username);
        baseViewHolder.setText(R.id.tv_days_time, body.getMessage());
        baseViewHolder.setText(R.id.tv_gold_nums, lastMessage.getMsgTime()+"");
    }
}
