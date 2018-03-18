package com.casesoft.dmc.service.push;

import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.Style;

import java.util.List;

/**
 * Created by Alvin on 2018/1/4.
 */
public interface pushBaseInfo {
    Boolean WxShopStyle(Style style, List<Product> saveList);
}
