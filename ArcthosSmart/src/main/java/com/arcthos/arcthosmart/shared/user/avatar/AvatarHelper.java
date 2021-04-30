package com.arcthos.arcthosmart.shared.user.avatar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import static com.arcthos.arcthosmart.shared.user.avatar.AvatarOrganizer.AVATAR_FILE_PATH;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 28/11/2018
 * Empresa : TOPi
 * ************************************************************
 */
public class AvatarHelper {
    public static void putAvatarIntoView(ImageView imageView) {
        Bitmap bMap = BitmapFactory.decodeFile(AVATAR_FILE_PATH);
        imageView.setImageBitmap(bMap);
    }
}
