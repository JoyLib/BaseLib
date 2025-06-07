package com.ho.baselib.utils

import android.util.Log
import java.io.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Logger 是一个简化的日志记录工具，支持输出到控制台、文件及网络。
 */
object Logger {

    private const val TAG = "Logger"  // 日志的标识
    private var isFileLoggingEnabled = false  // 是否启用文件日志记录
    private var logFile: File? = null  // 日志文件的路径
    private var networkLogEnabled = false  // 是否启用网络日志记录
    private var networkLogUrl: String? = null  // 网络日志的 URL

    // ExecutorService 用于处理异步日志写入
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val networkExecutorService: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * 日志级别枚举，定义不同的日志等级。
     */
    enum class LogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    /**
     * 初始化文件日志。
     *
     * @param logFilePath 日志文件的完整路径。
     */
    fun initFileLogging(logFilePath: String) {
        isFileLoggingEnabled = true  // 启用文件日志记录
        logFile = File(logFilePath)  // 创建文件对象
        if (!logFile!!.exists()) {  // 如果文件不存在，创建新文件
            logFile!!.createNewFile()
        }
    }

    /**
     * 初始化网络日志。
     *
     * @param url 网络日志服务器的 URL。
     */
    fun initNetworkLogging(url: String) {
        networkLogEnabled = true  // 启用网络日志
        this.networkLogUrl = url  // 设置网络 URL
    }

    /**
     * 记录到文件。
     *
     * @param message 要记录的日志消息。
     */
    private fun logToFile(message: String) {
        if (isFileLoggingEnabled && logFile != null) {  // 检查文件日志是否启用
            executorService.execute {  // 异步写入日志
                try {
                    BufferedWriter(FileWriter(logFile, true)).use { writer ->  // 追加模式写入
                        writer.append(message).append("\n")  // 添加新日志记录
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Failed to write log to file", e)  // 错误处理
                }
            }
        }
    }

    /**
     * 记录到网络。
     *
     * @param message 要记录的日志消息。
     */
    private fun logToNetwork(message: String) {
        if (networkLogEnabled && networkLogUrl != null) {  // 检查网络日志是否启用
            networkExecutorService.execute {  // 异步写入日志
                try {
                    val url = URL(networkLogUrl)  // 创建 URL 对象
                    val outputStream = url.openConnection().outputStream  // 获取输出流
                    outputStream.write(message.toByteArray())  // 写入日志
                    outputStream.close()  // 关闭流
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to write log to network", e)  // 错误处理
                }
            }
        }
    }

    /**
     * 记录日志到控制台、文件和网络。
     *
     * @param level 日志级别。
     * @param message 要记录的消息。
     */
    private fun log(level: LogLevel, message: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())  // 获取当前时间戳
        val fullMessage = "$timestamp - $message"  // 完整日志消息格式

        // 根据日志级别记录到控制台
        when (level) {
            LogLevel.DEBUG -> Log.d(TAG, fullMessage)
            LogLevel.INFO -> Log.i(TAG, fullMessage)
            LogLevel.WARN -> Log.w(TAG, fullMessage)
            LogLevel.ERROR -> Log.e(TAG, fullMessage)
        }

        logToFile(fullMessage)  // 写入文件
        logToNetwork(fullMessage)  // 写入网络
    }

    // 公开的日志记录方法
    fun d(message: String) = log(LogLevel.DEBUG, message)  // 调用 DEBUG 级别日志
    fun i(message: String) = log(LogLevel.INFO, message)  // 调用 INFO 级别日志
    fun w(message: String) = log(LogLevel.WARN, message)  // 调用 WARN 级别日志
    fun e(message: String) = log(LogLevel.ERROR, message)  // 调用 ERROR 级别日志
}