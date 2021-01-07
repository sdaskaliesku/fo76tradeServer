package com.manson.fo76.helper;

import com.github.opendevl.JFlat;
import java.io.Writer;

public class JFlatCustom extends JFlat {

  public JFlatCustom(String jsonString) {
    super(jsonString);
  }

  @Override
  public JFlatCustom json2Sheet() {
    return (JFlatCustom) super.json2Sheet();
  }

  @Override
  public JFlatCustom headerSeparator() throws Exception {
    return (JFlatCustom) super.headerSeparator();
  }

  @Override
  public JFlatCustom headerSeparator(String separator) throws Exception {
    return (JFlatCustom) super.headerSeparator(separator);
  }

  public void write2csv(Writer writer, Character delimiter) {
    try {
      boolean comma;
      for (Object[] o : this.getJsonAsSheet()) {
        comma = false;
        for (Object t : o) {
          if (t == null) {
            writer.write(comma ? delimiter.toString() : "");
          } else {
            writer.write(comma ? delimiter + t.toString() : t.toString());
          }
          if (!comma) {
            comma = true;
          }
        }
        writer.write(System.lineSeparator());
      }
      writer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
