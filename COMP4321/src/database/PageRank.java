package database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

public class PageRank implements Serializable{
	public static final double d = 0.8;
	public static final double theta = 0.0001;
	public static void ranking(){
		
		try {
			ForwardFile ff = ForwardFile.getInstance();
			HTree pages = ff.getDocPageIndices();
			UrlToIdMap UtId = UrlToIdMap.getInstance();
			int num = UtId.NumOfDocs();			
			initialize(pages, num);
			boolean end = false;
			while(!end){
				double newScore[] = new double[num];
				double delta = 0;
				for(int i = 0; i < num; i++){
					double rankScore = 0;
					Documents doc = (Documents)pages.get(i);
					ArrayList<String> parents = doc.getParentURLs();					
					if (parents.size() != 0) {
						for (int j = 0; j < parents.size(); j++) {
							String URL = parents.get(j);
							Documents parent = (Documents) pages.get(UtId.getDocID(URL));
							rankScore += parent.getPageRank() / parent.numOfChildren();
						} 
					}
					rankScore = 1-d + d*(rankScore);
					newScore[i] = rankScore;
					double d = Math.abs(doc.getPageRank() - rankScore);
					delta = Math.max(delta, d);
				}
				for(int i = 0; i < num; i++){
					Documents doc = (Documents)pages.get(i);
					doc.setPageRank(newScore[i]);
					pages.put(i, doc);
				}
				if(delta < theta) {end = true;}
			}
			
			ff.finalize();
			UtId.finalize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void initialize(HTree pages, int numOfPages) throws IOException{
		for(int i = 0; i < numOfPages; i++){
			Object obj = pages.get(i);
			Documents doc = (Documents)obj;
			doc.setPageRank(1);
		}
	}
	
	public static void prinScore(){
		try {
			ForwardFile ff = ForwardFile.getInstance();
			HTree pages = ff.getDocPageIndices();
			FastIterator it = pages.values();
			int i = 0;
			Object obj  = null;
			while((obj = it.next()) != null){
				Documents doc = (Documents) obj;
				System.out.println("Id " + doc.getDocID() + " URL " + doc.getURL());
				System.out.println("PageRank score: " + doc.getPageRank());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
