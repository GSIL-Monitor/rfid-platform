package com.casesoft.dmc.extend.sqlite.db;

import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.file.FileUtil;
import com.casesoft.dmc.core.util.file.PropertyUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class DBHelper {
    private static Logger logger = LoggerFactory.getLogger(DBHelper.class);

    private static ConnectionSource connectionSource = null;
    private static String sqliteUrl = "base.db";


    //region 初始化sqlite

    /**
     * 初始化
     */
    private static void initDB() {
        try {
            String baseSqlitePath = String.format("jdbc:sqlite:%s", getDBPath());//sqlite存储路径
            if (CommonUtil.isBlank(connectionSource)) {
                connectionSource =
                        new JdbcConnectionSource(baseSqlitePath);
            }
        } catch (Exception e) {
            logger.error("Sqlite连接失败" + e.getMessage());
            e.printStackTrace();
        }
    }
    //endregion

    //region sqlite路径

    /**
     * sqlite路径
     */
    private static String getDBPath() {
        try {
            String tempPath = PropertyUtil.getValue("caseSoft_temp");// + "\\"
            File file=new File(new StringBuffer(tempPath).append(File.separator).append("sqlite").append(File.separator).toString());
            if(!file.exists()){
                file.mkdirs();
            }
            return tempPath + sqliteUrl;//sqlite存储路径
        } catch (Exception e) {
            logger.error("Sqlite路径获取失败" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //endregion


    //region 获取数据库连接

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static ConnectionSource getConnectionSource() {
        if (CommonUtil.isBlank(connectionSource)) {
            initDB();
        }
        return connectionSource;
    }
    //endregion

    //region 根据class获取dao

    /**
     * 根据class获取dao
     *
     * @param clazz
     * @return
     */
    public static synchronized <D extends Dao<T, ?>, T> D createDao(Class<T> clazz) {
        try {
            TableUtils.createTableIfNotExists(getConnectionSource(), clazz);
            return DaoManager.createDao(getConnectionSource(), clazz);
        } catch (Exception e) {
            logger.error("Sqlite获取Dao失败" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region 关闭连接

    /**
     * 根据class获取dao
     */
    public static void close() {
        try {
            if (CommonUtil.isNotBlank(connectionSource)) {
                connectionSource.close();
            }
        } catch (Exception e) {
            logger.error("Sqlite关闭连接失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    //endregion

    //region 压缩获取sqlite文件
    /**
     * 压缩获取sqlite文件
     * @param zipName
     * @return
     */
    public static Boolean zipDB(String zipName){
        try {
            String destDBPath=getDBPath().replace(".db","_tmp.db");
            FileUtil.copyFile(getDBPath(),destDBPath);
            FileUtil.zip(destDBPath,zipName);
            return true;
        }catch (Exception e){
            logger.error(String.format("复制文件%s失败",getDBPath()));
            e.printStackTrace();
            return false;

        }
    }

    //endregion

}
