package com.casesoft.dmc.service.product;

import com.casesoft.dmc.cache.CacheManager;
import com.casesoft.dmc.core.dao.PropertyFilter;
import com.casesoft.dmc.core.service.AbstractBaseService;
import com.casesoft.dmc.core.util.CommonUtil;
import com.casesoft.dmc.core.util.page.Page;
import com.casesoft.dmc.dao.product.ProductDao;
import com.casesoft.dmc.model.product.Product;
import com.casesoft.dmc.model.product.ProductInfoList;
import com.casesoft.dmc.model.product.StyleCollocation;
import com.casesoft.dmc.model.product.vo.ColorVo;
import com.casesoft.dmc.model.product.vo.SizeVo;
import com.casesoft.dmc.model.shop.Collocation;
import com.casesoft.dmc.model.tag.EpcBindBarcode;
import com.casesoft.dmc.service.log.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductService extends AbstractBaseService<Product, String> {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SysLogService sysLogService;

    public void updateRemarkById(String id,String remark){
        this.productDao.batchExecute("update Product set remark = '"+remark+"' where id =?",id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findPage(Page<Product> page, List<PropertyFilter> filters) {
        return this.productDao.findPage(page, filters);
    }

    public void save(Map<String, EpcBindBarcode> tmps) {
        if (CommonUtil.isNotBlank(tmps)) {
            this.productDao.doBatchInsert(new ArrayList<>(tmps.values()));
        }
    }


    public List<Product> findByStyleId(String styleId) {
        String hql = "from Product p where p.styleId=? order by p.sizeId desc";
        return this.productDao.find(hql, new Object[]{styleId});
    }

    @Override
    public void save(Product entity) {
        Product dtoProd = CacheManager.getProductByCode(entity.getCode());
        if (!entity.equals(dtoProd)) {
            long version = this.sysLogService.getVersionByTable("Product");
            this.sysLogService.saveUpdateVersion("Product", version + 1);
           /* entity.setVersion(version + 1);*/
            this.productDao.saveOrUpdate(entity);
        }
    }

    public void saveOrUpdate(Product entity) {
        this.productDao.saveOrUpdate(entity);
    }

    public void save(List<Product> entityList) {
        List<Product> dtoList = new ArrayList<Product>();

        long version = this.sysLogService.getVersionByTable("Product");
        for (Product p : entityList) {
            boolean have = false;
            if (CommonUtil.isNotBlank(CacheManager.getProductMap())) {
                for (Product cache : CacheManager.getProductMap().values()) {
                    if (cache.equals(p)) {
                        have = true;
                        break;
                    }
                }
            }
            if (!have) {
              /*  p.setVersion(version + 1);*/
                dtoList.add(p);
            }
        }
        if (dtoList.size() > 0) {
            this.productDao.doBatchInsert(dtoList);
            this.sysLogService.saveUpdateVersion("Product", version + 1);
        }
    }

    public void save(List<Product> entityList, List<EpcBindBarcode> epcBindBarcodes) {
        this.save(entityList);
        this.productDao.doBatchInsert(epcBindBarcodes);
    }

    public void save(ProductInfoList list) {
        this.save(list.getProductList());

        if (CommonUtil.isNotBlank(list.getStyleList())) {
            this.productDao.doBatchInsert(list.getStyleList());
        }
        if (CommonUtil.isNotBlank(list.getColorList())) {
            this.productDao.doBatchInsert(list.getColorList());
        }
        if (CommonUtil.isNotBlank(list.getSizeList())) {
            this.productDao.doBatchInsert(list.getSizeList());
        }
        if (CommonUtil.isNotBlank(list.getSizeSort())) {
            this.productDao.doBatchInsert(list.getSizeSort());
        }
        if (CommonUtil.isNotBlank(list.getPropertyKeyList())) {
            this.productDao.doBatchInsert(list.getPropertyKeyList());
        }
        if (CommonUtil.isNotBlank(list.getPropertyTypeList())) {
            this.productDao.doBatchInsert(list.getPropertyTypeList());
        }


    }

    // John保存款式搭配
    public void saveStyleCollocationList(List<StyleCollocation> StyleCollocations) {
        for (StyleCollocation st : StyleCollocations) {
            this.productDao.saveOrUpdateX(st);
        }
    }

    // john 搭配款式Product
    public List<Product> findWithStyles(String sku) {
        String hql = "from Product p where p.styleId in (select s.style2 from StyleCollocation s where s.style1  in ("
                + "select pr.styleId from Product pr where pr.code=? ))";
        return this.productDao.find(hql, new Object[]{sku});
    }

    // 根据SKU查找Product；
    public Product findProductByCode(String Sku) {
        String hql = "from Product p where p.code=?";
        return this.productDao.findUnique(hql, new Object[]{Sku});
    }


    @Transactional(readOnly = true)
    public List<Product> find(int type) {
        // 0,未搭配1,已搭配
        String sql = "";
        if (type == 0) {
            // sql =
            // "select * from tbl_cfg_sku p where p.code not in (select c.code from tbl_shop_collocation c)";
            sql = "select * from tcsk p where p.cd not in (select c.cd from tsc c)";
        } else {
            sql = "select * from tcsk p where p.cd in (select c.cd from tsc c)";
        }

        return this.productDao.findObjectBySQL(sql);
    }

    public void saveCollocation(Collocation c) {
        this.productDao.saveOrUpdateX(c);
    }

    @Override
    public Product load(String id) {
        return this.productDao.load(id);
    }

    @Override
    public Product get(String propertyName, Object value) {
        return null;
    }

    @Override
    public List<Product> find(List<PropertyFilter> filters) {
        return this.productDao.find(filters);
    }

    @Override
    public List<Product> getAll() {
        return this.productDao.getAll();
    }

    @Override
    public <X> List<X> findAll() {
        return null;
    }

    @Override
    public void update(Product entity) {

    }

    @Override
    public void delete(Product entity) {
        this.productDao.delete(entity);
    }

    public void deleteCollocation(String id) {
        this.productDao.batchExecute("delete Collocation dtl where dtl.id=?", id);

    }

    public <X> List<X> findAll(PropertyFilter filter) {
        return this.productDao.find(filter.getHql(), filter.getValues());
    }

    @Override
    public void delete(String id) {
        this.productDao.delete(id);
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void saveProductInfo(ProductInfoList productInfoList) {
        if (CommonUtil.isNotBlank(productInfoList.getProductList())) {
            this.productDao.doBatchInsert(productInfoList.getProductList());
        }
        if (CommonUtil.isNotBlank(productInfoList.getStyleList())) {
            this.productDao.doBatchInsert(productInfoList.getStyleList());
        }
        if (CommonUtil.isNotBlank(productInfoList.getColorList())) {
            this.productDao.doBatchInsert(productInfoList.getColorList());
        }
        if (CommonUtil.isNotBlank(productInfoList.getSizeList())) {
            this.productDao.doBatchInsert(productInfoList.getSizeList());
        }
        if (CommonUtil.isNotBlank(productInfoList.getPropertyKeyList())) {
            this.productDao.doBatchInsert(productInfoList.getPropertyKeyList());
        }
    }

    public List<ColorVo> getColorsByStyleId(String styleId) {
        String hql ="select distinct new com.casesoft.dmc.model.product.vo.ColorVo(p.colorId as id,p.colorName) from"
                +" Product p where p.styleId=?";
        return this.productDao.find(hql, new Object[] {styleId});
    }

    public List<SizeVo> getSizesByStyleId(String styleId) {
        String hql ="select distinct new com.casesoft.dmc.model.product.vo.SizeVo(p.sizeId as id,p.sizeName) from"
                +" Product p where p.styleId=?";
        return this.productDao.find(hql, new Object[] {styleId});
    }

    public String getMaxProductId(){
        String hql="select max(id) from Product";
        String maxId = this.productDao.findUnique(hql);
        return maxId;
    }
    public Long getMaxVersionId(){
        String hql="select max(version) from Product";
        Long maxVersionId = this.productDao.findUnique(hql);
        return maxVersionId == null ? 0 : maxVersionId;
    }

    /**
     * add by yushen 订单中选择商品时，商品先按颜色尺寸排序。颜色按颜色名首字母排序，尺寸按XS,S,M,L,XL,XXL排序(seqNo)
     */
    public List<Product> listOrderByColorAndSize(String styleId){
        String hql = "select p from Product p,Size s where p.sizeId = s.id and p.styleId=? ORDER BY p.colorName, s.seqNo";
        return this.productDao.find(hql, styleId);
    }
}
