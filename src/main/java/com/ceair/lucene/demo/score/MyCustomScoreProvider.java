package com.ceair.lucene.demo.score;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.Longs;

public class MyCustomScoreProvider extends CustomScoreProvider {

	private AtomicReaderContext context;
	
	public MyCustomScoreProvider(AtomicReaderContext context) {
		super(context);
		this.context = context;
	}

	
	 //重写评分方法
    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
    	System.out.println("docId:" + doc);
    	System.out.println("subQueryScore: " + subQueryScore); //boost越高，这个值就越高
    	System.out.println("valSrcScore: " + valSrcScore);
    	Longs longs= FieldCache.DEFAULT.getLongs(context.reader(), "price", false);
    	long price = longs.get(doc);
    	System.out.println("price: " + longs.get(doc));
        return subQueryScore * valSrcScore * price;
    }
}
