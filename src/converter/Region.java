package converter;

import java.util.ArrayList;

public class Region implements CompainingFieldsAndParametrs{
	
	private String name;
	
	private ArrayList<Line> lines = new ArrayList<Line>();

	public Region(String name){
		this.name = name;
	}
	
	@Override
	public void setSourceInLastField(String className, String fieldName) {
		lines.get(lines.size()-1).getLastField().setSource(className, fieldName);
		
	}

	public void setLine(Line line){
		lines.add(line);
	}

	public Line getLastLine(){
		return lines.get(lines.size()-1);
	}
	
	@Override
	public void setFieldInLastPosition(Field field) {
		
		getLastLine().addField(field);
		
	}

	@Override
	public Field getLastField() {
		return getLastLine().getLastField(); 
		
	}
	
	public String getName(){
		return this.name;
	}
	
	public Line getLine(String num){
		Line ret = null;
		for(Line ln:lines){
			if(ln.getNumber().equals(num)){
				ret = ln;
				break;
			}
		}
		return ret;
	}
}
