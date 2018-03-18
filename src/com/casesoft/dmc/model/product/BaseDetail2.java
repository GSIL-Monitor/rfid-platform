package com.casesoft.dmc.model.product;

import java.io.Serializable;
import java.lang.reflect.Field;

public class BaseDetail2 implements Serializable {

  private static final long serialVersionUID = 1L;

  private String styleId;
  private String styleName;
  private Double price;
  private String colorId;
  private String colorName;

  private Long size1;
  private Long size2;
  private Long size3;
  private Long size4;
  private Long size5;
  private Long size6;
  private Long size7;
  private Long size8;
  private Long size9;
  private Long size10;

  public String getStyleId() {
    return styleId;
  }

  public void setStyleId(String styleId) {
    this.styleId = styleId;
  }

  public String getStyleName() {
    return styleName;
  }

  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getColorId() {
    return colorId;
  }

  public void setColorId(String colorId) {
    this.colorId = colorId;
  }

  public String getColorName() {
    return colorName;
  }

  public void setColorName(String colorName) {
    this.colorName = colorName;
  }

  public Long getSize1() {
    return size1;
  }

  public void setSize1(Long size1) {
    this.size1 = size1;
  }

  public Long getSize2() {
    return size2;
  }

  public void setSize2(Long size2) {
    this.size2 = size2;
  }

  public Long getSize3() {
    return size3;
  }

  public void setSize3(Long size3) {
    this.size3 = size3;
  }

  public Long getSize4() {
    return size4;
  }

  public void setSize4(Long size4) {
    this.size4 = size4;
  }

  public Long getSize5() {
    return size5;
  }

  public void setSize5(Long size5) {
    this.size5 = size5;
  }

  public Long getSize6() {
    return size6;
  }

  public void setSize6(Long size6) {
    this.size6 = size6;
  }

  public Long getSize7() {
    return size7;
  }

  public void setSize7(Long size7) {
    this.size7 = size7;
  }

  public Long getSize8() {
    return size8;
  }

  public void setSize8(Long size8) {
    this.size8 = size8;
  }

  public Long getSize9() {
    return size9;
  }

  public void setSize9(Long size9) {
    this.size9 = size9;
  }

  public Long getSize10() {
    return size10;
  }

  public void setSize10(Long size10) {
    this.size10 = size10;
  }

  public void addSizeQty(Long seqNo, long qty) {
    try {
      Field f = this.getClass().getDeclaredField("size" + seqNo);
      f.setAccessible(true);
      Long srcQty = f.getLong(this);
      f.set(this, srcQty + qty);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}
