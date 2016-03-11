package com.ceair.lucene.demo.query;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class PageQueryTest {

    private void pageQuery(int pageNumber, int pageSize) throws Exception {
        Directory directory = FSDirectory.open(new File("index"));
        // 定义索引搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

        // 分页信息
        int start = (pageNumber - 1) * pageSize;
        int end = start + pageSize;

        // 构建查询对象
        Query query = new TermQuery(new Term("title", "苹果"));

        // 搜索
        TopDocs topDocs = indexSearcher.search(query, end); // 根据end查询
        
        int totalRecord = topDocs.totalHits;
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;

        System.out.println("总记录数：" + totalRecord);
        System.out.println("总页数：" + totalPage);
        System.out.println("---------------");
        
        ScoreDoc[] docs = topDocs.scoreDocs;
        for (int i = start; i < end; i++) {
            ScoreDoc doc = docs[i];
            System.out.println("得分：" + doc.score);
            Document document = indexSearcher.doc(doc.doc);
            System.out.println("ID: " + document.get("id"));
            System.out.println("Title: " + document.get("title"));
            System.out.println("SellPoint: " + document.get("sellPoint"));
            System.out.println("Price: " + document.get("price"));
            System.out.println("---------------");
        }
    }
    
    @Test
    public void pageQueryTest() throws Exception {
    	this.pageQuery(5, 5);
    }
	
}
