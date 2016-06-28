package converter;

import java.io.File;
import java.io.IOError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

public class Parametr {
	
	/**
	 * Имя параметра
	 */
	private String name;
		
	/**
	 * Значение сформированное параметром 
	 */
	private String value;
	
	/**
	 * Используется для передачи дополнительных значений. 
	 */
	private String[] connotation;
	
	/**
	 * Объект форматирования 
	 */
	private Format format;
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @return возвращает форматированное значение в соответствии с настройками объекта {@link #format}
	 */
	public String getFormatValue(){
		format.formatingValue(this.value);
		return format.getValue();
	}
	
	/**
	 * Устанавливает объект {@link #format}
	 * @param format
	 */
	public void setFormat(Format format){
		this.format = format;
	}
	

	/**
	 * Создать параметр 
	 * @param name - имя параметра.
	 * @param execute - имя исполняемой команды (агрегата) соответсвует имени метода в этом классе.
	 */
	public Parametr(String name, String execute){
		if(!agregateExist(execute)) this.value = "";		
	}
	
	/**
	 * Создать параметр с использованием сопутствующих данных 
	 * @param name - имя параметра.
	 * @param execute - имя исполняемой команды (агрегата) соответсвует имени метода в этом классе.
	 * @param connotation - массив сопутствующих данных.
	 */
	public Parametr(String name, String execute, String[] connotation){
		this.name = name;
		this.connotation = connotation;
		if(!agregateExist(execute)) this.value = "";		
	}	
	
	/**
	 * Проверить существование агрегата (вызываемого метода этого класса).
	 * @param execute - незвание агрегата.
	 * @return true - команда выполнена успешно. false - команда не выполнена.
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
	 *Получить текущую дату и установить ее значением value. 
	 */
	private void get_current_date(){
		Date date = new Date();
		this.value = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
	}
	
	/**
	 *Получить порядковый номер файла в директории 
	 *данные о директории передаются в connotation[0] 
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
