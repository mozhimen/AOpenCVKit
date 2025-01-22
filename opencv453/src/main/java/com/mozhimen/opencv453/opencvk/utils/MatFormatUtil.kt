package com.mozhimen.opencv453.opencvk.utils

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * @ClassName OpenCVKTrans
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/8 18:47
 * @Version 1.0
 */
fun Mat.mat2mat_gray(): Mat =
    MatFormatUtil.mat2mat_gray(this)

fun Bitmap.bitmap2mat(): Mat =
    MatFormatUtil.bitmap2mat(this)

fun Mat.mat2bitmap(): Bitmap =
    MatFormatUtil.mat2bitmap(this)

///////////////////////////////////////////////////////////////////////

object MatFormatUtil {
    /**
     * 转为灰度图
     */
    @JvmStatic
    fun mat2mat_gray(mat: Mat): Mat {
        val matGray = Mat()
        Imgproc.cvtColor(mat, matGray, Imgproc.COLOR_BGR2GRAY)
        return matGray
    }

    /**
     * bitmap转mat
     */
    @JvmStatic
    fun bitmap2mat(bitmap: Bitmap): Mat {
        val matSrc = Mat()
        Utils.bitmapToMat(bitmap, matSrc)
        return matSrc
    }

    /**
     * mat转bitmap
     */
    @JvmStatic
    fun mat2bitmap(mat: Mat): Bitmap {
        val bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmap)
        return bitmap
    }
}