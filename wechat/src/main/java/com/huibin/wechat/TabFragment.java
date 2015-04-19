package com.huibin.wechat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by huibin on 15/4/18.
 */
public class TabFragment extends Fragment {

    private String mContext ;
    public static final String KEY_CONTEXT = "context" ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mContext = getArguments().getString(KEY_CONTEXT) ;
        } else {
            mContext = "Default" ;
        }

        TextView tv = new TextView(getActivity()) ;
        tv.setText(mContext);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#ffffffff"));

        return tv;
    }
}
