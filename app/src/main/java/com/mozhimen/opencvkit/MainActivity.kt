package com.mozhimen.opencvkit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mozhimen.opencvk.OpenCVK
import com.mozhimen.opencvkit.databinding.ActivityMainBinding
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity>>>>>"
    private val vb by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)

        if (OpenCVK.initSDK()) {
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

    @SuppressLint("SetTextI18n")
    private fun comPareHist(srcMat: Mat, desMat: Mat) {
        srcMat.convertTo(srcMat, CvType.CV_32F)
        desMat.convertTo(desMat, CvType.CV_32F)
        val target = Imgproc.compareHist(srcMat, desMat, Imgproc.CV_COMP_CORREL)
        vb.mainTv.text = "相似度: $target"
    }

    fun cropSameSize(bitmap1: Bitmap, bitmap2: Bitmap): Pair<Bitmap, Bitmap> {
        val minWidth = kotlin.math.min(bitmap1.width, bitmap2.width)
        val minHeight = kotlin.math.min(bitmap1.height, bitmap2.height)
        val bitmap1XY: Pair<Int, Int> = ((bitmap1.width - minWidth) / 2) to ((bitmap1.height - minHeight) / 2)
        val bitmap2XY: Pair<Int, Int> = ((bitmap2.width - minWidth) / 2) to ((bitmap2.height - minHeight) / 2)
        return cropBitmap(bitmap1, minWidth, minHeight, bitmap1XY.first, bitmap1XY.second) to
                cropBitmap(
            bitmap2,
            minWidth,
            minHeight,
            bitmap2XY.first,
            bitmap2XY.second
        )
    }

    fun cropBitmap(origin: Bitmap, width: Int, height: Int, x: Int, y: Int): Bitmap {
        val originWidth: Int = origin.width // 得到图片的宽，高
        val originHeight: Int = origin.height
        val cropWidth = if (width >= originWidth) originWidth else width // 裁切后所取的正方形区域边长
        val cropHeight = if (height >= originHeight) originHeight else height
        return Bitmap.createBitmap(origin, x, y, cropWidth, cropHeight, null, false)
    }
}