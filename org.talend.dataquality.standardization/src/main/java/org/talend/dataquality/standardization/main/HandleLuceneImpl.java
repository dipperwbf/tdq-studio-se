// ============================================================================
//
// Copyright (C) 2006-2010 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dataquality.standardization.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.talend.dataquality.standardization.index.IndexBuilder;
import org.talend.dataquality.standardization.query.FirstNameStandardize;

/**
 * DOC klliu class global comment.
 */
public class HandleLuceneImpl implements HandleLucene {
	private final int hitsPerPage = 10;
	private final String indexfolder="./data/TalendGivenNames_index";
	private Map<String,String[]> hits = new HashMap<String,String[]>();
	private ArrayList<String> soreDoc=null;
	/**
	 * Input filename to be indexed once for all and indexfolder to store the
	 * files of indexing.
	 * 
	 * @param filename
	 * @param indexfolder
	 * @return
	 */
	public boolean createIndex(String filename, String indexfolder) {
		IndexBuilder idxBuilder = getIndexBuilder(indexfolder);
		int[] columnsToBeIndexed = new int[] { 0, 1, 2, 3 };
		try {
			idxBuilder.initializeIndex(filename, columnsToBeIndexed);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Expect that by accepting parameter and returns the correct result, if not
	 * correspond with the result of searchwords, do a fuzzy query, returns the
	 * result of the similar.
	 * 
	 * @param searchWords
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public Map<String,String[]> getSearchResult(String searchType,String[] searchWords)
			throws IOException, ParseException {
		// TODO Auto-generated method stub

		IndexSearcher firtNameIs = getIndexSearcher(indexfolder);

		Analyzer searchAnalyzer = getAnalyzer();

		FirstNameStandardize stdname = new FirstNameStandardize(firtNameIs,
				searchAnalyzer, hitsPerPage);
		for (int searchCount = 0; searchCount < searchWords.length; searchCount++) {
			ScoreDoc[] docs = stdname.standardize(searchType,searchWords[searchCount]);
			treatSearchResult(firtNameIs,searchType,docs,searchWords[searchCount]);
		}
		firtNameIs.close();

		return getHits();
	}
	/**
	 * 
	 * DOC Override getSearchResult
	 */
	public Map<String, String[]> getSearchResult(String folderName,
			String searchType, String[] searchWords) throws IOException,
			ParseException {
		// TODO Auto-generated method stub
		IndexSearcher firtNameIs = getIndexSearcher(folderName);
        
		Analyzer searchAnalyzer = getAnalyzer();

		FirstNameStandardize stdname = new FirstNameStandardize(firtNameIs,
				searchAnalyzer, hitsPerPage);
		for (int searchCount = 0; searchCount < searchWords.length; searchCount++) {
			ScoreDoc[] docs = stdname.standardize(searchType,searchWords[searchCount]);
			treatSearchResult(firtNameIs,searchType,docs,searchWords[searchCount]);
		}
		firtNameIs.close();

		return getHits();
	}
	private void treatSearchResult(IndexSearcher firtNameIs,String searchType,ScoreDoc[] docs,String searchWords) {
		soreDoc=new ArrayList<String>();
		for (int i = 0; i < docs.length; ++i) {
			int docId = docs[i].doc;
			Document d = null;
			try {
				d = firtNameIs.doc(docId);
				String name = d.get(searchType);
				soreDoc.add(name);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] resultArray =new String[soreDoc.size()];
		hits.put(searchWords, soreDoc.toArray(resultArray));
		

	}

	private Map<String,String[]> getHits() {
		return hits;
	}

	private Analyzer getAnalyzer() {
		return new SimpleAnalyzer();
	}

	private IndexBuilder getIndexBuilder(String folderName) {
		return new IndexBuilder(folderName);
	}

	private IndexSearcher getIndexSearcher(String folderName){
		Directory dir = null;
		IndexSearcher is = null;
		try {
			dir = FSDirectory.open(new File(folderName));
			is = new IndexSearcher(dir);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	

}
