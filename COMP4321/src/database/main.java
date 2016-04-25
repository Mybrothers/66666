package database;

import java.io.IOException;
import java.util.ArrayList;

import org.htmlparser.util.ParserException;

import searchEngine.Retrive;

public class main {
	public static void constructURLtoID(Page root) throws IOException{
		UrlToIdMap IDmap = null;
        try {
        	IDmap = UrlToIdMap.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(IDmap != null){
        	IDmap.addEntry(root.url, root.index);
        }
        if(root.children.size() == 0){ return;}
        	
        for(Page p : root.children){
        	constructURLtoID(p);
        }
	}
	
	public static void test(){
		
	}

    public static void main (String[] args)
    {
        boolean links;
        String url;
        StringExtractor se;
        Indexer indexer = new Indexer();

        links = false;
        url = null;
        for (int i = 0; i < args.length; i++)
            if (args[i].equalsIgnoreCase ("-links"))
                links = true;
            else
                url = args[i];
        if (null != url)
        {
            se = new StringExtractor (url);
            Page root = null;
           
                try {
                	root = se.extractStrings(60);
//					se.print();
					UrlToIdMap IDmap = null;
			        try {
			        	IDmap = UrlToIdMap.getInstance();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(IDmap != null){
												
							indexer.indexingPageRoot(root);
							constructURLtoID(root);
							ForwardFile ff = ForwardFile.getInstance();
//							ff.printFile();
						
					}					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					ForwardFile ff = ForwardFile.getInstance();
					InvertedFile ivf = InvertedFile.getInstance();
//					ivf.print();
					UrlToIdMap imap = UrlToIdMap.getInstance();
					ff.finalize();
					ivf.finalize();
					imap.finalize();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//                Retrive a = new Retrive();
//                ArrayList<String> query = new ArrayList<String>();
////                query.add("Hong Kong");
////                query.add("Science");
//                ArrayList<Documents> result = null;
//                try {
//                	for (String str: a.process("\"Hong Kong\" Computer ")) {
//                		query.add(str);
//                	}
//                	result = a.retrive(query);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                for (Documents doc:result) {
//                	System.out.println(doc.getTitle());
//        			System.out.println(doc.getURL());
//        			System.out.println(doc.getDate() + " " + doc.getSize());
//        			System.out.println(doc.getScore());
//                }
        }
        else
            System.out.println ("Usage: java -classpath htmlparser.jar org.htmlparser.parserapplications.StringExtractor [-links] url");
        
              
    }
}
