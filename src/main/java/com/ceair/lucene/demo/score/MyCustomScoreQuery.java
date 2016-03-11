package com.ceair.lucene.demo.score;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

public class MyCustomScoreQuery extends CustomScoreQuery {

	public MyCustomScoreQuery(Query subQuery) {
		super(subQuery);
	}
	
	@Override
    protected CustomScoreProvider getCustomScoreProvider(
            AtomicReaderContext context) throws IOException {
        return new MyCustomScoreProvider(context);
    }

}
