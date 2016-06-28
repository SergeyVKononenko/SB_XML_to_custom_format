package converter;

import java.io.File;
import java.io.IOError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

public class Parametr {
	
	/**
	 * ��� ���������
	 */
	private String name;
		
	/**
	 * �������� �������������� ���������� 
	 */
	private String value;
	
	/**
	 * ������������ ��� �������� �������������� ��������. 
	 */
	private String[] connotation;
	
	/**
	 * ������ �������������� 
	 */
	private Format format;
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @return ���������� ��������������� �������� � ������������ � ����������� ������� {@link #format}
	 */
	public String getFormatValue(){
		format.formatingValue(this.value);
		return format.getValue();
	}
	
	/**
	 * ������������� ������ {@link #format}
	 * @param format
	 */
	public void setFormat(Format format){
		this.format = format;
	}
	

	/**
	 * ������� �������� 
	 * @param name - ��� ���������.
	 * @param execute - ��� ����������� ������� (��������) ������������ ����� ������ � ���� ������.
	 */
	public Parametr(String name, String execute){
		if(!agregateExist(execute)) this.value = "";		
	}
	
	/**
	 * ������� �������� � �������������� ������������� ������ 
	 * @param name - ��� ���������.
	 * @param execute - ��� ����������� ������� (��������) ������������ ����� ������ � ���� ������.
	 * @param connotation - ������ ������������� ������.
	 */
	public Parametr(String name, String execute, String[] connotation){
		this.name = name;
		this.connotation = connotation;
		if(!agregateExist(execute)) this.value = "";		
	}	
	
	/**
	 * ��������� ������������� �������� (����������� ������ ����� ������).
	 * @param execute - �������� ��������.
	 * @return true - ������� ��������� �������. false - ������� �� ���������.
	 */
	private boolean agregateExist(String execute){
		boolean retValue = false;
		
		try {			
			Method m = this.getClass().getDeclaredMethod(execute);
			m.invoke(this);
			retValue = true;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			retValue = false;
		}
		return retValue;
	}
	
	/**
	 *�������� ������� ���� � ���������� �� ��������� value. 
	 */
	private void get_current_date(){
		Date date = new Date();
		this.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
	}
	
	/**
	 *�������� ���������� ����� ����� � ���������� 
	 *������ � ���������� ���������� � connotation[0] 
	 */
	private void get_file_index_in_output_directory(){
		this.value = "";
		
		File path;
		File listFiles[];
		
		int count = 0;
		
		if(connotation.length!=0){
			if(!connotation[0].equals("")){
				path = new File(connotation[0]);
				if(!path.exists()) path.mkdirs();
				listFiles = path.listFiles();
				if(listFiles.length == 0) this.value = "1";
				else{
					for(File f:listFiles){
						if(!f.isDirectory()){
							count++;
						}
					}
					this.value = Integer.toString(++count);
				}				
			}
		}
	}
}
