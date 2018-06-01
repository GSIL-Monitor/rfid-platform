package com.casesoft.dmc.core.util.rfidReader;

/**
 * Created by yushen on 2018/5/30.
 */
public class SerialCmdGenerator {

    /**
     * 复位读写器
     */
    public static byte[] reset() {
        byte[] reset = {(byte) 0xA0, 0x03, 0x01, 0x70, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(reset);
        reset[reset.length - 1] = sumCheck;
        return reset;
    }

    /**
     * 设置波特率
     *
     * @param baudrate 波特率: 0x30 -> 38400bps ; 0x40 -> 115200bps
     */
    public static byte[] setUartBaudrate(byte baudrate) {
        byte[] setUartBaudrate = {(byte) 0xA0, 0x04, 0x01, (byte) 0x71, baudrate, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setUartBaudrate);
        setUartBaudrate[setUartBaudrate.length - 1] = sumCheck;
        return setUartBaudrate;
    }


    /**
     * 设置读写器地址
     *
     * @param address 读写器地址0-254: 0x00 - 0xFE
     */
    public static byte[] setReaderAddress(byte address) {
        byte[] setReaderAddress = {(byte) 0xA0, 0x04, 0x01, (byte) 0x73, address, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setReaderAddress);
        setReaderAddress[setReaderAddress.length - 1] = sumCheck;
        return setReaderAddress;
    }

    /**
     * 设置工作天线
     *
     * @param antennaId 天线序号1-4：0x00 - 0x03
     */
    public static byte[] setWorkAntenna(byte antennaId) {
        byte[] setWorkAntenna = {(byte) 0xA0, 0x04, 0x01, (byte) 0x74, antennaId, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setWorkAntenna);
        setWorkAntenna[setWorkAntenna.length - 1] = sumCheck;
        return setWorkAntenna;
    }

    /**
     * 读工作天线
     */
    public static byte[] getWorkAntenna() {
        byte[] getWorkAntenna = {(byte) 0xA0, 0x03, 0x01, (byte) 0x75, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getWorkAntenna);
        getWorkAntenna[getWorkAntenna.length - 1] = sumCheck;
        return getWorkAntenna;
    }

    /**
     * 设置所有天线的发射功率
     *
     * @param rfPower 功率0-33dbm: 0x00 - 0x21
     */
    public static byte[] setOutPutPower(byte rfPower) {
        byte[] setOutPutPower = {(byte) 0xA0, 0x04, 0x01, (byte) 0x76, rfPower, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setOutPutPower);
        setOutPutPower[setOutPutPower.length - 1] = sumCheck;
        return setOutPutPower;
    }

    /**
     * 按天线设置发射功率
     *
     * @param rfPower1 功率0-33dbm: 0x00 - 0x21
     * @param rfPower2 功率0-33dbm: 0x00 - 0x21
     * @param rfPower3 功率0-33dbm: 0x00 - 0x21
     * @param rfPower4 功率0-33dbm: 0x00 - 0x21
     */
    public static byte[] setOutPutPower(byte rfPower1, byte rfPower2, byte rfPower3, byte rfPower4) {
        byte[] setOutPutPower = {(byte) 0xA0, 0x07, 0x01, (byte) 0x76, rfPower1, rfPower2, rfPower3, rfPower4, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(setOutPutPower);
        setOutPutPower[setOutPutPower.length - 1] = sumCheck;
        return setOutPutPower;
    }

    /**
     * 读天线功率
     */
    public static byte[] getOutPutPower() {
        byte[] getOutPutPower = {(byte) 0xA0, 0x03, 0x01, (byte) 0x77, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getOutPutPower);
        getOutPutPower[getOutPutPower.length - 1] = sumCheck;
        return getOutPutPower;
    }

    /**
     * 实时盘存
     */
    public static byte[] realTimeInventory() {
        byte[] realTimeInventory = {(byte) 0xA0, 0x04, 0x01, (byte) 0x89, 0x01, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(realTimeInventory);
        realTimeInventory[realTimeInventory.length - 1] = sumCheck;
        return realTimeInventory;
    }

    /**
     * 盘存，标签数据存入缓存，不直接返回
     */
    public static byte[] inventory() {
        byte[] inventory = {(byte) 0xA0, 0x04, 0x01, (byte) 0x80, 0x01, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(inventory);
        inventory[inventory.length - 1] = sumCheck;
        return inventory;
    }

    /**
     * 读取并重置盘存缓存
     */
    public static byte[] getAndResetInventoryBuffer() {
        byte[] getAndResetInventoryBuffer = {(byte) 0xA0, 0x04, 0x01, (byte) 0x91, 0x01, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getAndResetInventoryBuffer);
        getAndResetInventoryBuffer[getAndResetInventoryBuffer.length - 1] = sumCheck;
        return getAndResetInventoryBuffer;
    }

    /**
     * 快速4天线盘存, 分别设置天线
     */
    public static byte[] fastSwitchAntInventory(byte stay1, byte stay2, byte stay3, byte stay4, byte interval, byte repeat) {
        byte[] getAndResetInventoryBuffer = {(byte) 0xA0, 0x0D, 0x01, (byte) 0x8A, 0x00, stay1, 0x01, stay2, 0x02, stay3, 0x03, stay4, interval, repeat, 0x00};
        byte sumCheck = SerialHandleUtil.sumCheck(getAndResetInventoryBuffer);
        getAndResetInventoryBuffer[getAndResetInventoryBuffer.length - 1] = sumCheck;
        return getAndResetInventoryBuffer;
    }

    /**
     * 快速4天线盘存, 统一设置天线
     * @param stay      天线重复轮询次数 00-03
     * @param interval  天线间的休息时间 单位ms
     * @param repeat    重复次数
     * @return
     */
    public static byte[] fastSwitchAntInventory(byte stay, byte interval, byte repeat) {
        return fastSwitchAntInventory(stay, stay, stay, stay, interval, repeat);
    }
}