package com.jumang.shortvideo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.activity.LoginActivity;
import com.jumang.shortvideo.activity.WebViewActivity;
import com.jumang.shortvideo.data.AppInfo;

import java.util.Objects;

/**
 * Created by steven on 2017/1/12.
 * Email-songzhonghua_1987@msn.com
 */

public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    public void setListener() {
    }

    protected void startWebView(String url, String title, long id) {
        startWebView(url, title, false, id);
    }

    protected void startWebView(String url, String title, Boolean checkLogin, long id) {
        if (checkLogin && AppInfo.getUserInfo() == null) {
            startActivity(LoginActivity.class);
        } else {
            Intent intent = new Intent(this.getContext(), WebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("title", title);
            intent.putExtra("adId", id);
            startActivity(intent);
            Objects.requireNonNull(this.getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle, Boolean checkLogin) {
        if (checkLogin && AppInfo.getUserInfo() == null) {
            startActivity(LoginActivity.class);
        } else {
            Intent intent = new Intent();
            intent.setClass(Objects.requireNonNull(this.getActivity()), cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }
        Objects.requireNonNull(this.getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        startActivity(cls, bundle, false);
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null, false);
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Boolean checkLogin) {
        startActivity(cls, null, checkLogin);
    }

    /**
     * 公共方法： 从碎片fragment1跳转到碎片fragment2
     *
     * @param fragment1 当前fragment
     * @param fragment2 跳转后的fragment
     */
    public void transformFragment(BaseFragment fragment1, BaseFragment fragment2) {
        // 获取 FragmentTransaction  对象
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        //如果fragment2没有被添加过，就添加它替换当前的fragment1
        if (!fragment2.isAdded()) {
            transaction.replace(R.id.fragment_container, fragment2)
                    //加入返回栈，这样你点击返回键的时候就会回退到fragment1了
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();

        } else { //如果已经添加过了的话就隐藏fragment1，显示fragment2
            transaction
                    // 隐藏fragment1，即当前碎片
                    .hide(fragment1)
                    // 显示已经添加过的碎片，即fragment2
                    .show(fragment2)
                    // 加入返回栈
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();
        }
    }
}
