package database;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

public class ForwardFile implements Serializable{

	public final int TO_PAGE_BODY = 0;
	public final int TO_PAGE_TITLE = 1;
	
	private int NumberOfDocuments;
	private transient RecordManager recman;
	private transient HTree docPageIndices;
	private transient HTree docTitleIndices;
	private static ForwardFile instance;
	
	public static ForwardFile getInstance() throws IOException{
		if (instance == null) {
			instance = new ForwardFile();
		}
		return instance;
	}
	
	private ForwardFile() throws IOException {
		recman = RecordManagerFactory.createRecordManager("FindPlayersForwardIndex");
		long recid = recman.getNamedObject("Page_Forward_Index");
		if(recid == 0){
			docPageIndices = HTree.createInstance(recman);
			recman.setNamedObject("Page_Forward_Index", docPageIndices.getRecid());
		}
		else{
			docPageIndices = HTree.load(recman, recid);
		}
		recid = recman.getNamedObject("Title_Forward_Index");
		if(recid == 0){
			docTitleIndices = HTree.createInstance(recman);
			recman.setNamedObject("Title_Forward_Index", docTitleIndices.getRecid());
		}
		else{
			docTitleIndices = HTree.load(recman, recid);
		}
		recid = recman.getNamedObject("NumberOfDocuments");
		if (recid == 0) {
			NumberOfDocuments = 0;
			recid = recman.insert(0);
			recman.setNamedObject("NumberOfDocuments", recid);
		} else {
			NumberOfDocuments = (int)recman.getNamedObject("NumberOfDocuments");
		}
	}
	
	public int getNumberOfDocuments() {
		return this.NumberOfDocuments;
	}
	
	public void insertDoc(int id, int tfmax,String url, ArrayList<String> terms, int type, Date Date, String Title, ArrayList<String> childURLs, ArrayList<String> parentURLs,int Size, HashMap<String, ArrayList<Integer>> map) throws IOException{
		Documents doc = new Documents(id, tfmax, url,Date, Title, terms, childURLs, parentURLs,Size, map);
		switch(type){
		case TO_PAGE_BODY:
			Documents test = (Documents) docPageIndices.get(id);
			if(test != null) {System.out.println("Warning: same Doc ID, documents alredy exist"); return;}
			docPageIndices.put(id, doc);
			NumberOfDocuments++;
			break;
		case TO_PAGE_TITLE:
			Documents test2 = (Documents) docTitleIndices.get(id);
			if(test2 != null) {System.out.println("Warning: same Doc ID, documents alredy exist"); return;}
			docTitleIndices.put(id, doc);
		}
	}
	
	public void deleteDoc(int id, int type) throws IOException{
		InvertedFile invf = InvertedFile.getInstance();;
		ArrayList<String> terms;
		if (type == TO_PAGE_BODY) {
			Object data = docPageIndices.get(id);
			if (data == null) {
				System.out.println("Warning: the doc ID doesn't exist in page indices");
				return;
			}
			Documents body = (Documents) data;
			terms = body.getAllTerms();
			for (String term : terms) {
				invf.deleteEntry(term, id, TO_PAGE_BODY);
			}
			docPageIndices.remove(id);
			NumberOfDocuments--;
		}
		if (type == TO_PAGE_TITLE) {
			Object data2 = docTitleIndices.get(id);
			if (data2 == null) {
				System.out.println("Warning: the doc ID doesn't exist in title indices");
				return;
			}
			Documents title = (Documents) data2;
			terms = title.getAllTerms();
			for (String term : terms) {
				invf.deleteEntry(term, id, TO_PAGE_TITLE);
			}
			docTitleIndices.remove(id);
		}
		
	}
	
	public void finalize() throws IOException
	{
		long recid = recman.getNamedObject("NumberOfDocuments");
		recman.update(recid, NumberOfDocuments);
		recman.commit();
		recman.close();		
		instance = null;
	}

	public boolean isModified(int docId, Date date) throws IOException {
		Documents doc = (Documents)docPageIndices.get(docId);
		if(date.after(doc.getDate())) return true;
		return false;
	}

	public HTree getDocPageIndices() {
		return docPageIndices;
	}
	
	public static void printFile() throws IOException{
		ForwardFile ff = ForwardFile.getInstance();
		HTree docs = ff.getDocPageIndices();
		FastIterator it = docs.values();
		Documents doc = null;
		while((doc = (Documents) it.next()) != null){
			System.out.println(doc.getTitle());
			System.out.println(doc.getURL());
			System.out.println(doc.getDate() + " " + doc.getSize());
			for (String term : doc.getMap().keySet()) {
				System.out.print(term + " " + doc.getMap().get(term).size() + "; ");
			}
			
			if	(doc.getChildURLs() != null) {
				for (String childurl : doc.getChildURLs()) {
					System.out.println(childurl);
				}	
			}
//			for (Pair term: doc.getKeywords()) {
//				System.out.println(term.getL());
//			}
			String newLine = System.getProperty("line.separator");
		    System.out.println(newLine );
		}
	}
}