package com.ceair.lucene.demo.query;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class QueryParserTest {

   
	private void showByLanguage(String field, String language) throws Exception {
        // 定义索引位置
        Directory directory = FSDirectory.open(new File("index"));
        
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

        Analyzer analyzer = new IKAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(language);
        TopDocs topDocs = indexSearcher.search(query, 10);
        
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

	//词搜索
    @Test
    public void termQuery() throws Exception {
        showByLanguage("title", "苹果");
    }


    //模糊搜索,?代表1个任意字符,*代表0或多个任意字符
    @Test
    public void wildcardQuery() throws Exception {
    	showByLanguage("sellPoint", "天下？？");
    }
    
    
    //组合搜索
    @Test
    public void booleanQuery() throws Exception {
        showByLanguage("title", "苹果 49");
    }
    
}
