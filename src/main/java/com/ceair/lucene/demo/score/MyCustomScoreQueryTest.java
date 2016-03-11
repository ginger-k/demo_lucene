package com.ceair.lucene.demo.score;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class MyCustomScoreQueryTest {
	
	@Test
	public void test() throws IOException {
		Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        
        
        BooleanQuery booleanQuery = new BooleanQuery();
//        booleanQuery.add(new TermQuery(new Term("title", "49")), Occur.MUST);
        booleanQuery.add(new TermQuery(new Term("title", "苹果")), Occur.MUST);
        
        Query query = new MyCustomScoreQuery(booleanQuery);
        TopDocs topDocs = indexSearcher.search(query, 10);
        
        System.out.println("-------------------------");
        System.out.println("搜索结果总结：" + topDocs.totalHits);
        System.out.println("-------------------------");
        
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            System.out.println("得分：" + scoreDoc.score);
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("price：" + doc.get("price"));
            System.out.println("-------------------------");
        }
	}

}
