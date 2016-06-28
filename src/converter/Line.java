package converter;

import java.util.ArrayList;


public class Line {
	
	
	private String number;//можно считать это именем. 
	
	private String value;//макет по которому нужно выводить данные линии.
	
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	public Line(String number, String value){
		this.number = number;
		this.value = value;
	}
	
	public void addField(Field field){
		fields.add(field);
	}
	
	public Field getLastField(){
		return fields.get(fields.size()-1);
	}
	
	/**
	 * @return по шаблону находящемуся в поле {@link #value} 
	 * расставляются отформатированные значения полей. 
	 */
	public String getValueOnFields(){
		String ret = this.value;
		for(Field field:fields){
			String fieldValue = field.getFormatValue();
			String patern = "["+field.getName()+"]";
			ret = ret.replace(patern, fieldValue);
		}
		return ret;
	}
	
	public String getValueOnFields(SourceTableItem tableItem){
		String ret = this.value;
		for(Field field:fields){
			String fieldName = field.getSourceField();
			try {
				java.lang.reflect.Field f = tableItem.getClass().getDeclaredField(fieldName);
				f.setAccessible(true);
				String val = (String) f.get(tableItem);
				field.setValue(val);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				field.setValue("");
			}
			
			String fieldValue = field.getFormatValue();
			String patern = "["+field.getName()+"]";
			ret = ret.replace(patern, fieldValue);
		}
		return ret;
	}
	
	public String getNumber(){
		return this.number;
	}
}
