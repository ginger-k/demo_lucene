package com.ceair.lucene.demo.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

public class MyAnalyzerTest {

	 @Test
	  public void test() throws IOException {
	    final String text = "This is a demo of the TokenStream API";
	    @SuppressWarnings("resource")
		MyAnalyzer analyzer = new MyAnalyzer();
	    TokenStream stream = analyzer.tokenStream("field", new StringReader(text));
	    //get the CharTermAttribute from the TokenStream
	    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
	    stream.reset();
	    // print all tokens until stream is exhausted
	    while (stream.incrementToken()) {
	    	System.out.println(termAtt.toString());
	    }
	    stream.end();
	    stream.close();
	  }
	
}
