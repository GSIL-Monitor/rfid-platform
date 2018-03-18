package com.casesoft.dmc.extend.tiantan.dao;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.Constant;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.extend.tiantan.dao.basic.ISynErpDao;
import com.casesoft.dmc.extend.tiantan.dao.basic.TiantanBasicDao;
import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.sys.Unit;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//@Repository
public class TiantanSynErpDao extends TiantanBasicDao implements ISynErpDao {
	private int index;
	@Override
	public List<Style> findAllStyle() {
		/*
		 * 商品级别 商品附加属性1表(品牌) 商品附加属性2表(商品大类) 商品附加属性3表(季节) 商品附加属性4表(年度)
		 */

		final String dateTime = CommonUtil.getDateString(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		List<Style> styles = this.tianTanJdbcTemplate.query(
				"select spdm,spmc,FJSX1,FJSX2,FJSX3,FJSX4,"
						+ "FJSX5,FJSX6,FJSX7,FJSX8,FJSX9,FJSX10,"
						+ "bzsj,bzjj,bzhu from ShangPin",
				new RowMapper<Style>() {
					@Override
					public Style mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Style style = new Style();
						style.setUpdateTime(dateTime);
						style.setBrandCode(rs.getString("FJSX1"));
						style.setId(rs.getString("spdm"));
						style.setStyleId(rs.getString("spdm"));
						style.setStyleName(rs.getString("spmc"));

						style.setClass1(rs.getString("FJSX1"));
						style.setClass2(rs.getString("FJSX2"));
						style.setClass3(rs.getString("FJSX3"));
						style.setClass4(rs.getString("FJSX4"));
						style.setClass5(rs.getString("FJSX5"));
						style.setClass6(rs.getString("FJSX6"));
						style.setClass7(rs.getString("FJSX7"));
						style.setClass8(rs.getString("FJSX8"));
						style.setClass9(rs.getString("FJSX9"));
						style.setClass10(rs.getString("FJSX10"));

						style.setPrice(rs.getDouble("bzsj"));
						style.setPreCast(rs.getDouble("bzjj"));
						style.setRemark(rs.getString("bzhu"));
						style.setSeqNo(arg1);
						return style;
					}
				});

		return styles;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Color> findAllColor() {
		final String dateTime = CommonUtil.getDateString(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		List<Color> colors = this.tianTanJdbcTemplate.query(
				"select ggdm,ggmc from GuiGe1", new RowMapper<Color>() {
					@Override
					public Color mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Color color = new Color();
						color.setColorId(rs.getString("ggdm"));
						color.setId(rs.getString("ggdm"));
						color.setColorName(rs.getString("ggmc"));
						color.setUpdateTime(dateTime);
						return color;
					}
				});

		return colors;
	}

	@Override
	public List<Size> findAllSize() {
		final String dateTime = CommonUtil.getDateString(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		List<Size> sizes = this.tianTanJdbcTemplate.query(
				"select ggdm,ggmc from GuiGe2", new RowMapper<Size>() {
					@Override
					public Size mapRow(ResultSet rs, int index)
							throws SQLException {
						Size size = new Size();
						size.setId(rs.getString("ggdm"));
						size.setSizeName(rs.getString("ggmc"));
						size.setSizeId(rs.getString("ggdm"));
						size.setSeqNo(index);
						size.setUpdateTime(dateTime);
						return size;
					}
				});
		List<Map<String, Object>> rows = this.tianTanJdbcTemplate
				.queryForList("SELECT gpdm,ggdm FROM gpgg2");
		if (CommonUtil.isNotBlank(rows)) {
			if (CommonUtil.isNotBlank(sizes)) {
				for (Size s : sizes) {
					if (CommonUtil.isBlank(s.getSizeId())) {
						System.out.println(s.getSizeName() + "===="
								+ sizes.indexOf(s));
					}
					for (int i = 0; i < rows.size(); i++) {
						if (rows.get(i).get("ggdm").toString()
								.equalsIgnoreCase(s.getSizeId())) {
							s.setSortId(rows.get(i).get("gpdm").toString());
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
		final String dateTime = CommonUtil.getDateString(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		 index = CacheManager.getMaxProductId();
		List<Product> products = this.tianTanJdbcTemplate
				.query("select s.spdm,s.spmc,s.FJSX1,s.FJSX2,s.FJSX3,s.FJSX4,"
						+ "s.FJSX5,s.FJSX6,s.FJSX7,s.FJSX8,s.FJSX9,s.FJSX10,"
						+ "s.bzsj,s.bzjj,s.bzhu,"
						+ "g1.GGDM as colorId,g2.GGDM as sizeId ,gu1.GGMC as colorName,gu2.GGMC as sizeName "
						+ "from ShangPin s,dbo.SPGG1 g1,SPGG2 g2 ,GuiGe1 gu1,GuiGe2 gu2"
						+ " where s.SPDM=g1.SPDM and g2.SPDM=s.SPDM and gu1.GGDM=g1.GGDM and gu2.GGDM=g2.GGDM",
						new RowMapper<Product>() {
							@Override
							public Product mapRow(ResultSet rs, int arg1)
									throws SQLException {
								Product product = new Product();
								product.setUpdateTime(dateTime);
								product.setBrandCode(rs.getString("FJSX1"));
								product.setStyleId(rs.getString("spdm"));
								product.setStyleName(rs.getString("spmc"));
								product.setSizeId(rs.getString("sizeId"));
								product.setSizeName(rs.getString("sizeName"));
								product.setColorId(rs.getString("colorId"));
								product.setColorName(rs.getString("colorName"));
								product.setCode(product.getStyleId()
										+ product.getColorId()
										+ product.getSizeId());
								Product pt=CacheManager.getProductByCode(product.getCode());
								if(CommonUtil.isNotBlank(pt)){
									product.setId(pt.getId());
								}else{
									String format = "000000";
									String suf = String.valueOf(index);
									String id = format.substring(0, format.length() - suf.length())
												+ suf;
									product.setId(id);
									index++;
								}
								product.setBarcode(product.getCode());
								product.setPrice(rs.getDouble("bzsj"));
								product.setPreCast(rs.getDouble("bzjj"));
								product.setClass1(rs.getString("FJSX1"));
								product.setClass2(rs.getString("FJSX2"));
								product.setClass3(rs.getString("FJSX3"));
								product.setClass4(rs.getString("FJSX4"));
								product.setClass5(rs.getString("FJSX5"));
								product.setClass6(rs.getString("FJSX6"));
								product.setClass7(rs.getString("FJSX7"));
								product.setClass8(rs.getString("FJSX8"));
								product.setClass9(rs.getString("FJSX9"));
								product.setClass10(rs.getString("FJSX10"));
								product.setRemark(rs.getString("bzhu"));
								return product;
							}
						});
		return products;
	}

	@Override
	public List<SizeSort> findAllSizeSort() {
		final String dateTime = CommonUtil.getDateString(new Date(),
				"yyyy-MM-dd HH:mm:ss");
		List<SizeSort> sizeSorts = this.tianTanJdbcTemplate.query(
				"select gpdm,gpmc from group2", new RowMapper<SizeSort>() {
					@Override
					public SizeSort mapRow(ResultSet rs, int index)
							throws SQLException {
						SizeSort sizeSort = new SizeSort();
						sizeSort.setId(rs.getString("gpdm"));
						sizeSort.setSeqNo(index);
						sizeSort.setSortName(rs.getString("gpmc"));
						sizeSort.setUpdateTime(dateTime);
						sizeSort.setSortNo(rs.getString("gpdm"));
						return sizeSort;
					}
				});
		return sizeSorts;
	}

	@Override
	public List<ColorGroup> findAllColorGroup() {
		List<ColorGroup> colorGroups = this.tianTanJdbcTemplate.query(
				"select gpdm,gpmc from group1", new RowMapper<ColorGroup>() {
					@Override
					public ColorGroup mapRow(ResultSet rs, int index)
							throws SQLException {
						ColorGroup colorGroup = new ColorGroup();
						colorGroup.setId(rs.getString("gpdm"));
						colorGroup.setGroupNo(rs.getString("gpdm"));
						colorGroup.setGroupName(rs.getString("gpmc"));
						return colorGroup;
					}
				});
		/*
		 * List<ColorGroupRef> colorGroupRefs=
		 * this.tianTanJdbcTemplate.query("SELECT gpdm,ggdm FROM gpgg1", new
		 * RowMapper<ColorGroupRef>(){
		 * 
		 * @Override public ColorGroupRef mapRow(ResultSet rs, int index) throws
		 * SQLException { ColorGroupRef colorGroupRef=new ColorGroupRef();
		 * colorGroupRef.setId(rs.getString("gpdm")+rs.getString("ggdm"));
		 * colorGroupRef.setColorNo(rs.getString("ggdm"));
		 * colorGroupRef.setGroupId(rs.getString("gpdm"));
		 * colorGroupRef.setSeqNo(index); return colorGroupRef; }});
		 */
		return colorGroups;
	}

	@Override
	public List<PropertyKey> findAllPropertyKey(final String ownerId) {
		/*
		 * 品牌
		 */
		final Date dt = new Date();
		List<PropertyKey> propertyKeys = new ArrayList<PropertyKey>();
		List<PropertyKey> bds = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX1", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setRegisterId("admin");
						property.setSeqNo(index);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("BD");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						return property;
					}
				});
		List<PropertyKey> c1s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX1", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C1");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c2s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX2", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C2");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c3s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX3", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C3");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c4s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX4", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C4");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c5s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX5", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C5");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c6s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX6", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C6");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c7s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX7", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C7");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c8s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX8", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C8");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c9s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX9", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C9");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});
		List<PropertyKey> c10s = this.tianTanJdbcTemplate.query(
				"select  SXDM,SXMC from FJSX10", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("SXDM"));
						property.setRegisterDate(dt);
						property.setSeqNo(index);

						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setName(rs.getString("SXMC"));
						property.setType("C10");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						property.setRegisterId("admin");

						return property;
					}
				});

		/**
		 * 供应商分类
		 * */
		List<PropertyKey> vts = this.tianTanJdbcTemplate.query(
				"select  LBDM,LBMC from GHSLB", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("LBDM"));
						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setSeqNo(index);

						property.setName(rs.getString("LBMC"));
						property.setType("VT");
						property.setRegisterDate(dt);
						property.setRegisterId("admin");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
						return property;
					}
				});
		/**
		 * 仓库分类
		 * */
		List<PropertyKey> wts = this.tianTanJdbcTemplate.query(
				"select  LBDM,LBMC from CKLB", new RowMapper<PropertyKey>() {
					@Override
					public PropertyKey mapRow(ResultSet rs, int index)
							throws SQLException {
						PropertyKey property = new PropertyKey();
						property.setCode(rs.getString("LBDM"));
						property.setOwnerId(ownerId);
						property.setLocked(0);
						property.setYnuse("Y");
						property.setSeqNo(index);
						property.setName(rs.getString("LBMC"));
						property.setType("WT");
						property.setRegisterDate(dt);
						property.setRegisterId("admin");
						property.setId(property.getOwnerId()
								+ property.getType() + property.getCode());
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
		return propertyKeys;
	}

	@Override
	public List<Unit> findAllVendor(final String ownerId) {
		final Date dt = new Date();
		List<Unit> units = this.tianTanJdbcTemplate
				.query("select GHSDM,GHSMC,XZDM,dz,lxr,yb,dh1,sj,khh,zh,sh,fr,email,bz from GongHuoShang",
						new RowMapper<Unit>() {
							@Override
							public Unit mapRow(ResultSet rs, int index)
									throws SQLException {
								Unit vendor = new Unit();
								vendor.setId(Constant.UnitCodePrefix.Vender
										+ rs.getString("GHSDM"));
								vendor.setSeqNo(index);
								vendor.setCreatorId("admin");
								vendor.setCreateTime(dt);
								vendor.setOwnerId(ownerId);
								vendor.setCode(Constant.UnitCodePrefix.Vender
										+ rs.getString("GHSDM"));
								vendor.setName(rs.getString("GHSMC"));
								vendor.setType(Constant.UnitType.Vender);
								vendor.setLinkman(rs.getString("lxr"));
								vendor.setAddress(rs.getString("dz"));
								vendor.setPostCode(rs.getString("yb"));
								vendor.setTel(rs.getString("sj"));
								vendor.setLocked(0);
								vendor.setSeqNo(index);
								vendor.setEmail(rs.getString("email"));
								vendor.setBankAccount(rs.getString("zh"));
								vendor.setRemark(rs.getString("bz"));
								return vendor;
							}
						});
		return units;
	}

	@Override
	public List<Unit> findAllAgent(final String ownerId) {
		final Date dt = new Date();
		List<Unit> units = this.tianTanJdbcTemplate
				.query("select KHDM,KHMC,XZDM,dz,lxr,yb,dh1,sj,khh,zh,sh,fr,email,bz from KeHu where XZDM='1'",
						new RowMapper<Unit>() {
							@Override
							public Unit mapRow(ResultSet rs, int index)
									throws SQLException {
								Unit vendor = new Unit();
								vendor.setId(Constant.UnitCodePrefix.Agent
										+ rs.getString("KHDM"));
								vendor.setSeqNo(index);
								vendor.setCreatorId("admin");
								vendor.setCreateTime(dt);
								vendor.setOwnerId(ownerId);
								vendor.setCode(Constant.UnitCodePrefix.Agent
										+ rs.getString("KHDM"));
								vendor.setName(rs.getString("KHMC"));
								vendor.setType(Constant.UnitType.Agent);
								vendor.setLinkman(rs.getString("lxr"));
								vendor.setAddress(rs.getString("dz"));
								vendor.setPostCode(rs.getString("yb"));
								vendor.setTel(rs.getString("sj"));
								vendor.setRemark(rs.getString("dz"));
								vendor.setLocked(0);
								vendor.setSeqNo(index);
								vendor.setEmail(rs.getString("email"));
								vendor.setBankAccount(rs.getString("zh"));
								vendor.setRemark(rs.getString("bz"));

								return vendor;
							}
						});
		return units;
	}

	@Override
	public List<Unit> findAllFactory(final String ownerId) {
		final Date dt = new Date();
		List<Unit> units = this.tianTanJdbcTemplate.query(
				"select GCDM,GCMC,GHSDM,dz,fzr,dh,bz from factory ",
				new RowMapper<Unit>() {
					@Override
					public Unit mapRow(ResultSet rs, int index)
							throws SQLException {
						Unit vendor = new Unit();
						vendor.setId(Constant.UnitCodePrefix.Factory
								+ rs.getString("GCDM"));
						vendor.setSeqNo(index);
						vendor.setCreatorId("admin");
						vendor.setCreateTime(dt);
						if (CommonUtil.isBlank(rs.getString("GHSDM"))) {
							vendor.setOwnerId(ownerId);
						} else {
							vendor.setOwnerId(rs.getString("GHSDM"));

						}

						vendor.setCode(Constant.UnitCodePrefix.Factory
								+ rs.getString("GCDM"));
						vendor.setName(rs.getString("GCMC"));
						vendor.setType(Constant.UnitType.Factory);
						vendor.setLinkman(rs.getString("fzr"));
						vendor.setAddress(rs.getString("dz"));
						vendor.setTel(rs.getString("dh"));
						vendor.setLocked(0);
						vendor.setSeqNo(index);
						vendor.setRemark(rs.getString("bz"));
						return vendor;
					}
				});
		return units;
	}

	@Override
	public List<Unit> findAllShop(final String ownerId) {
		final Date dt = new Date();
		List<Unit> units = this.tianTanJdbcTemplate
				.query("select CKDM,CKMC,XZDM,dz,lxr,yb,dh1,sj,bz from CangKu where XZDM='1'",
						new RowMapper<Unit>() {
							@Override
							public Unit mapRow(ResultSet rs, int index)
									throws SQLException {
								Unit vendor = new Unit();
								vendor.setId(Constant.UnitCodePrefix.Shop
										+ rs.getString("CKDM"));
								vendor.setSeqNo(index);
								vendor.setCreatorId("admin");
								vendor.setCreateTime(dt);
								vendor.setOwnerId(ownerId);
								vendor.setCode(Constant.UnitCodePrefix.Shop
										+ rs.getString("CKDM"));
								vendor.setName(rs.getString("CKMC"));
								vendor.setType(Constant.UnitType.Shop);
								vendor.setLinkman(rs.getString("lxr"));
								vendor.setAddress(rs.getString("dz"));
								vendor.setPostCode(rs.getString("yb"));
								vendor.setTel(rs.getString("sj"));
								vendor.setLocked(0);
								vendor.setSeqNo(index);
								vendor.setRemark(rs.getString("bz"));

								return vendor;
							}
						});
		return units;
	}

	@Override
	public List<Unit> findAllWharehouse(final String ownerId) {
		final Date dt = new Date();
		List<Unit> units = this.tianTanJdbcTemplate
				.query("select CKDM,CKMC,XZDM,dz,lxr,yb,dh1,sj,bz from CangKu where XZDM='0'",
						new RowMapper<Unit>() {
							@Override
							public Unit mapRow(ResultSet rs, int index)
									throws SQLException {
								Unit vendor = new Unit();
								vendor.setId(Constant.UnitCodePrefix.Warehouse
										+ rs.getString("CKDM"));
								vendor.setSeqNo(index);
								vendor.setCreatorId("admin");
								vendor.setCreateTime(dt);
								vendor.setOwnerId(ownerId);
								vendor.setCode(Constant.UnitCodePrefix.Warehouse
										+ rs.getString("CKDM"));
								vendor.setName(rs.getString("CKMC"));
								vendor.setType(Constant.UnitType.Warehouse);
								vendor.setLinkman(rs.getString("lxr"));
								vendor.setAddress(rs.getString("dz"));
								vendor.setPostCode(rs.getString("yb"));
								vendor.setTel(rs.getString("sj"));
								vendor.setLocked(0);
								vendor.setSeqNo(index);
								vendor.setRemark(rs.getString("bz"));

								return vendor;
							}
						});
		return units;
	}

}
