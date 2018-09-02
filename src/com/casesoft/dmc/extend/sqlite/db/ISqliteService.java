package com.casesoft.dmc.extend.sqlite.db;

import com.casesoft.dmc.extend.sqlite.model.SqliteEpcBindBarcode;
import com.casesoft.dmc.extend.sqlite.model.SqliteEpcStock;
import com.casesoft.dmc.extend.sqlite.model.SqliteProduct;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface ISqliteService {
    /**
     * 批量存储商品信息
     * @param sqliteProductDao
     * @param sqliteProducts
     * */
    void batchProducts(Dao<SqliteProduct, String> sqliteProductDao, List<SqliteProduct> sqliteProducts) throws SQLException;


    /**
     * 绑定信息
     * @param sqliteEpcBindBarcodeDao
     * @param sqliteEpcBindBarcodeList
     * */
    void batchEpcBand(Dao<SqliteEpcBindBarcode, String> sqliteEpcBindBarcodeDao, List<SqliteEpcBindBarcode> sqliteEpcBindBarcodeList) throws SQLException;

    /**
     * 批量存库存品信息
     * @param sqliteStockDao
     * @param sqliteStocks
     * */
    void batchEpcStocks(Dao<SqliteEpcStock, String> sqliteStockDao, List<SqliteEpcStock> sqliteStocks) throws SQLException;
}
