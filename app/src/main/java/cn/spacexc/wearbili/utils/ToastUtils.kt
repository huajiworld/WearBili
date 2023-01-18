package cn.spacexc.wearbili.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import cn.spacexc.wearbili.Application
import cn.spacexc.wearbili.R
import cn.spacexc.wearbili.manager.SettingsManager.isDebug

/**
 * Created by XC-Qan on 2022/7/14.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

object ToastUtils {
    private const val ContentId: Int = R.id.toastTextView
    private const val LayoutId: Int = R.layout.layout_toast_test
    fun makeText(content: String): Toast {
        val toast = Toast(Application.getContext())
        val view: View =
            LayoutInflater.from(Application.getContext()).inflate(LayoutId, null, false)
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        val text: TextView = view.findViewById(ContentId)
        toast.view = view
        text.text = content
        return toast
    }

    fun showText(content: String) {
        val toast = Toast(Application.getContext())
        val view: View =
            LayoutInflater.from(Application.getContext()).inflate(LayoutId, null, false)
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        val text: TextView = view.findViewById(ContentId)
        toast.view = view
        text.text = content
        toast.show()
    }

    fun debugShowText(content: String) {
        if (isDebug()) {
            val toast = Toast(Application.getContext())
            val view: View =
                LayoutInflater.from(Application.getContext()).inflate(LayoutId, null, false)
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
            toast.duration = Toast.LENGTH_SHORT
            val text: TextView = view.findViewById(ContentId)
            toast.view = view
            text.text = content
            toast.show()
        }

    }

    fun Any.debugToast(description: String?) {
        if (isDebug()) {
            makeText("$description: $this").show()
        }
    }

    fun <T> T.debugToast(): T {
        if (isDebug()) {
            makeText("$this").show()
        }
        return this
    }

    fun debugToast(content: String?) {
        if (isDebug()) {
            makeText("$content").show()
        }
    }

    fun <T> T.debugToastWithGeneric(): T {
        if (isDebug()) makeText("$this")
        return this
    }
}