package converter;

interface CompainingFieldsAndParametrs {
	
	/**
	 * ���������� ���� � ��������� ������� �������� ������� �������� ����
	 * @param field
	 */
	public void setFieldInLastPosition(Field field);
	
	/**
	 * @return ��������� � ������ ����
	 */
	public Field getLastField();
	
	/**
	 * ���������� �������� ����� SOURCE
	 * @param className
	 * @param fieldName
	 */
	public void setSourceInLastField(String className, String fieldName);

}
