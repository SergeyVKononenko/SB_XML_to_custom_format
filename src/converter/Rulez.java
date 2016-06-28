package converter;

public class Rulez {
	@SuppressWarnings("unused") 
	private String regionName;
	
	@SuppressWarnings("unused")
	private String lineNumber;
	
	@SuppressWarnings("unused")
	private int repeat;
	
	@SuppressWarnings("unused")
	private boolean isTable;
	
	public Rulez(String regionName, String lineNumber, String repeat, String table){
		this.regionName = regionName;
		this.lineNumber = lineNumber;
		this.repeat = Integer.getInteger(repeat);
		this.isTable = table.equals("true") ? true:false;
	}

}
