package com.manson.fo76.domain

import org.apache.commons.lang3.StringUtils
import java.util.HashMap

open class XTranslatorConfig {
    var id: String? = null
    var texts: MutableMap<String, String> = HashMap()
    var rec: String? = null
    var sid: String? = null
    var enabled: Boolean = true

    companion object {
        @JvmStatic
        fun fromFo76String(fo76String: Fo76String): XTranslatorConfig {
            val xTranslatorConfig = XTranslatorConfig()
            xTranslatorConfig.id = fo76String.edid
            xTranslatorConfig.sid = fo76String.sid
            xTranslatorConfig.rec = fo76String.rec.toString()
            val texts: MutableMap<String, String> = HashMap()
            texts[fo76String.lang] = fo76String.source!!
            xTranslatorConfig.texts = texts
            return xTranslatorConfig
        }

        fun merge(base: XTranslatorConfig, another: XTranslatorConfig): XTranslatorConfig {
            if (StringUtils.equalsIgnoreCase(base.id, another.id)) {
                base.texts.putAll(another.texts)
            }
            return base
        }
    }
}