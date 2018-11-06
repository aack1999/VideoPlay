/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.framework.core.viewcontroller;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framework.core.R;
import com.framework.core.common.AppSetting;
import com.framework.core.utils.imageloader.ImageLoaderUtil;
import com.framework.core.widget.FlatButton;


public class VaryViewHelperController {

    public IVaryViewHelper helper;

    public VaryViewHelperController(View view) {
        this(new VaryViewHelper(view));
    }

    public VaryViewHelperController(IVaryViewHelper helper) {
        super();
        this.helper = helper;
    }


    public void showEmpty(String emptyMsg,String btnText, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.framework_view_controller_empty);
        TextView textView = (TextView) layout.findViewById(R.id.message_info);
        ImageView img=(ImageView)layout.findViewById(R.id.message_icon);
        textView.setText(emptyMsg);
        FlatButton btn=(FlatButton)layout.findViewById(R.id.btn);
        if (TextUtils.isEmpty(btnText)){
            btn.setVisibility(View.GONE);
        }else {
            btn.setText(btnText);
            btn.setVisibility(View.VISIBLE);
        }
        if (null != onClickListener) {
            btn.setOnClickListener(onClickListener);
        }
        Glide.with(layout.getContext()).load(R.drawable.no_date).into(img);
        helper.showLayout(layout);
    }

    public void showLoading(String msg) {
        View layout = helper.inflate(R.layout.framework_view_controller_loading);
        if (!TextUtils.isEmpty(msg)) {
            TextView textView = (TextView) layout.findViewById(R.id.message_info);
            textView.setText(msg);
        }
        helper.showLayout(layout);
    }



    public void restore() {
        helper.restoreView();
    }
}
