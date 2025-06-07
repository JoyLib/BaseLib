package com.ho.baselib.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.util.*

/**
 * Gson provider
 * @constructor 修复gson序列化问题
 */
object GsonProvider {
    val gson: Gson
        get() = GsonBuilder().registerTypeAdapter(object :
            TypeToken<TreeMap<String?, Any?>?>() {}.type, GsonTypeAdapter()).disableHtmlEscaping()
            .create()

    internal class GsonTypeAdapter : TypeAdapter<Any?>() {
        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Any? {
            // 反序列化
            return when (`in`.peek()) {
                JsonToken.BEGIN_ARRAY -> {
                    val list: MutableList<Any?> = ArrayList()
                    `in`.beginArray()
                    while (`in`.hasNext()) {
                        list.add(read(`in`))
                    }
                    `in`.endArray()
                    list
                }
                JsonToken.BEGIN_OBJECT -> {
                    val map: MutableMap<String, Any?> = TreeMap()
                    `in`.beginObject()
                    while (`in`.hasNext()) {
                        map[`in`.nextName()] = read(`in`)
                    }
                    `in`.endObject()
                    map
                }
                JsonToken.STRING -> `in`.nextString()
                JsonToken.NUMBER -> {
                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    val dbNum = `in`.nextDouble()

                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum
                    }

                    // 判断数字是否为整数值
                    val lngNum = dbNum.toLong()
                    if (dbNum == lngNum.toDouble()) {
                        lngNum
                    } else {
                        dbNum
                    }
                }
                JsonToken.BOOLEAN -> `in`.nextBoolean()
                JsonToken.NULL -> {
                    `in`.nextNull()
                    null
                }
                else -> throw IllegalStateException()
            }
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Any?) {
            // 序列化不处理
        }
    }
}
