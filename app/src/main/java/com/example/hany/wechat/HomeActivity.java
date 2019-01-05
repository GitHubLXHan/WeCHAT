package com.example.hany.wechat;

import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.hany.wechat.Fragment.ContractFragment;
import com.example.hany.wechat.Fragment.FindFragment;
import com.example.hany.wechat.Fragment.NearFragment;
import com.example.hany.wechat.Fragment.SetFragment;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    String TAG = "HomeActivityTAG";

    private ImageButton mNearFragmentBtn;
    private ImageButton mContractFragmentBtn;
    private ImageButton mFindFragmrntBtn;
    private ImageButton mSetFragmentBtn;
    private FragmentManager fragmentManager;
    private NearFragment mNearFragment;
    private ContractFragment mContractFragment;
    private FindFragment mFindFragment;
    private SetFragment mSetFragment;
    private String userId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        // logOrReg表示打开HomeActivity中哪个Fragment,near表示打开NearFragment,reg表示打开SetFragment
        String open = getIntent().getStringExtra("open");
        // 获取此时登录的账号的ID
        userId = getIntent().getStringExtra("userId");
        if (savedInstanceState != null) { // 以防HomeActivity被系统回收而导致userId丢失
            userId = savedInstanceState.getString("userId");
        }
        // 创建一个全局的Bundle，传递userId给各个Fragment
        bundle = new Bundle();
        bundle.putString("userId", userId);
        // 获取Fragment管理器并且通过logOrReg变量判断打开Fragment
        fragmentManager = getSupportFragmentManager();
        // 开启一个事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (open.equals("near")) {
            openNearFragment(bundle);
        } else if (open.equals("reg")) {
            openSetFragment(bundle);
        } else if (open.equals("contract")) {
            openContractFragment(bundle);
        }
        transaction.commit();
    }


    private void initView() {
        mNearFragmentBtn = findViewById(R.id.btn_near_contract);
        mContractFragmentBtn = findViewById(R.id.btn_all_contract);
        mFindFragmrntBtn = findViewById(R.id.btn_find);
        mSetFragmentBtn = findViewById(R.id.btn_set);
        mNearFragmentBtn.setOnClickListener(this);
        mContractFragmentBtn.setOnClickListener(this);
        mFindFragmrntBtn.setOnClickListener(this);
        mSetFragmentBtn.setOnClickListener(this);
        mNearFragmentBtn.setBackgroundColor(Color.TRANSPARENT);
        mContractFragmentBtn.setBackgroundColor(Color.TRANSPARENT);
        mFindFragmrntBtn.setBackgroundColor(Color.TRANSPARENT);
        mSetFragmentBtn.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("userId", userId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_near_contract:
                openNearFragment(bundle);
                break;
            case R.id.btn_all_contract:
                openContractFragment(bundle);
                break;
            case R.id.btn_find:
                openFindFragment(bundle);
                break;
            case R.id.btn_set:
                openSetFragment(bundle);
                break;
        }
    }

    private void openNearFragment(Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 若NearFragment还未初始化，则将其初始化之后添加到事务中
        if (mNearFragment == null) {
            mNearFragment = new NearFragment();
            mNearFragment.setArguments(bundle);
//            transaction.add(mNearFragment, NEAR_FRAGMENT_TAG);
            transaction.add(R.id.fragment_container, mNearFragment);
        }
        // 先隐藏所有的Fragment
        hideAllFragment(transaction);
        // 在显示需要展示出来的Fragment
        transaction.show(mNearFragment);
        // 提交事务
        transaction.commit();
    }

    private void openContractFragment(Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 若NearFragment还未初始化，则将其初始化之后添加到事务中
        if (mContractFragment == null) {
            mContractFragment = new ContractFragment();
//            transaction.add(mContractFragment, CONTRACT_FRAGMENT_TAG);
            mContractFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, mContractFragment);
        }
        // 先隐藏所有的Fragment
        hideAllFragment(transaction);
        // 在显示需要展示出来的Fragment
        transaction.show(mContractFragment);
        // 提交事务
        transaction.commit();
    }

    private void openFindFragment(Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 若NearFragment还未初始化，则将其初始化之后添加到事务中
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
            mFindFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, mFindFragment);
//            transaction.add(mFindFragment, FIND_FRAGMENT_TAG);
        }
        // 先隐藏所有的Fragment
        hideAllFragment(transaction);
        // 在显示需要展示出来的Fragment
        transaction.show(mFindFragment);
        // 提交事务
        transaction.commit();
    }

    private void openSetFragment(Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 若NearFragment还未初始化，则将其初始化之后添加到事务中
        if (mSetFragment == null) {
            mSetFragment = new SetFragment();
            mSetFragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, mSetFragment);
//            transaction.add(mSetFragment, SET_FRAGMENT_TAG);
        }
        // 先隐藏所有的Fragment
        hideAllFragment(transaction);
        // 在显示需要展示出来的Fragment
        transaction.show(mSetFragment);
        // 提交事务
        transaction.commit();
    }
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mNearFragment != null) {
            transaction.hide(mNearFragment);
        }
        if (mContractFragment != null) {
            transaction.hide(mContractFragment);
        }
        if (mFindFragment != null) {
            transaction.hide(mFindFragment);
        }
        if (mSetFragment != null) {
            transaction.hide(mSetFragment);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "HomeOnActivityResult: ");
        switch (requestCode) {
            // requestCode==1时表示回传给NearFragment
            case 1:
                openNearFragment(bundle);
                mNearFragment.onActivityResult(requestCode, resultCode, data);
            break;
        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_near);
//        Fragment fragment = fragmentManager.findFragmentByTag(NEAR_FRAGMENT_TAG);
    }
}
