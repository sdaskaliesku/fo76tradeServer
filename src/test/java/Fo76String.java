import com.fasterxml.jackson.annotation.JsonProperty;

public class Fo76String {

  @JsonProperty("EDID")
  private String edid;
  @JsonProperty("REC")
  private String rec;
  @JsonProperty("Source")
  private String source;
  @JsonProperty("Dest")
  private String dest;
  @JsonProperty("-List")
  private String list;
  @JsonProperty("-sID")
  private String sid;

  public String getList() {
    return list;
  }

  public void setList(String list) {
    this.list = list;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getEdid() {
    return edid;
  }

  public void setEdid(String edid) {
    this.edid = edid;
  }

  public String getRec() {
    return rec;
  }

  public void setRec(String rec) {
    this.rec = rec;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDest() {
    return dest;
  }

  public void setDest(String dest) {
    this.dest = dest;
  }
}
