package net.melove.demo.easechat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.easyutils.EasyUtil;

public class GroupActivity extends AppCompatActivity {
    private EditText mEditName;
    private EditText mEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        mEditName = findViewById(R.id.edit_name);
        mEditProfile = findViewById(R.id.edit_profile);

        findViewById(R.id.btn_create_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建群组
                final String groupName = mEditName.getText().toString().trim();
                String desc = mEditProfile.getText().toString();

                EMGroupOptions option = new EMGroupOptions();
                option.maxUsers = 200;
                option.inviteNeedConfirm = true;

                String reason = "建个群测试一下";
                reason = EMClient.getInstance().getCurrentUser() + reason + groupName;
                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                String[] members = new String[0];
                try {
                    EasyUtil.getEmManager().createGroup(groupName, desc, members, reason, option);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
