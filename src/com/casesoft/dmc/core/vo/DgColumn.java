package com.casesoft.dmc.core.vo;

import java.io.Serializable;

public class DgColumn implements Serializable {

  private static final long serialVersionUID = -5326632183495515209L;

  private String title;
  private String field;
  private int width = 60;
  private String aligh = "center";
  private boolean sortable = true;

  public DgColumn(String title) {
    super();
    this.title = title;
  }

  public DgColumn(String title, String field) {
    super();
    this.title = title;
    this.field = field;
  }

  public DgColumn() {
    super();
    // TODO Auto-generated constructor stub
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public String getAligh() {
    return aligh;
  }

  public void setAligh(String aligh) {
    this.aligh = aligh;
  }

  public boolean isSortable() {
    return sortable;
  }

  public void setSortable(boolean sortable) {
    this.sortable = sortable;
  }

}
