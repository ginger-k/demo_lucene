package com.ceair.lucene.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.ceair.lucene.pojo.Item;
import com.ceair.lucene.util.IdUtil;
import com.ceair.lucene.util.JdbcUtil;

public class ItemService {
	
	/*
	 * 1.如果已经存在，就不添加
	 * 2.更新ext.dic，将新增的术语添加到 ext.dic 中
	 */
	public void addItem(Item item) throws SQLException, IOException {
		if (item  == null || item.getName() == null || item.getDescription() == null) {
			throw new IllegalArgumentException();
		}
		/*Item oldItem = this.getItemByName(item.getName());
		if (oldItem != null) {
			throw new IllegalArgumentException();
		}*/
		String sql = "INSERT INTO t_item (id,name,description) VALUES(?,?,?)";
		String id = item.getId();
		if (StringUtils.isBlank(id)) {
			id = IdUtil.generatorId();
		}
		JdbcUtil.update(sql, id, item.getName(), item.getDescription());
		//更新ext.dic
		this.updateExtDic(item.getName());
	}
	
	public Item getItemByName(String name) throws SQLException {
		String sql = "SELECT id,name,description FROM t_item WHERE name = ?";
		return JdbcUtil.getPojo(sql, Item.class, name);
	}
	
	private void updateExtDic(String item) throws IOException {
		String fileName = ItemService.class.getClassLoader().getResource("item_procedure.dic").getFile();
		File file = new File(fileName);
		FileWriter writer = new FileWriter(file, true);  
		BufferedWriter bw = new BufferedWriter(writer);
		bw.newLine();
		bw.write(item);
		bw.close();
		writer.close();
	}
}
