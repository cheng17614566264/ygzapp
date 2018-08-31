package com.cjit.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cjit.common.util.PaginationList;

/**
 * ˵��:ͨ��DAO�ӿ�
 * 
 * @version
 * @since 2008-6-30 ����11:25:45
 */
public interface GenericService {

	/**
	 * ���棨�־û���һ������
	 * 
	 * @param object
	 *            Ҫ����Ķ���
	 * 
	 * @since Jun 24, 2008
	 */
	public int save(String statements, Map map);

	/**
	 * ���棨�־û���һ������
	 * 
	 * @param object
	 *            Ҫ����Ķ���
	 * 
	 * @since Jun 24, 2008
	 */
	public void update(String statements, Map map);

	/**
	 * ɾ��һ������
	 * 
	 * @param object
	 *            Ҫ���µĶ���
	 * 
	 * @since Jun 24, 2008
	 */
	public void delete(String statements, Serializable id);

	/**
	 * @param statements
	 * @param map
	 */
	public void delete(String statements, Map map);

	/**
	 * ����ɾ��
	 * 
	 * @param Collection
	 *            ��Ҫɾ��Ķ��󼯺�
	 * 
	 * @since Jun 24, 2008
	 */
	public void deleteAll(String statements, Serializable[] id);

	/**
	 * ���HQL����ѯ�������
	 * 
	 * @param countHql
	 *            �����ѯ���������HQL���
	 * @return Integer ��ѯ���
	 * 
	 * @since Jun 24, 2008
	 */
	public Long getResultCount(String statements);

	/**
	 * ���Ԥ��SQL��ѯ���
	 * 
	 * @param query
	 *            hql���
	 * @param parameters
	 *            Ҫ���Ĳ����
	 * @return List ��ѯ����
	 */
	public List find(final String query, final Map parameters);

	public Double findForDouble(final String query, final Map parameters);

	public Object findForObject(final String query, final Map parameters);
	
	public List findForList(final String query, final Map parameters);

	/**
	 * ���QBC��ѯ�������
	 * 
	 * @param query
	 *            hql���
	 * @return parameters Ҫ���Ĳ����
	 */
	public Long getRowCount(final String query, final Map parameters);

	/**
	 * ���Ԥ��SQL��ѯ���
	 * 
	 * @param query
	 *            hql���
	 * @param parameters
	 *            Ҫ���Ĳ����
	 * @param pageInfo
	 *            ��ҳʵ��
	 * @return List ��ѯ����
	 */
	public List find(final String hql, final Map parameters,
			final PaginationList paginationList);
	
	public List findLargeData(final String hql, final Map parameters,
			final PaginationList paginationList);

	public void insertBatch(String hql, List data);

	public void updateBatch(String hql, List data);
	
	public void updateRptDataBatch(String hql, List data);
	/**
	 * @param hql
	 * @param data ɾ������
	 */
	public void deleteBatch(String hql, List data);

	/**
	 * ���ڻش�һ��Spring��װ��JDBC�ӿ�
	 * 
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate();

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	

}
