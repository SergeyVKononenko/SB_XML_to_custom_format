package converter;

import java.lang.reflect.Field;

/**
 * @author ��������� ������ ������������
 *
 */
public class SourceTableItem {
	/**
	 * �������
	 */
	private String surname;
	
	/**
	 * ���
	 */
	private String name;
	
	/**
	 * ��������
	 */
	private String patronym;
	
	/**
	 * ������� ����
	 */
	private String persAccount;
	
	/**
	 * ����� ����������
	 */
	private String summa;

	private String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String getPatronym() {
		return patronym;
	}

	public void setPatronym(String patronym) {
		this.patronym = patronym;
	}

	private String getPersAccount() {
		return persAccount;
	}

	public void setPersAccount(String persAccount) {
		this.persAccount = persAccount;
	}

	private String getSumma() {
		return summa;
	}

	public void setSumma(String summa) {
		this.summa = summa;
	}

	@Override
	public String toString() {
		for(Field field:this.getClass().getDeclaredFields()){
			field.setAccessible(true);
			try {
				System.out.print((String)field.get(this)+" ");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.out.print(" ����� ");
				e.printStackTrace();
			}
		}
		System.out.println("");
		return super.toString();
	}
	
	
	
}
