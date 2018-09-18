package net.melove.demo.easechat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.easyutils.EasyUtil;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class GroupActivity extends AppCompatActivity {
    private EditText mEditName;
    private EditText mEditProfile;
    protected List<EMGroup> grouplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        mEditName = findViewById(R.id.edit_name);
        mEditProfile = findViewById(R.id.edit_profile);

        findViewById(R.id.btn_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showloading();

                //创建群组
                final String groupName = mEditName.getText().toString().trim();
                final String desc = mEditProfile.getText().toString();

                final EMGroupOptions option = new EMGroupOptions();
                option.maxUsers = 200;
                option.inviteNeedConfirm = true;

                final String reason = "建个群玩一下";
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                final String[] members = new String[0];

                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //参数为要添加的好友的username和添加理由
                        try {
                            EasyUtil.getEmManager().createGroup(groupName, desc, members, reason, option);
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GroupActivity.this, "建群失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return null;
                    }
                }).continueWith(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
//                hideloading();
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });


        findViewById(R.id.btn_get_group_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task.callInBackground(new Callable<List<EMGroup>>() {
                    @Override
                    public List<EMGroup> call() throws Exception {
                        //参数为要添加的好友的username和添加理由
                        try {
                            //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                            List<EMGroup> grouplist = EasyUtil.getEmManager().getJoinedGroupsFromServer();//需异步处理
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GroupActivity.this, "建群失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return grouplist;
                    }
                }).continueWith(new Continuation<List<EMGroup>, Object>() {
                    @Override
                    public Object then(Task<List<EMGroup>> task) throws Exception {
                        grouplist = EasyUtil.getEmManager().getAllGroups();
                        EMGroup one = grouplist.get(0);
                        one.getGroupId();
                        return grouplist;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });
    }
}
