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

/**
 * Created by longjianlin on 15/1/21.
 */
public class ShareUtils {
    /**
     * 分享
     *
     * @param c
     * @param photoUpload
     */
    public static void share(Context c, PhotoUpload photoUpload, String platform, String content) {


        if ("SinaWeibo".equals(platform)) {//新浪微博
            shareToSinaWeibo(c, photoUpload, content);
        } else if ("TencentWeibo".equals(platform)) {//腾讯微博

        } else if ("QZone".equals(platform)) {//QQ空间

        } else if ("WechatMoments".equals(platform)) {//微信朋友圈

        } else if ("WechatFavorite".equals(platform)) {//微信朋友

        } else if ("Douban".equals(platform)) {//豆瓣

        } else if ("Instagram".equals(platform)) {//腾讯微博

        }


    }


    /**
     * 微信好用分享
     *
     * @param context
     * @param file
     */
    private void shareToWeiXinFriend(Context context, File file) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
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
     * @param files
     */
    private void shareMultiplePictureToTimeLine(Context context, File... files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        context.startActivity(intent);
    }

    /**
     * 发送微博
     *
     * @param context
     * @param photoUpload 分享图片
     * @param content     分享内容
     */
    public static void shareToSinaWeibo(Context context, PhotoUpload photoUpload, String content) {
        UploadQuality quality = photoUpload.getUploadQuality();

        //Uri uri = photoUpload.getOriginalPhotoUri();
        //String url = Utils.getPathFromContentUri(context.getContentResolver(), uri);
        File shareFile = photoUpload.getUploadSaveFile();
        if (shareFile.exists()) {
            shareFile.delete();
        }else{
            //shareFile = new File(url);
        }

        try {
            shareFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ComponentName comp = new ComponentName("com.sina.weibo", "com.sina.weibo.EditActivity");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        context.startActivity(intent);
    }


}
