package com.ceair.lucene.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class HomeSearch {
	
	public List<String> getIdsByKeyword(String keyword) throws Exception {
        Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        Query query = new TermQuery(new Term("content", keyword)); // 构造查询对象，分词查询
        TopDocs topDocs = indexSearcher.search(query, 2000);
        List<String> ids = new ArrayList<String>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            ids.add(doc.get("id"));
        }
        return ids;
	}

}
