package com.casesoft.dmc.core.util.secret;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * DES加密解密工具
 * @author Administrator
 *
 */

public class DESUtil {

	private static final String Default_Algorithm = "DES";
	private static final String Default_Thansformation = "DES/CBC/PKCS5Padding";
	
	/**
	 * 初始化Cliper,mode加密时值为1，解密时值为2
	 * @throws Exception 
	 * @throws InvalidKeyException 
	 */
	private static Cipher initCipher(String key, String iv, int mode) throws InvalidKeyException, Exception {
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),Default_Algorithm);
		Cipher cipher = Cipher.getInstance(Default_Thansformation);
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(mode, keySpec, ivSpec);//// 设置工作模式，给出密钥和向量
		return cipher;
	}
	
	/**
	 * 加密文件到文件
	 * @param srcFile
	 * @param destFile
	 * @param key
	 * @param iv
	 * @return
	 * @throws Exception 
	 * @throws InvalidKeyException 
	 */
	public static void encryptToFile(String srcFile, String destFile,
			String key, String iv) throws InvalidKeyException, Exception {
		Cipher cipher = initCipher(key,iv,Cipher.ENCRYPT_MODE);//加密模式
		
		try {
			FileInputStream in = new FileInputStream(srcFile);
			FileOutputStream fos = new FileOutputStream(destFile);
            // Bytes written to out will be encrypted
			CipherOutputStream out = new CipherOutputStream(fos, cipher);
            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            byte[] buf = new byte[1024];
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {

        }
	}
	
	/**
	 * 解密文件到字符串
	 * @param destFile
	 * @throws InvalidKeyException
	 * @throws Exception
	 */
	public static String decryptToStr(String srcFile, String key, String iv)
			throws InvalidKeyException, Exception {
		Cipher cipher = initCipher(key,iv,Cipher.DECRYPT_MODE);

		StringBuffer strBuf =  null;
		try {
			InputStream is = new FileInputStream(srcFile);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cis));
			String line = null;
			strBuf = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				strBuf.append(line);
			}
			reader.close();
			cis.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
	
	/**
	 * 将文件内容解密后，保持成文件
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void decryptToFile(String srcFile, String destFile,
			String key, String iv) throws NoSuchAlgorithmException, Exception {
		Cipher cipher = initCipher(key, iv, Cipher.DECRYPT_MODE);
		// Decrypt
		File src = new File(srcFile);
		// decrypt(new FileInputStream(src), new FileOutputStream(destFile));
		FileInputStream in1 = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(destFile);
		byte[] buf = new byte[1024];
		try {
			// Bytes read from in will be decrypted
			CipherInputStream in = new CipherInputStream(in1, cipher);
			// Read in the decrypted bytes and write the cleartext to out
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0) {
				out.write(buf, 0, numRead);
			}
			out.close();
		} catch (java.io.IOException e) {

		}

	}
	public static void decryptToFile(File src, String destFile,
			String key, String iv) throws NoSuchAlgorithmException, Exception {
		Cipher cipher = initCipher(key, iv, Cipher.DECRYPT_MODE);
		// Decrypt
		//File src = new File(srcFile);
		// decrypt(new FileInputStream(src), new FileOutputStream(destFile));
		FileInputStream in1 = new FileInputStream(src);
		FileOutputStream out = new FileOutputStream(destFile);
		byte[] buf = new byte[1024];
		try {
			// Bytes read from in will be decrypted
			CipherInputStream in = new CipherInputStream(in1, cipher);
			// Read in the decrypted bytes and write the cleartext to out
			int numRead = 0;
			while ((numRead = in.read(buf)) >= 0) {
				out.write(buf, 0, numRead);
			}
			out.close();
		} catch (java.io.IOException e) {

		} finally {
			in1.close();
			out.close();
		}

	}
	
	public static void main(String[] args) throws  Exception {
		//String srcFile = "K:\\destest1.dtl";
		String srcFile = "K:\\MilanUpload\\TSK20121203KA2012010002\\TSK20121203KA2012010002.tsk";
		String destFile = "K:\\TSK20121203KA2012010002.tsk.xml";
		String key ="KA201201";
		String iv = "12345678";
		//String iv = "TSK20121203KA2012010002";
		
		decryptToFile(srcFile,destFile,key,iv);
		
		srcFile = "K:\\MilanUpload\\TSK20121203KA2012010002\\TSK20121203KA2012010002.ctn";
		destFile = "K:\\TSK20121203KA2012010002.ctn.xml";
		decryptToFile(srcFile,destFile,key,iv);
		
		srcFile = "K:\\MilanUpload\\TSK20121203KA2012010002\\CTN121203000139941.sku";
		destFile = "K:\\CTN121203000139941.sku.xml";
		decryptToFile(srcFile,destFile,key,iv);		
	}
	
	/*
	 private void button6_Click(object sender, RoutedEventArgs e)
        {
            Text.Text = RayDecode(Text.Text, 1);
        }

        private void button5_Click(object sender, RoutedEventArgs e)
        {
            //Text.Text = "314431230410000020000000";
            Text.Text=RayEncode(Text.Text, 1);
        }

        //165324895956847123651249
        //private int[] pinArray = { 1,6,5,3,2,4,8,9,5,9,5,6,8,4,7,1,2,3,6,5,1,2,4,9};
        private int[] pinArray = { 1, 6, 5, 3, 2, 4, 8, 9, 7, 0 };
        private int[] intEpcArray,intEncryptEpcArray;
        private int[] finalEncodeArray,finalDecodeArray;

        //314431230410000010000000
        //
        //362236531261111161111111

        private string RayEncode(string epc,int iCompanyID)
        {
            intEpcArray=new int[24];
            finalEncodeArray=new int[24];
            string result="";

            switch (iCompanyID)
            {                    
                case 1:
                    for (int i = 0; i < 24; i++)
                    {
                        intEpcArray[i] = Convert.ToInt32(epc.Substring(i, 1));
                    }
                    for (int j = 0; j < 24; j++)
                    {
                        finalEncodeArray[j] = pinArray[intEpcArray[j]];
                        result = result + finalEncodeArray[j].ToString();
                    }
                    break;
                case 2:
                    break;

                default:
                    break;
            }
            return result;
        }
        
        private string RayDecode(string encryptEpc, int iCompanyID)
        {
            intEncryptEpcArray = new int[24];
            finalDecodeArray = new int[24];
            string result = "";
            
            switch (iCompanyID)
            {
                case 1:
                    for (int i = 0; i < 24; i++)
                    {
                        intEncryptEpcArray[i] = Convert.ToInt32(encryptEpc.Substring(i, 1));
                    }
                    
                    for (int j = 0; j < 24; j++)
                    {
                        pinArray.Select((a, index) => new { a, index }).Where(a => a.a == intEncryptEpcArray[j]).ToList().ForEach(a => finalDecodeArray[j] = a.index);                        
                        result = result + finalDecodeArray[j].ToString();
                    }
                    break;
                case 2:
                    break;

                default:
                    break;
            }
            return result;
        }


	 */
		
}
