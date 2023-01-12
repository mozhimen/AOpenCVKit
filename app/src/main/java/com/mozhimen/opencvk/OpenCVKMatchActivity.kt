package com.mozhimen.opencvk

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.os.Bundle
import androidx.camera.core.ImageProxy
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.permissionk.PermissionK
import com.mozhimen.basick.permissionk.annors.APermissionKCheck
import com.mozhimen.basick.permissionk.annors.APermissionKRequire
import com.mozhimen.basick.permissionk.cons.CPermission
import com.mozhimen.basick.permissionk.cons.CUseFeature
import com.mozhimen.basick.utilk.UtilKPermission
import com.mozhimen.basick.utilk.UtilKRes
import com.mozhimen.basick.utilk.UtilKScreen
import com.mozhimen.basick.utilk.bitmap.UtilKBitmapDeal
import com.mozhimen.basick.utilk.exts.cropBitmap
import com.mozhimen.basick.utilk.exts.drawable2Bitmap
import com.mozhimen.componentk.cameraxk.annors.ACameraXKFacing
import com.mozhimen.componentk.cameraxk.commons.ICameraXKFrameListener
import com.mozhimen.componentk.cameraxk.helpers.ImageConverter
import com.mozhimen.componentk.cameraxk.mos.CameraXKConfig
import com.mozhimen.opencvk.databinding.ActivityOpencvkMatchBinding
import com.mozhimen.opencvk.exts.setMat
import com.mozhimen.opencvk.libs.OpenCVKLib
import java.util.concurrent.locks.ReentrantLock

@APermissionKRequire(CPermission.CAMERA, CUseFeature.CAMERA, CUseFeature.CAMERA_AUTOFOCUS)
@APermissionKCheck(CPermission.CAMERA)
class OpenCVKMatchActivity : BaseActivityVB<ActivityOpencvkMatchBinding>() {
    private val _templateMat by lazy { OpenCVKTrans.bitmap2Mat(UtilKRes.getDrawable(R.mipmap.opencvk_contrast_test)!!.drawable2Bitmap()) }

    override fun initData(savedInstanceState: Bundle?) {
        PermissionK.initPermissions(this) {
            if (it) {
                super.initData(savedInstanceState)
            } else {
                UtilKPermission.openSettingSelf(this)
            }
        }
    }

    @Throws(Exception::class)
    override fun initView(savedInstanceState: Bundle?) {
        require(OpenCVKLib.initSDK()) { "$TAG opencv init fail" }
        initCamera()
    }

    private fun initCamera() {
        vb.opencvkMatchPreview.initCamera(this, CameraXKConfig(facing = ACameraXKFacing.BACK))
        vb.opencvkMatchPreview.setCameraXKFrameListener(_frameAnalyzer)
        vb.opencvkMatchPreview.startCamera()
    }

    private val _frameAnalyzer: ICameraXKFrameListener by lazy {
        object : ICameraXKFrameListener {
            private val _reentrantLock = ReentrantLock()

            @SuppressLint("UnsafeOptInUsageError")
            override fun onFrame(image: ImageProxy) {
                try {
                    _reentrantLock.lock()
                    val bitmap: Bitmap = if (image.format == ImageFormat.YUV_420_888) {
                        ImageConverter.yuv420888Image2JpegBitmap(image)!!
                    } else {
                        ImageConverter.jpegImage2JpegBitmap(image)
                    }
                    val rotateBitmap = UtilKBitmapDeal.rotateBitmap(bitmap, 90)
                    val ratio: Double =
                        vb.opencvkMatchQrscan.getRectSize().toDouble() / UtilKScreen.getRealScreenWidth().toDouble()

                    val cropBitmap = rotateBitmap.cropBitmap(
                        (ratio * rotateBitmap.width).toInt(),
                        (ratio * rotateBitmap.width).toInt(),
                        ((1 - ratio) * rotateBitmap.width / 2).toInt(),
                        ((rotateBitmap.height - ratio * rotateBitmap.width) / 2).toInt()
                    )

                    val srcMat = OpenCVKTrans.bitmap2Mat(cropBitmap)

                    val resultMat = OpenCVKMatch.templateMatch(srcMat, _templateMat)
                    Thread.sleep(100)
                    try {
                        vb.opencvkMatchImg.setMat(resultMat)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        srcMat.release()
                        resultMat.release()
                    }
                } finally {
                    _reentrantLock.unlock()
                }

                image.close()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _templateMat.release()
    }
}