package com.framework.core.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framework.core.tools.ToastTools;
import com.framework.core.viewcontroller.VaryViewHelperController;

import java.lang.reflect.Field;

import butterknife.ButterKnife;


/**
 * Created by quxiang on 2017/1/10.
 */

public abstract class BaseFragment extends Fragment {

    public View root;

    private boolean isFirstVisible = true;
    private boolean isPrepared;

    public VaryViewHelperController mVaryViewHelperController = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return root=getActivity().getLayoutInflater().inflate(getContentLayoutID(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
        ButterKnife.bind(this,root);
        initValue();
    }

    public abstract int getContentLayoutID();

    public abstract void initValue();

    @Override
    public void onDetach() {
        super.onDetach();
        // for bug ---> java.lang.IllegalStateException: Activity has been destroyed
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 当第一次showFragment的时候调
     */
    protected abstract void onFirstUserVisible();


    /**
     * get loading target view
     */
    protected abstract View getLoadingTargetView();

    public void doActivity(Class c){
        Intent intent=new Intent(getActivity(),c);
        startActivity(intent);
    }

    public void doActivity(Class c,Bundle b){
        Intent intent=new Intent(getActivity(),c);
        if (b!=null){
            intent.putExtras(b);
        }
        startActivity(intent);
    }

    public void doActivityForresult(int requestCode,Class c){
        Intent intent=new Intent(getActivity(),c);
        startActivityForResult(intent,requestCode);
    }

    public void doActivityForresult(int requestCode,Class c,Bundle bundle){
        Intent intent=new Intent(getActivity(),c);
        intent.putExtras(bundle);
        startActivityForResult(intent,requestCode);
    }

    public void showToast(String msg){
        if (!TextUtils.isEmpty(msg))
        ToastTools.showToast(getContext(),msg);
    }

}
