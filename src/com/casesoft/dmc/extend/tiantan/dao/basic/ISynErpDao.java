package com.casesoft.dmc.extend.tiantan.dao.basic;

import com.casesoft.dmc.model.cfg.PropertyKey;
import com.casesoft.dmc.model.product.*;
import com.casesoft.dmc.model.sys.Unit;

import java.util.List;

public interface ISynErpDao {
	public abstract List<Style> findAllStyle();

	public abstract List<Color> findAllColor();
	
	public abstract List<ColorGroup> findAllColorGroup();

	public abstract List<Size> findAllSize();

	public abstract List<Product> findAllProduct();

	public abstract List<SizeSort> findAllSizeSort();
	
	public abstract List<PropertyKey> findAllPropertyKey(String ownerId);
	
	public abstract List<Unit> findAllVendor(String ownerId);
	
	public abstract List<Unit> findAllAgent(String ownerId);
	
	public abstract List<Unit> findAllFactory(String ownerId);
	
	public abstract List<Unit> findAllShop(String ownerId);
	
	public abstract List<Unit> findAllWharehouse(String ownerId);


}
