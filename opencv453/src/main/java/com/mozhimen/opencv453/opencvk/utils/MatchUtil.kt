package com.mozhimen.opencv453.opencvk.utils

import android.util.Log
import com.mozhimen.kotlin.utilk.commons.IUtilK
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

/**
 * @ClassName OpenCVKMatch
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/6/15 13:53
 * @Version 1.0
 */
object MatchUtil : IUtilK {
    const val MATCH_MAX_VAL = 7.0E8
    const val MATCH_MIN_VAL = 6.5E8

    const val TM_SQDIFF = Imgproc.TM_SQDIFF
    const val TM_SQDIFF_NORMED = Imgproc.TM_SQDIFF_NORMED
    const val TM_CCOEFF = Imgproc.TM_CCOEFF
    const val TM_CCOEFF_NORMED = Imgproc.TM_CCOEFF_NORMED
    const val TM_CCORR = Imgproc.TM_CCORR
    const val TM_CCORR_NORMED = Imgproc.TM_CCORR_NORMED

    /**
     * 模板匹配
     * @param srcMat Mat
     * @param templateMat Mat
     * @param method Int
     * @return Mat
     */
    @JvmStatic
    fun templateMatch(srcMat: Mat, templateMat: Mat, method: Int = TM_SQDIFF): Mat {
        val srcRgbMat = Mat()
        val temRgbMat = Mat()
        val result = Mat()
        var drawResMat = Mat()
        try {
            Imgproc.cvtColor(srcMat, srcRgbMat, Imgproc.COLOR_BGR2RGB)
            Imgproc.cvtColor(templateMat, temRgbMat, Imgproc.COLOR_BGR2RGB)
            drawResMat = srcRgbMat.clone()

            Imgproc.matchTemplate(srcRgbMat, temRgbMat, result, method)
            val minMaxLoc = Core.minMaxLoc(result)
            Log.v(TAG, "templateMatch maxVal = ${minMaxLoc.maxVal}, maxLocation = ${minMaxLoc.maxLoc}, minVal = ${minMaxLoc.minVal}, minLocation = ${minMaxLoc.minLoc}")
            val topLeft = if (method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
                minMaxLoc.minLoc
            } else {
                minMaxLoc.maxLoc
            }
            val rect = Rect(topLeft, Size(templateMat.cols().toDouble(), templateMat.rows().toDouble()))
            Imgproc.rectangle(drawResMat, rect, Scalar(255.0, 0.0, 0.0), 4, Imgproc.LINE_8)
            return drawResMat
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            srcRgbMat.release()
            temRgbMat.release()
            result.release()
        }
        return drawResMat
    }

    /**
     * 模板匹配
     * @param srcMat Mat
     * @param templateMat Mat
     * @param method Int
     * @return Pair<Double, Double>
     */
    @JvmStatic
    fun templateMatch2(srcMat: Mat, templateMat: Mat, method: Int = TM_SQDIFF): Pair<Double, Double> {
        val srcRgbMat = Mat()
        val temRgbMat = Mat()
        val result = Mat()
        try {
            Imgproc.cvtColor(srcMat, srcRgbMat, Imgproc.COLOR_BGR2RGB)
            Imgproc.cvtColor(templateMat, temRgbMat, Imgproc.COLOR_BGR2RGB)

            Imgproc.matchTemplate(srcRgbMat, temRgbMat, result, method)
            val minMaxLoc = Core.minMaxLoc(result)
            return minMaxLoc.maxVal to minMaxLoc.minVal
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            srcRgbMat.release()
            temRgbMat.release()
            result.release()
        }
        return 0.0 to 0.0
    }

    /**
     * 霍夫圆匹配
     */
    @JvmStatic
    fun getCircleNum(matSrc: Mat): Int {
        val matGray = Mat()
        val matCircles = Mat()

        try {
            Imgproc.pyrMeanShiftFiltering(matSrc, matSrc, 15.0, 80.0)
            Imgproc.cvtColor(matSrc, matGray, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(matGray, matGray, Size(3.0, 3.0), 0.0)
            Imgproc.HoughCircles(matGray, matCircles, Imgproc.HOUGH_GRADIENT, 1.0, 20.0, 100.0, 25.0, 35, 70)
            return matCircles.cols()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            matSrc.release()
            matGray.release()
            matCircles.release()
        }
        return 0
    }

}