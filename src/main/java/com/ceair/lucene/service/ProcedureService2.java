package com.ceair.lucene.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.ceair.lucene.pojo.Item;
import com.ceair.lucene.pojo.Procedure;
import com.ceair.lucene.util.IdUtil;
import com.ceair.lucene.util.JdbcUtil;

public class ProcedureService2 {
	
	/* 保存程序
	 * 1.如果已经存在，就不添加
	 * 2.程序保存
	 * 3.迭代术语库，结合程序内容，生成该程序的术语库
	 * 4.往t_procedure_item表中建立手册和术语的关系
	 */
	public List<Item> addProcedure(Procedure procedure) throws SQLException {
		if (procedure  == null || procedure.getName() == null 
				|| procedure.getPath() == null || procedure.getClass() == null) {
			throw new IllegalArgumentException();
		}
		
		//1.如果已经存在，就不添加
		Procedure oldProcedure = this.getProcedureByName(procedure.getName());
		if (oldProcedure != null) {
			throw new IllegalArgumentException();
		}
		String sql = "INSERT INTO t_procedure (id,name,content,path,createTime,updateTime) VALUES(?,?,?,?,?,?)";
		if (StringUtils.isBlank(procedure.getId())) {
			procedure.setId(IdUtil.generatorId());
		}
		
		//2.程序保存
		long start1 = System.currentTimeMillis();
		JdbcUtil.update(sql, procedure.getId(), procedure.getName(), procedure.getContent(), 
				procedure.getPath(), procedure.getCreateTime(), procedure.getUpdateTime());
		long end1 = System.currentTimeMillis();
		System.out.println("插入数据到程序表花费：" + (end1 - start1) + "ms");
		
		//3.迭代术语库，结合程序ID，生成该程序的术语列表
		long start2 = System.currentTimeMillis();
		String sql2 = "select id,name,description from t_item";
		List<Item> items = JdbcUtil.getPojoList(sql2, Item.class);
		long end2 = System.currentTimeMillis();
		System.out.println("查询术语库花费：" + (end2 - start2) + "ms");
		List<Item> list = new ArrayList<Item>();
		if (items != null && items.size() > 0) {
			long start3 = System.currentTimeMillis();
			for (Item item : items) {
				String content = procedure.getContent();
				String name = item.getName();
				if (content.contains(name)) {
					list.add(item);
				}
			}
			long end3 = System.currentTimeMillis();
			System.out.println("内容字符串检索索引花费：" + (end3 - start3) + "ms");
		}
		
		//4.往 t_procedure_item表中建立手册和术语的关系
		long start4 = System.currentTimeMillis();
		this.addProcedureItem(procedure, list);
		long end4 = System.currentTimeMillis();
		System.out.println("插入中间表花费：" + (end4 - start4) + "ms");
		return list;
	}
	
	public Procedure getProcedureByName(String name) throws SQLException {
		long start = System.currentTimeMillis();
		String sql = "SELECT id,name,path FROM t_procedure WHERE name = ?";
		Procedure pojo = JdbcUtil.getPojo(sql, Procedure.class, name);
		long end = System.currentTimeMillis();
		System.out.println("查询是否已经存在花费：" + (end - start) + "ms");
		return pojo;
	}
	
	
	private void addProcedureItem(Procedure procedure, List<Item> items) throws SQLException {
		String sql = "insert into t_procedure_item (id, procedure_id, item_id) values(? ,? ,?)";
		if (items != null && items.size() > 0) {
			for (Item item : items) {
				String id = IdUtil.generatorId();
				String procedureId = procedure.getId();
				String itemId = item.getId();
				JdbcUtil.update(sql, id, procedureId, itemId);
			}
		}
	}
}
