package rs.skupstinans.elementi;

import java.util.List;

public interface ElementInterface {
	public List<Object> getContent();
	public void setId(String value);
    public String getId();
    public void setRednaOznaka(String value);
    public String getRednaOznaka();
    
}
