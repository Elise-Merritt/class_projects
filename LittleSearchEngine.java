package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		HashMap<String, Occurrence> keywords = new HashMap<String, Occurrence>();
		File check = new File(docFile);
		if(!check.exists()) {
			throw new FileNotFoundException();
		}
		File dF = new File(docFile);
		Scanner reader=new Scanner(dF);
		reader.useDelimiter(" |\\r|\\n");
		while(reader.hasNext()) {
			String str=reader.next();
			String key=this.getKeyword(str);
			if (key!=null && !str.equals("")) {
				if(keywords.containsKey(key)) {
					Occurrence oldOcc=keywords.get(key);
					Occurrence newOcc=new Occurrence(docFile, oldOcc.frequency+1);
					keywords.remove(key, oldOcc);
					keywords.put(key, newOcc);
				} else {
					Occurrence occ=new Occurrence(docFile, 1);
					keywords.put(key, occ);
				}
			}
		}
			// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return keywords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		Iterator items= kws.entrySet().iterator();
		while(items.hasNext()) {
			Map.Entry<String, Occurrence> item= (Map.Entry) items.next();
			String key=item.getKey();
			Occurrence occ =item.getValue();
			if(keywordsIndex.containsKey(key)) {
				keywordsIndex.get(key).add(occ);
				insertLastOccurrence(keywordsIndex.get(key));
				
			}else {
				ArrayList<Occurrence> o = new ArrayList<Occurrence>();
				o.add(occ);
				keywordsIndex.put(key, o);
			}
		}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		if (word.length()<=1) {
			return null;
		}
		for(int i=word.length()-1; i>=0; i--) {
			char c=word.charAt(i);
			if(c=='.' || c==',' || c=='?' || c==':' || c==';' || c=='!') {
				word=word.substring(0, i);
			}else if (Character.isLetter(c)==true){
				break;
			}
		}
		for(int j=0; j<word.length(); j++) {
			char d =word.charAt(j);
			if(Character.isLetter(d)!=true) {
				return null;
			}else {
				
			}
		}
		boolean x=noiseWords.contains(word.toLowerCase());
		if(x==true) {
			return null;
		}else {
			return word.toLowerCase();
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> answer= new ArrayList<Integer>();
		int lastOcc, indexLastOcc;
		Occurrence temp=occs.get(occs.size()-1);
		if(occs.size()<=1) {
			return null;
		}else {
		lastOcc=temp.frequency;
		indexLastOcc=occs.size()-1;
		occs.remove(indexLastOcc);
		}
		int low=0, high=occs.size()-1, middle = 0, position=0;
		while(low<=high) {
			middle=(low+high)/2;
			int valueAt=occs.get(middle).frequency;
			answer.add(middle);
			if(valueAt==lastOcc) {
				break;
			}else if (valueAt>lastOcc) {
				low=middle+1;
			}else {
				high=middle-1;
			}
		}
		position=middle;
		if(occs.size()==1) {
			if(lastOcc>occs.get(0).frequency) {
				occs.add(position, temp);
			}else {
				occs.add(temp);
			}
		}else if(position!=occs.size()-1) {
		occs.add(position, temp);
		}else {
		occs.add(temp);
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return answer;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Occurrence> occ1, occ2, occ3=new ArrayList<Occurrence>();
		if(keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)) {
			occ1 = keywordsIndex.get(kw1);
			occ2 = keywordsIndex.get(kw2);	
			for(Occurrence c : occ1) {
				occ3.add(c);
			}
			for(Occurrence d : occ2) {
				occ3.add(d);
				insertLastOccurrence(occ3);
			}
			int i=0;
			while(i<occ3.size() && result.size()<=5) {
				Occurrence e=occ3.get(i);
				String value=e.document;
				if(!result.contains(value)) {
					result.add(e.document);
				}
				i++;
			}
		}else if (keywordsIndex.containsKey(kw1)) {
			occ1=keywordsIndex.get(kw1);
			int i=0;
			while(i<occ1.size() && i<=5) {
				result.add(occ1.get(i).document);
				i++;
			}
		}else if (keywordsIndex.containsKey(kw2)) {
			int i =0;
			occ2=keywordsIndex.get(kw2);
			while(i<occ2.size() && i<=5) {
				result.add(occ2.get(i).document);
				i++;
			}
		}else {
			return null;
		}
		ArrayList<String> result2 = new ArrayList<String>();
		for(int j=0; j<5; j++) {
			result2.add(result.get(j));
		}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		return result2;
	
	}
}
