package com.casesoft.dmc.extend.playlounge.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.playlounge.dao.basic.ISynErpDao;
import com.casesoft.dmc.extend.playlounge.dao.basic.PlayloungeBasicDao;
import com.casesoft.dmc.extend.third.model.DayThirdStock;
import com.casesoft.dmc.extend.third.model.ThirdStock;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.erp.Bill;
import com.casesoft.dmc.model.erp.BillDtl;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.shop.Customer;
import com.casesoft.dmc.model.shop.FittingRecord;
import com.casesoft.dmc.model.sys.Unit;
import com.casesoft.dmc.model.sys.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class PlayloungeSynErpDao extends PlayloungeBasicDao implements ISynErpDao {

    private int index = 0;

    @Override
    public List<Style> findAllStyle() {
        final String dateTime = CommonUtil.getDateString(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.HOUR_OF_DAY, -2);
        String begin = CommonUtil.getDateString(ca.getTime(), "yyyy-MM-dd HH:mm:00");
        System.out.println("开始：" + begin);
        List<Style> styles = this.playloungeJdbcTemplate.query(
                "select clthno,name,RuleCode,SizeGroup,BrandCode,"
                        + "Year,Season,LargeCategory,SmallCategory,"
                        + "Catena,Style,Type,Unit,sex,"
                        + "Company,ListingDate,"
                        + "gbmny,qtmny,Price1,Cost1,Cost6,"
                        + "ChangerName,ChangeDate,"
                        + "ChangeDay,Property1,P"
                        + "roperty2,Property3,Property4,Property5,"
                        + "Property6,Property7,Property8,sclothno,"
                        + "ean_13,banner,aqjslb,dj,cd,jy,cost7,cost8,cost9,"
                        + "location,firstindate,lastindate,"
                        + "firstoutdate,lastoutdate,indate"
                        + " from jbCloth where  changeDate>=?", new Object[]{begin},
                new RowMapper<Style>() {
                    @Override
                    public Style mapRow(ResultSet rs, int arg1)
                            throws SQLException {
                        Style style = new Style();
                        style.setUpdateTime(dateTime);
                        style.setBrandCode(rs.getString("BrandCode"));
                        style.setId(rs.getString("clthno"));
                        style.setStyleId(rs.getString("clthno"));
                        style.setStyleName(rs.getString("name"));
                        style.setSizeSortId(rs.getString("SizeGroup"));
                        style.setClass1(rs.getString("BrandCode"));
                        style.setClass2(rs.getString("Year"));
                        style.setClass3(rs.getString("LargeCategory"));
                        style.setClass4(rs.getString("SmallCategory"));
                        style.setClass5(rs.getString("sex"));
                        style.setClass6(rs.getString("Company"));
                        if (CommonUtil.isBlank(rs.getString("Location"))) {

                        } else {
                            style.setClass7(rs.getString("Location"));

                        }
                        style.setClass8(rs.getString("Property3"));
                        style.setClass9(rs.getString("Property5"));
                        style.setClass10(rs.getString("Property6"));

                        style.setPrice(rs.getDouble("Price1"));
                        style.setPreCast(rs.getDouble("Cost1"));
                        style.setPuPrice(rs.getDouble("Cost6"));
                        style.setRemark(rs.getString("aqjslb"));
                        style.setSeqNo(arg1);
                        return style;
                    }
                });

        return styles;
    }

    @Override
    public List<Color> findAllColor() {
        final String dateTime = CommonUtil.getDateString(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        List<Color> colors = this.playloungeJdbcTemplate.query(
                "select item,name from color", new RowMapper<Color>() {
                    @Override
                    public Color mapRow(ResultSet rs, int arg1)
                            throws SQLException {
                        Color color = new Color();
                        color.setColorId(rs.getString("item"));
                        color.setId(rs.getString("item"));
                        color.setColorName(rs.getString("name"));
                        color.setUpdateTime(dateTime);
                        return color;
                    }
                });

        return colors;
    }

    @Override
    public List<ColorGroup> findAllColorGroup() {
        return null;
    }

    @Override
    public List<Size> findAllSize() {
        final String dateTime = CommonUtil.getDateString(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        List<Size> sizes = this.playloungeJdbcTemplate.query(
                "select item,name from size", new RowMapper<Size>() {
                    @Override
                    public Size mapRow(ResultSet rs, int index)
                            throws SQLException {
                        Size size = new Size();
                        size.setId(rs.getString("item"));
                        size.setSizeName(rs.getString("name"));
                        size.setSizeId(rs.getString("item"));
                        size.setSeqNo(index);
                        size.setSortId("001");
                        size.setUpdateTime(dateTime);
                        return size;
                    }
                });
        List<Map<String, Object>> rows = this.playloungeJdbcTemplate
                .queryForList("SELECT sizeitem,sizegroupitem FROM Size_Group");
        if (CommonUtil.isNotBlank(rows)) {
            if (CommonUtil.isNotBlank(sizes)) {
                for (Size s : sizes) {
                    if (CommonUtil.isBlank(s.getSizeId())) {
                        System.out.println(s.getSizeName() + "===="
                                + sizes.indexOf(s));
                    }
                    for (int i = 0; i < rows.size(); i++) {
                        if (rows.get(i).get("sizeitem").toString()
                                .equalsIgnoreCase(s.getSizeId())) {
                            s.setSortId(rows.get(i).get("sizegroupitem").toString());
                            continue;
                        }
                    }
                }
            }
        }
        return sizes;
    }

    @Override
    public List<Product> findAllProduct() {
        try {
            final String dateTime = CommonUtil.getDateString(new Date(),
                    "yyyy-MM-dd HH:mm:ss");
            index = CacheManager.getMaxProductId();
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.HOUR_OF_DAY, -2);
            String begin = CommonUtil.getDateString(ca.getTime(), "yyyy-MM-dd HH:mm:00");
            System.out.println("开始：" + begin);

            List<Product> products = this.playloungeJdbcTemplate
                    .query("select b.barcode,s.clthno,s.name,s.RuleCode,s.SizeGroup,s.BrandCode, " +
                                    "s.Year,s.Season,s.LargeCategory,s.SmallCategory, " +
                                    "s.Catena,s.Style,s.Type,s.Unit,s.sex,s.Company, " +
                                    "s.ListingDate,s.gbmny, " +
                                    "s.qtmny,s.Price1,s.Cost1,s.ChangerName,s.ChangeDate,s.ChangeDay,s.Property1, " +
                                    "s.Property2,s.Property3,s.Property4,s.Property5, " +
                                    "s.Property6,s.Property7,s.Property8,s.sclothno,s." +
                                    "ean_13,s.banner,s.aqjslb,s.dj,s.cd,s.jy,s.cost7,s.cost8,s.cost9,s." +
                                    "location,s.firstindate,s.lastindate," +
                                    "s.firstoutdate,s.lastoutdate,s.indate,i.item as size,c.item as color " +
                                    "from jbCloth s,size i, Color c,jbClothBarcode b where " +
                                    "s.clthno=b.clthno and " +
                                    "b.size=i.item and b.color=c.item and  s.changeDate>=?", new Object[]{begin},
                            new RowMapper<Product>() {
                                @Override
                                public Product mapRow(ResultSet rs, int arg1)
                                        throws SQLException {
                                    Product product = new Product();
                                    product.setUpdateTime(dateTime);
                                    product.setBrandCode(rs.getString("BrandCode"));
                                    product.setStyleId(rs.getString("clthno"));
                                    product.setColorId(rs.getString("color"));
                                    product.setSizeId(rs.getString("size"));
                                    Color color = CacheManager.getColorById(product.getColorId());
                                    if (CommonUtil.isNotBlank(color)) {
                                        product.setColorName(color.getColorName());
                                    }
                                    Size size = CacheManager.getSizeById(product.getSizeId());
                                    if (CommonUtil.isNotBlank(size)) {
                                        product.setSizeName(size.getSizeName());
                                    }
                                    product.setSizeSortId(rs.getString("SizeGroup"));
                                    product.setStyleName(rs.getString("name"));
                            /*	product.setSizeName(rs.getString("sizeName"));
                                product.setColorName(rs.getString("colorName"));*/
                                    StringBuffer code = new StringBuffer();
                                /*    product.setCode(code.append(product.getStyleId()).append(
                                            product.getColorId())
                                            .append(product.getSizeId()).toString());*/
                                    product.setCode(rs.getString("barcode"));
                                    Product pt = CacheManager.getProductByCode(product.getCode());
                                    if (CommonUtil.isNotBlank(pt)) {
                                        product.setId(pt.getId());
                                    } else {
                                        String format = "000000";
                                        String suf = String.valueOf(index);
                                        String id = format.substring(0, format.length() - suf.length())
                                                + suf;
                                        product.setId(id);
                                        System.out.println("新增：" + id + ":" + index);
                                        index++;
                                    }
                                    product.setBarcode(product.getCode());
                                    product.setEan(rs.getString("ean_13"));
                                    product.setClass1(rs.getString("BrandCode"));
                                    product.setClass2(rs.getString("Year"));
                                    product.setClass3(rs.getString("LargeCategory"));
                                    product.setClass4(rs.getString("SmallCategory"));
                                    product.setClass5(rs.getString("sex"));
                                    product.setClass6(rs.getString("Company"));
                                    product.setClass7(rs.getString("Location"));
                                    product.setClass8(rs.getString("Property3"));
                                    product.setClass9(rs.getString("Property5"));
                                    product.setClass10(rs.getString("Property6"));

                                    product.setPrice(rs.getDouble("Price1"));
                                    product.setPreCast(rs.getDouble("Cost1"));
                                    product.setRemark(rs.getString("aqjslb"));
                                    return product;
                                }
                            });
            StringBuilder sql = new StringBuilder("select clthno,size,color,url from jbClothPicture where Disp='1' and modifieddate>=?");

            List<Product> imgs = this.playloungeJdbcTemplate
                    .query(sql.toString(),
                            new Object[]{begin}, new RowMapper<Product>() {
                                @Override
                                public Product mapRow(ResultSet rs, int index)
                                        throws SQLException {
                                    Product product = new Product();
                                    product.setStyleId(rs.getString("clthno"));
                                    product.setColorId(rs.getString("color"));
                                    product.setSizeId(rs.getString("size"));
                                    product.setImage("http://yuyong.playlounge.cn:2865/Images/" + rs.getString("url"));
                                    return product;
                                }
                            });
            for (Product p : imgs) {
                for (Product p2 : products) {
                    if (p.getStyleId().equals(p2.getStyleId())) {
                        p2.setImage(p.getImage());
                    }
                    if (CommonUtil.isNotBlank(p.getColorId()) && p.getColorId().equals(p2.getColorId())) {
                        p2.setImage(p.getImage());
                    }
                }
            }
            System.out.println("下载ERP商品:" + products.size());
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载ERP失败:");
            return new ArrayList<>();
        }

    }

    @Override
    public List<SizeSort> findAllSizeSort() {
        final String dateTime = CommonUtil.getDateString(new Date(),
                "yyyy-MM-dd HH:mm:ss");
        List<SizeSort> sizeSorts = this.playloungeJdbcTemplate.query(
                "select  item,name from SizeGroup", new RowMapper<SizeSort>() {
                    @Override
                    public SizeSort mapRow(ResultSet rs, int index)
                            throws SQLException {
                        SizeSort sizeSort = new SizeSort();
                        sizeSort.setId(rs.getString("item"));
                        sizeSort.setSeqNo(index);
                        sizeSort.setSortName(rs.getString("name"));
                        sizeSort.setUpdateTime(dateTime);
                        sizeSort.setSortNo(rs.getString("item"));
                        return sizeSort;
                    }
                });
        return sizeSorts;
    }

    @Override
    public List<PropertyKey> findAllPropertyKey(final String ownerId) {
        /*
         * 品牌
		 * 
		 * 
item	name	isort	disp	VColName	kind
A	品牌	1	1	BrandCode	0
B	年份	2	1	Year	0
C	季节	3	1	Season	0
D	大类	4	1	LargeCategory	0
E	小类	5	1	SmallCategory	0
F	系列	6	1	Catena	0
G	款式	7	1	Style	0
H	版型	8	1	Type	0
I	单位	9	1	Unit	0
J	性别	10	1	Sex	0
K	厂商	11	1	Company	0
L	故事	12	1	NULL	0
M	卖场区域	13	1	Property1	1
N	买手名	14	1	Property2	1
O	材质	15	1	Property3	1
P	流水号	16	1	Property4	1
Q	库类	17	1	Property5	1
R	四季分类	18	1	Property6	1
S	合作方式	19	1	Property7	1
T	属性6	20	0	Property8	1
U	库位码	21	1	Location	0

		 */
        final Date dt = new Date();
        List<PropertyKey> propertyKeys = new ArrayList<PropertyKey>();
        List<PropertyKey> bds = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='A' ", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setRegisterId("admin");
                        property.setSeqNo(index);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("BD");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        return property;
                    }
                });
        List<PropertyKey> c1s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='A'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C1");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c2s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='B'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C2");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c3s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='D'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C3");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c4s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='E'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C4");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c5s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='J'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C5");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c6s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='K'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C6");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c7s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='U'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C7");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c8s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='O'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C8");
                        property.setId(property.getOwnerId()
                                + "-" + property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c9s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='Q'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C9");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });
        List<PropertyKey> c10s = this.playloungeJdbcTemplate.query(
                "select  Propertyitem,item,name from jbPropertymx where Propertyitem='R'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setRegisterDate(dt);
                        property.setSeqNo(index);

                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setName(rs.getString("name"));
                        property.setType("C10");
                        property.setId(property.getType() + "-" + rs.getString("Propertyitem") + "-" + property.getCode());
                        property.setRegisterId("admin");

                        return property;
                    }
                });

        /**
         * 供应商分类
         * */
        List<PropertyKey> vts = this.playloungeJdbcTemplate.query(
                "select  dlitem,item,name from jbitemmx where dlitem='A'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setSeqNo(index);

                        property.setName(rs.getString("name"));
                        property.setType("VT");
                        property.setRegisterDate(dt);
                        property.setRegisterId("admin");
                        property.setId(property.getType() + "-" + rs.getString("dlitem") + "-" + property.getCode());
                        return property;
                    }
                });
        /**
         * 门店分类
         * */
        List<PropertyKey> wts = this.playloungeJdbcTemplate.query(
                "select  dlitem,item,name from jbitemmx where dlitem='B'", new RowMapper<PropertyKey>() {
                    @Override
                    public PropertyKey mapRow(ResultSet rs, int index)
                            throws SQLException {
                        PropertyKey property = new PropertyKey();
                        property.setCode(rs.getString("item"));
                        property.setOwnerId(ownerId);
                        property.setLocked(0);
                        property.setYnuse("Y");
                        property.setSeqNo(index);
                        property.setName(rs.getString("name"));
                        property.setType("ST");
                        property.setRegisterDate(dt);
                        property.setRegisterId("admin");
                        property.setId(property.getType() + "-" + rs.getString("dlitem") + "-" + property.getCode());
                        return property;
                    }
                });
        if (CommonUtil.isNotBlank(bds)) {
            propertyKeys.addAll(bds);
        }
        if (CommonUtil.isNotBlank(c1s)) {
            propertyKeys.addAll(c1s);
        }
        if (CommonUtil.isNotBlank(c2s)) {
            propertyKeys.addAll(c2s);
        }
        if (CommonUtil.isNotBlank(c3s)) {
            propertyKeys.addAll(c3s);
        }
        if (CommonUtil.isNotBlank(c4s)) {
            propertyKeys.addAll(c4s);
        }

        if (CommonUtil.isNotBlank(c5s)) {
            propertyKeys.addAll(c5s);
        }
        if (CommonUtil.isNotBlank(c6s)) {
            propertyKeys.addAll(c6s);
        }
        if (CommonUtil.isNotBlank(c7s)) {
            propertyKeys.addAll(c7s);
        }
        if (CommonUtil.isNotBlank(c8s)) {
            propertyKeys.addAll(c8s);
        }
        if (CommonUtil.isNotBlank(c9s)) {
            propertyKeys.addAll(c9s);
        }
        if (CommonUtil.isNotBlank(c10s)) {
            propertyKeys.addAll(c10s);
        }
        if (CommonUtil.isNotBlank(vts)) {
            propertyKeys.addAll(vts);
        }
        if (CommonUtil.isNotBlank(wts)) {
            propertyKeys.addAll(wts);
        }
        Map<String, PropertyKey> map = new HashMap<>();
        for (PropertyKey p : propertyKeys) {
            if (map.containsKey(p.getId())) {
                System.out.println(JSON.toJSON(p));
            } else {
                map.put(p.getId(), p);
            }
        }
        return propertyKeys;
    }

    @Override
    public List<Unit> findAllVendor(final String ownerId) {
        final Date dt = new Date();
        List<Unit> units = this.playloungeJdbcTemplate
                .query("SELECT item,conid,kind,Nature,Area,name,allname,"
                                + "lxr,tel1,tel2,address,zip,email,fax,mem,kjm,disp,"
                                + "account,bank,bank2"
                                + " from jbcsjbk",
                        new RowMapper<Unit>() {
                            @Override
                            public Unit mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Unit vendor = new Unit();
                                vendor.setId(rs.getString("item"));
                                vendor.setSeqNo(index);
                                vendor.setCreatorId("admin");
                                vendor.setCreateTime(dt);
                                vendor.setOwnerId(ownerId);
                                vendor.setCode(rs.getString("item"));
                                vendor.setName(rs.getString("name"));
                                vendor.setType(Constant.UnitType.Vender);
                                vendor.setLinkman(rs.getString("lxr"));
                                vendor.setAddress(rs.getString("address"));
                                vendor.setPostCode(rs.getString("zip"));
                                vendor.setGroupId(rs.getString("Area"));

                                //warehouse.setEmail(rs.getString("email"));
                                vendor.setTel(rs.getString("tel1"));
                                vendor.setLocked(0);
                                vendor.setSeqNo(index);
                                vendor.setRemark(rs.getString("mem"));

                                return vendor;
                            }
                        });
        return units;
    }

    @Override
    public List<Unit> findAllAgent(String ownerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Unit> findAllFactory(String ownerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Unit> findAllShop(final String ownerId) {
        final Date dt = new Date();
        List<Unit> units = this.playloungeJdbcTemplate
                .query("SELECT item,conid,kind,Nature,Area,chlx,"
                                + "name,allname,lxr,tel1,tel2,address,fax,mem,"
                                + "kjm,whichprice,Negative,disp,pdrq,"
                                + "salesprice,httpname,onlinebz,Province,"
                                + "City,mj,rs,state,bitem,htksrq,"
                                + "htzzrq,FilterStr,BrandCode,dj,"
                                + "djautokind,djautocsbm,djautokind2,"
                                + "ChangeDate,autoaccept FROM jbckxx where Nature=2",
                        new RowMapper<Unit>() {
                            @Override
                            public Unit mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Unit shop = new Unit();
                                shop.setId(rs.getString("item"));
                                shop.setSeqNo(index);
                                shop.setCreatorId("admin");
                                shop.setCreateTime(dt);
                                shop.setOwnerId(ownerId);
                                shop.setGroupId(rs.getString("Area"));

                                shop.setCode(rs.getString("item"));
                                shop.setName(rs.getString("name"));
                                shop.setType(Constant.UnitType.Shop);
                                shop.setLinkman(rs.getString("lxr"));
                                shop.setAddress(rs.getString("address"));
                                //shop.setPostCode(rs.getString("zip"));
                                //	shop.setEmail(rs.getString("email"));
                                shop.setTel(rs.getString("tel1"));
                                shop.setLocked(0);
                                shop.setSeqNo(index);
                                shop.setRemark(rs.getString("mem"));

                                return shop;
                            }
                        });
        return units;
    }

    @Override
    public List<Unit> findAllWarehouse(final String ownerId) {
        final Date dt = new Date();
        List<Unit> units = this.playloungeJdbcTemplate
                .query("SELECT item,conid,kind,Nature,Area,chlx,"
                                + "name,allname,lxr,tel1,tel2,address,fax,mem,"
                                + "kjm,whichprice,Negative,disp,pdrq,"
                                + "salesprice,httpname,onlinebz,Province,"
                                + "City,mj,rs,state,bitem,htksrq,"
                                + "htzzrq,FilterStr,BrandCode,dj,"
                                + "djautokind,djautocsbm,djautokind2,"
                                + "ChangeDate,autoaccept FROM jbckxx where Nature!=2",
                        new RowMapper<Unit>() {
                            @Override
                            public Unit mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                Unit warehouse = new Unit();
                                warehouse.setId(rs.getString("item"));
                                warehouse.setSeqNo(index);
                                warehouse.setCreatorId("admin");
                                warehouse.setCreateTime(dt);
                                warehouse.setOwnerId(ownerId);
                                warehouse.setCode(rs.getString("item"));
                                warehouse.setName(rs.getString("name"));
                                warehouse.setType(Constant.UnitType.Warehouse);
                                warehouse.setLinkman(rs.getString("lxr"));
                                warehouse.setAddress(rs.getString("address"));
                                //warehouse.setPostCode(rs.getString("zip"));
                                //warehouse.setEmail(rs.getString("email"));
                                warehouse.setTel(rs.getString("tel1"));
                                warehouse.setLocked(0);
                                warehouse.setGroupId(rs.getString("Area"));
                                warehouse.setSeqNo(index);
                                warehouse.setRemark(rs.getString("mem"));

                                return warehouse;
                            }
                        });
        return units;
    }

    @Override
    public List<Customer> findAllCustomer(final String ownerId) {
        final Date dateTime = new Date();
        List<JSONObject> ranks = this.playloungeJdbcTemplate.query(

                "select  * from vip_jbdjk ", new RowMapper<JSONObject>() {
                    @Override
                    public JSONObject mapRow(ResultSet rs, int index)
                            throws SQLException {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", rs.getString("item"));
                        jsonObject.put("name", rs.getString("name"));
                        return jsonObject;
                    }
                });
        System.out.println("获取属性成功！");
        final Map<String, String> map = new HashMap<>();
        if (CommonUtil.isNotBlank(ranks)) {
            for (JSONObject jsonObject : ranks) {
                map.put(jsonObject.getString("id"), jsonObject.getString("name"));
            }
        }
        final Map<String, String> mapCodes = new HashMap<>();
        List<Customer> customers = this.playloungeJdbcTemplate.query(

                "select  * from vip_zdkhk order by vip DESC ", new RowMapper<Customer>() {
                    @Override
                    public Customer mapRow(ResultSet rs, int index)
                            throws SQLException {
                        Customer customer = new Customer();
                        customer.setEndDate(dateTime);
                        customer.setId(rs.getString("vip"));
                        customer.setCode(rs.getString("vipcardno"));
                        if (mapCodes.containsKey(customer.getCode())) {
                            System.out.println(customer.getCode());
                        } else {
                            mapCodes.put(customer.getCode(), customer.getCode());
                        }
                        customer.setIdCard(rs.getString("vipcardno"));
                        customer.setName(rs.getString("name"));
                        if (CommonUtil.isBlank(customer.getName())) {
                            customer.setName("无");
                        }
                        customer.setSex(Integer.parseInt(rs.getString("sex")));
                        customer.setPassword(rs.getString("password"));
                        if (CommonUtil.isBlank(customer.getPassword())) {
                            customer.setPassword("123456");
                        }
                    /*    customer.setTypeId(Constant.RoleType.Shop);
                        customer.setIsAdmin(Constant.UserType.Customer);*/
                        StringBuilder birthDay = new StringBuilder();
                        birthDay.append(rs.getLong("yyyy"))
                                .append("-").append(rs.getLong("mm"))
                                .append("-").append(rs.getLong("dd"));
                        try {
                            customer.setBirth(CommonUtil.converStrToDate(birthDay.toString(), "yyyy-MM-dd"));
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // customer.setType(Constant.UserType.Customer);
                        customer.setPhone(rs.getString("sjhm"));
                        customer.setEmail(rs.getString("email"));
                        customer.setCreateTime(rs.getDate("sqrq"));
                        if (CommonUtil.isBlank(customer.getCreateTime())) {
                            customer.setCreateTime(dateTime);
                        }
                        customer.setCreatorId("admin");
                        customer.setHomeZipcode(rs.getString("youbian"));
                        customer.setBuyAmount(rs.getDouble("ljxf"));
                        customer.setJob(rs.getString("job"));
                        if (CommonUtil.isNotBlank(rs.getString("djbm"))) {
                            customer.setRank(map.get(rs.getString("djbm")));//等级
                        }
                        customer.setLocked(0);
                        customer.setLastBuyDate(rs.getDate("lastbuydate"));//最后消费日期
                        if (CommonUtil.isNotBlank(rs.getString("outcode"))) {
                            customer.setOwnerId(rs.getString("outcode"));
                        } else {
                            customer.setOwnerId(ownerId);
                        }
                        customer.setGrade(rs.getDouble("mark"));//积分
                        return customer;
                    }
                });
        System.out.println(customers.size());
        return customers;
    }

    @Override
    public List<User> findAllUser(final String ownerId) {
        final Date dateTime = new Date();

        List<User> users = this.playloungeJdbcTemplate.query(

                "select j.CKItem,s.item ,s.passwd,s.name,s.bmitem,s.email from dictuser s,Sys_User_jbckxx j "
                        + " where j.Item=s.item and s.bmitem is not null", new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int index)
                            throws SQLException {
                        User user = new User();
                        user.setId(rs.getString("item") + "_" + rs.getString("CKItem"));
                        user.setCode(rs.getString("item") + "_" + rs.getString("CKItem"));
                        user.setName(rs.getString("name"));
                        if (CommonUtil.isBlank(user.getName())) {
                            user.setName("无");
                        }
                        user.setPassword(rs.getString("passwd"));
                        if (CommonUtil.isBlank(user.getPassword())) {
                            user.setPassword("123456");
                        }
                        String tp = rs.getString("bmitem");
                  /*      if (CommonUtil.isBlank(tp) || tp.equals("07")) {
                            user.setTypeId("R2");
                        } else {
                            user.setTypeId("R0");
                        }*/
                        user.setIsAdmin(Constant.UserType.User);
                        user.setType(Constant.UserType.Shop_Saler);
                        user.setCreateDate(dateTime);
                        user.setCreatorId("admin");
                        user.setRoleId("POS_RES");
                        user.setLocked(0);
                        user.setEmail(rs.getString("email"));
                        if (CommonUtil.isNotBlank(rs.getString("CKItem"))) {
                            user.setOwnerId(rs.getString("CKItem"));
                        } else {
                            user.setOwnerId(ownerId);
                        }
                        return user;
                    }
                });
        return new ArrayList<>();
