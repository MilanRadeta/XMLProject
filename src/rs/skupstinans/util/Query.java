package rs.skupstinans.util;

public class Query {
	private String username;
	private int brojPropisa = -1;
	private boolean predlog;
	private boolean inProcedure;
	public final static int PROPIS = 0;
	public final static int AMANDMAN = 1;
	private int type = PROPIS;
	private boolean notUsvojen = true;

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

	public boolean isInProcedure() {
		return inProcedure;
	}

	public void setInProcedure(boolean inProcedure) {
		this.inProcedure = inProcedure;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isNotUsvojen() {
		return notUsvojen;
	}

	public void setNotUsvojen(boolean notUsvojen) {
		this.notUsvojen = notUsvojen;
	}

}
