package net.melove.demo.easechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.melove.demo.easechat.frg.GroupDetailFrg;
import net.melove.demo.easechat.frg.GroupSearchFragment;
import net.melove.demo.easechat.utils.FragmentManagerUtil;

public class GroupDetailActivity extends AppCompatActivity  {
    FragmentManagerUtil fragmentManagerUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);

        fragmentManagerUtil.chAddFrag(new GroupDetailFrg(), "", false);
    }

}
