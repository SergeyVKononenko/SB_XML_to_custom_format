package converter;

import java.util.ArrayList;

public class Field {
	
	/**
	 * ��� ���� 
	 */
	private String name;
	
	/**
	 * <b><i>������ ���� � ��������.</i></b><br>
	 * ��� ������ ������� ���������� {@link #getFormatValue()}, ���������� �� ������� �������.<br>
	 * ���� �������� 0 - ��� �������������� �����. ��������� ��� ��������.<br>
	 * ���� ������ ������ ������������ {@link #getFormatValue()} ������ ������� ���� ������������ ���������� �� �������� ���� �������� {@link #sypplementSymbol}. 
	 */
	private int size;

	/**
	 *<b><i>������ ����������</b></i><br>
	 *���� ��������� {@link #getFormatValue()} ����� ����� ������ �������� ���� {@link #size}.<br>
	 *�� ��������� ���������� ������������ ��������. 
	 */
	private String supplementSymbol;
	
	/**
	 * ������������ 
	 */
	private AligmentField aligment;
	
	/**
	 * ������� �������������� �������� ����. 
	 */
	private Format format;
	
	/**
	 * �������� ���� ���������� �� ���������
	 */
	private String value;
	
	/**
	 * ����� �������� ������. 
	 */
	private String sourceClass;
	
	/**
	 * ���� � ������-��������� �� �������� ������� ��������. 
	 */
	private String sourceField;

	/**
	 * <b><i>�������� �������� ����.</i></b><br>
	 * ����������� ������� ���. ��������� ���� ��������������� ��������� �������:<br>
	 * {@link #supplementSymbol} - ������ (" ").<br>
	 * {@link #size} - 0. ���� ��������� ������ ��������<br>
	 * {@link #aligment} - {@link AligmentField #right}.<br>
	 * {@link #format} - ������ ����� ������������� �� ��������� (��������� ��������){@link Format #Format()}
	 * @param name - ��� ������������ ����.
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
	 * ���������� �������� ���� �� ��� �����
	 * @param nameField - ��� ����
	 * @param value - ��������
	 */
	public void setFieldValue(String nameField, String value){
		if(nameField.equals("size")) setSize(value);
		if(nameField.equals("aligment")) setAligment(AligmentField.getAtStringName(value));
		if(nameField.equals("supplementSymbol")) setSupplementSymbol(value);		
	}
	
	/**
	 * ���������� �������� ���� {@link #value}.
	 * @param value
	 */
	public void setValue(String value){
		this.value = value;
	}
	
	/**
	 * ���������� �������� ������ � ���� ����� ������ �� �������� ����� �������� ��������.
	 * @param sourceClass
	 * @param sourceField
	 */
	public void setSource(String sourceClass, String sourceField){
		this.sourceClass = sourceClass;
		this.sourceField = sourceField;
	}
	
	
	/**
	 * ���������� ������ ����
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
	 * ���������� ������ ����������. ��������� ���� �������� ���� {@link #value} ������ �������� ���������� � ���� {@link #size}.
	 * @param supplementSymbol
	 */
	public void setSupplementSymbol(String supplementSymbol) {
		this.supplementSymbol = supplementSymbol;
	}

	/**
	 * ���������� ��������� ������������ � �������� ���� ������ �������� ���������� ����� {@link #size}.
	 * @param aligment
	 */
	public void setAligment(AligmentField aligment) {
		this.aligment = aligment;
	}

	/**
	 * ���������� ������ �������������� ���������� ��������.
	 * @param format
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * @return ��� ����.
	 */
	public String getName(){
		return this.name;
	}
	
	public String getSourceField(){
		return sourceField;
	}
	
	/**
	 * @return �������� ���� {@link #value}:<br>
	 *  {@link #format} �����������������,<br>
	 *  {@link #aligment} �����������,<br>
	 *  {@link #size} ���������� ��� ����������� �������� {@link #supplementSymbol}.<br>
	 *  ���� {@link #size} ����� 0 - ��������� �������� "��� ����" - ��� ���������� {@link #aligment} � {@link #size} �� �����������������.  
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
	 * ��������� � �������� ���������� ������� ����.
	 * ���������� ���������� ���� ��������� �������� {@link #supplementSymbol}.
	 * @param value ������ ������� ����� ���������������.
	 * @return ������ ���������� � ���� ���� ����������� �������� {@link #supplementSymbol}, �
	 * ������ �������� �������� value. ���� �������� ��������� ������ ���� - ��� ��������.
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
		 * �� ������ ���� ���� 
		 */
		left,
		
		/**
		 * �� ������� ���� ���� 
		 */
		right,
		
		/**
		 * �� ������ 
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


