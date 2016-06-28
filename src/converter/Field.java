package converter;

import java.util.ArrayList;

public class Field {
	
	/**
	 * Имя поля 
	 */
	private String name;
	
	/**
	 * <b><i>Размер поля в символах.</i></b><br>
	 * Все данные которые возвращает {@link #getFormatValue()}, обрезаются до данного размера.<br>
	 * Если содержит 0 - это неограниченная длина. Выводится все значение.<br>
	 * Если размер данных возвращаемых {@link #getFormatValue()} меньше размера поля производится дополнение до значения поля символом {@link #sypplementSymbol}. 
	 */
	private int size;

	/**
	 *<b><i>Символ дополнения</b></i><br>
	 *Если выводимые {@link #getFormatValue()} имеют длину меньше значения поля {@link #size}.<br>
	 *По умолчанию дополнение производится пробелом. 
	 */
	private String supplementSymbol;
	
	/**
	 * Выравнивание 
	 */
	private AligmentField aligment;
	
	/**
	 * Правила форматирования значения поля. 
	 */
	private Format format;
	
	/**
	 * Значение поля полученное из источника
	 */
	private String value;
	
	/**
	 * класс источник данных. 
	 */
	private String sourceClass;
	
	/**
	 * поле в классе-источнике из которого берется значение. 
	 */
	private String sourceField;

	/**
	 * <b><i>Создание простого поля.</i></b><br>
	 * Обязательно указать имя. Остальные поля устанавливаются следующим образом:<br>
	 * {@link #supplementSymbol} - пробел (" ").<br>
	 * {@link #size} - 0. Поле принимает размер значения<br>
	 * {@link #aligment} - {@link AligmentField #right}.<br>
	 * {@link #format} - формат задан конструктором по умолчанию (строковое значение){@link Format #Format()}
	 * @param name - имя создаваемого поля.
	 */
	public Field(String name){
		this.supplementSymbol = " ";
		this.size = 0;
		this.aligment = AligmentField.right;
		this.name = name;
		this.format = new Format();
		
		this.value = "";
		this.sourceClass = "";
		this.sourceField = "";		
	}
	
	/**
	 * Установить значение поля по его имени
	 * @param nameField - имя поля
	 * @param value - значение
	 */
	public void setFieldValue(String nameField, String value){
		if(nameField.equals("size")) setSize(value);
		if(nameField.equals("aligment")) setAligment(AligmentField.getAtStringName(value));
		if(nameField.equals("supplementSymbol")) setSupplementSymbol(value);		
	}
	
	/**
	 * Установить значение поля {@link #value}.
	 * @param value
	 */
	public void setValue(String value){
		this.value = value;
	}
	
	/**
	 * Установить название класса и поля этого класса из которого нужно получить значение.
	 * @param sourceClass
	 * @param sourceField
	 */
	public void setSource(String sourceClass, String sourceField){
		this.sourceClass = sourceClass;
		this.sourceField = sourceField;
	}
	
	
	/**
	 * Установить размер поля
	 * @param size
	 */
	public void setSize(String size) {
		int intSize = 0;
		try{
			intSize = Integer.parseInt(size);
		}catch(NumberFormatException e){
			intSize = 0;
		}
		this.size = intSize;
	}

	/**
	 * Установить символ дополнение. Выводится если значение поля {@link #value} меньше значения указанного в поле {@link #size}.
	 * @param supplementSymbol
	 */
	public void setSupplementSymbol(String supplementSymbol) {
		this.supplementSymbol = supplementSymbol;
	}

	/**
	 * Установить положение выравнивания в пределах поля размер которого обозначено полем {@link #size}.
	 * @param aligment
	 */
	public void setAligment(AligmentField aligment) {
		this.aligment = aligment;
	}

	/**
	 * Установить объект форматирования выводимого значения.
	 * @param format
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * @return имя поля.
	 */
	public String getName(){
		return this.name;
	}
	
	public String getSourceField(){
		return sourceField;
	}
	
	/**
	 * @return значение поля {@link #value}:<br>
	 *  {@link #format} отформатированное,<br>
	 *  {@link #aligment} выравненное,<br>
	 *  {@link #size} обрезанное или дополненное символом {@link #supplementSymbol}.<br>
	 *  если {@link #size} равно 0 - выводится значение "как есть" - без применения {@link #aligment} и {@link #size} но отформатированное.  
	 */
	public String getFormatValue(){
		
		this.format.formatingValue(value);
		if(size == 0) return this.format.getValue();
		
		return aligmentAndSizeVolume(this.format.getValue());
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void CalculateValue(Source src) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		if(sourceClass.equals("Source")){
			java.lang.reflect.Field field = src.getClass().getDeclaredField(this.sourceField);
			field.setAccessible(true);			
			this.value = this.value.concat((String) field.get(src));
		}else this.value = "";
	}
	
	public void CalculateTableValue(){
		
	}
	
	/**
	 * Выравнять в пределах указанного размера поля.
	 * Происходит заполнение поля указанным символом {@link #supplementSymbol}.
	 * @param value строка которую нужно отформатировать.
	 * @return строка включающая в себя поле заполненное символом {@link #supplementSymbol}, а
	 * сверху наложено значение value. Если значение превышает размер поля - оно образано.
	 */
	private String aligmentAndSizeVolume(String value){
		char[] strField = new char[size];		
		char[] strValue = value.trim().toCharArray();
		if(this.supplementSymbol.equals("")) this.supplementSymbol = " ";
		char supplementSymbol = this.supplementSymbol.toCharArray()[0];
		
				
		int countEnd = strField.length >= strValue.length ? strValue.length:strField.length;
		
		for(int index = 0;index<strField.length;index++) strField[index]=supplementSymbol;
		
		
		if(this.aligment == AligmentField.left){
			for(int count = 0;count<countEnd;count++){				
				strField[count]= strValue[count];
			}
		}
		if(this.aligment == AligmentField.right){
			for(int count = 0, indexRevValue = strValue.length-1, indexRevField = strField.length-1;count<countEnd; count++,indexRevValue--,indexRevField--){
				strField[indexRevField] = strValue[indexRevValue];
			}
		}
		if(this.aligment == AligmentField.center){
			int indexValue, indexField;
			
			int centrValue = strValue.length/2;
			int centrField = strField.length/2;
			
			
			
			if (centrValue >= centrField) {

				for (indexValue = centrValue - centrField, indexField = 0; indexField < strField.length; indexValue++, indexField++) {
					strField[indexField] = strValue[indexValue];
				}

			} else {
				for (indexField = centrField - centrValue, indexValue = 0; indexValue < strValue.length; indexField++, indexValue++) {
					strField[indexField] = strValue[indexValue];
				}
			}
			
		}
		return new String(strField);
	}
	
	public enum AligmentField{

		/**
		 * по левому краю поля 
		 */
		left,
		
		/**
		 * по правому краю поля 
		 */
		right,
		
		/**
		 * по центру 
		 */
		center;	
		
		public static AligmentField getAtStringName(String name){
			AligmentField ret = AligmentField.right;
			if(name.equals("left")) ret = AligmentField.left;
			if(name.equals("center")) ret = AligmentField.center;
			return ret;			
		}
	}

}


