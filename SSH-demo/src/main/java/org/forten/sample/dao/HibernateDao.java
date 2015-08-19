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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:du_yi@tsinghua.edu.cn">Duyi</a>
 */
@Repository("hibernateDao")
public class HibernateDao {
    @Resource
    private SessionFactory sessionFactory;

    public <T> void save(T entity) {
        getSession().save(entity);
    }

    public <T> void update(T entity) {
        getSession().update(entity);
    }

    public <T> void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    public <T> void delete(Class<T> entityClass, Serializable id) {
        Field[] fields = entityClass.getDeclaredFields();
        String idName = null;
        Field idField = null;
        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                idField = field;
                break;
            }
        }
        Column cAnn = idField.getAnnotation(Column.class);
        if (cAnn == null) {
            idName = idField.getName();
        } else {
            if (cAnn.name() == null || cAnn.name().equals("")) {
                idName = idField.getName();
            } else {
                idName = cAnn.name();
            }
        }
        String hql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE "
                + idName + "=:id";
        Query query = getSession().createQuery(hql);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public <T> T findByGet(Class<?> entityClass, Serializable id) {
        T entity = (T) getSession().get(entityClass, id);
        return entity;
    }

    public <T> List<T> findAll(Class<T> clazz) {
        String hql = "FROM " + clazz.getSimpleName();
        Query query = getSession().createQuery(hql);
        query.setCacheable(true);
        List<T> list = query.list();
        return list;
    }

    public <T> T findObject(String hql, Map<String, Object> params) {
        Query query = getSession().createQuery(hql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        T obj = (T) query.uniqueResult();
        return obj;
    }

    public <T> List<T> findBy(String hql, Map<String, Object> params) {
        Query query = getSession().createQuery(hql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> list = query.list();
        return list;
    }

    public <T> List<T> findBySql(String sql, Map<String, Object> params) {
        Query query = getSession().createSQLQuery(sql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> list = query.list();
        return list;
    }

    public <T> int updateBy(String hql, Map<String, Object> params) {
        Query query = getSession().createQuery(hql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        return query.executeUpdate();
    }

    public <T> int updateBySQL(String sql, Map<String, Object> params) {
        Query query = getSession().createSQLQuery(sql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        return query.executeUpdate();
    }

    public <T> List<T> findBy(String hql, Map<String, Object> params,
                              Integer firstNo, Integer pageSize) {
        Query query = getSession().createQuery(hql);
        for (String key : params.keySet()) {
            Object val = params.get(key);
            if (val.getClass().isArray()) {
                query.setParameterList(key, (Object[]) val);
            } else if (val instanceof Collection<?>) {
                query.setParameterList(key, (Collection<?>) val);
            } else {
                query.setParameter(key, params.get(key));
            }
        }
        query.setFirstResult((int) firstNo);
        query.setMaxResults(pageSize);
        List<T> list = query.list();
        return list;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
