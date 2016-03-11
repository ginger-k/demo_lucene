package com.ceair.lucene.demo.analyzer;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class AnalyzerTest {

	//使用IKanalyzer
    @Test
    public void createIndexWithIKAnalyzer() throws Exception {
        // 索引位置
        Directory directory = FSDirectory.open(new File("index"));
        // 定义分词器(标准分词器)
        Analyzer analyzer = new IKAnalyzer();

        // 定义索引配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        indexWriterConfig.setOpenMode(OpenMode.CREATE);

        // 定义索引对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        for (int i = 0; i < 100; i++) {
            // 将商品数据转化为文档对象
            Document document = new Document();// 商品数据
            document.add(new LongField("id", i, Field.Store.YES));
            TextField titleField = new TextField("title", "苹果（Apple）iPhone 6 (A1586) 16GB 金色 移动联通电信4G手机  " + i, Field.Store.YES);
//            titleField.setBoost(5); //激励因子，类似与百度排名，默认为1，必须加载能被索引的字段上
            document.add(titleField);
            document.add(new TextField("sellPoint",
                    "下单送12000毫安移动电源！双3.5英寸魔焕炫屏，以非凡视野纵观天下时局，尊崇翻盖设计，张弛中，尽显从容气度！" + i, Field.Store.YES));
            document.add(new LongField("price", 100 * i, Field.Store.YES));
            document.add(new StringField("image", "http://image.taotao.com/jd/4ef8861cf6854de9889f3db9b24dc371.jpg", Field.Store.YES));
            // 写入数据
            indexWriter.addDocument(document);
        }
        // 关闭
        indexWriter.close();
    }

    //查询搜索
    @Test
    public void queryTest() throws Exception {
        // 定义索引位置
        Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        // 构造查询对象，词条搜索
        Query query = new TermQuery(new Term("title", "移动"));
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

    //分词搜索
    @Test
    public void searchTest() throws Exception {
        // 定义索引位置
        Directory directory = FSDirectory.open(new File("index"));
        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        Analyzer analyzer = new IKAnalyzer(); // 定义分词器(标准分词器)
        QueryParser parser = new QueryParser("title", analyzer); // 定义查询分析器
        Query query = parser.parse("苹果22"); // 构造查询对象，分词查询
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

}
