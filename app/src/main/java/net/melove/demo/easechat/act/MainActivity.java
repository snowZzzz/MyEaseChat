package net.melove.demo.easechat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.bean.InviteMessage;
import net.melove.demo.easechat.bean.ItemModel;
import net.melove.demo.easechat.easyutils.EaseNotifier;
import net.melove.demo.easechat.easyutils.emlisenter.MyCallBackImpl;
import net.melove.demo.easechat.easyutils.EasyUtil;
import net.melove.demo.easechat.utils.DataCacheUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends AppCompatActivity {

    // 发起聊天 username 输入框
    private EditText mChatIdEdit;
    // 发起聊天
    private Button mStartChatBtn;
    // 退出登录
    private Button mSignOutBtn;

    private TextView ec_tv_invite_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断sdk是否登录成功过，并没有退出和被踢，否则跳转到登陆界面
        if (!EMClient.getInstance().isLoggedInBefore()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

        mChatIdEdit = findViewById(R.id.ec_edit_chat_id);

        mStartChatBtn = findViewById(R.id.ec_btn_start_chat);

        ec_tv_invite_friend = findViewById(R.id.ec_tv_invite_friend);

        mStartChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取我们发起聊天的者的username
                String chatId = mChatIdEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(chatId)) {
                    // 获取当前登录用户的 username
                    String currUsername = EMClient.getInstance().getCurrentUser();
                    if (chatId.equals(currUsername)) {
                        Toast.makeText(MainActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 跳转到聊天界面，开始聊天
                    Intent intent = new Intent(MainActivity.this, ChatActivity2.class);
                    intent.putExtra("ec_chat_id", chatId);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Username 不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });

        mSignOutBtn = findViewById(R.id.ec_btn_sign_out);
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        findViewById(R.id.ec_btn_get_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task.callInBackground(new Callable<List<String>>() {
                    @Override
                    public List<String> call() throws Exception {
                        List<String> usernames = null;
                        usernames = EasyUtil.getEmManager().getAllContactsFromServer();
                        return usernames;
                    }
                }).continueWith(new Continuation<List<String>, Object>() {
                    @Override
                    public Object then(Task<List<String>> task) throws Exception {
                        ec_tv_invite_friend.setText(task.getResult().toString());
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });

        findViewById(R.id.ec_btn_add_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String chatId = mChatIdEdit.getText().toString().trim();

                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //参数为要添加的好友的username和添加理由
                        try {
                            EasyUtil.getEmManager().addContact(chatId, "加个好友呗");
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "加个好友失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return null;
                    }
                }).continueWith(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });

        findViewById(R.id.ec_btn_get_friend_invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                List<InviteMessage> msgs = DataCacheUtil.getInstance(MainActivity.this).getInviteMsgList();
                if (msgs != null && msgs.size() > 0) {
                    for (InviteMessage inviteMessage : msgs) {
                        sb.append(inviteMessage.getFrom());
                    }
                }

                ec_tv_invite_friend.setText(sb.toString());
            }
        });

        findViewById(R.id.ec_btn_get_convertion_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ConversationActivity.class));
            }
        });

        //群组操作
        findViewById(R.id.ec_btn_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupActivity.class));
            }
        });
    }

    /**
     * 退出登录
     */
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EasyUtil.getEmManager().logout(false, new MyCallBackImpl() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                Log.i("yyl", "logout success");
                finish();
            }

            @Override
            public void onError(int code, String error) {
                super.onError(code, error);
                Log.i("yyl", "logout error ");
            }
        });
    }

}
