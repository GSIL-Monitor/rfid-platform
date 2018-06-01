package com.casesoft.dmc.service.rfidReader;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.core.exception.RfidReaderException;
import com.casesoft.dmc.core.exception.RfidReaderExceptionCode;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.rfidReader.SerialCmdGenerator;
import com.casesoft.dmc.core.util.rfidReader.SerialHandleUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static org.apache.xalan.xsltc.compiler.sym.error;

/**
 * Created by yushen on 2018/5/30.
 */
@Service
@Transactional
public class RfidReaderService {

    private String remoteReaderUrl = "http://114.215.186.211:8080/fsu/transparent/rfid/";

    /**
     * 读写器复位
     */
    public void reset(String deviceId) throws RfidReaderException {
        byte[] reset = SerialCmdGenerator.reset();
        String serialCmd = SerialHandleUtil.epcToHexString(reset);
        System.out.println("reset 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        if(CommonUtil.isNotBlank(bytes)){
            byte[] checkedData = checkCmdReturnData(bytes);
            if(checkedData[1] == 0x04 && checkedData[4] > 0x10){
                throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
            }
        }
    }

    /**
     * 设置工作天线
     * @param antenna 天线序号1-4：0x00 - 0x03
     */
    public String setWorkAntenna(String deviceId, byte antenna) throws RfidReaderException {
        byte[] setWorkAntenna = SerialCmdGenerator.setWorkAntenna(antenna);
        String serialCmd = SerialHandleUtil.epcToHexString(setWorkAntenna);
        System.out.println("setWorkAntenna 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        byte[] decodedData = checkCmdReturnData(bytes);
        if(decodedData[1] == 0x04 && decodedData[4] > 0x10){
            throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
        }
        return "Success";
    }


    /**
     * 读取工作天线
     */
    public Map getWorkAntenna(String deviceId) throws RfidReaderException {
        byte[] getWorkAntenna = SerialCmdGenerator.getWorkAntenna();
        String serialCmd = SerialHandleUtil.epcToHexString(getWorkAntenna);
        System.out.println("getWorkAntenna 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        byte[] decodedData = checkCmdReturnData(bytes);
        if(decodedData[1] == 0x04 && decodedData[4] > 0x10){
            throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("antennaID", decodedData[4]);
        return map;
    }

    /**
     * 设置天线功率
     * @param outputPower 功率0-33dbm: 0x00 - 0x21
     */
    public String setOutPutPower(String deviceId, byte outputPower) throws RfidReaderException {
        byte[] setOutPutPower = SerialCmdGenerator.setOutPutPower(outputPower);
        String serialCmd = SerialHandleUtil.epcToHexString(setOutPutPower);
        System.out.println("setOutPutPower 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        byte[] decodedData = checkCmdReturnData(bytes);
        if(decodedData[1] == 0x04 && decodedData[4] > 0x10){
            throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
        }
        return "Success";
    }

    /**
     * 获取天线功率
     */
    public Map getOutPutPower(String deviceId) throws RfidReaderException {
        byte[] getOutPutPower = SerialCmdGenerator.getOutPutPower();
        String serialCmd = SerialHandleUtil.epcToHexString(getOutPutPower);
        System.out.println("getOutPutPower 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        byte[] decodedData = checkCmdReturnData(bytes);
        if(decodedData[1] == 0x04 && decodedData[4] > 0x10){
            throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("power1", decodedData[4]);
        if(decodedData.length == 9){
            map.put("power2", decodedData[5]);
            map.put("power3", decodedData[6]);
            map.put("power4", decodedData[7]);
        }
        return map;
    }

    /**
     * 实时盘存
     */
    public List<String> realTimeInventory(String deviceId) throws RfidReaderException {
        byte[] realTimeInventory = SerialCmdGenerator.realTimeInventory();
        String serialCmd = SerialHandleUtil.epcToHexString(realTimeInventory);
        System.out.println("realTimeInventory 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        Set<String> epcSet = decodeSerialEpcData(bytes);
        return new ArrayList<>(epcSet);
    }

    /**
     * 盘存，标签数据存入缓存，不直接返回
     */
    public Map inventory(String deviceId) throws RfidReaderException {
        byte[] inventory = SerialCmdGenerator.inventory();
        String serialCmd = SerialHandleUtil.epcToHexString(inventory);
        System.out.println("inventory 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        byte[] decodedData = checkCmdReturnData(bytes);
        if(decodedData[1] == 0x04 && decodedData[4] > 0x10){
            throw new RfidReaderException(RfidReaderExceptionCode.ReturnError, "读卡器返回错误结果");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("antID", decodedData[4]);
        map.put("tagCount", SerialHandleUtil.calculateBytes(decodedData[5], decodedData[6]));
        map.put("readRate", SerialHandleUtil.calculateBytes(decodedData[7], decodedData[8]));
        map.put("totalRead", SerialHandleUtil.calculateBytes(decodedData[9], decodedData[10], decodedData[11], decodedData[12]));
        return map;
    }

    /**
     * 读取并重置盘存缓存
     */
    public List<String> getAndResetInventoryBuffer(String deviceId) throws RfidReaderException {
        byte[] getAndResetInventoryBuffer = SerialCmdGenerator.getAndResetInventoryBuffer();
        String serialCmd = SerialHandleUtil.epcToHexString(getAndResetInventoryBuffer);
        System.out.println("getAndResetInventoryBuffer 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        Set<String> epcSet = decodeSerialEpcData(bytes);
        return new ArrayList<>(epcSet);
    }

    /**
     * 快速4天线盘存
     * @param stay      天线重复轮询次数 00-03
     * @param interval  天线间的休息时间 单位ms
     * @param repeat    重复次数
     */
    public List<String> fastSwitchAntInventory(String deviceId, byte stay, byte interval, byte repeat) throws RfidReaderException {
        byte[] fastSwitchAntInventory = SerialCmdGenerator.fastSwitchAntInventory(stay, interval, repeat);
        String serialCmd = SerialHandleUtil.epcToHexString(fastSwitchAntInventory);
        System.out.println("fastSwitchAntInventory 的串口指令为：" + serialCmd);
        byte[] bytes = httpHandle(serialCmd, deviceId);
        Set<String> epcSet = decodeSerialEpcData(bytes);
        return new ArrayList<>(epcSet);
    }


    /**
     * 给串口指令加上http头，请求远端读卡器识别标签
     * 拼接成如下http请求
     * http://114.215.186.211:8080/fsu/transparent/rfid/6252/A015018A00010101020103010401050106010701000A92
     *
     * @param serialPortCmd 串口指令  A015018A00010101020103010401050106010701000A92
     * @param deviceId      读卡器编号  6252
     * @return HTTP请求返回结果进行base64解码后的byte数组
     * @throws RfidReaderException http请求返回值解析错误时抛出异常
     */
    private byte[] httpHandle(String serialPortCmd, String deviceId) throws RfidReaderException {
        byte[] resultData = null;

        String url = remoteReaderUrl + deviceId + "/" + StringUtils.deleteWhitespace(serialPortCmd);
        HttpGet httpGet = new HttpGet(url);
        System.out.println("executing request: " + httpGet.getURI());

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            if (CommonUtil.isNotBlank(entity)) {
                String rfidReaderStr = EntityUtils.toString(entity);
                System.out.println("response content: " + rfidReaderStr);
                JSONObject jsonObject = JSON.parseObject(rfidReaderStr);
                if (CommonUtil.isBlank(jsonObject)) {
                    throw new RfidReaderException(RfidReaderExceptionCode.NoReturn, "未获取到返回数据，请检查网络链接");
                }
                if (CommonUtil.isNotBlank(jsonObject.get("error"))) {
                    JSONObject error = (JSONObject) jsonObject.get("error");
                    throw new RfidReaderException(RfidReaderExceptionCode.RFIDReaderAccessError, "访问RFID读卡器错误，错误码：" + error.get("code"));
                }
                if (CommonUtil.isNotBlank(jsonObject.get("result"))) {
                    JSONObject result = (JSONObject) jsonObject.get("result");
                    if (CommonUtil.isNotBlank(result.get("data"))) {
                        String data = (String) result.get("data");
                        Base64 base64 = new Base64();
                        resultData = base64.decode(data);
                        System.out.println("decode value: " + Arrays.toString(resultData));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * http返回数据还是串口的byte[]数组，解析成epc标签数据集合
     * 盘存，用于解码大量携带EPC信息的数据
     *
     * @param byteArray 串口返回数据
     * @return 标签epc信息构成的Set
     * @throws RfidReaderException 读写器返回数据为错误时抛出异常
     */
    private Set<String> decodeSerialEpcData(byte[] byteArray) throws RfidReaderException {
        Set<String> epcSet = new HashSet<>();
        for (int i = 0; i < byteArray.length; ) {
            if (byteArray[i] == (byte) 0xA0) { //判断数据报头
                Byte length = byteArray[i + 1]; //取数据报长
                //截取有效字符串计算校验和
                byte[] subArray = Arrays.copyOfRange(byteArray, i, i + length + 1);
                Byte sumCheck = byteArray[i + length + 1]; //回传校验和
                byte res = SerialHandleUtil.sumCheck(subArray); //计算校验和
                if (res == sumCheck) { //验证校验和，如果校验正确循环向后直接向后跳 (length + 2) 位；否则继续向后单步解析
                    byte[] checkedArray = Arrays.copyOfRange(byteArray, i, i + length + 2);
                    i = i + length + 2;
                    String epc = resolveEpc(checkedArray);
                    if (CommonUtil.isNotBlank(epc)) {
                        epcSet.add(epc);
                    }
                } else {
                    i++;
                    System.out.println("数据校验和错误");
                }
            } else {
                i++;
            }
        }
        return epcSet;
    }

    /**
     * 从串口返回的数据中，提取epc信息
     *
     * @throws RfidReaderException 读写器返回数据为错误时抛出异常
     */
    private String resolveEpc(byte[] bytes) throws RfidReaderException {
        if(bytes[3] == (byte) 0x89 || bytes[3] == (byte) 0x8A){ // 实时盘存，快速4天线盘存
            if (bytes[1] == 4) {//错误数据包的长度为4
                System.out.println("返回错误数据包");
            }
            if (bytes[1] == 19) { //0x89盘存指令 返回正确EPC数据包长度为19
                return SerialHandleUtil.epcToHexString(Arrays.copyOfRange(bytes, 7, 19)); //EPC信息储存在数据包的7-19位
            }
        }
        if(bytes[3] == (byte) 0x91){ // 0x91读取缓存指令
            if (bytes[1] == 4) {//错误数据包的长度为4
                System.out.println("返回错误数据包");
            }
            if(bytes[1] == 25){ //0x91读取缓存指令 返回正确EPC数据包长度为25
                return SerialHandleUtil.epcToHexString(Arrays.copyOfRange(bytes, 9, 21)); //EPC信息储存在数据包的7-19位
            }
        }
        return "";
    }

    /**
     * 用于解码单条返回数据
     */
    private byte[] checkCmdReturnData(byte[] byteArray) throws RfidReaderException {
        if(CommonUtil.isBlank(byteArray)){
            throw new RfidReaderException(RfidReaderExceptionCode.NoResultData, "有返回数据，返回值的结果为空");
        }
        if (byteArray[0] != (byte) 0xA0) {
            throw new RfidReaderException(RfidReaderExceptionCode.ErrorDataHeader, "错误的数据报头");
        }
        Byte length = byteArray[1]; //取数据报长
        //截取有效字符串计算校验和
        byte[] subArray = Arrays.copyOfRange(byteArray, 0, length + 1);
        Byte sumCheck = byteArray[length + 1]; //回传校验和
        byte res = SerialHandleUtil.sumCheck(subArray); //计算校验和
        if (res != sumCheck) { //验证校验和，如何校验正确循环向后直接向后跳 (length + 2) 位；否则继续向后单步解析
            throw new RfidReaderException(RfidReaderExceptionCode.SumCheckError, "校验和错误");
        }
        return Arrays.copyOfRange(byteArray, 0, 0 + length + 2);
    }




}
