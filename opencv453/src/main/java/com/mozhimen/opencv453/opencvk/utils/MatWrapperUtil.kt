package com.mozhimen.opencv453.opencvk.utils

import android.widget.ImageView
import org.opencv.core.Mat

/**
 * @ClassName OpenCVKExts
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/6/15 0:35
 * @Version 1.0
 */
fun ImageView.setImageMat(mat: Mat) {
    MatWrapperUtil.setImageMat(this, mat)
}

object MatWrapperUtil {
    @JvmStatic
    fun setImageMat(imageView: ImageView, mat: Mat) {
        imageView.setImageBitmap(MatFormatUtil.mat2bitmap(mat))
    }
}