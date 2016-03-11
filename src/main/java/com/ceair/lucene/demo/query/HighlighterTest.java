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
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class HighlighterTest {
	
	@Test
    public void test() throws Exception {
        Directory directory = FSDirectory.open(new File("index"));
        // 定义索引搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));

        Analyzer analyzer = new IKAnalyzer();

        // 构建查询对象
        Query query = new QueryParser("title", analyzer).parse("苹果");

        // 定义高亮组件
        Formatter formatter = new SimpleHTMLFormatter("<span class='red'>", "</span>");
        // 构建Scorer,用于选取最佳切片
        Scorer scorer = new QueryScorer(query);
        // 实例化Highlighter组件
        Highlighter highlighter = new Highlighter(formatter, scorer);
        // 构建Fragmenter对象,用于文档切片
        highlighter.setTextFragmenter(new SimpleFragmenter(100));
        highlighter.setEncoder(new SimpleHTMLEncoder());

        // 搜索
        TopDocs topDocs = indexSearcher.search(query, 10); // 根据end查询

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            System.out.println("得分：" + scoreDoc.score);
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("ID: " + document.get("id"));
            String title = document.get("title");
            System.out.println("Title: " + title);
            title = highlighter.getBestFragment(analyzer, "title", title);

            System.out.println("Title(高亮): " + title);
            System.out.println("SellPoint: " + document.get("sellPoint"));
            System.out.println("Price: " + document.get("price"));
            System.out.println("---------------");
        }

    }

}
