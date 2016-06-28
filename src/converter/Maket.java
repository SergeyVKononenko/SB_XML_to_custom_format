package converter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Кононенко Сергей Владимирович 
 * 
 * Хранит преобразованную модель шаблона вывода.
 * 
 */
public class Maket {
	
	/**
	 * путь для сохранения файла сформированного на основании шаблона  
	 */
	private String outputDirectory;
	
	/**
	 *  имя файла сформированного на основании шаблона
	 */
	private String outputName;
	
	/**
	 *Ссылка на заполненный экземпляр источника 
	 */
	private Source source;
	
	private ArrayList<Command>	commands	= new ArrayList<Command>();
	private ArrayList<Region>	regions		= new ArrayList<Region>();
	//private ArrayList<Rulez>	rulez		= new ArrayList<Rulez>();
	
	private ArrayList<String> outputLines = new ArrayList<String>();
	
	
	
	
	
	
	
	public Maket(File maketFile, Source source) throws ParserConfigurationException, SAXException, IOException{
		this.source = source;
		SAXParserFactory parseFactory = SAXParserFactory.newInstance();
		SAXParser parser = parseFactory.newSAXParser();
		DH handler = new DH();
		parser.parse(maketFile, handler);
		
	}
	
	@Override
	public String toString() {
		System.out.println("output directory: " + outputDirectory);
		System.out.println("output name: " + outputName);
		System.out.println("");
		for(String str:outputLines){
			System.out.println(str);
		}
		
		
		
		
		return super.toString();
	}

	private class DH extends DefaultHandler{
		String levelOne = "";//элемент первого(верхнего) уровня
		String levelTwo = "";//элемент второго уровня (вложенный элемент)
		String anyLevel = "";//имя для проверки при получении данных внутри тэга
		
		Field newField;//создание нового поля
		Parametr newParametr;//создание нового праметра
		Format newFormat;//создание нового формата
		Command newCommand;//создание новой команды
		
		String attrLocalName;//имя итрибута 
		boolean startElement;		
		/**
		 *Элемент первого уровня в который передается новое поле ({@link Field}) или 
		 *извлекается для внесения изменений 
		 */
		CompainingFieldsAndParametrs levelOneObject; 
		
		String[] connotation;// для передачи дополнительных параметров в структуру параметра
		
		
		
		
				
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (startElement) {
				if (anyLevel.equals("df"))
					newFormat.setDf(new String(ch, start, length));
				if (anyLevel.equals("dfs"))
					newFormat.setDfs(new String(ch, start, length));
				if (anyLevel.equals("ip"))
					newFormat.setIp(new String(ch, start, length));
				if (anyLevel.equals("lz"))
					newFormat.setLz(new String(ch, start, length));
				if (anyLevel.equals("ns"))
					newFormat.setNs(new String(ch, start, length));
				if (anyLevel.equals("f"))
					newFormat.setF(new String(ch, start, length));
				if (anyLevel.equals("stringCase"))
					newFormat.setStringCase(new String(ch, start, length));
			}
			super.characters(ch, start, length);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			startElement = true;
			if(qName.equals("commands")|qName.equals("regions")|qName.equals("outputRulez")) levelOne = qName;
			if(qName.equals("fields")|qName.equals("parametrs")|qName.equals("lines")) levelTwo = qName;
			anyLevel = qName;
			
			if(qName.equals("field")){
				newField = new Field(attributes.getValue("name"));
								
				for(int attrIndex = 0;attrIndex<attributes.getLength();attrIndex++){
					attrLocalName = attributes.getLocalName(attrIndex);
					if(attrLocalName.equals("size")) newField.setSize(attributes.getValue(attrIndex));
					if(attrLocalName.equals("supplementSymbol")) newField.setSupplementSymbol(attributes.getValue(attrIndex));
					if(attrLocalName.equals("aligment")) newField.setAligment(Field.AligmentField.getAtStringName(attributes.getValue(attrIndex)));
				}
				
				if(levelOne.equals("commands"))
					levelOneObject = commands.get(commands.size()-1);
				if(levelOne.equals("regions"))
					levelOneObject = regions.get(regions.size()-1);
				
				levelOneObject.setFieldInLastPosition(newField);
			}
			
