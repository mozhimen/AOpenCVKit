package com.mozhimen.opencvk.test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.mozhimen.kotlin.utilk.android.graphics.UtilKBitmapDeal
import com.mozhimen.mvvmk.bases.activity.viewbinding.BaseActivityVB
import com.mozhimen.opencvk.libs.OpenCVKLib
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import com.mozhimen.opencvk.test.databinding.ActivityDemoBinding

class DemoActivity : BaseActivityVB<ActivityDemoBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        if (OpenCVKLib.initSDK()) {
            val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.tour_green)
            val orgBitmap = BitmapFactory.decodeResource(resources, R.mipmap.tour_green)

            val bitmapPair = cropSameSize(bitmap, orgBitmap)
            vb.mainImg1.setImageBitmap(bitmapPair.first)
            vb.mainImg2.setImageBitmap(bitmapPair.second)

            vb.mainBtnCompare.setOnClickListener {
                val mat1 = Mat()
                val mat2 = Mat()
                val mat11 = Mat()
                val mat22 = Mat()
                Utils.bitmapToMat(bitmapPair.first, mat1)
                Utils.bitmapToMat(bitmapPair.second, mat2)

                Imgproc.cvtColor(mat1, mat11, Imgproc.COLOR_BGR2GRAY)
                Imgproc.cvtColor(mat2, mat22, Imgproc.COLOR_BGR2GRAY)
                comPareHist(mat11, mat22)
            }
        } else {
            Log.e(TAG, "onCreate: OpenCV init fail")
        }
    }

    private fun comPareHist(srcMat: Mat, desMat: Mat) {
        srcMat.convertTo(srcMat, CvType.CV_32F)
        desMat.convertTo(desMat, CvType.CV_32F)
        val target = Imgproc.compareHist(srcMat, desMat, Imgproc.CV_COMP_CORREL)
        vb.mainTv.text = "相似度: $target"
    }

    private fun cropSameSize(bitmap1: Bitmap, bitmap2: Bitmap): Pair<Bitmap, Bitmap> {
        val minWidth = kotlin.math.min(bitmap1.width, bitmap2.width)
        val minHeight = kotlin.math.min(bitmap1.height, bitmap2.height)
        val bitmap1XY: Pair<Int, Int> = ((bitmap1.width - minWidth) / 2) to ((bitmap1.height - minHeight) / 2)
        val bitmap2XY: Pair<Int, Int> = ((bitmap2.width - minWidth) / 2) to ((bitmap2.height - minHeight) / 2)
        return UtilKBitmapDeal.applyBitmapAnyCrop(bitmap1, minWidth, minHeight, bitmap1XY.first, bitmap1XY.second) to
                UtilKBitmapDeal.applyBitmapAnyCrop(bitmap2, minWidth, minHeight, bitmap2XY.first, bitmap2XY.second)
    }
}