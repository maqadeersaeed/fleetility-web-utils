package com.fmis;

public class Constants {
	
	public enum DATE_FORMATE  {
		
		DD_MM_YYYY("MM/dd/yyyy"),
	    DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm"),
	    DD_MM_YYYY_ONLY("dd/MM/yyyy"),
	    DDMMYYYYHHMM("ddMMyyyyHHmm"),
	    DDMMYYYYHHMMSS("ddMMyyyyHHmmss"),
	    DD_MM_YYYY_HH_MM_SS("dd/MM/yyyy HH:mm:ss"),
	    DD_MMM_YYYY("dd-MMM-yyyy"),
	    YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
	    YYYY_MM_DD_ONLY("yyyy/MM/dd"),
	    HH_MM_SS_YYYY_MM_DD("HH:mm:ss yyyy/MM/dd"),
	    YYYY_MM_DD_ONLY_WITH_HYPHENS("yyyy-MM-dd"),
	    YY_MM_DD_WITH_SLASHES("yy/MM/dd"),
	    YY_MM_DD_WITH_HYPHENS("yy-MM-dd"),
	    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
	    YYYYMMDD("yyyyMMdd"),
	    YYYY_MM_DD_T_HH_MM_SS("yyyy-MM-dd'T'HH:mm:ss");

	    private final String text;

	    /**
	     * @param text
	     */
	    DATE_FORMATE(final String text) {
	        this.text = text;
	    }

	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
	}
}