package net.melove.demo.easechat.frg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.act.ChatActivity2;
import net.melove.demo.easechat.act.GroupSearchActivity;
import net.melove.demo.easechat.adapter.ChatAdapter;
import net.melove.demo.easechat.base.BaseFragment;
import net.melove.demo.easechat.bean.ChatModel;
import net.melove.demo.easechat.bean.ItemModel;
import net.melove.demo.easechat.easyutils.EaseNotifier;
import net.melove.demo.easechat.easyutils.EasyUtil;
import net.melove.demo.easechat.easyutils.emlisenter.MyEMMessageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/20
 */
public class ChatFragment extends BaseFragment {
    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private TextView mSendBtn;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    // 当前聊天的 ID
    private String mChatId;
    private String currUsername;

    @Override
    protected void init(Bundle savedInstanceState) {

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getActivity().getIntent().getStringExtra("ec_chat_id");
        currUsername = EMClient.getInstance().getCurrentUser();
        adapter = new ChatAdapter();
        initView();

        ((ChatActivity2)(getActivity())).initAddMsgLis();
        ((ChatActivity2)(getActivity())).initConversation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        mInputEdit = (EditText) fragmentView.findViewById(R.id.et);
        mSendBtn = (TextView) fragmentView.findViewById(R.id.tvSend);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recylerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username:为对方用户或者群聊的id
                    EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(content, mChatId);

                    //如果是群聊，设置chattype，默认是单聊
//                    if (chatType == CHATTYPE_GROUP)
//                        message.setChatType(ChatType.GroupChat);

                    // 将新的消息内容和时间加入到下边
//                    mContentText.setText(mContentText.getText()
//                        + "\n发送："
//                        + content
//                        + " - time: "
//                        + message.getMsgTime());
                    ArrayList<ItemModel> newLists = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.setContent(content);
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                    newLists.add(new ItemModel(ItemModel.CHAT_B, model));
                    adapter.addAll(newLists);

                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                    hideKeyBorad(mInputEdit);

                    // 调用发送消息的方法
                    EasyUtil.getEmManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("yyl", "send message on success");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("yyl", "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });
    }

    public void addLists(ArrayList<ItemModel> newLists) {
        adapter.addAll(newLists);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void refresh(ArrayList<ItemModel> models) {
        adapter.replaceAll(models);
        recyclerView.scrollToPosition(models.size() - 1);
    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
