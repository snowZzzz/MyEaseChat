package net.melove.demo.easechat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hyphenate.chat.EMGroupInfo;

import net.melove.demo.easechat.frg.GroupDetailFrg;
import net.melove.demo.easechat.frg.GroupSearchFragment;
import net.melove.demo.easechat.R;
import com.trade.beauty.mylibrary.utils.FragmentManagerUtil;

public class GroupSearchActivity extends AppCompatActivity {
    FragmentManagerUtil fragmentManagerUtil;
    GroupSearchFragment groupSearchFragment;
    GroupDetailFrg groupDetailFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        groupSearchFragment = new GroupSearchFragment();

        fragmentManagerUtil.chAddFrag(groupSearchFragment, "", false);
    }

    public void changeFrg(EMGroupInfo info) {
        fragmentManagerUtil.chHideFrag(groupSearchFragment);
        groupDetailFrg = GroupDetailFrg.getInstance(info);
        fragmentManagerUtil.chReplaceFrag(groupDetailFrg, "", false);
    }
}
