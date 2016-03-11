package com.ceair.lucene.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ceair.lucene.pojo.Item;
import com.ceair.lucene.pojo.Procedure;
import com.ceair.lucene.util.IdUtil;
import com.ceair.lucene.util.JdbcUtil;

public class ProcedureService {
	
	/* 保存程序
	 * 1.如果已经存在，就不添加
	 * 2.程序保存后，将程序索引
	 * 3.迭代术语库，结合程序ID，生成该程序的术语库
	 * 4.往t_procedure_item表中建立手册和术语的关系
	 */
	public List<Item> addProcedure(Procedure procedure) throws SQLException, IOException, ParseException {
		if (procedure  == null || procedure.getName() == null 
				|| procedure.getPath() == null || procedure.getClass() == null) {
			throw new IllegalArgumentException();
		}
		
		//1.如果已经存在，就不添加
		Procedure oldProcedure = this.getItemByName(procedure.getName());
		if (oldProcedure != null) {
			throw new IllegalArgumentException();
		}
		String sql = "INSERT INTO t_procedure (id,name,path) VALUES(?,?,?)";
		if (StringUtils.isBlank(procedure.getId())) {
			procedure.setId(IdUtil.generatorId());
		}
		long start0 = System.currentTimeMillis();
		JdbcUtil.update(sql, procedure.getId(), procedure.getName(), procedure.getPath());
		long end0 = System.currentTimeMillis();
		System.out.println("插入数据到程序表花费：" + (end0 - start0) + "ms");
		
		//2.程序保存后，将程序索引
		long start1 = System.currentTimeMillis();
		this.createIndex(procedure);
		long end1 = System.currentTimeMillis();
		System.out.println("建立索引花费：" + (end1 - start1) + "ms");
		
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
				boolean has = this.indexHasItem(item.getName(), procedure.getId());
				if (has) {
					list.add(item);
				}
			}
			long end3 = System.currentTimeMillis();
			System.out.println("检索索引花费：" + (end3 - start3) + "ms");
		}
		
		//4.往t_procedure_item表中建立手册和术语的关系
		long start4 = System.currentTimeMillis();
		this.addProcedureItem(procedure, list);
		long end4 = System.currentTimeMillis();
		System.out.println("插入中间表花费：" + (end4 - start4) + "ms");
		return list;
	}
	
	public Procedure getItemByName(String name) throws SQLException {
		long start = System.currentTimeMillis();
		String sql = "SELECT id,name,path FROM t_procedure WHERE name = ?";
		Procedure pojo = JdbcUtil.getPojo(sql, Procedure.class, name);
		long end = System.currentTimeMillis();
		System.out.println("查询是否已经存在花费：" + (end - start) + "ms");
		return pojo;
	}
	
	private void createIndex(Procedure procedure) throws IOException {
		Document document = new Document();// 商品数据
        document.add(new StringField("id", procedure.getId(), Field.Store.YES));
        document.add(new StringField("name", procedure.getName(), Field.Store.YES));
        document.add(new TextField("content", procedure.getContent(), Field.Store.YES));
        document.add(new StringField("path", procedure.getPath(), Field.Store.YES));
        Directory directory = FSDirectory.open(new File("index"));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        indexWriter.addDocument(document);
        indexWriter.close();
	}
	
	private boolean indexHasItem(String item, String procedureId) throws IOException {
		Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        BooleanQuery query = new BooleanQuery();
        TermQuery query1 = new TermQuery(new Term("id", procedureId));
        TermQuery query2 = new TermQuery(new Term("content", item));
        query.add(query1, Occur.MUST);
        query.add(query2, Occur.MUST);
        TopDocs topDocs = indexSearcher.search(query, 1);
        int hit = topDocs.totalHits;
        if (hit == 1) {
        	return true;
        } else {
        	return false;
        }
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
