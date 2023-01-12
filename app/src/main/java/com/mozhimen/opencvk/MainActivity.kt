package com.mozhimen.opencvk

import android.view.View
import com.mozhimen.basick.elemk.activity.bases.BaseActivityVB
import com.mozhimen.basick.utilk.exts.start
import com.mozhimen.opencvk.databinding.ActivityMainBinding


/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/12 11:43
 * @Version 1.0
 */
class MainActivity : BaseActivityVB<ActivityMainBinding>() {
    fun goDemo(view: View) {
        start<DemoActivity>()
    }

    fun goOpenCVKContrast(view: View) {
        start<OpenCVKContrastActivity>()
    }

    fun goOpenCVKShape(view: View) {
        start<OpenCVKShapeActivity>()
    }

    fun goOpenCVKMatch(view: View) {
        start<OpenCVKMatchActivity>()
    }
}