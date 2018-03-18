package com.casesoft.dmc.core.util.secret;

import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;

/**
 * 对唯一码进行加密，产生EPC
 * 
 * @author WinstonLi
 * 
 */
public class EpcSecretUtil {

  private static int[] pinArray = { 1, 6, 5, 3, 2, 4, 8, 9, 7, 0 };

  // 314431230410000010000000
  //
  // 362236531261111161111111

  public static String encode(String epc) {
    char[] intEpcArray;// ,intEncryptEpcArray;
    int[] finalEncodeArray;// finalDecodeArray;
    intEpcArray = new char[24];
    finalEncodeArray = new int[24];
    String result = "";

    intEpcArray = epc.toCharArray();
    for (int j = 0; j < 24; j++) {
      finalEncodeArray[j] = pinArray[(Integer.parseInt("" + intEpcArray[j]))];
      result = result + finalEncodeArray[j];
    }
    return result;
  }

  /**
   * 计算得出唯一码：314D41212320000001
   * 
   * @param sku
   * @param startNum
   * @param
   * @return
   * @throws Exception
   */
  public static String getUniqueCode(String sku, int startNum, int serialSpan) throws Exception {
    int serialLength = PropertyUtil.getIntValue("serial_length");

    return sku + CommonUtil.produceIntToString(startNum + serialSpan - 1, serialLength);
  }


  public static String convertEpcToUniqueCode(String epc) throws Exception {
    String indexStr = PropertyUtil.getValue("Sample_Code_Char_Index");
    String[] indexChars = indexStr.split(",");
    String uniqueCode = epc;
    for (int i = 0; i < indexChars.length; i++) {
      int charIndex = Integer.parseInt(indexChars[i]);
      uniqueCode = CommonUtil.asciiToChar(uniqueCode, charIndex);
    }
    return uniqueCode;
  }


  protected static String _revEnArr = "BA1357D264C80E9F";

  public static String encodeEpc(String pEpc)// 53290010400001 S
  {
    char[] revEnArr = new StringBuffer(_revEnArr).reverse().toString().toCharArray();//StringUtils.reverse(_revEnArr).toCharArray();
    StringBuilder builder = new StringBuilder(pEpc.length());
    char[] epcArray = pEpc.toCharArray();
    for (char c : epcArray) {
      int i = Integer.parseInt("" + c, 16);// .Convert.ToInt32(c.ToString().PadLeft(2, '0'), 16);
      // builder.Append(_revEnArr[i]);
      builder.append(revEnArr[i]);
    }
    return builder.toString();
  }

  public static String decodeEpc(String pEpc) {
    int index = -1;
    StringBuilder builder = new StringBuilder(pEpc.length());
    for (char c : pEpc.toCharArray()) {
      index = findCharPostion(c);
      // builder.Append(Convert.ToString(index, 16).ToUpper());
      builder.append(Integer.toHexString(index).toUpperCase());// 转16进制)
    }
    return builder.toString();
  }

  protected static int findCharPostion(char c) {
    char[] revEnArr = new StringBuffer(_revEnArr).reverse().toString().toCharArray();
    int i = 0;
    for (i = 0; i < revEnArr.length; i++) {
      if (revEnArr[i] == c) {
        return i;
      }
    }
    return -1;
  }

}
