package com.casesoft.dmc.controller.exchange;

/**
 * Created by WinLi on 2017-06-06.
 */
public class ExcgConstant {
    public static class SqlServer {
        public static String Select_All_Table_Name = "SELECT name as table_name FROM SysObjects Where XType='U' ORDER BY Name";
        public static String Select_All_Column_Name = "select name as column_name from syscolumns where id = object_id('%s');";
    }

    public static class Oracle {
        public static String Select_All_Table_Name = "select table_name from user_tables;";
        public static String Select_All_Column_Name = "SELECT COLUMN_NAME FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '%s' ORDER BY COLUMN_ID;";
    }

    public static class Mysql {
        public static String Select_All_Table_Name =
                "select table_name from information_schema.tables where table_schema='%s' and  table_type='base table'";
        public static String Select_All_Column_Name =
                "select column_name from information_schema.columns where table_schema='%s' and table_name='%s';";
    }
}
