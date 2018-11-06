package com.qxhd.yellow.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.framework.core.base.BaseFragment;
import com.qxhd.yellow.R;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

public class BqsxFragment extends BaseFragment {

    @BindView(R.id.tag_group)
    TagGroup mDefaultTagGroup;


    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_bqsx;
    }

    @Override
    public void initValue() {

        String tags[]={"有码(1470)",  "无码(11188)"  ,"亚洲(2807)"  ,"欧美(2257)"  ,"黑人(1546)" , "白人(722)" , "萝莉(5272)"  ,"学生(2865)",  "御姐(2326)"   , "肛交(1557)",  "乳交(770)",  "足交(727)",  "波推(624)",  "大保健(1944)",  "按摩(1639)",  "潮喷(1871)", "大屌(742)" , "颜射(675)",  "VR视频(1296)","看不了(6004)"};



        mDefaultTagGroup.setTags(tags);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }


    public static BqsxFragment newFragmet(String title){
        BqsxFragment f=new BqsxFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title",title);
        f.setArguments(bundle);
        return f;
    }
}
