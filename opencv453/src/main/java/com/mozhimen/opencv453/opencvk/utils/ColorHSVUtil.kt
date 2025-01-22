package com.mozhimen.opencv453.opencvk.utils

import android.graphics.Bitmap
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.opencv453.opencvk.cons.EColorHSV
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * @ClassName OpenCVKHSV
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/9 11:16
 * @Version 1.0
 */
object ColorHSVUtil : IUtilK {
    /**
     * 颜色过滤
     * @param bitmap Bitmap
     * @param colorHSV EColorHSV
     * @return Bitmap
     */
    @JvmStatic
    fun colorFilter(bitmap: Bitmap, colorHSV: EColorHSV): Bitmap {
        val matSrc = MatFormatUtil.bitmap2mat(bitmap)
        val matHsv = Mat()
        var hsv: DoubleArray
        try {
            Imgproc.cvtColor(matSrc, matHsv, Imgproc.COLOR_BGR2HSV)
            val rowNum = matHsv.rows()
            val colNum = matHsv.cols()
            for (i in 0 until rowNum) {
                for (j in 0 until colNum) {
                    hsv = matHsv.get(i, j).clone()
                    if (!(hsv[0] in colorHSV.hMin..colorHSV.hMax && hsv[1] in colorHSV.sMin..colorHSV.sMax && hsv[2] in colorHSV.vMin..colorHSV.vMax)) {
                        matHsv.put(i, j, 0.0, 0.0, 255.0)
                    }
                }
            }
            return MatFormatUtil.mat2bitmap(matHsv)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            matSrc.release()
            matHsv.release()
        }
        return bitmap
    }

    /**
     * 颜色过滤
     * @param matSrc Mat
     * @param colorHSV EColorHSV
     * @return Mat
     */
    @JvmStatic
    fun colorFilter(matSrc: Mat, colorHSV: EColorHSV): Mat {
        val matHsv = Mat()
        var hsv: DoubleArray
        try {
            Imgproc.cvtColor(matSrc, matHsv, Imgproc.COLOR_BGR2HSV)
            val rowNum = matHsv.rows()
            val colNum = matHsv.cols()
            for (i in 0 until rowNum) {
                for (j in 0 until colNum) {
                    hsv = matHsv.get(i, j).clone()
                    if (!(hsv[0] in colorHSV.hMin..colorHSV.hMax && hsv[1] in colorHSV.sMin..colorHSV.sMax && hsv[2] in colorHSV.vMin..colorHSV.vMax)) {
                        matHsv.put(i, j, 0.0, 0.0, 255.0)
                    }
                }
            }
            return matHsv
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return matHsv
    }
}