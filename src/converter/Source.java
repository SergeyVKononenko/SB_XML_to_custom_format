package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.JPopupMenu.Separator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 
 * @author Кононенко Сергей Владимирович
 *
 *         Описывает действия над преобразуемым XML файлом
 *
 */
public class Source {
		
	private void setCount(String count) {
		this.count = count;
	}


	private void setTotal(String total) {
		this.total = total;
	}


	private void setAgreementNumber(String agreementNumber) {
		this.agreementNumber = agreementNumber;
	}


	private void setAgreementData(String agreementData) {
		this.agreementData = agreementData;
	}


	private void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}


	private void setTaxPayerIdentNum(String taxPayerIdentNum) {
		this.taxPayerIdentNum = taxPayerIdentNum;
	}


	private void setCurAccountOrg(String curAccountOrg) {
		this.curAccountOrg = curAccountOrg;
	}


	private void setBankIdentCode(String bankIdentCode) {
		this.bankIdentCode = bankIdentCode;
	}


	private void setRegistryNumber(String registryNumber) {
		this.registryNumber = registryNumber;
	}


	private void setRegistryDate(String registryDate) {
		this.registryDate = registryDate;
	}


	private void setBranchBank(String BranchBank) {
		this.branchBank = BranchBank;
	}


	private void setBranchBankBranch(String BranchBankBranch) {
		this.branchBankBranch = BranchBankBranch;
	}

	private String getBranchBank() {
		return branchBank;
	}


	private String getBranchBankBranch() {
		return branchBankBranch;
	}
	
	public String getFieldValue(String fieldName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field field = this.getClass().getDeclaredField(fieldName);
		return (String) field.get(this);
	}
	
	
	/**
	 * количество записей в списке на зачисление;
	 */
	private String count = "";
	/**
	 * сумма итого;
	 */
	private String total = "";
	
	/**
	 * номер договора;
	 */
	private String agreementNumber = "";
	
	/**
	 * дата договора;
	 */
	private String agreementData = "";
	
	/**
	 * наименование оргаизации;
	 */
	private String organizationName = "";
	
	/**
	 * ИНН организации;
	 */
	private String taxPayerIdentNum = "";
	
	/**
	 * расчетный счет организации;
	 */
	private String curAccountOrg = "";
	
	/**
	 * БИК банка;
	 */
	private String bankIdentCode = "";
	
	/**
	 * номер реестра;
	 */
	private String registryNumber = "";
	
	/**
	 * дата реестра;
	 */
	private String registryDate = "";
	
	/**
	 * отделение банка;
	 */
	private String branchBank = "";
	
	/**
	 * филиал отделения банка;
	 */
	private String branchBankBranch = "";
	
	/**
	 * табличная часть
	 */
	public ArrayList<SourceTableItem> table ;
	
	/**
	 * ссылка на загружаемый файл xml
	 */
	private File sourceFile;
	
	private boolean CompanyNumberCompleted = false;
	
	private boolean isCompanyNumberCompleted() {
		return CompanyNumberCompleted;
	}

	private void setCompanyNumberCompleted(boolean companyNumberCompleted) {
		this.CompanyNumberCompleted = companyNumberCompleted;
	}

	public Source(String path, String filename){
		table = new ArrayList<SourceTableItem>();
		
		try {
			sourceFile = readSourceFile(path, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			parseFile(sourceFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}

	
	private File readSourceFile(String path, String filename) throws IOException{
	/**
	 * Получить ссылку на файл источник.
	 * @param path		-	String путь к папке. 
	 * @param filename	-	String имя файла включая расширение (.xml).
	 * @throws IOException если файл не существует.
	 * 
	 * @return ссылка на файл.
	 */	
		
		File returnValue;
		String sSeparator = "\\";
		String rightSymbol2 = path.substring(path.length()-1);
		
		if(!rightSymbol2.equals(sSeparator)){
			path = path.concat(sSeparator);
		}
		
		String fullFileName = path.concat(filename);
		
		returnValue = new File(fullFileName);
		if(!returnValue.exists()){
			throw new IOException();
		}
		
		return returnValue;
	}

	
	private void parseFile(File sourceFile) throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ParseInst instParse = new ParseInst();
		parser.parse(sourceFile, instParse);
	}
	
	public class ParseInst extends DefaultHandler{
		private String thisElement;
		private boolean isStartElement = false;
		private String value;
		
		private String dataConverter(String strData) {
			return strData.replaceAll("-", ".");
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			isStartElement = true;
			thisElement = qName;

			if (qName.equals("Сотрудник")) {
				SourceTableItem item = new SourceTableItem();
				table.add(item);
			}

			if (qName.equals("СчетаПК")) {
				for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
					String attrName = attributes.getQName(attrIndex);
					String attrValue = attributes.getValue(attrIndex);

					if (attrName.equals("НомерДоговора")) {
						setAgreementNumber(attrValue);;
					}
					if (attrName.equals("ДатаДоговора")) {
						setAgreementData(dataConverter(attrValue));
					}
					if (attrName.equals("НаименованиеОрганизации")) {
						setOrganizationName(attrValue);
					}
					if (attrName.equals("ИНН")) {
						setTaxPayerIdentNum(attrValue);
					}
					if (attrName.equals("РасчетныйСчетОрганизации")) {
						setCurAccountOrg(attrValue);
					}
					if (attrName.equals("БИК")) {
						setBankIdentCode(attrValue);
					}
					if (attrName.equals("НомерРеестра")) {
						setRegistryNumber(attrValue);
					}
					if (attrName.equals("ДатаРеестра")) {
						setRegistryDate(dataConverter(attrValue));
					}

				}
			}

			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			isStartElement = false;
			super.endElement(uri, localName, qName);
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			value = new String(ch, start, length);
			if (isStartElement) {
				if (thisElement.equals("Фамилия")) {
					table.get(table.size() - 1).setSurname(value);
				}
				if (thisElement.equals("Имя")) {
					table.get(table.size() - 1).setName(value);
				}
				if (thisElement.equals("Отчество")) {
					table.get(table.size() - 1).setPatronym(value);
				}
				if (thisElement.equals("ЛицевойСчет")) {
					table.get(table.size() - 1).setPersAccount(value);
				}
				if (thisElement.equals("Сумма")) {
					table.get(table.size() - 1).setSumma(value);
				}

				if (thisElement.equals("КоличествоЗаписей")) {
					setCount(value);
				}
				if (thisElement.equals("СуммаИтого")) {
					setTotal(value);
				}

				if (!isCompanyNumberCompleted()) {
					if (thisElement.equals("ОтделениеБанка")) {
						setBranchBank(value);
					}

					if (thisElement.equals("ФилиалОтделенияБанка")) {
						setBranchBankBranch(value);
					}

					if ((!getBranchBank().equals("")) && (!getBranchBankBranch().equals(""))) {
						setCompanyNumberCompleted(true);
					}
				}
			}
			super.characters(ch, start, length);
		}		
	}

	
	@Override
	public String toString() {
		String value = "";
		for(Field field:this.getClass().getDeclaredFields()){
			field.setAccessible(true);
			try {
				if(field.getType().getSimpleName().equals("ArrayList")){
					System.out.println("Значения таблицы:");
					for(SourceTableItem item:table){
						item.toString();
					}
					System.out.println("");
					value = "";
				}else {
					if(field.getType().getSimpleName().equals("String")) value = (String) field.get(this);
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				value = " ошибка ";
				e.printStackTrace();
			}
			
			System.out.println("поле: " + field.getName() + " значение " + value.toString());
			
		}
		return super.toString();
	}
	
	
}	

