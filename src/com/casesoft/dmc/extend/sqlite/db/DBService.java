package com.casesoft.dmc.extend.sqlite.db;

import com.casesoft.dmc.extend.sqlite.model.SqliteEpcBindBarcode;
import com.casesoft.dmc.extend.sqlite.model.SqliteEpcStock;
import com.casesoft.dmc.extend.sqlite.model.SqliteProduct;
import com.j256.ormlite.dao.Dao;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public class DBService implements ISqliteService {
    /**
     * 批量存储商品信息
     * @param sqliteProductDao
     * @param sqliteProducts
     * */
    @Override
    public void batchProducts(Dao<SqliteProduct,String> sqliteProductDao, List<SqliteProduct> sqliteProducts) throws SQLException {
        sqliteProductDao.createOrUpdate(sqliteProducts);
    }

    @Override
    public void batchEpcBand(Dao<SqliteEpcBindBarcode, String> sqliteEpcBindBarcodeDao, List<SqliteEpcBindBarcode> sqliteEpcBindBarcodeList) throws SQLException {
        sqliteEpcBindBarcodeDao.createOrUpdate(sqliteEpcBindBarcodeList);
    }

    @Override
    public void batchEpcStocks(Dao<SqliteEpcStock, String> sqliteStockDao, List<SqliteEpcStock> sqliteStocks) throws SQLException {
        sqliteStockDao.createOrUpdate(sqliteStocks);
    }


}
