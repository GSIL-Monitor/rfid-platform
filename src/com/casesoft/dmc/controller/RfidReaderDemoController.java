package com.casesoft.dmc.controller;

import com.casesoft.dmc.core.exception.RfidReaderException;
import com.casesoft.dmc.service.rfidReader.RfidReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yushen on 2018/5/31.
 */
@Controller
@RequestMapping("/rfidReader")
public class RfidReaderDemoController {
    @Autowired
    private RfidReaderService rfidReaderService;
    private String deviceId = "6252";

    @RequestMapping("/reset.do")
    @ResponseBody
    public void reset() {
        try {
            rfidReaderService.reset(deviceId);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/setWorkAntenna.do")
    @ResponseBody
    public void setWorkAntenna() {
        try {
            rfidReaderService.setWorkAntenna(deviceId, (byte) 0x00);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getWorkAntenna.do")
    @ResponseBody
    public Map getWorkAntenna() {
        Map workAntenna = new HashMap();
        try {
            workAntenna = rfidReaderService.getWorkAntenna(deviceId);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return workAntenna;
    }


    @RequestMapping("/setOutPutPower.do")
    @ResponseBody
    public void setOutPutPower() {
        try {
            rfidReaderService.setOutPutPower(deviceId, (byte) 0x10);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/getOutPutPower.do")
    @ResponseBody
    public Map getOutPutPower() {
        Map outPutPower = new HashMap();
        try {
            outPutPower = rfidReaderService.getOutPutPower(deviceId);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return outPutPower;
    }

    @RequestMapping("/realTimeInventory.do")
    @ResponseBody
    public List<String> realTimeInventory() {
        List<String> epcList = null;
        try {
            epcList = rfidReaderService.realTimeInventory(deviceId);
            for (String str : epcList) {
                System.out.println("唯一码：" + str);
            }
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return epcList;
    }


    @RequestMapping("/inventory.do")
    @ResponseBody
    public Map inventory() {
        Map inventory = new HashMap();
        try {
            inventory = rfidReaderService.inventory(deviceId);
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    @RequestMapping("/getAndResetInventoryBuffer.do")
    @ResponseBody
    public List<String> getAndResetInventoryBuffer() {
        List<String> epcList = null;
        try {
            epcList = rfidReaderService.getAndResetInventoryBuffer(deviceId);
            for (String str : epcList) {
                System.out.println("唯一码：" + str);
            }
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return epcList;
    }

    @RequestMapping("/fastSwitchAntInventory.do")
    @ResponseBody
    public List<String> fastSwitchAntInventory() {
        List<String> epcList = null;
        try {
            epcList = rfidReaderService.fastSwitchAntInventory(deviceId, (byte) 0x01, (byte) 0x00, (byte) 0x01);
            for (String str : epcList) {
                System.out.println("唯一码：" + str);
            }
        } catch (RfidReaderException e) {
            e.printStackTrace();
        }
        return epcList;
    }
}
