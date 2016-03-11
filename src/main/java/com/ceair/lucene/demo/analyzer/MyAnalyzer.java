package com.ceair.lucene.demo.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

public class MyAnalyzer extends Analyzer {

	  @Override
	  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
//	    return new TokenStreamComponents(new WhitespaceTokenizer(reader));
		final Tokenizer tokenizer = new WhitespaceTokenizer(reader);
		//单词长度至少3个
		TokenStream tokenStream = new MyLengthFilter(tokenizer, 3, Integer.MAX_VALUE);
		//大小写转换
		tokenStream = new LowerCaseFilter(tokenStream);
		return new TokenStreamComponents(tokenizer, tokenStream);
	  }
	  
}