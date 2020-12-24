package com.manson.fo76.helper

import com.github.opendevl.JFlat
import java.io.Writer

class JFlatCustom(jsonString: String?) : JFlat(jsonString) {
    override fun json2Sheet(): JFlatCustom {
        return super.json2Sheet() as JFlatCustom
    }

    @Throws(Exception::class)
    override fun headerSeparator(): JFlatCustom {
        return super.headerSeparator() as JFlatCustom
    }

    @Throws(Exception::class)
    override fun headerSeparator(separator: String): JFlatCustom {
        return super.headerSeparator(separator) as JFlatCustom
    }

    fun write2csv(writer: Writer, delimiter: Char) {
        try {
            var comma: Boolean
            for (o in jsonAsSheet) {
                comma = false
                for (t in o) {
                    if (t == null) {
                        var str = delimiter.toString()
                        if (!comma) {
                            str = ""
                        }
                        writer.write(str)
                    } else {
                        writer.write(if (comma) delimiter.toString() + t.toString() else t.toString())
                    }
                    if (!comma) {
                        comma = true
                    }
                }
                writer.write(System.lineSeparator())
            }
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}