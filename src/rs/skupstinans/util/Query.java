package rs.skupstinans.util;

public class Query {
	private String username;
	private int brojPropisa = -1;
	private boolean predlog;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	public int getBrojPropisa() {
		return brojPropisa;
	}

	public void setBrojPropisa(int brojPropisa) {
		this.brojPropisa = brojPropisa;
	}

	public boolean isPredlog() {
		return predlog;
	}

	public void setPredlog(boolean predlog) {
		this.predlog = predlog;
	}
	
	
}
