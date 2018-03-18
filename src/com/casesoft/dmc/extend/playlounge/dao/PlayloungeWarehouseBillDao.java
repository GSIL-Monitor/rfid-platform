package com.casesoft.dmc.extend.playlounge.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.IPlayloungeWarehouseBillDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeConstants;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.erp.ErpStock;
import com.casesoft.dmc.model.product.Color;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Size;
import com.casesoft.dmc.model.product.Style;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayloungeWarehouseBillDao extends PlayloungeBasicDao implements
        IPlayloungeWarehouseBillDao {

    @Override
    public List<Bill> findWarehouseInBills(String storageId, String beginDate,
                                           String endDate, final String ownerId, final int token) {

        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,chnum,mem from dj_zgsjhtzda where "
                                + "flag=1 and status=0 and Incode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item"));
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);
                                bill.setBillType("仓库入库");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setInitQty(rs.getLong("chnum"));
                                bill.setActQty(rs.getLong("chnum"));
                                bill.setTotQty((long) rs.getDouble("num"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });

        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseInBillDtls(final String billId,
                                                 final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        listBillDtls = this.playloungeJdbcTemplate
                .query("select item,chnum,clthno,color,size,num from dj_zgsjhtzdb where item=?",
                        new Object[]{src[1]}, new RowMapper<BillDtl>() {
                            @Override
                            public BillDtl mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                BillDtl billDtl = new BillDtl();
                                billDtl.setType(token);
                                billDtl.setBillNo(rs.getString("item"));
                                billDtl.setBillId(billId);
                                billDtl.setStyleId(rs.getString("clthno"));
                                billDtl.setColorId(rs.getString("color"));
                                billDtl.setSizeId(rs.getString("size"));
                                billDtl.setSku(billDtl.getStyleId()
                                        + billDtl.getColorId()
                                        + billDtl.getSizeId());
                                billDtl.setId(billDtl.getSku());
                                billDtl.setQty((long) rs.getDouble("num"));
                                billDtl.setActQty(rs.getLong("chnum"));
                                billDtl.setInitQty(rs.getLong("chnum"));
                                return billDtl;
                            }
                        });
        return listBillDtls;
    }

    @Override
    public List<Bill> findWarehouseReturnOutBills(String storageId,
                                                  String beginDate, String endDate, final String ownerId,
                                                  final int token) {
        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,mem,chnum from dj_zgsthtzda where "
                                + "flag=0 and status=0 and submit=1 and outcode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item"));
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                bill.setBillType("仓库退货出库");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty(rs.getLong("num"));
                                bill.setInitQty(rs.getLong("chnum"));
                                bill.setActQty(rs.getLong("chnum"));

                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });
        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseReturnOutBillDtls(final String billId,
                                                        final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        listBillDtls = this.playloungeJdbcTemplate
                .query("select item,clthno,color,size,num,chnum from dj_zgsthtzdb where item=?",
                        new Object[]{src[1]}, new RowMapper<BillDtl>() {
                            @Override
                            public BillDtl mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                BillDtl billDtl = new BillDtl();
                                billDtl.setType(token);
                                billDtl.setBillNo(rs.getString("item"));
                                billDtl.setBillId(billId);
                                billDtl.setStyleId(rs.getString("clthno"));
                                billDtl.setColorId(rs.getString("color"));
                                billDtl.setSizeId(rs.getString("size"));
                                billDtl.setSku(billDtl.getStyleId()
                                        + billDtl.getColorId()
                                        + billDtl.getSizeId());
                                billDtl.setId(billDtl.getSku());
                                billDtl.setQty(rs.getLong("num"));
                                billDtl.setActQty(rs.getLong("chnum"));
                                billDtl.setInitQty(rs.getLong("chnum"));
                                return billDtl;
                            }
                        });
        return listBillDtls;
    }

    @Override
    public List<Bill> findWarehouseReturnInBills(String storageId,
                                                 String beginDate, String endDate, final String ownerId,
                                                 final int token) {
        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,mem,types from dj_zgthda where "
                                + "flag=1 and status=0 and accept=0 and incode=? and  billDate between ? and ? and item not in (select item from dj_zgtrda)",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item") + "_"
                                        + rs.getString("types") + "_"
                                        + PlayloungeConstants.BillTo.SHOP);
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                bill.setBillType("仓库退货入库");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty((long) rs.getDouble("num"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });

        List<Bill> listCustBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,mem,types from dj_jxsthtzda where "
                                + "flag=1 and status=0 and incode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item") + "_"
                                        + rs.getString("types") + "_"
                                        + PlayloungeConstants.BillTo.CUST);
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                bill.setBillType("仓库退货入库");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty((long) rs.getDouble("num"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });
        List<Bill> lists = new ArrayList<>();
        if (CommonUtil.isNotBlank(listCustBill)) {
            lists.addAll(listCustBill);
        }
        if (CommonUtil.isNotBlank(listBill)) {
            lists.addAll(listBill);
        }
        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseReturnInBillDtls(final String billId,
                                                       final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        if (src[3].equals(String.valueOf(PlayloungeConstants.BillTo.SHOP))) {
            listBillDtls = this.playloungeJdbcTemplate
                    .query("select item,clthno,color,size,num from dj_zgthdb where item=?",
                            new Object[]{src[1]}, new RowMapper<BillDtl>() {
                                @Override
                                public BillDtl mapRow(ResultSet rs, int index)
                                        throws SQLException {
                                    BillDtl billDtl = new BillDtl();
                                    billDtl.setType(token);
                                    billDtl.setBillNo(rs.getString("item"));
                                    billDtl.setBillId(billId);
                                    billDtl.setStyleId(rs.getString("clthno"));
                                    billDtl.setColorId(rs.getString("color"));
                                    billDtl.setSizeId(rs.getString("size"));
                                    billDtl.setSku(billDtl.getStyleId()
                                            + billDtl.getColorId()
                                            + billDtl.getSizeId());
                                    billDtl.setId(billDtl.getSku());
                                    billDtl.setQty((long) rs.getDouble("num"));
                                    return billDtl;
                                }
                            });
        } else {
            listBillDtls = this.playloungeJdbcTemplate
                    .query("select item,clthno,color,size,num from dj_jxsthtzdb where item=?",
                            new Object[]{src[1]}, new RowMapper<BillDtl>() {
                                @Override
                                public BillDtl mapRow(ResultSet rs, int index)
                                        throws SQLException {
                                    BillDtl billDtl = new BillDtl();
                                    billDtl.setType(token);
                                    billDtl.setBillNo(rs.getString("item"));
                                    billDtl.setBillId(billId);
                                    billDtl.setStyleId(rs.getString("clthno"));
                                    billDtl.setColorId(rs.getString("color"));
                                    billDtl.setSizeId(rs.getString("size"));
                                    billDtl.setSku(billDtl.getStyleId()
                                            + billDtl.getColorId()
                                            + billDtl.getSizeId());
                                    billDtl.setId(billDtl.getSku());
                                    billDtl.setQty((long) rs.getDouble("num"));
                                    return billDtl;
                                }
                            });
        }
        return listBillDtls;
    }

    @Override
    public List<Bill> findWarehouseOutBills(String storageId, String beginDate,
                                            String endDate, final String ownerId, final int token) {
        List<Bill> lists = new ArrayList<>();
        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,chnum,Incode,num,mem,types from dj_zgchtzda where "
                                + "flag=1 and status=0 and outcode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item") + "_"
                                        + rs.getString("types") + "_"
                                        + PlayloungeConstants.BillTo.SHOP);
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);
                                // 0-无，1-订单，2-补单
                                if (rs.getString("types").equals("0")) {
                                    bill.setBillType("手动出库单");
                                } else if (rs.getString("types").equals("1")) {
                                    bill.setBillType("订单");
                                } else {
                                    bill.setBillType("补单");
                                }
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty(rs.getLong("num"));
                                bill.setInitQty(rs.getLong("chnum"));
                                bill.setActQty(rs.getLong("chnum"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });

        List<Bill> listCustBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,chnum,mem,types from dj_jxschtzda where "
                                + "flag=1 and status=0 and outcode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item") + "_"
                                        + rs.getString("types") + "_"
                                        + PlayloungeConstants.BillTo.CUST);
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                if (rs.getString("types").equals("0")) {
                                    bill.setBillType("手动出库单");
                                } else if (rs.getString("types").equals("1")) {
                                    bill.setBillType("订单");
                                } else {
                                    bill.setBillType("补单");
                                }
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty(rs.getLong("num"));
                                bill.setInitQty(rs.getLong("chnum"));
                                bill.setActQty(rs.getLong("chnum"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });
        if (CommonUtil.isNotBlank(listBill)) {
            lists.addAll(listBill);
        }
        if (CommonUtil.isNotBlank(listCustBill)) {
            lists.addAll(listCustBill);
        }
        return lists;
    }

    @Override
    public List<BillDtl> findWarehouseOutBillDtls(final String billId,
                                                  final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        if (src[3].equals(String.valueOf(PlayloungeConstants.BillTo.SHOP))) {
            listBillDtls = this.playloungeJdbcTemplate
                    .query("select item,clthno,color,size,num,chnum from dj_zgchtzdb where item=?",
                            new Object[]{src[1]}, new RowMapper<BillDtl>() {
                                @Override
                                public BillDtl mapRow(ResultSet rs, int index)
                                        throws SQLException {
                                    BillDtl billDtl = new BillDtl();
                                    billDtl.setType(token);
                                    billDtl.setBillNo(rs.getString("item"));
                                    billDtl.setBillId(billId);
                                    billDtl.setStyleId(rs.getString("clthno"));
                                    billDtl.setColorId(rs.getString("color"));
                                    billDtl.setSizeId(rs.getString("size"));
                                    billDtl.setSku(billDtl.getStyleId()
                                            + billDtl.getColorId()
                                            + billDtl.getSizeId());
                                    billDtl.setId(billDtl.getSku());
                                    billDtl.setQty(rs.getLong("num"));
                                    billDtl.setActQty(rs.getLong("chnum"));
                                    billDtl.setInitQty(rs.getLong("chnum"));
                                    return billDtl;
                                }
                            });
        } else {
            listBillDtls = this.playloungeJdbcTemplate
                    .query("select item,clthno,color,size,num,chnum from dj_jxschtzdb where item=?",
                            new Object[]{src[1]}, new RowMapper<BillDtl>() {
                                @Override
                                public BillDtl mapRow(ResultSet rs, int index)
                                        throws SQLException {
                                    BillDtl billDtl = new BillDtl();
                                    billDtl.setType(token);
                                    billDtl.setBillNo(rs.getString("item"));
                                    billDtl.setBillId(billId);
                                    billDtl.setStyleId(rs.getString("clthno"));
                                    billDtl.setColorId(rs.getString("color"));
                                    billDtl.setSizeId(rs.getString("size"));
                                    billDtl.setSku(billDtl.getStyleId()
                                            + billDtl.getColorId()
                                            + billDtl.getSizeId());
                                    billDtl.setId(billDtl.getSku());
                                    billDtl.setQty(rs.getLong("num"));
                                    billDtl.setActQty(rs.getLong("chnum"));
                                    billDtl.setInitQty(rs.getLong("chnum"));
                                    billDtl.setActQty(rs.getLong("chnum"));
                                    return billDtl;
                                }
                            });
        }
        return listBillDtls;
    }

    @Override
    public List<Bill> findWarehouseTransferInBills(String storageId,
                                                   String beginDate, String endDate, final String ownerId,
                                                   final int token) {
        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,num,mem from dj_ycda where "
                                + "flag=1 and status=0 and accept=0 and incode=? and  billDate between ? and ? and item not in (select item from dj_yrda)",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item"));
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                bill.setBillType("仓库调拨入库");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty((long) rs.getDouble("num"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });

        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseTransferInBillDtls(final String billId,
                                                         final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        listBillDtls = this.playloungeJdbcTemplate.query(
                "select item,clthno,color,size,num from dj_ycdb where item=?",
                new Object[]{src[1]}, new RowMapper<BillDtl>() {
                    @Override
                    public BillDtl mapRow(ResultSet rs, int index)
                            throws SQLException {
                        BillDtl billDtl = new BillDtl();
                        billDtl.setType(token);
                        billDtl.setBillNo(rs.getString("item"));
                        billDtl.setBillId(billId);
                        billDtl.setStyleId(rs.getString("clthno"));
                        billDtl.setColorId(rs.getString("color"));
                        billDtl.setSizeId(rs.getString("size"));
                        billDtl.setSku(billDtl.getStyleId()
                                + billDtl.getColorId() + billDtl.getSizeId());
                        billDtl.setId(billDtl.getSku());
                        billDtl.setQty((long) rs.getDouble("num"));
                        return billDtl;
                    }
                });
        return listBillDtls;
    }

    @Override
    public List<Bill> findWarehouseTransferOutBills(String storageId,
                                                    String beginDate, String endDate, final String ownerId,
                                                    final int token) {
        List<Bill> listBill = this.playloungeJdbcTemplate
                .query("select item,billDate,Outcode,Incode,chnum,num,mem,types from dj_yctzda where "
                                + "flag=1 and status=0 and outcode=? and  billDate between ? and ?",
                        new Object[]{storageId, beginDate, endDate},
                        new RowMapper<Bill>() {
                            @Override
                            public Bill mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Bill bill = new Bill();
                                bill.setType(token);
                                bill.setId(String.valueOf(new Date().getTime())
                                        + "_" + rs.getString("item") + "_"
                                        + rs.getString("types"));
                                bill.setBillNo(rs.getString("item"));// 用客户的Id作为本地的billNo
                                bill.setBillDate(rs.getDate("billDate"));
                                bill.setOwnerId(ownerId);

                                bill.setBillType("仓库调拨发货");
                                bill.setOrigId(rs.getString("Outcode"));// //
                                // 发货方ID
                                Unit unit1 = CacheManager.getUnitByCode(bill
                                        .getOrigId());
                                if (CommonUtil.isNotBlank(unit1)) {
                                    bill.setOrigName(unit1.getName());
                                }
                                bill.setDestId(rs.getString("Incode"));// 收货方
                                Unit unit2 = CacheManager.getUnitByCode(bill
                                        .getDestId());
                                if (CommonUtil.isNotBlank(unit2)) {
                                    bill.setDestName(unit2.getName());
                                }
                                bill.setTotQty(rs.getLong("num"));
                                bill.setInitQty(rs.getLong("chnum"));
                                bill.setActQty(rs.getLong("chnum"));
                                bill.setRemark(rs.getString("mem"));
                                return bill;
                            }
                        });

        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseTransferOutBillDtls(final String billId,
                                                          final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        listBillDtls = this.playloungeJdbcTemplate
                .query("select item,clthno,color,size,num,chnum from dj_yctzdb where item=?",
                        new Object[]{src[1]}, new RowMapper<BillDtl>() {
                            @Override
                            public BillDtl mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                BillDtl billDtl = new BillDtl();
                                billDtl.setType(token);
                                billDtl.setBillNo(rs.getString("item"));
                                billDtl.setBillId(billId);
                                billDtl.setStyleId(rs.getString("clthno"));
                                billDtl.setColorId(rs.getString("color"));
                                billDtl.setSizeId(rs.getString("size"));
                                billDtl.setSku(billDtl.getStyleId()
                                        + billDtl.getColorId()
                                        + billDtl.getSizeId());
                                billDtl.setId(billDtl.getSku());
                                billDtl.setQty(rs.getLong("num"));
                                billDtl.setActQty(rs.getLong("chnum"));
                                billDtl.setInitQty(rs.getLong("chnum"));
                                billDtl.setActQty(rs.getLong("chnum"));
                                return billDtl;
                            }
                        });
        return listBillDtls;
    }

    public List<ErpStock> findErpStocks(String sku, String styleId, String colorId,
                                        String sizeId, String warehouseId) {
        StringBuilder sql = new StringBuilder("select * from store s where  s.num!=0 ");

        if (CommonUtil.isNotBlank(warehouseId)) {
            sql.append(" and s.Whcode='").append(warehouseId).append("'");
        }
        if (CommonUtil.isNotBlank(styleId)) {
            if (!sql.toString().contains("where")) {
                sql.append(" where s.clthno like '").append(styleId).append("%'");
            } else {
                sql.append(" and s.clthno like '").append(styleId).append("%'");
            }
        }
        if (CommonUtil.isNotBlank(colorId)) {
            if (!sql.toString().contains("where")) {
                sql.append(" where s.color like '").append(colorId).append("%'");
            } else {
                sql.append(" and s.color like '").append(colorId).append("%'");
            }
        }
        if (CommonUtil.isNotBlank(sizeId)) {
            if (!sql.toString().contains("where")) {
                sql.append(" where s.size like '").append(sizeId).append("%'");
            } else {
                sql.append(" and s.size like '").append(sizeId).append("%'");
            }
        }
        if (CommonUtil.isNotBlank(sku)) {
            if (!sql.toString().contains("where")) {
                sql.append(" where  s.clthno+s.color+s.size like '").append(sku).append("%'");
            } else {
                sql.append(" and s.clthno+s.color+s.size like '").append(sku).append("%'");
            }
        }
        sql.append(" order by s.Whcode,s.clthno,s.color,s.size");
        return this.playloungeJdbcTemplate
                .query(sql.toString(),
                        new Object[]{}, new RowMapper<ErpStock>() {
                            @Override
                            public ErpStock mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                ErpStock erpStock = new ErpStock();
                                erpStock.setWarehouseId(rs.getString("Whcode"));
                                Unit unit = CacheManager.getUnitByCode(erpStock.getWarehouseId());
                                if (CommonUtil.isNotBlank(unit)) {
                                    erpStock.setWarehouseName(unit.getName());
                                }
                                erpStock.setStyleId(rs.getString("clthno"));
                                Style style = CacheManager.getStyleById(erpStock.getStyleId());
                                if (CommonUtil.isNotBlank(style)) {
                                    erpStock.setStyleName(style.getStyleName());
                                }
                                erpStock.setColorId(rs.getString("color"));
                                Color color = CacheManager.getColorById(erpStock.getColorId());
                                if (CommonUtil.isNotBlank(color)) {
                                    erpStock.setColorName(color.getColorName());
                                }
                                erpStock.setSizeId(rs.getString("size"));
                                Size size = CacheManager.getSizeById(erpStock.getSizeId());
                                if (CommonUtil.isNotBlank(size)) {
                                    erpStock.setSizeName(size.getSizeName());
                                }
                                erpStock.setQty(rs.getLong("num"));
                                return erpStock;
                            }
                        });
    }

    public List<ErpStock> findErpStocks(String sku) {
        StringBuilder sql = new StringBuilder("select * from store s where  s.num!=0 ");

        if (CommonUtil.isNotBlank(sku)) {
            if (!sql.toString().contains("where")) {
                sql.append(" where  s.clthno+s.color+s.size in (").append(sku).append(")");
            } else {
                sql.append(" and s.clthno+s.color+s.size in (").append(sku).append(")");
            }
        }
        sql.append(" order by s.Whcode,s.clthno+s.color+s.size");
        return this.playloungeJdbcTemplate
                .query(sql.toString(),
                        new Object[]{}, new RowMapper<ErpStock>() {
                            @Override
                            public ErpStock mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                ErpStock erpStock = new ErpStock();
                                erpStock.setWarehouseId(rs.getString("Whcode"));
                                Unit unit = CacheManager.getUnitByCode(erpStock.getWarehouseId());
                                if (CommonUtil.isNotBlank(unit)) {
                                    erpStock.setWarehouseName(unit.getName());
                                }
                                erpStock.setStyleId(rs.getString("clthno"));
                                Style style = CacheManager.getStyleById(erpStock.getStyleId());
                                if (CommonUtil.isNotBlank(style)) {
                                    erpStock.setStyleName(style.getStyleName());
                                }
                                erpStock.setColorId(rs.getString("color"));
                                Color color = CacheManager.getColorById(erpStock.getColorId());
                                if (CommonUtil.isNotBlank(color)) {
                                    erpStock.setColorName(color.getColorName());
                                }
                                erpStock.setSizeId(rs.getString("size"));
                                Size size = CacheManager.getSizeById(erpStock.getSizeId());
                                if (CommonUtil.isNotBlank(size)) {
                                    erpStock.setSizeName(size.getSizeName());
                                }
                                erpStock.setQty(rs.getLong("num"));
                                return erpStock;
                            }
                        });
    }

    public List<Product> findErpImg(final String styleId, final String colorId) {
        StringBuilder sql = new StringBuilder("select url from jbClothPicture s where s.Disp='1' and s.ClthNo=? ");
        if (CommonUtil.isNotBlank(colorId) && !"".equals(colorId)) {
            sql.append(" and s.color= '").append(colorId).append("'");
        }
        return this.playloungeJdbcTemplate
                .query(sql.toString(),
                        new Object[]{styleId}, new RowMapper<Product>() {
                            @Override
                            public Product mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Product product = new Product();
                                product.setColorId(colorId);
                                product.setStyleId(styleId);
                                product.setImage("http://yuyong.playlounge.cn:2865/Images/" + rs.getString("url"));
                                return product;
                            }
                        });
    }

    @Override
    public List<Bill> findWarehouseInventoryBills(String storageId,
                                                  String beginDate, String endDate,
                                                  String billDate, String conditions,
                                                  String ownerId, int token) {
        Date date = new Date();
        String billId = String.valueOf(token) + "_" + ownerId + "_"
                    + CommonUtil.getDateString(date, "yyMMddHHmmss");
        List<BillDtl> dtls = findWarehouseInventoryBillDtls(billId,billDate,conditions, token);
        List<Bill> listBill = new ArrayList<Bill>();
        if (CommonUtil.isBlank(dtls)) {
        } else {
            Bill bill = new Bill();
            long totQty = 0l;
            for (BillDtl dtl : dtls) {
                totQty += dtl.getQty();
            }
            bill.setTotQty(totQty);
            bill.setId(billId);
            bill.setBillNo(bill.getId());
            bill.setSkuQty(Long.parseLong(String.valueOf(dtls.size())));
            bill.setOrigId(ownerId);
            bill.setType(token);
            if(CommonUtil.isBlank(billDate)){
                bill.setBillDate(date);
            }else{
                try {
                    bill.setBillDate(CommonUtil.converStrToDate(billDate,"yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    bill.setBillDate(date);
                }
            }
            bill.setDtlList(dtls);
            listBill.add(bill);
        }
        return listBill;
    }

    @Override
    public List<BillDtl> findWarehouseInventoryBillDtls(final String billId,
                                                        String billDate, String conditions,
                                                        final int token) {
        List<BillDtl> listBillDtls = null;
        String[] src = billId.split("_");
        StringBuffer sqlCondition=new StringBuffer();
        try{
              sqlCondition=DaoUtil.convertPropertis(JSON.parseObject(conditions));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(CommonUtil.isBlank(billDate)){
            billDate=CommonUtil.getDateString(new Date(),"yyyy-MM-dd");
        }
        StringBuffer sql=new StringBuffer("begin declare @guid varchar(40) ; select d.whcode,d.clthno,d.color,d.size,d.num ,b.barcode")
                .append(" from  fn_Get_AllStoreInfo(@guid,'ADM',?) d  ,jbClothBarcode b,jbCloth s ")
                .append(" where  d.whcode=? and s.clthno=d.clthno and d.clthno=b.clthno and d.size=b.size and d.color=b.color and d.num>0 ")
                .append(sqlCondition)
                .append(" ;end");
/*
        select clthno,color,size,num from store where Whcode=? and num!=0
*/
        listBillDtls = this.playloungeJdbcTemplate
                .query(sql.toString(),
                        new Object[]{billDate,src[1]}, new RowMapper<BillDtl>() {
                            @Override
                            public BillDtl mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                BillDtl billDtl = new BillDtl();
                                billDtl.setType(token);
                                billDtl.setBillNo(billId);
                                billDtl.setBillId(billId);
                                billDtl.setStyleId(rs.getString("clthno"));
                                billDtl.setColorId(rs.getString("color"));
                                billDtl.setSizeId(rs.getString("size"));
                                billDtl.setSku(rs.getString("barcode"));
                                billDtl.setId(billId + "_" + billDtl.getSku());
                                billDtl.setQty(rs.getLong("num"));
                                return billDtl;
                            }
                        });
        return listBillDtls;
    }
}
