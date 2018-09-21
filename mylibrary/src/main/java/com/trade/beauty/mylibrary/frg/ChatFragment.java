package com.trade.beauty.mylibrary.frg;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.trade.beauty.mylibrary.R;
import com.trade.beauty.mylibrary.adapter.ChatAdapter;
import com.trade.beauty.mylibrary.base.BaseFragment;
import com.trade.beauty.mylibrary.bean.ChatModel;
import com.trade.beauty.mylibrary.bean.ItemModel;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;

import java.util.ArrayList;

/**
 * Created by zhangzz on 2018/9/20
 */
public class ChatFragment extends BaseFragment {
    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private TextView mSendBtn;
    private TextView tvAdd;
    private LinearLayout layout_bottom;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    // 当前聊天的 ID
    private String mChatId;
    private String currUsername;
    private boolean isShowBottom = false;

    @Override
    protected void init(Bundle savedInstanceState) {

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getActivity().getIntent().getStringExtra("ec_chat_id");
        currUsername = EMClient.getInstance().getCurrentUser();
        adapter = new ChatAdapter();
        initView();
        mOnAddLisenterEvent.addEvent();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        mInputEdit = (EditText) fragmentView.findViewById(R.id.et);
        mSendBtn = (TextView) fragmentView.findViewById(R.id.tvSend);
        tvAdd = fragmentView.findViewById(R.id.tvAdd);
        layout_bottom = fragmentView.findViewById(R.id.layout_bottom);

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

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowBottom = !isShowBottom;
                layout_bottom.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
//                if (isShowBottom) {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in);
//                    LayoutAnimationController controller = new LayoutAnimationController(animation);
//                    layout_bottom.setLayoutAnimation(controller);
//                } else {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out);
//                    LayoutAnimationController controller = new LayoutAnimationController(animation);
//                    layout_bottom.setLayoutAnimation(controller);
//                }
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

    private OnAddLisenterEvent mOnAddLisenterEvent;

    public interface OnAddLisenterEvent {
        void addEvent();
    }

    public void setOnAddLisenterEvent(OnAddLisenterEvent onAddLisenterEvent) {
        this.mOnAddLisenterEvent = onAddLisenterEvent;
    }
}
