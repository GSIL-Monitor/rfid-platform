package com.casesoft.dmc.extend.tiantan.dao.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class TiantanBasicDao {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	//@Autowired
	protected JdbcTemplate tianTanJdbcTemplate;
/*	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
*/	//@Autowired
  // protected PlatformTransactionManager tianTanTransactionManager;// 根据当前数据源连接创建的实例

 
	public JdbcTemplate getTianTanJdbcTemplate() {
		return tianTanJdbcTemplate;
	}

	public void setTianTanJdbcTemplate(JdbcTemplate tianTanJdbcTemplate) {
		this.tianTanJdbcTemplate = tianTanJdbcTemplate;
		/*this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				this.tianTanJdbcTemplate.getDataSource());*/
	}

	/*public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
*/
/*	public PlatformTransactionManager getTianTanTransactionManager() {
		return tianTanTransactionManager;
	}

	public void setTianTanTransactionManager(
			PlatformTransactionManager tianTanTransactionManager) {
		this.tianTanTransactionManager = tianTanTransactionManager;
	}*/

}
