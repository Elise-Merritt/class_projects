package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		// github.com/11/TechTalks
		TrieNode root=new TrieNode(null, null, null);
		if(allWords==null) {
			return root;
		}
		TrieNode t;
		for(int i=0; i<allWords.length; i++) {
			String str1=allWords[i];
			t=new TrieNode(new Indexes(i,(short) 0,(short) (str1.length()-1)), null, null);
			if(root.firstChild==null) {
				t=new TrieNode(new Indexes(i,(short) 0,(short) (str1.length()-1)), null, null);
				root.firstChild=t;	
			}else {
				TrieNode traverse=root.firstChild;
				boolean tf=true;
				int count=0;
				while(tf) {
					String str2=allWords[traverse.substr.wordIndex].substring(traverse.substr.startIndex, traverse.substr.endIndex);
					String last=allWords[traverse.substr.wordIndex].substring(traverse.substr.endIndex, traverse.substr.endIndex+1);
					str2=str2+last;
					if(str1.equals(str2)) {
						tf=false;
					}else {
						String prefix="";
						int k=0;
						boolean xyz=true;
						while(xyz && k<str1.length() && k<str2.length()) {
							if(str1.charAt(k)==str2.charAt(k)) {
								prefix=prefix+""+str2.charAt(k);
								
							}else {
								xyz=false;
							}
							k++;
						}
						System.out.println("Str1="+str1);
						System.out.println("Str2="+str2);
						System.out.println("prefix="+prefix);
						if(prefix.equals("")) {
							if(traverse.sibling==null) {
								traverse.sibling=t;
								t.substr.startIndex=(short) (count);
								tf=false;
							}else {
								traverse=traverse.sibling;
								tf=true;
							}
						}else {
						/*	traverse.substr.endIndex=(short)(prefix.length()-1);
						    TrieNode temp=new TrieNode(new Indexes(traverse.substr.wordIndex, (short)(prefix.length()), (short)(str2.length()-1)),traverse.firstChild,null );
							traverse.firstChild=temp;
							t.substr.startIndex=(short) (prefix.length());
							traverse.firstChild.sibling=t;
							tf=false; */
						
						if(traverse.firstChild==null && traverse.sibling!=null) {
							traverse.firstChild=new TrieNode(new Indexes((short) (traverse.substr.wordIndex),(short) (traverse.substr.startIndex+prefix.length()), (short) (traverse.substr.endIndex)), null, t);
							traverse.substr.endIndex=(short) (traverse.firstChild.substr.startIndex-1);
							t.substr.startIndex=(short)(traverse.substr.startIndex + prefix.length());
							tf=false;
						}else if(traverse.firstChild!=null && traverse.sibling==null){
							if(prefix.length()<str2.length()) {
								traverse.substr.endIndex=(short)(prefix.length()-1);
							    TrieNode temp=new TrieNode(new Indexes(traverse.substr.wordIndex, (short)(prefix.length()), (short)(str2.length()-1)),traverse.firstChild,null );
								traverse.firstChild=temp;
								t.substr.startIndex=(short) (prefix.length());
								traverse.firstChild.sibling=t;
								tf=false;
							}else {
								str1=str1.substring(traverse.substr.endIndex+1);
								count=traverse.substr.endIndex+1;
								traverse=traverse.firstChild;
								tf=true;
							}
						}else if(traverse.firstChild==null && traverse.sibling==null) {
							traverse.firstChild=new TrieNode(new Indexes((short) (traverse.substr.wordIndex),(short) (traverse.substr.startIndex +prefix.length()), (short) (traverse.substr.endIndex)), null, t);
							traverse.substr.endIndex=(short) (traverse.firstChild.substr.startIndex-1);
							t.substr.startIndex=(short)(traverse.substr.startIndex +prefix.length());
							tf=false;
						}else if(traverse.firstChild!=null && traverse.sibling!=null){
							System.out.println("Traverse.firstChild = "+traverse.firstChild);
							str1=str1.substring(traverse.substr.endIndex+1);
					        count=traverse.substr.endIndex+1;
							traverse=traverse.firstChild;
							tf=true;
						}else {
							
						}
							
						}
						
					}
				}
			}
		}
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TrieNode> answer = new ArrayList<TrieNode>(allWords.length);
		TrieNode search=root.firstChild;
		while(search!=null) {
			String s=allWords[search.substr.wordIndex].substring(0, search.substr.endIndex+1);
			if(s.equals(prefix)) {
				return getLeafNodes(search.firstChild, allWords);
			}else if(s.contains(prefix)) {
				return getLeafNodes(search.firstChild, allWords);
				
			}else if (prefix.contains(s)) {
				search=search.firstChild;
			}else {
				if(search.sibling!=null) {
				search=search.sibling;
				}else {
					return null;
				}
			}
		}
			
		
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return answer;
	}
	private static ArrayList<TrieNode> getLeafNodes(TrieNode x, String[] allWords){
		ArrayList<TrieNode> answ = new ArrayList<TrieNode>(allWords.length);
		TrieNode traverse=x;
		while(traverse!=null) {
			if(traverse.firstChild!=null) {
				answ.addAll(getLeafNodes(traverse.firstChild, allWords));
			}else {
				answ.add(traverse);
			}
			traverse=traverse.sibling;
		}
		return answ;
		}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
