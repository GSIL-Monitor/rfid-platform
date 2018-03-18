package com.casesoft.dmc.extend.api.web.epay.fepay.wechat;

public class WechatConfigure {
	//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
		// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
		// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

		private static String key = "casesoftcasesoftcasesoftcasesoft";

		//微信分配的公众号ID（开通公众号之后可以获取到）
		private static String appID = "wxa8c1eec2008f0ed7";

		//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		private static String mchID = "1279307201";

		//受理模式下给子商户分配的子商户号
		private static String subMchID = "";

		//HTTPS证书的本地路径
		private static String certLocalPath = "F:\\cert\\apiclient_cert.p12";

		//HTTPS证书密码，默认密码等于商户号MCHID
		private static String certPassword = "1279307201";

		public static String getKey() {
			return key;
		}

		public static String getAppID() {
			return appID;
		}

		public static String getMchID() {
			return mchID;
		}

		public static String getSubMchID() {
			return subMchID;
		}

		public static String getCertLocalPath() {
			return certLocalPath;
		}

		public static String getCertPassword() {
			return certPassword;
		}
		
		
}
