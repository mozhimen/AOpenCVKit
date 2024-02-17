package com.mozhimen.opencvk.test

import android.view.View
import com.mozhimen.basick.elemk.androidx.appcompat.bases.databinding.BaseActivityVB
import com.mozhimen.basick.lintk.optins.permission.OPermission_CAMERA
import com.mozhimen.basick.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA
import com.mozhimen.basick.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA_ANY
import com.mozhimen.basick.lintk.optins.use_feature.OUseFeature_HARDWARE_CAMERA_AUTOFOCUS
import com.mozhimen.basick.utilk.android.content.startContext
import com.mozhimen.opencvk.test.databinding.ActivityMainBinding


/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/12 11:43
 * @Version 1.0
 */
class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    fun goDemo(view: View) {
        startContext<DemoActivity>()
    }

    @OptIn(OPermission_CAMERA::class, OUseFeature_HARDWARE_CAMERA::class, OUseFeature_HARDWARE_CAMERA_ANY::class, OUseFeature_HARDWARE_CAMERA_AUTOFOCUS::class)
    fun goOpenCVKContrast(view: View) {
        startContext<OpenCVKContrastActivity>()
    }

    fun goOpenCVKShape(view: View) {
        startContext<OpenCVKShapeActivity>()
    }

    fun goOpenCVKMatch(view: View) {
        startContext<OpenCVKMatchActivity>()
    }
}