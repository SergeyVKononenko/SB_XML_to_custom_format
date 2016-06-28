package converter;

interface CompainingFieldsAndParametrs {
	
	/**
	 * Установить поле в последнюю позицию элемента который содержит поля
	 * @param field
	 */
	public void setFieldInLastPosition(Field field);
	
	/**
	 * @return последнее в списке поле
	 */
	public Field getLastField();
	
	/**
	 * Установить значение полей SOURCE
	 * @param className
	 * @param fieldName
	 */
	public void setSourceInLastField(String className, String fieldName);

}
