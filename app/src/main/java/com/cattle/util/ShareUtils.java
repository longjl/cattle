package com.cattle.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.cattle.model.PhotoUpload;
import com.cattle.model.UploadQuality;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longjianlin on 15/1/21.
 */
public class ShareUtils {
    /**
     * 分享
     *
     * @param c
     * @param uploads
     */
    public void share(Context c, List<PhotoUpload> uploads, String platform, String content) {
        if ("SinaWeibo".equals(platform)) {//新浪微博
            shareToSinaWeibo(c, uploads, content);
        } else if ("TencentWeibo".equals(platform)) {//腾讯微博

        } else if ("QZone".equals(platform)) {//QQ空间
            shareToQQ(c, uploads, content);
        } else if ("WechatMoments".equals(platform)) {//微信朋友圈
            shareMultiplePictureToTimeLine(c, uploads, content);
        } else if ("WechatFavorite".equals(platform)) {//微信朋友
            // shareToWeiXinFriend(c, uploads, content);
        } else if ("Douban".equals(platform)) {//豆瓣

        } else if ("Instagram".equals(platform)) {//腾讯微博

        }
    }


    private void shareToQQ(Context context, List<PhotoUpload> uploads, String content){

      /*  Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.tanth", "com.tencent.tauth.Tencent.shareToQQ");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image*//*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);*/


        PhotoUpload mUpload = uploads.get(0);//默认取第一个图片
        UploadQuality quality = mUpload.getUploadQuality();
        File uploadFile;
        if (UploadQuality.ORIGINAL == quality && !mUpload.requiresNativeEditing(context)) {
            final String filePath = Utils
                    .getPathFromContentUri(context.getContentResolver(),
                            mUpload.getOriginalPhotoUri());
            uploadFile = new File(filePath);
        } else {
            uploadFile = mUpload.getUploadSaveFile();
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            Bitmap bitmap = mUpload.getUploadImage(context, quality);
            OutputStream os = null;
            try {
                uploadFile.createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(uploadFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality.getJpegQuality(), os);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bitmap.recycle();
        }
        ComponentName comp = new ComponentName("com.tencent", "com.tencent.tauth.Tencent.shareToQQ");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(uploadFile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        context.startActivity(intent);
    }



    /**
     * 微信好用分享
     *
     * @param context
     * @param uploads
     */
    private void shareToWeiXinFriend(Context context, List<PhotoUpload> uploads, String content) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("Kdescription", content);//发表的内容或者描述
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (PhotoUpload mUpload : uploads) {
            File uploadFile;
            UploadQuality quality = mUpload.getUploadQuality();
            if (UploadQuality.ORIGINAL == quality && !mUpload.requiresNativeEditing(context)) {
                final String filePath = Utils
                        .getPathFromContentUri(context.getContentResolver(),
                                mUpload.getOriginalPhotoUri());
                uploadFile = new File(filePath);
            } else {
                uploadFile = mUpload.getUploadSaveFile();
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                Bitmap bitmap = mUpload.getUploadImage(context, quality);
                OutputStream os = null;
                try {
                    uploadFile.createNewFile();
                    os = new BufferedOutputStream(new FileOutputStream(uploadFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality.getJpegQuality(), os);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != os) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                bitmap.recycle();
            }
            imageUris.add(Uri.fromFile(uploadFile));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

        //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }


    /**
     * 分享到微信朋友圈单张图
     *
     * @param context
     * @param file
     */
    private void shareToTimeLine(Context context, File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(intent);
    }

    /**
     * 分享到微信朋友圈多张图片
     *
     * @param context
     * @param uploads
     */
    private void shareMultiplePictureToTimeLine(Context context, List<PhotoUpload> uploads, String content) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        intent.putExtra("Kdescription", content);//发表的内容或者描述
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (PhotoUpload mUpload : uploads) {
            File uploadFile;
            UploadQuality quality = mUpload.getUploadQuality();
            if (UploadQuality.ORIGINAL == quality && !mUpload.requiresNativeEditing(context)) {
                final String filePath = Utils
                        .getPathFromContentUri(context.getContentResolver(),
                                mUpload.getOriginalPhotoUri());
                uploadFile = new File(filePath);
            } else {
                uploadFile = mUpload.getUploadSaveFile();
                if (uploadFile.exists()) {
                    uploadFile.delete();
                }
                Bitmap bitmap = mUpload.getUploadImage(context, quality);
                OutputStream os = null;
                try {
                    uploadFile.createNewFile();
                    os = new BufferedOutputStream(new FileOutputStream(uploadFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality.getJpegQuality(), os);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (null != os) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                bitmap.recycle();
            }
            imageUris.add(Uri.fromFile(uploadFile));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(intent);
    }

    /**
     * 发送微博
     *
     * @param context
     * @param uploads 分享图片
     * @param content 分享内容
     */
    public static void shareToSinaWeibo(Context context, List<PhotoUpload> uploads, String content) {
        PhotoUpload mUpload = uploads.get(0);//默认取第一个图片
        UploadQuality quality = mUpload.getUploadQuality();
        File uploadFile;
        if (UploadQuality.ORIGINAL == quality && !mUpload.requiresNativeEditing(context)) {
            final String filePath = Utils
                    .getPathFromContentUri(context.getContentResolver(),
                            mUpload.getOriginalPhotoUri());
            uploadFile = new File(filePath);
        } else {
            uploadFile = mUpload.getUploadSaveFile();
            if (uploadFile.exists()) {
                uploadFile.delete();
            }
            Bitmap bitmap = mUpload.getUploadImage(context, quality);
            OutputStream os = null;
            try {
                uploadFile.createNewFile();
                os = new BufferedOutputStream(new FileOutputStream(uploadFile));
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality.getJpegQuality(), os);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bitmap.recycle();
        }
        ComponentName comp = new ComponentName("com.sina.weibo", "com.sina.weibo.EditActivity");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(uploadFile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        context.startActivity(intent);
    }


}
