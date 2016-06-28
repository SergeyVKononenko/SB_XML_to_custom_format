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
 * @author ��������� ������ ������������
 *
 *         ��������� �������� ��� ������������� XML ������
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
	 * ���������� ������� � ������ �� ����������;
	 */
	private String count = "";
	/**
	 * ����� �����;
	 */
	private String total = "";
	
	/**
	 * ����� ��������;
	 */
	private String agreementNumber = "";
	
	/**
	 * ���� ��������;
	 */
	private String agreementData = "";
	
	/**
	 * ������������ ����������;
	 */
	private String organizationName = "";
	
	/**
	 * ��� �����������;
	 */
	private String taxPayerIdentNum = "";
	
	/**
	 * ��������� ���� �����������;
	 */
	private String curAccountOrg = "";
	
	/**
	 * ��� �����;
	 */
	private String bankIdentCode = "";
	
	/**
	 * ����� �������;
	 */
	private String registryNumber = "";
	
	/**
	 * ���� �������;
	 */
	private String registryDate = "";
	
	/**
	 * ��������� �����;
	 */
	private String branchBank = "";
	
	/**
	 * ������ ��������� �����;
	 */
	private String branchBankBranch = "";
	
	/**
	 * ��������� �����
	 */
	public ArrayList<SourceTableItem> table ;
	
	/**
	 * ������ �� ����������� ���� xml
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
	 * �������� ������ �� ���� ��������.
	 * @param path		-	String ���� � �����. 
	 * @param filename	-	String ��� ����� ������� ���������� (.xml).
	 * @throws IOException ���� ���� �� ����������.
	 * 
	 * @return ������ �� ����.
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

			if (qName.equals("���������")) {
				SourceTableItem item = new SourceTableItem();
				table.add(item);
			}

			if (qName.equals("�������")) {
				for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
					String attrName = attributes.getQName(attrIndex);
					String attrValue = attributes.getValue(attrIndex);

					if (attrName.equals("�������������")) {
						setAgreementNumber(attrValue);;
					}
					if (attrName.equals("������������")) {
						setAgreementData(dataConverter(attrValue));
					}
					if (attrName.equals("�����������������������")) {
						setOrganizationName(attrValue);
					}
					if (attrName.equals("���")) {
						setTaxPayerIdentNum(attrValue);
					}
					if (attrName.equals("������������������������")) {
						setCurAccountOrg(attrValue);
					}
					if (attrName.equals("���")) {
						setBankIdentCode(attrValue);
					}
					if (attrName.equals("������������")) {
						setRegistryNumber(attrValue);
					}
					if (attrName.equals("�����������")) {
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
				if (thisElement.equals("�������")) {
					table.get(table.size() - 1).setSurname(value);
				}
				if (thisElement.equals("���")) {
					table.get(table.size() - 1).setName(value);
				}
				if (thisElement.equals("��������")) {
					table.get(table.size() - 1).setPatronym(value);
				}
				if (thisElement.equals("�����������")) {
					table.get(table.size() - 1).setPersAccount(value);
				}
				if (thisElement.equals("�����")) {
					table.get(table.size() - 1).setSumma(value);
				}

				if (thisElement.equals("�����������������")) {
					setCount(value);
				}
				if (thisElement.equals("����������")) {
					setTotal(value);
				}

				if (!isCompanyNumberCompleted()) {
					if (thisElement.equals("��������������")) {
						setBranchBank(value);
					}

					if (thisElement.equals("��������������������")) {
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
					System.out.println("�������� �������:");
					for(SourceTableItem item:table){
						item.toString();
					}
					System.out.println("");
					value = "";
				}else {
					if(field.getType().getSimpleName().equals("String")) value = (String) field.get(this);
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				value = " ������ ";
				e.printStackTrace();
			}
			
			System.out.println("����: " + field.getName() + " �������� " + value.toString());
			
		}
		return super.toString();
	}
	
	
}	

