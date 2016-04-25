package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Documents implements Serializable{
		double score;
		int docID;
		int tfMax;
		String URL;
		int size;
		Date date;
		String title;
		ArrayList<String> allTerms;
		ArrayList<String> childURLs;
		HashMap<String, ArrayList<Integer>> map;
		
		public Documents(int id, int tfmax, String url, Date Date, String Title, ArrayList<String> terms, ArrayList<String> childURLs, int Size, HashMap<String, ArrayList<Integer>> map) {
			docID = id;
			tfMax = tfmax;
			URL = url;
			date = Date;
			title = Title;
			size = Size;
			allTerms = terms;
			this.childURLs = childURLs;
			this.map = map;
			this.score = -1;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getURL() {
			return URL;
		}

		public void setURL(String uRL) {
			URL = uRL;
		}

		public int getDocID() {
			return docID;
		}

		public void setDocID(int docID) {
			this.docID = docID;
		}

		public int getTfMax() {
			return tfMax;
		}

		public void setTfMax(int tfMax) {
			this.tfMax = tfMax;
		}
		
		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public void addTerms(String term){
			if(allTerms != null) allTerms.add(term);
		}
		
		public void deleteTerms(String term){
			if(allTerms != null) allTerms.add(term);
		}
		
		public ArrayList<String> getAllTerms() {
			return allTerms;
		}

		public void setAllTerms(ArrayList<String> allTerms) {
			this.allTerms = allTerms;
		}
		
		public ArrayList<String> getChildURLs() {
			return childURLs;
		}

		public void setChildURLs(ArrayList<String> childURLs) {
			this.childURLs = childURLs;
		}
		
		public HashMap<String, ArrayList<Integer>> getMap() {
			return this.map;
		}
		
		public void setScore(double score) {
			this.score = score;
		}
		
		public double getScore() {
			return this.score;
		}
	}
