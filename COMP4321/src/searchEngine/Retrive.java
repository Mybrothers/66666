import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import jdbm.helper.FastIterator;
import jdbm.htree.HTree;


public class Retrive {
	/*
	 * This function takes a query, which is an ArrayList of Strings, and returns
	 * an ArrayList of Documents in a descending order according to their cosine value 
	 * in vector space model. A match in the title will increase its score by 0.3
	 * Supports phrase search
	 */
	public ArrayList<Documents> retrive(ArrayList<String> query) throws IOException {
		ForwardFile ff = ForwardFile.getInstance();
		HTree docs = ff.getDocPageIndices();
		FastIterator it = docs.values();
		Documents doc = null;
		InvertedFile invertfile = InvertedFile.getInstance();
		HTree PageBodyIndices = invertfile.getpagebody();
		HTree PageTitleIndices = invertfile.getpagetitle();
		double N = ff.getNumberOfDocuments();
		ArrayList<Documents> result = new ArrayList<Documents>();
		while((doc = (Documents) it.next()) != null) {
			int titleMatch = 0;
			ArrayList<Double> vector = new ArrayList<Double>();
			for (String str: query) {
				str = str.toLowerCase();
				HashMap<String, ArrayList<Integer>> map = doc.getMap();
				// phase search
				if (str.contains(" ")) {
					// case sensitive
					ArrayList<Integer> preLocations = null;
					String[] phrase = str.split(" ");
					int tf = ContainsPhrase(map, phrase);
					if (tf == 0) {
						vector.add(0.0);
					} else {
						// how to get df of a phrase
						double idf = Math.log(10)/Math.log(2);
						vector.add(tf*idf/doc.getTfMax());
						if (doc.getTitle().contains(str)) {
							titleMatch++;
						}
					}
					continue;
				}
				if (map.containsKey(str)) {
					// case sensitive
					int tf = map.get(str).size();
					int df = ((LinkedList<Posting>)PageBodyIndices.get(str)).size();
					double idf = Math.log(N/df)/Math.log(2);
					vector.add(tf*idf/doc.getTfMax());
					LinkedList<Posting> titlePosting = (LinkedList<Posting>) PageTitleIndices.get(str);
					if (titlePosting != null) {
						for (Posting post:titlePosting) {
							if (post.getDocID() == doc.getDocID()) {
								titleMatch++;
								break;
							}
						}
					}
				} else {
					vector.add(0.0);
				}
			}
			double innerProduct = 0;
			double module2 = 0;
			for (double base:vector) {
				innerProduct += base;
				module2 += base*base;
			}
			if (innerProduct != 0 || titleMatch != 0) {
				double cosine = innerProduct/(Math.sqrt(module2)*Math.sqrt(query.size()));
				cosine += 0.3*titleMatch;
				doc.setScore(cosine);
				result.add(doc);
			}
		}
		Collections.sort(result, new Comparator<Documents>() {
	        @Override
	        public int compare(Documents doc1, Documents doc2)
	        {
	        	double difference = doc1.getScore() - doc2.getScore();
	        	if (difference > 0) {
	        		return -1;
	        	} else if (difference == 0) {
	        		return 0;
	        	} else {
	        		return 1;
	        	}
	        }
	    });
		if (result.size() <= 50) {
			return result;
		} else {
			return new ArrayList<Documents>(result.subList(0, 50));
		}
	}
	
	public ArrayList<String> process(String str) {
		Indexer index = new Indexer();
		str = str.trim();
		if (str.isEmpty()) {
			return null;
		} else {
			ArrayList<String> result = new ArrayList<String>();
			String substr = "";
			boolean phrase = false;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '\"') {
					if (phrase && !substr.isEmpty()) {
						substr = substr.trim();
						result.add(substr);
						substr = "";
					}
					phrase = !phrase;
				} else if (!phrase && Character.isWhitespace(str.charAt(i)) && !substr.isEmpty()) {
					substr = substr.trim();
					result.add(substr);
					substr = "";
				} else {
					substr += str.charAt(i);
				}
			}
			substr = substr.trim();
			if (!substr.isEmpty()) {
				result.add(substr);
			}
			index.stemming(result);
			return result;
		}
	}
	
	private int ContainsPhrase(HashMap<String, ArrayList<Integer>> map, String[] phrase) {
		ArrayList<Integer> preLocations = null;
		for (String tmp:phrase) {
			if (map.containsKey(tmp)) {
				if (preLocations == null) {
					preLocations = new ArrayList<Integer>(map.get(tmp));
				} else {
					ArrayList<Integer> curLocations = map.get(tmp);
					for (int i = 0; i < preLocations.size(); i++) {
						if (curLocations.contains(preLocations.get(i)+1)) {
							preLocations.set(i, preLocations.get(i)+1);
						} else {
							preLocations.remove(i);
						}
					}
					if (preLocations.isEmpty()) {
						return 0;
					}
				}
			} else {
				return 0;
			}
		}
		return preLocations.size();
	}
}
