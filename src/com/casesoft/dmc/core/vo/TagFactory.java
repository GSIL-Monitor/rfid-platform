package com.casesoft.dmc.core.vo;

import com.casesoft.dmc.core.util.file.PropertyUtil;

public class TagFactory {

  private void TagFactory() {}

  private static ITag tag;

  public static ITag getCurrentTag() {
    if(tag != null) return tag;
    else {
      try {
        tag = getTag(PropertyUtil.getValue("tag_name"));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return tag;
    }
  }

  public static ITag getTag(String className) {
    if (tag == null) {

      try {
        tag = (ITag) Class.forName("com.casesoft.dmc.extend.tag." + className).newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return tag;
  }

  public static ITag getTag(String className, String styleId, String colorId, String sizeId) {
    if (tag == null) {

      try {
        tag = (ITag) Class.forName("com.casesoft.dmc.extend.tag." + className).newInstance();
        tag.setStyleId(styleId);
        tag.setColorId(colorId);
        tag.setSizeId(sizeId);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return tag;
  }
}
