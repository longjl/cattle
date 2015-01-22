/*
 * Copyright 2013 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cattle.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.cattle.Constants;
import com.cattle.PhotoUploadController;
import com.cattle.PhotupApplication;
import com.cattle.R;
import com.cattle.model.PhotoUpload;
import com.cattle.util.ShareUtils;

import java.util.List;

/**
 * 分享
 */
public class ShareFragment extends PhotupDialogFragment implements
        RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private PhotoUploadController photoUploadController;

    private RadioGroup mTargetRadioGroup;
    private ImageButton btn_share;
    private EditText ed_share_content;
    private String platform;
    private List<PhotoUpload> photoUploads;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_share, container, false);
        mTargetRadioGroup = (RadioGroup) view.findViewById(R.id.rg_share);
        mTargetRadioGroup.setOnCheckedChangeListener(this);

        btn_share = (ImageButton) view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);

        ed_share_content = (EditText) view.findViewById(R.id.ed_share_content);
        if (platform == null) {
            platform = Constants.SINA_WEIBO_KEY;
        }
        photoUploadController = PhotupApplication.getApplication(getActivity()).getPhotoUploadController();
        photoUploads = photoUploadController.getSelected();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_sina_weibo://新浪微博
                platform = Constants.SINA_WEIBO_KEY;
                break;
            case R.id.rb_tencent_weibo://腾讯微博
                platform = Constants.TENCENT_WEIBO_KEY;
                break;
            case R.id.rb_qzone://QQ空间
                platform = Constants.QZONE_KEY;
                break;
            case R.id.rb_wechat_moments://微信朋友圈
                platform = Constants.WECHAT_MOMENTS_KEY;
                break;
            case R.id.rb_wechat_favorite://微信朋友
                platform = Constants.WECHAT_FAVORITE_KEY;
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        if (photoUploads != null && photoUploads.size() > 0) {
            ShareUtils.share(getActivity(), photoUploads.get(0), platform, ed_share_content.getText().toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btn_share.getId()) {
            share();
        }
    }
}
