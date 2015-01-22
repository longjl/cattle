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
package com.cattle.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.Checkable;

import com.cattle.PhotoUploadController;
import com.cattle.PhotupApplication;
import com.cattle.R;
import com.cattle.model.PhotoUpload;
import com.cattle.util.MediaStoreCursorHelper;
import com.cattle.views.PhotoItemLayout;
import com.cattle.views.PhotupImageView;

public class UsersPhotosCursorAdapter extends ResourceCursorAdapter {

    private final PhotoUploadController mController;

    public UsersPhotosCursorAdapter(Context context, Cursor c) {
        super(context, R.layout.item_grid_photo_user, c, 0);

        PhotupApplication app = PhotupApplication.getApplication(context);
        mController = app.getPhotoUploadController();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        PhotoItemLayout layout = (PhotoItemLayout) view;
        PhotupImageView iv = layout.getImageView();

        final PhotoUpload upload = MediaStoreCursorHelper.photosCursorToSelection(
                MediaStoreCursorHelper.MEDIA_STORE_CONTENT_URI, cursor);

        if (null != upload) {
            iv.setFadeInDrawables(false);
            iv.requestThumbnail(upload, false);
            layout.setPhotoSelection(upload);

            if (null != mController) {
                ((Checkable) view).setChecked(mController.isSelected(upload));
            }
        }
    }
}
