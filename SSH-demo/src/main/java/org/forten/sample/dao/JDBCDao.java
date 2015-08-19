/*
 * Copyright 2003-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.forten.sample.dao;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author <a href="mailto:du_yi@tsinghua.edu.cn">Duyi</a>
 */
@Repository("jdbcDao")
public class JDBCDao implements InitializingBean {
	// 空参数Map
	public static final Map<String, Object> EMPTY_PARAMS = new HashMap<>();

	// 数据源Bean
	private DataSource dataSource;
	// 命名参数Jdbc模板类的Bean
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	// 把数据源对象的Bean注入到本Bean中，并初始化namedParameterJdbcTemplate
	@Resource
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	/**
	 * 用于查询单独的数据对象，如一个数据对象，一个统计数字等
	 *
	 * @param sql
	 *            执行的SQL语句，语句中的参数使用命名参数的形式，如:name、:price
	 * @param params
	 *            封装参数的Map，key对应SQL中的参数名称，如name、price，value是参数的值
	 * @param mapper
	 *            RowMapper的映射对象，把java.sql.ResultSet对象映射成为自定义的数据类型对象
	 * @return 查询的结果
	 */
	public <T> T findObject(String sql, Map<String, Object> params, RowMapper<T> mapper) {
		try {
			return namedParameterJdbcTemplate.queryForObject(sql, params, mapper);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于查询数据对象的集合
	 *
	 * @param sql
	 *            执行的SQL语句，语句中的参数使用命名参数的形式，如:name、:price
	 * @param params
	 *            封装参数的Map，key对应SQL中的参数名称，如name、price，value是参数的值
	 * @param mapper
	 *            RowMapper的映射对象，把java.sql.ResultSet对象映射成为自定义的数据类型对象
	 * @return 查询结果
	 */
	public <T> List<T> findList(String sql, Map<String, Object> params, RowMapper<T> mapper) {
		try {
			return namedParameterJdbcTemplate.query(sql, params, mapper);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行JDBC的写操作，如update、delete、insert into
	 *
	 * @param sql
	 *            写入数据的SQL语句，语句中的参数使用命名参数的形式，如:name、:price
	 * @param params
	 *            封装参数的Map，key对应SQL中的参数名称，如name、price，value是参数的值
	 * @return 操作的记录数量
	 */
	public <T> int update(String sql, Map<String, Object> params) {
		try {
			return namedParameterJdbcTemplate.update(sql, params);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 覆写InitializingBean接口中的抽象方法，在此Bean的属性被设置完成后会执行，用于校验所依赖的Bean是否注入成功
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if (dataSource == null) {
			throw new BeanCreationException("dataSource Bean没有被注入到jdbcDao Bean中");
		}
		if (namedParameterJdbcTemplate == null) {
			throw new BeanCreationException("jdbcDao Bean中的NamedParameterJdbcTemplate为null");
		}
	}
}
