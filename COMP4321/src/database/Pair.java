package database;

import java.io.Serializable;

public class Pair implements Serializable{
    private String l;
    private int r;
	public Pair(String l, int r){
        this.l = l;
        this.r = r;
    }
    public String getL(){ return l; }
    public int getR(){ return r; }
    public void setL(String l){ this.l = l; }
    public void setR(int r){ this.r = r; }
}
