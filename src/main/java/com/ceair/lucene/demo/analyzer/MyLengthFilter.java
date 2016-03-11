package com.ceair.lucene.demo.analyzer;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.FilteringTokenFilter;

public final class MyLengthFilter extends FilteringTokenFilter {

	  private final int min;
	  private final int max;
	  
	  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	  public MyLengthFilter(TokenStream in, int min, int max) {
	    super(in);
	    this.min = min;
	    this.max = max;
	  }

	  @Override
	  public boolean accept() {
	    final int len = termAtt.length();
	    return (len >= min && len <= max);
	  }

	}
