package com.huibin.wechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{

    private ViewPager mViewPager ;
    private String[] mContexts = {"微信","通讯录","发现","我"} ;
    private List<TabFragment> mTabs = new ArrayList<TabFragment>() ;
    private FragmentPagerAdapter mAdapter ;

    private List<MagicTabIndicator> mTabIndicators = new ArrayList<MagicTabIndicator>() ;

    private final int MAIN = 0 ;
    private final int CONTACT = 1 ;
    private final int FIND = 2 ;
    private final int ME = 3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOverflowButtonShowAlways();
        getActionBar().setDisplayShowHomeEnabled(false);

        initView() ;
        initData() ;
    }

    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.view_pager) ;
        mViewPager.setOnPageChangeListener(this);

        MagicTabIndicator first = (MagicTabIndicator) findViewById(R.id.tab_indicator_first);
        mTabIndicators.add(first) ;
        MagicTabIndicator second = (MagicTabIndicator) findViewById(R.id.tab_indicator_second);
        mTabIndicators.add(second) ;
        MagicTabIndicator third = (MagicTabIndicator) findViewById(R.id.tab_indicator_third);
        mTabIndicators.add(third) ;
        MagicTabIndicator fourth = (MagicTabIndicator) findViewById(R.id.tab_indicator_fourth);
        mTabIndicators.add(fourth) ;

        first.setOnClickListener(this);
        second.setOnClickListener(this) ;
        third.setOnClickListener(this);
        fourth.setOnClickListener(this);

        first.setIconAlpha(1.0f);

    }

    private void initData() {
        for (String context : mContexts) {
            TabFragment tab = new TabFragment() ;
            Bundle bundle = new Bundle() ;
            bundle.putString(TabFragment.KEY_CONTEXT,context);
            tab.setArguments(bundle);
            mTabs.add(tab) ;
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        } ;
        mViewPager.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                Toast.makeText(this,"search is clicked!",Toast.LENGTH_SHORT).show() ;
                break ;
            case R.id.action_group_chat:
                Toast.makeText(this,"group chat is clicked!",Toast.LENGTH_SHORT).show() ;
                break ;
            case R.id.action_add_friend:
                Toast.makeText(this,"add friend is clicked!",Toast.LENGTH_SHORT).show() ;
                break ;
            case R.id.action_scan:
                Toast.makeText(this,"scan is clicked!",Toast.LENGTH_SHORT).show() ;
                break ;
            case R.id.action_feedback:
                Toast.makeText(this,"feedback is clicked!",Toast.LENGTH_SHORT).show() ;
                break ;

        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if (Window.FEATURE_ACTION_BAR == featureId && menu!=null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE) ;
                    method.setAccessible(true);
                    method.invoke(menu,true) ;
                } catch (Exception e) {

                }
            }

        }


        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowButtonShowAlways() {
        ViewConfiguration config = ViewConfiguration.get(this) ;
        try{
            Field hasPermanentMenuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey") ;
            hasPermanentMenuKey.setAccessible(true) ;
            hasPermanentMenuKey.setBoolean(config,false) ;
        } catch (Exception e) {

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.e("TAG","position: "+position + " positionOffset: " + positionOffset + " positionOffsetPixels: " + positionOffsetPixels) ;

        if (positionOffset > 0) {

            MagicTabIndicator leftIndicator = mTabIndicators.get(position) ;
            MagicTabIndicator rightIndicator = mTabIndicators.get(++position) ;
            leftIndicator.setIconAlpha(1-positionOffset);
            rightIndicator.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        clearAllTabs();
        switch (v.getId()) {
            case R.id.tab_indicator_first:
                mTabIndicators.get(MAIN).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(MAIN,false);
                break;
            case R.id.tab_indicator_second:
                mTabIndicators.get(CONTACT).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(CONTACT,false);
                break ;
            case R.id.tab_indicator_third:
                mTabIndicators.get(FIND).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(FIND,false);
                break;
            case R.id.tab_indicator_fourth:
                mTabIndicators.get(ME).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(ME,false);
                break;
        }
    }

    private void clearAllTabs() {
        for(MagicTabIndicator indicator : mTabIndicators) {
            indicator.setIconAlpha(0);
        }
    }
}
