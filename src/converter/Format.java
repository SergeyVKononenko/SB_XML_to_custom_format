package converter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Format {
	/**
	 * тип форматированных данных 
	 */
	private Type type;
//*************************************************************************************	
	/**
	 * <b><i>Вид форматирования даты</i> - точки, тире и прочие разделители игнорируются.</b><br>
	 * <b>mm</b>	- месяц число;<br>
	 * <b>mmmm</b>	- месяц прописью;<br>
	 * <b>Mmmm</b>	- месяц прописью первая буква заглавная;<br>
	 * <b>MMMM</b>	- месяц прописью все буквы верхнего регистра;<br>
	 * <b>dd</b>	- номер дня;<br>
	 * <b>yy</b>	- год 2 последние цифры;<br> 
	 * <b>yyyy</b>	- год полностью;<br>
	 */
	private String df;
	
	/**
	 * <b>Разделитель частей даты. Может быть любым символом</b>
	 */
	private String dfs;
	
//*************************************************************************************	
	/**
	 * integer part - целая часть 
	 */
	private String ip;
	
	/**
	 * fraction - дробная часть 
	 */
	private String f;
	
	/**
	 * leader zero - выводить лидирующие нули; 
	 */
	private boolean lz;
	
	/**
	 * разделитель дробной части 
	 */
	private String ns;
//**************************************************************************************
	
	/**
	 * правило вывода символов 
	 */
	private StringCase stringCase;
//**************************************************************************************	
	/**
	 * отформатированное значение поля 
	 */
	private String value;
	
//**************************************************************************************	
	
	/**
	 * <b><i>Конструктор формата по умолчанию. Настроен на форматирование строки.</i></b><br>
	 *  Первая буква - заглавная.
	 */
	public Format(){
		this.type = Type.string;
		stringDefaultInit();		
	}
	
	
	/**
	 * Создает объект для форматирования стандартными настройками формата выбранного типа.
	 * @param type {@link Type}:
	 * 	<i><b>Type.number :</b></i><br> 		
	 * 	ip = "15" 	- количество цифр целой части.<br>
	 *	f = "0"		- количество цифр дробной части.<br>
	 *	ns = ".";	- разделитель.<br>
	 *	lz = false; - выводить лидирующие нули в пределах количества цифр целой части.<br>
	 *<i><b>Type.string:</b></i><br>
	 * {@link StringCase} stringCase - описание форматирования строки: строчные, прописные и т.д.<br>
	 * <i><b>Type.data:</b></i><br>
	 * df = "ddmmyyyy" - формат следования частей даты {@link #df}.<br>
	 * dfs= "." - разделитель частей даты при выводе {@link #dfs}.
	 */
	public Format(Type type){
		this.type = type;
		
		if(type == Type.string){
			stringDefaultInit();
		}
		if(type==Type.data){
			dataDefaultInit();
		}
		if(type==Type.number){
			numberDefaultInit();
		}
		
	}

	/**
	 * <b>Создать объект форматирования типа string.</b>
	 * 
	 * @param stringCase{@link StringCase}
	 * 
	 */
	public Format(StringCase stringCase){
		this.type = Type.string;
		this.stringCase = stringCase;
	}
	

	/**
	 * <b>Создать объект форматирования типа number.</b>
	 * @param ip {@link #ip} - размерность целой части. 
	 * @param f {@link #f} - размерность дробной части.
	 * @param ns {@link #ns} - разделитель дробной части.
	 * @param lz {@link #lz} - выводить лидирующие нули.
	 */
	public Format(String ip, String f, String ns, boolean lz){
		this.type = Type.number;
		this.ip = ip;
		this.f = f;
		this.ns = ns;
		this.lz = lz;
	}
	
	/**
	 * <b>Создать объект форматирования типа data.</b>
	 * @param df{@link #df}
	 * @param dfs{@link #dfs}
	 */
	public Format(String df, String dfs){
		this.type = Type.data;
		this.df = df;
		this.dfs= dfs;	
	}
	
 	private void numberDefaultInit(){
		this.ip = "15";
		this.f = "0";
		this.ns = ".";
		this.lz = false;
	}
	
	private void dataDefaultInit(){
		this.df = "ddmmyyyy";
		this.dfs= ".";
	}
	
	private void stringDefaultInit(){
		this.stringCase = StringCase.UpCase;
	}
	
	public void setDf(String value){
		this.df = value;
	}
	public void setDfs(String value){
		this.dfs = value;
	}
	
	public void setIp(String value){
		this.ip = value;
	}
	public void setLz(String value){
		this.lz = value=="true" ? true:false;
	}
	public void setF(String value){
		this.f = value;
	}
	public void setNs(String value){
		this.ns = value;
	}
	
	public void setStringCase(String value){
		this.stringCase = Format.StringCase.getValueOnName(value) ;
	}
	
	
	/**
	 * Форматировать значение value в соответствии с настройками форматирования.
	 * 
	 *  @param value - значение которое нужно отформатировать.
	 * 	Type.data - значение передается в виде - dd.mm.yyyy.
	 * 
	 *  @return инициализирует поле value отформатированным значением.
	 */
	public void formatingValue(String value){
		if(value.equals("")) value=" ";
		
		if(this.type==Type.number){
			
			String separator = "";
			int sepIndex = 0;
			String emptyBuffer = "";
			String integerPart = "";
			String fraction = "";
			char leadZerro = ' ';
			
			String local = "";
			
			if(value.indexOf(".") != -1) separator = ".";
			if(value.indexOf(",") != -1) separator = ",";
			if(value.indexOf("-") != -1) separator = "-";
				
			if(separator.equals("")){
				integerPart = value;
				fraction = "00";
			}else{
				sepIndex = value.indexOf(separator);
				integerPart = value.substring(0, sepIndex);
				fraction = value.substring(sepIndex+1);
			}
			
			if(this.lz) leadZerro = '0';
			
			emptyBuffer = CreateEmtyStringBuffer(leadZerro, Integer.valueOf(this.ip));
			local = emptyBuffer.concat(integerPart);
			integerPart = local.substring(local.length()- Integer.valueOf(this.ip), local.length());
			
			emptyBuffer = CreateEmtyStringBuffer('0', Integer.valueOf(this.f));
			local = fraction.concat(emptyBuffer);
			fraction = local.substring(0, Integer.valueOf(this.f));
			
			if(this.f.equals("")|this.f.equals("0"))
				this.value = integerPart.trim();
			else
				this.value = integerPart.trim().concat(this.ns).concat(fraction);
		}
	
		if(this.type == Type.string){
			if(this.stringCase == StringCase.LowCase){
				this.value = value.toLowerCase();
			}
			
			if(this.stringCase == StringCase.CapsCase){
				this.value = value.toUpperCase();
			}
			
			if(this.stringCase == StringCase.UpCase){
				this.value = value.replaceFirst(value, value.substring(0, 1).toUpperCase()).concat(value.substring(1));
			}
		}
		
		if(this.type == Type.data){
			/**
			 * value в виде даты dd.mm.yyyy
			 */
			if(value.length()!=10) value = "01.01.0001";
			
			String key;
			
			this.value = "";
			
			String year 	= value.substring(6,10);
			String month	= value.substring(3,5);
			String day		= value.substring(0,2);
			String shortYear= value.substring(8);
			
			String[] monthInWords = {"январь","февраль","март","апрель","май","июнь","июль","август","сентябрь","октябрь","ноябрь","декабр"};
			int indexMonth = 0;
			String addMonth = "";
			
			Map<String,Integer> formatOut	= restrictDataString(this.df);
			
			for(Map.Entry<String, Integer> entry:formatOut.entrySet()){
				key = entry.getKey();
				if(key.equals("mm")){
					this.value = this.value.concat(month).concat(this.dfs);
				}
				if(key.equals("mmmm")|key.equals("Mmmm")|key.equals("MMMM")){
					indexMonth = Integer.valueOf(month) - 1;
					if(key.equals("mmmm"))
						addMonth = monthInWords[indexMonth];
					if(key.equals("MMMM"))
						addMonth = monthInWords[indexMonth].toUpperCase();
					if(key.equals("Mmmm"))
						addMonth = monthInWords[indexMonth].substring(0, 1).toUpperCase().concat(monthInWords[indexMonth].substring(2));
					this.value = this.value.concat(addMonth).concat(this.dfs);
				}
				if(key.equals("yyyy"))
					this.value = this.value.concat(year).concat(this.dfs);
				if(key.equals("yy"))
					
					this.value = this.value.concat(shortYear).concat(this.dfs);
				if(key.equals("dd"))
					this.value = this.value.concat(day).concat(this.dfs);
			}
			
			this.value = this.value.substring(0, this.value.length()-1);

			
		}
	}

		
	private Map<String, Integer> restrictDataString(String FormatValue){ 
		
		final HashMap<String,Integer> dateParticle = new HashMap<>();
		String[] datePartArray = {"mmmm","Mmmm","MMMM","yyyy","mm","dd","yy"};
				
		final Map<String, Integer> sortedMap = new TreeMap<>(new Comparator<Object>() {
	
			private int result;
			
			@Override
			public int compare(Object o1, Object o2) {
				result = (int) dateParticle.get(o1).compareTo((int)dateParticle.get(o2));
				return result;
			}
		});
		
		boolean longm = false;
		boolean longy = false;
		String datePart = "";
		
		Integer indexOf = 0; 
		for(int index = 0;index<datePartArray.length;index++){
			
			indexOf = FormatValue.indexOf(datePartArray[index]);
			datePart = datePartArray[index];
			
			if(indexOf != -1){
				if(datePart.equals(datePartArray[0])|datePart.equals(datePartArray[1])|datePart.equals(datePartArray[2])) longm = true;
				if(datePart.equals(datePartArray[4]) && longm==true) continue;
				
				if(datePart.equals(datePartArray[3])) longy = true;
				if(datePart.equals(datePartArray[6])&& longy == true) continue;
				
				dateParticle.put(datePartArray[index], indexOf);
			}
			
		}
		sortedMap.putAll(dateParticle);

		
		 
		return sortedMap;
	}
	
	
	private String CreateEmtyStringBuffer(char symbol, int length){
		char[] charArray = new char[length];
		for(int index = 0;index<length;index++){
			charArray[index]=symbol;
		}
		return new String(charArray);
	}
	
	
	
	
	@Override
	public String toString() {
		System.out.println(this.value);
		return super.toString();
	}


	public String getValue(){
		return this.value;
	}


	/**
	 * Тип данных для форматирования:
	 * число, строка, дата
	 */
	public enum Type{
		number, string, data;
		
		public static Type getTypeValueNamed(String typeName){
			Type typ = Type.string;
			if(typeName.equals("number")) typ = Type.number;
			if(typeName.equals("data")) typ =Type.data;
			
			return typ;
		}
	}
	
	/**
	 *<b>Описание форматирования параемтра типа стринг</b><br>
	 *UpCase - первая буква заглавная.<br>
	 *LowCase - все буквы строчные.<br>
	 *CapsCase - все буквы прописные.	 *
	 */
	public enum StringCase{
		UpCase, LowCase, CapsCase;
		
		public static StringCase getValueOnName(String stringCase){
			StringCase ret=StringCase.LowCase;
			if(stringCase.equals("UpCase")) ret=StringCase.UpCase;
			if(stringCase.equals("CapsCase")) ret=StringCase.CapsCase;
			return ret;
		}
	}
	
}
