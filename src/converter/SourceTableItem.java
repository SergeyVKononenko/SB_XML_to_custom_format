package converter;

import java.lang.reflect.Field;

/**
 * @author Кононенко Сергей Владимирович
 *
 */
public class SourceTableItem {
	/**
	 * Фамилия
	 */
	private String surname;
	
	/**
	 * Имя
	 */
	private String name;
	
	/**
	 * Отчество
	 */
	private String patronym;
	
	/**
	 * лицевой счет
	 */
	private String persAccount;
	
	/**
	 * сумма зачисления
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
				System.out.print(" пусто ");
				e.printStackTrace();
			}
		}
		System.out.println("");
		return super.toString();
	}
	
	
	
}
