package com.mozhimen.opencv453.opencvk.utils

import android.graphics.Bitmap
import org.opencv.core.CvType
import org.opencv.imgproc.Imgproc

import com.mozhimen.kotlin.utilk.commons.IUtilK


/**
 * @ClassName OpenCVKContrast
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/9 13:49
 * @Version 1.0
 */
object ContrastUtil : IUtilK {

    /**
     * 相似度对比
     * @param bitmap Bitmap
     * @param orgBitmap Bitmap
     * @return Double
     */
    @JvmStatic
    @Throws(Exception::class)
    fun similarity(bitmap: Bitmap, orgBitmap: Bitmap): Double {
        require(bitmap.width == orgBitmap.width && bitmap.height == orgBitmap.height) { "$TAG two bmp must have same size" }

        var similarity = 0.0
        val matSrc = MatFormatUtil.bitmap2mat(bitmap)
        val matDes = MatFormatUtil.bitmap2mat(orgBitmap)
        val matSrcGray = MatFormatUtil.mat2mat_gray(matSrc)
        val matDesGray = MatFormatUtil.mat2mat_gray(matDes)

        try {
            matSrcGray.convertTo(matSrcGray, CvType.CV_32F)
            matDesGray.convertTo(matDesGray, CvType.CV_32F)
            similarity = Imgproc.compareHist(matSrcGray, matDesGray, Imgproc.CV_COMP_CORREL)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            matSrc.release()
            matDes.release()
            matSrcGray.release()
            matDesGray.release()
        }
        return similarity
    }

}