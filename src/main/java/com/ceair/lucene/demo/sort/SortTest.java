package com.ceair.lucene.demo.sort;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class SortTest {

	private void sortQuery(Sort sort) throws IOException, ParseException {
		// 定义索引位置
        Directory directory = FSDirectory.open(new File("index"));
        
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

        Analyzer analyzer = new IKAnalyzer();

        // 构建查询对象
        Query query = new QueryParser("title", analyzer).parse("苹果");
        TopDocs topDocs = indexSearcher.search(query, 10, sort);
        System.out.println("搜索结果总结：" + topDocs.totalHits);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            System.out.println("得分：" + scoreDoc.score);
            // 通过文档id查询文档数据
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("商品ID：" + doc.get("id"));
            System.out.println("商品标题：" + doc.get("title"));
            System.out.println("商品卖点：" + doc.get("sellPoint"));
            System.out.println("商品价格：" + doc.get("price"));
            System.out.println("商品图片：" + doc.get("image"));
        }
	}
	
	//默认使用关联性评分
	@Test
	public void test1() throws IOException, ParseException {
		Sort sort = new Sort();//默认使用关联性评分 Sort.RELEVANCE;
		this.sortQuery(sort);
	}
	
	//指定按某个字段进行排序，就不需要打分
	@Test
	public void test2() throws IOException, ParseException {
		Sort sort = new Sort(new SortField("price", org.apache.lucene.search.SortField.Type.LONG, true));//true为降序排列
		this.sortQuery(sort);
	}

	//多字段排序
	@Test
	public void test3() throws IOException, ParseException {
		Sort sort = new Sort(new SortField("price", org.apache.lucene.search.SortField.Type.LONG, true),
				new SortField("id", org.apache.lucene.search.SortField.Type.LONG, false));//true为降序排列
		this.sortQuery(sort);
	}
}