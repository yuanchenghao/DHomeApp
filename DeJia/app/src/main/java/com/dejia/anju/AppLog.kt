package com.dejia.anju

import android.util.Log

object AppLog {
    var className //类名
            : String? = null
    var methodName //方法名
            : String? = null
    var lineNumber //行数
            = 0

    /**
     * 判断是否可以调试
     * @return
     */
    val isDebuggable: Boolean
        get() = BuildConfig.DEBUG

    private fun createLog(log: String): String {
        val buffer = StringBuffer()
        buffer.append("================")
        buffer.append(methodName)
        buffer.append("(").append(className).append(":").append(lineNumber).append(")================:")
        buffer.append(log)
        return buffer.toString()
    }

    /**
     * 获取文件名、方法名、所在行数
     * @param sElements
     */
    private fun getMethodNames(sElements: Array<StackTraceElement>) {
        className = sElements[1].fileName
        methodName = sElements[1].methodName
        lineNumber = sElements[1].lineNumber
    }

    @JvmStatic
    fun e(message: String) {
        if (!isDebuggable) {
            return
        }
        getMethodNames(Throwable().stackTrace)
        Log.e(className, createLog(message))
    }

    @JvmStatic
    fun i(message: String) {
        if (!isDebuggable) {
            return
        }
        getMethodNames(Throwable().stackTrace)
        Log.i(className, createLog(message))
    }

    fun d(message: String) {
        if (!isDebuggable) {
            return
        }
        getMethodNames(Throwable().stackTrace)
        Log.d(className, createLog(message))
    }

    fun v(message: String) {
        if (!isDebuggable) {
            return
        }
        getMethodNames(Throwable().stackTrace)
        Log.v(className, createLog(message))
    }

    fun w(message: String) {
        if (!isDebuggable) {
            return
        }
        getMethodNames(Throwable().stackTrace)
        Log.w(className, createLog(message))
    }
}