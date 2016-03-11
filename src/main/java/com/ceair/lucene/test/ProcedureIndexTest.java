package com.ceair.lucene.test;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

import com.ceair.lucene.service.ProcedureIndex;


public class ProcedureIndexTest{
	
	public static void main(String[] args) {
		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(3);
		ProcedureIndex command = new ProcedureIndex();
		threadPool.scheduleWithFixedDelay(command, 3, 60, TimeUnit.SECONDS);
	}

    @Test
    public void queryTest() throws Exception {
        Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        Query query = new TermQuery(new Term("id", "84d5e2864e0d47c8961e6f97517f5fb9"));
        TopDocs topDocs = indexSearcher.search(query, 1);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("id：" + doc.get("id"));
            System.out.println("name：" + doc.get("name"));
            System.out.println("content：" + doc.get("content"));
            System.out.println("path：" + doc.get("path"));
            System.out.println("createTime：" + doc.get("createTime"));
            System.out.println("updateTime：" + doc.get("updateTime"));
        }
    }

}
