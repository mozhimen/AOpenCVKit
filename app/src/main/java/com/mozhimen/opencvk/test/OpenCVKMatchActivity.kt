package com.mozhimen.opencvk.test

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.os.Bundle
import androidx.camera.core.ImageProxy
import com.mozhimen.kotlin.lintk.optins.OFieldCall_Close
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_CAMERA
import com.mozhimen.kotlin.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA
import com.mozhimen.kotlin.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA_ANY
import com.mozhimen.kotlin.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA_AUTOFOCUS
import com.mozhimen.kotlin.utilk.android.graphics.UtilKBitmapDeal
import com.mozhimen.kotlin.utilk.android.graphics.applyBitmapAnyCrop
import com.mozhimen.kotlin.utilk.android.graphics.drawable2bitmap
import com.mozhimen.camerak.camerax.annors.ACameraKXFacing
import com.mozhimen.camerak.camerax.commons.ICameraXKFrameListener
import com.mozhimen.camerak.camerax.mos.CameraKXConfig
import com.mozhimen.camerak.camerax.utils.imageProxyJpeg2bitmapJpeg
import com.mozhimen.camerak.camerax.utils.imageProxyYuv4208882bitmapJpeg
import com.mozhimen.kotlin.elemk.android.cons.CPermission
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityStart
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.kotlin.utilk.wrapper.UtilKScreen
import com.mozhimen.manifestk.permission.ManifestKPermission
import com.mozhimen.manifestk.permission.annors.APermissionCheck
import com.mozhimen.bindk.bases.viewbinding.activity.BaseActivityVB
import com.mozhimen.opencvk.OpenCVKMatch
import com.mozhimen.opencvk.OpenCVKTrans
import com.mozhimen.opencvk.exts.setMat
import com.mozhimen.opencvk.libs.OpenCVKLib
import com.mozhimen.opencvk.test.databinding.ActivityOpencvkMatchBinding
import java.util.concurrent.locks.ReentrantLock

@OPermission_CAMERA
@OUseFeature_HARDWARE_CAMERA
@OUseFeature_HARDWARE_CAMERA_ANY
@OUseFeature_HARDWARE_CAMERA_AUTOFOCUS
@APermissionCheck(CPermission.CAMERA)
class OpenCVKMatchActivity : BaseActivityVB<ActivityOpencvkMatchBinding>() {
    private val _templateMat by lazy { OpenCVKTrans.bitmap2Mat(UtilKRes.gainDrawable(R.mipmap.opencvk_contrast_test)!!.drawable2bitmap()) }

    override fun initData(savedInstanceState: Bundle?) {
        ManifestKPermission.requestPermissions(this) {
            if (it) {
                super.initData(savedInstanceState)
            } else {
                UtilKActivityStart.startSettingApplicationDetailsSettings(this)
            }
        }
    }

    @Throws(Exception::class)
    override fun initView(savedInstanceState: Bundle?) {
        require(OpenCVKLib.initSDK()) { "$TAG opencv init fail" }
        initCamera()
    }

    private fun initCamera() {
        vb.opencvkMatchPreview.apply {
            initCameraKX(this@OpenCVKMatchActivity, CameraKXConfig(facing = ACameraKXFacing.BACK))
            setCameraXFrameListener(_frameAnalyzer)
        }
    }

    @OptIn(OFieldCall_Close::class)
    private val _frameAnalyzer: ICameraXKFrameListener by lazy {
        object : ICameraXKFrameListener {
            private val _reentrantLock = ReentrantLock()

            @SuppressLint("UnsafeOptInUsageError")
            override fun invoke(imageProxy: ImageProxy) {
                try {
                    _reentrantLock.lock()
                    val bitmap: Bitmap =  if (imageProxy.format == ImageFormat.YUV_420_888) {
                        imageProxy.imageProxyYuv4208882bitmapJpeg()
                    } else {
                        imageProxy.imageProxyJpeg2bitmapJpeg()
                    }
                    val rotateBitmap = UtilKBitmapDeal.applyBitmapAnyRotate(bitmap, 90f)
                    val ratio: Double =
                        vb.opencvkMatchQrscan.getRectSize().toDouble() / UtilKScreen.getWidth().toDouble()

                    val cropBitmap = rotateBitmap.applyBitmapAnyCrop(
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

                imageProxy.close()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _templateMat.release()
    }
}