/*
        return users;
*/
    }

    @Override
    public void batchFittingRecord(final List<FittingRecord> fittings) {
        final List<Bill> bills = DaoUtil.convertFittingToBill(fittings);
        final List<BillDtl> dtls = DaoUtil.getBillDtls(bills);
        if (CommonUtil.isNotBlank(bills) && CommonUtil.isNotBlank(dtls)) {
          /*  String mainSql = " if not exists( select item from dj_dressda where item=?) "
                    + "insert dj_dressda(Item,BillDate,WHCode,Num,Mny"//5
                    + ",Creater,CreaterName,Created,Modifier,ModifyName,Modified"//11
                    + ",SubMit,flag,Mem,recebz) "//15
                    + "values(?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?)";*/
            String mainSql = "insert dj_dressda(Item,BillDate,WHCode,Num,Mny"//5
                    + ",Creater,CreaterName,Created,Modifier,ModifyName,Modified"//11
                    + ",SubMit,flag,Mem,recebz) "//15
                    + "values(?,?,?,?,?"
                    + ",?,?,?,?,?,?"
                    + ",?,?,?,?)";
            int[] mi = this.playloungeJdbcTemplate.batchUpdate(mainSql,
                    new BatchPreparedStatementSetter() {
                        public int getBatchSize() {
                            return bills.size();
                        }

                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            Bill bill = bills.get(i);
                            ps.setString(1, bill.getBillNo());
                            ps.setTimestamp(2, Timestamp.valueOf(CommonUtil
                                    .getDateString(bill.getBillDate(),
                                            "yyyy-MM-dd 00:00:00")));
                            ps.setString(3, bill.getOrigId());
                            ps.setLong(4, bill.getActQty());
                            ps.setDouble(5, bill.getTotPrice());
                            ps.setString(6, "CaseSoft");
                            ps.setString(7, "CaseSoft");
                            ps.setTimestamp(8, Timestamp.valueOf(CommonUtil
                                    .getDateString(bill.getBillDate(),
                                            "yyyy-MM-dd HH:mm:ss")));
                            ps.setString(9, "CaseSoft");
                            ps.setString(10, "CaseSoft");
                            ps.setTimestamp(11, Timestamp.valueOf(CommonUtil
                                    .getDateString(bill.getBillDate(),
                                            "yyyy-MM-dd HH:mm:ss")));
                            ps.setString(12, "0");
                            ps.setString(13, "0");
                            ps.setString(14, null);
                            ps.setString(15, null);
                        }
                    });
            String mainDtl = "insert dj_dressdb (item,clthno,color,size,price,"
                    + "num) "
                    + " values(?,?,?,?,?,?)";//6
            int[] mdi = this.playloungeJdbcTemplate.batchUpdate(mainDtl,
                    new BatchPreparedStatementSetter() {
                        public int getBatchSize() {
                            return fittings.size();
                        }

                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            BillDtl dtl = dtls.get(i);
                            ps.setString(1, dtl.getBillNo());
                            ps.setString(2, dtl.getStyleId());
                            ps.setString(3, dtl.getColorId());
                            ps.setString(4, dtl.getSizeId());
                            ps.setDouble(5, dtl.getPrice());
                            ps.setLong(6, dtl.getActQty());
                        }
                    });
        }
    }

    @Override
    public List<ThirdStock> synchronizeThirdStock() {
        List<ThirdStock> thirdStocks = this.playloungeJdbcTemplate
                .query("select Whcode,clthno,color,size,indate,num from store",
                        new Object[]{},
                        new RowMapper<ThirdStock>() {
                            @Override
                            public ThirdStock mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                ThirdStock thirdStock = new ThirdStock();
                                thirdStock.setStockCode(rs.getString("whcode"));
                                thirdStock.setStyleId(rs.getString("clthno"));
                                thirdStock.setColorId(rs.getString("color"));
                                thirdStock.setSizeId(rs.getString("size"));
                                thirdStock.setInDate(rs.getDate("indate"));
                                String sku = rs.getString("clthno") + rs.getString("color") + rs.getString("size");
                                thirdStock.setSku(sku);
                                thirdStock.setBarcode(sku);
                                thirdStock.setQty(rs.getLong("num"));
                                return thirdStock;
                            }
                        });

        return thirdStocks;
    }

    @Override
    public List<DayThirdStock> synchronizeDayThirdStock(String ownerId, final String date) {
        List<DayThirdStock> dayThirdStocks = this.playloungeJdbcTemplate
                .query("begin declare @guid varchar(40) ;"
                                + "select d.whcode,d.clthno,d.color,d.size,d.num ,s.indate" +
                                " from  fn_Get_AllStoreInfo(@guid,'ADM',?) d  left  JOIN " +
                                " store s on " +
                                " s.whcode=d.whcode and s.clthno=d.clthno and s.color=d.color and s.size=d.SIZE ;" +
                                " end",
                        new Object[]{date},
                        new RowMapper<DayThirdStock>() {
                            @Override
                            public DayThirdStock mapRow(ResultSet rs, int index)
                                    throws SQLException {
                                DayThirdStock thirdStock = new DayThirdStock();
                                thirdStock.setStockCode(rs.getString("whcode"));
                                thirdStock.setStyleId(rs.getString("clthno"));
                                thirdStock.setColorId(rs.getString("color"));
                                thirdStock.setSizeId(rs.getString("size"));
                                if (CommonUtil.isNotBlank(rs.getDate("indate"))) {
                                    thirdStock.setInDate(rs.getDate("indate"));
                                } else {
                                    try {
                                        thirdStock.setInDate(CommonUtil.converStrToDate(date, "yyyy-MM-dd"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                thirdStock.setDay(date);

                                String sku = rs.getString("clthno") + rs.getString("color") + rs.getString("size");
                                thirdStock.setSku(sku);
                                thirdStock.setBarcode(sku);
                                thirdStock.setQty(rs.getLong("num"));
                                return thirdStock;
                            }
                        });

        return dayThirdStocks;
    }
}
