package com.ceair.lucene.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ceair.lucene.pojo.Procedure;
import com.ceair.lucene.util.JdbcUtil;


public class ProcedureIndex implements Runnable {
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try {
			//获取所有的程序
			String sql = "select id,name,content,path,createTime,updateTime from t_procedure";
			List<Procedure> procedures = JdbcUtil.getPojoList(sql, Procedure.class);
			
			List<Procedure> addList = new ArrayList<Procedure>();
			List<Procedure> updateList = new ArrayList<Procedure>();
			
			//
			if (procedures != null) {
				for (Procedure bean : procedures) {
					Long updateTime = this.getUpdateTimeByIdFromIndex(bean.getId());
					//没有检索到数据，说明该数据为新增
					if (updateTime == null) {
						addList.add(bean);
						continue;
					}
					//检索到数据，并且有更新过
					if (bean.getUpdateTime().getTime() > updateTime) {
						updateList.add(bean);
					}
				}
			}
			
			//为新程序创建索引
			this.createIndex(addList);
			
			//更新需要更新的索引
			this.updateIndex(updateList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("takes: " + (end - start) + "ms");
	}
	
	
	private Long getUpdateTimeByIdFromIndex(String id) throws IOException {
		Long updateTime = null;
		try {
			Directory directory = FSDirectory.open(new File("index"));
			IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
			TermQuery termQuery = new TermQuery(new Term("id", id));
			TopDocs topDocs = indexSearcher.search(termQuery, 1);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			    // 通过文档id查询文档数据
			    Document doc = indexSearcher.doc(scoreDoc.doc);
			    String ct = doc.get("updateTime");
			    updateTime = Long.valueOf(ct);
			}
		//首次执行时，会抛出这个异常
		} catch (IndexNotFoundException e) {

		}
        return updateTime;
	}
	
	private void createIndex(List<Procedure> procedures) throws Exception{
        Directory directory = FSDirectory.open(new File("index"));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        
        for (Procedure procedure : procedures) {
        	Document doc = this.constructDocument(procedure);
        	indexWriter.addDocument(doc);
		}
        indexWriter.close();
    }
	
	private void updateIndex(List<Procedure> procedures) throws Exception{
		Directory directory = FSDirectory.open(new File("index"));
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
		indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		
		for (Procedure procedure : procedures) {
			Document doc = this.constructDocument(procedure);
			indexWriter.updateDocument(new Term("id", procedure.getId()), doc);
		}
		indexWriter.close();
	}
	
	
	private Document constructDocument(Procedure procedure) {
		Document document = new Document();// 商品数据
        document.add(new StringField("id", procedure.getId(), Field.Store.YES));
        document.add(new StringField("name", procedure.getName(), Field.Store.YES));
        document.add(new TextField("content", procedure.getContent(), Field.Store.YES));
        document.add(new StringField("path", procedure.getPath(), Field.Store.YES));
        document.add(new LongField("createTime", procedure.getCreateTime().getTime(), Field.Store.YES));
        document.add(new LongField("updateTime", procedure.getUpdateTime().getTime(), Field.Store.YES));
        return document;
	}
}
