package converter;


import java.util.ArrayList;

public class Command implements CompainingFieldsAndParametrs{
	private String name;
	private String value;
	
	private Source source;

	private ArrayList<Field> fields = new ArrayList<Field>();
	private ArrayList<Parametr> parametrs = new ArrayList<Parametr>();
	
	public Command(String name, String value){
		this.name = name;
		this.value = value;
		
	}
	
	public void addParametr(Parametr parametr){
		parametrs.add(parametr);
	}
	
	public Field getLastField(){
		return fields.get(fields.size()-1);
	}
	
	public String getName(){
		return this.name;
	}
	
	public Parametr getLastParameter(){
		return parametrs.get(parametrs.size()-1);
	}
	
	/**
	 * @return возвращает значение полученное с помощью обработки полей {@link #fields} 
	 * и параметров {@link #parametrs}. Содержимое {@link #value} обрабатывается, вместо ссылок
	 * подставляются рассчитанные значения. 
	 */
	public String getCommandValue(){
		String retValue = "";
		
		String fpName = "";
		String symbol;
		
		boolean intLoop = false;
		
		int beginIndex = 0;
		int endIndex = 0;  
		
		while(beginIndex < this.value.length()){
			endIndex = beginIndex + 1 > this.value.length() ? beginIndex:beginIndex+1;  
			
			symbol = this.value.substring(beginIndex, endIndex);
			if(symbol.equals("[")){
				intLoop = true;
				symbol = "";
						
			}
			if(symbol.equals("]")){
				intLoop = false;
				retValue = retValue.concat(getFPValue(fpName));
				fpName = "";
				symbol = "";
			}
			if(intLoop) 
				fpName = fpName.concat(symbol);
			else 
				retValue = retValue.concat(new String(symbol));
			
			beginIndex++;
		}
		
		return retValue;
	}
	
	@Override
	public void setSourceInLastField(String className, String fieldName) {
		fields.get(fields.size()-1).setSource(className, fieldName);		
	}

	private String getFPValue(String fpName){
		String ret = "";
		boolean haveValue = false;
		
		for(Field field:fields){
			if(field.getName().equals(fpName)){
				ret = field.getFormatValue();
				haveValue = true;
				break;
			}
		}
		
		if (!haveValue) {
			for (Parametr parametr : parametrs) {
				if (parametr.getName().equals(fpName)) {
					ret = parametr.getFormatValue();
					break;
				}
			}
		}
		return ret;		
	}

	@Override
	public void setFieldInLastPosition(Field field) {
		fields.add(field);		
	}
}