			if(qName.equals("parametr")){
				if(attributes.getValue("execute").equals("get_file_index_in_output_directory")) 
					connotation = new String[] {outputDirectory};	
				else connotation = new String[]{};
				
				newParametr = new Parametr(attributes.getValue("name"), attributes.getValue("execute"), connotation);
				commands.get(commands.size()-1).addParametr(newParametr);			
				
			}
			
			if(qName.equals("format")){
				newFormat = new Format(Format.Type.getTypeValueNamed(attributes.getValue("type")));
			}
			
			if(qName.equals("command")){
				commands.add(new Command(attributes.getValue("name"),attributes.getValue("value")));
			}
			
			if(qName.equals("region")){
				regions.add(new Region(attributes.getValue("name")));
			}
			
			if(qName.equals("line")){
				Line line = new Line(attributes.getValue("number"), attributes.getValue("value"));
				regions.get(regions.size()-1).setLine(line);
			}
			
			if(qName.equals("source")){
				if(levelOne.equals("commands"))
					levelOneObject = commands.get(commands.size()-1);
				if(levelOne.equals("regions"))
					levelOneObject = regions.get(regions.size()-1);
				
				levelOneObject.setSourceInLastField(attributes.getValue("class"), attributes.getValue("fieldName"));
			}
			
			if(qName.equals("rulez")){
				String regName	= "";
				String lnNum	= "";
				int repeat 		= 1;
				boolean isTable	= false;
				
				for(int index = 0;index<attributes.getLength();index++){
					if(attributes.getLocalName(index).equals("regionName")){
						regName = attributes.getValue(index);
					}
					if(attributes.getLocalName(index).equals("lineNumber")){
						lnNum = attributes.getValue(index);
					}
					if(attributes.getLocalName(index).equals("repeat")){
						repeat = Integer.parseInt(attributes.getValue(index));
					}
					if(attributes.getLocalName(index).equals("table")){
						isTable = attributes.getValue(index).equals("true") ? true:false;
					}
					
				}
				 	
				Region reg = getRegionsOnName(regName);
		 		Line line = reg.getLine(lnNum);
				String val = "";
				
		 		if(isTable){
		 			
		 			
				 	for(int index=0;index<=source.table.size()-1;index++){
				 		val = line.getValueOnFields(source.table.get(index));
				 		outputLines.add(val);
				 	}
				 		
				 }else{
				 	for(;repeat>0;repeat--){

				 		 val = line.getValueOnFields();
				 			
				 		outputLines.add(val);
				 	}
				 }
				
			}
			
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			startElement = false;
			if(qName.equals("source")){
				if(levelOne.equals("commands"))
					levelOneObject = commands.get(commands.size()-1);
				if(levelOne.equals(regions))
					levelOneObject = regions.get(commands.size() - 1);
				
				try {
					levelOneObject.getLastField().CalculateValue(source);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
						| IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
			if(qName.equals("format")){
				if(levelOne.equals("commands")){
					if(levelTwo.equals("fields")) 
						commands.get(commands.size()-1).getLastField().setFormat(newFormat);
					if(levelTwo.equals("parametrs")) 
						commands.get(commands.size()-1).getLastParameter().setFormat(newFormat);
				}
				if(levelOne.equals("regions")){
					regions.get(regions.size()-1).getLastLine().getLastField().setFormat(newFormat);
				}
			}
			
			if(qName.equals("command")){
				newCommand = commands.get(commands.size()-1);
				if(newCommand.getName().equals("output_directory")) 
					outputDirectory = newCommand.getCommandValue();
				if(newCommand.getName().equals("output_name"))
					outputName = newCommand.getCommandValue();				
			}
			
			if(qName.equals("commands")|qName.equals("regions")|qName.equals("outputRulez")) levelOne = "";
			if(qName.equals("fields")|qName.equals("parametrs")|qName.equals("lines")) levelTwo = "";

			
			super.endElement(uri, localName, qName);
		}
		
		
		private Region getRegionsOnName(String regionName){
			Region ret = null;
			for(Region reg:regions){
				if(reg.getName().equals(regionName)){
					ret = reg;
					break;
				}
			}
			
			return ret;
		}
		
	}
}
