

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package com.casesoft.dmc.extend.api.web.epay.alipay.constants;


/**

 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）

 * 

 * @author taixu.zqq

 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $

 */

public class AlipayServiceEnvConstants {


    /**支付宝公钥-从支付宝服务窗获取*/

    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";


    /**签名编码-视支付宝服务窗要求*/

    public static final String SIGN_CHARSET      = "UTF-8";


    /**字符编码-传递给支付宝的数据编码*/

    public static final String CHARSET           = "UTF-8";


    /**签名类型-视支付宝服务窗要求*/

    public static final String SIGN_TYPE         = "RSA";

    

    

    public static final String PARTNER           = "2088911227080000";


    /** 服务窗appId  */

    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id

    public static final String APP_ID            = "2016010601068758";


    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 

    public static final String PRIVATE_KEY       = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMnK7MKOMq/iKxpG2bZTsdSSs8b2xNURfLLh89eI5WyR2xiYBEeguf7uUJLXUzF5hmjqf0YiRvh5Mj645Zu79jAeRX2owug0j7Oityb+HCcloEG/MT7PheCJ459dxJ5Xz4iTKajC7IRIyZRWH7yk3RLF90hS5+2s4qKQK2OYoDlrAgMBAAECgYEAsnWjM2CclQ3Y/rEgA247O/rNwS0l4CVJ+c14X+oys/S+Jh7y0gHj3AAZA/QoH/4Qj6KrEnRn/YD5nZzZewQc8Ng/8q50pAxes0yA8NV0db6w0+L6jeU1vFiD+ehqYAeP90EcvJpMY8XA0itF8XLN8Mbn5uotKBACFqqNkovZTGECQQDyzhMdRiwGD+HvafTBirC73Y7e6aQOWMrmbGqp/NATfS3mJ3P0Va0t5YDhosSQoxODRf04PjWvmOioPsa5lZKRAkEA1MJIgH37m8ZfOxAvE6YLY/5yvktERb4cioNYmentyUi8JwIeg8DicnHfYRImz6OTlMqKj8b0Bf0YGkjuJ2VSOwJBAOviYlV9V8Wn5nLGeD8S8zIARA2x5Gz/vFbxmwp8DtXwNggz6gXEupMtNPGARQDTPWC6PRBH97YZkmXpXvzrH9ECQQDDNKaUzgJmgHGrnUO0QWq+Ch15H1G6s504MR6/yPQU9Bk9qSIaPIUIABa115OoeyrTH6pWFHKcdAUDSNrJZps1AkEA7q2q6WndFhB3E1g1YuOm9UuV+BXg0x6V7QlcC/AzDCmxp5rxBtPWNdR9IYdc6L/w4MZ3CmCMFcZDR1/nYGWf6A==";


    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患

    public static final String PUBLIC_KEY        = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJyuzCjjKv4isaRtm2U7HUkrPG9sTVEXyy4fPXiOVskdsYmARHoLn+7lCS11MxeYZo6n9GIkb4eTI+uOWbu/YwHkV9qMLoNI+zorcm/hwnJaBBvzE+z4XgieOfXcSeV8+IkymowuyESMmUVh+8pN0SxfdIUuftrOKikCtjmKA5awIDAQAB";


    /**支付宝网关*/

    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";


    /**授权访问令牌的授权类型*/

    public static final String GRANT_TYPE        = "authorization_code";

}