package com.ceair.lucene.demo.query;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class QueryTest {

   
	private void showByQuery(Query query) throws Exception {
        // 定义索引位置
        Directory directory = FSDirectory.open(new File("index"));
        
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

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

	//词查询
    @Test
    public void termQuery() throws Exception {
        Query query = new TermQuery(new Term("title", "苹果"));
        showByQuery(query);
    }

    //范围查询
    @Test
    public void numericRangeQuery() throws Exception {
        // 设置查询字段、最小值、最大值、最小值是否包含边界，最大值是否包含边界
        Query query = NumericRangeQuery.newLongRange("id", 1L, 10L, false, true);
        showByQuery(query);
    }

     // 匹配查询
    @Test
    public void matchAllDocsQuery() throws Exception {
        Query query = new MatchAllDocsQuery();
        showByQuery(query);
    }
    
    //模糊查询,?代表1个任意字符,*代表0或多个任意字符
    @Test
    public void wildcardQuery() throws Exception {
        Query query = new WildcardQuery(new Term("title", "2*"));
        showByQuery(query);
    }
    
     //相似度查询
    @Test
    public void fuzzyQuery() throws Exception {
        Query query = new FuzzyQuery(new Term("title", "平果"));
        showByQuery(query);
    }
    
    //组合查询
    @Test
    public void booleanQuery() throws Exception {
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("title", "49")), Occur.MUST);
        query.add(new TermQuery(new Term("title", "苹果")), Occur.MUST);
        showByQuery(query);
    }
    
}
