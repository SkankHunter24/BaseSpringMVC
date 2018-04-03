package utils;

import java.util.ArrayList;
import java.util.List;

public class CSelect {
	String id;
	String voName;
	String voPackage;
	String textField;
	String valueField;
	List list = null;
	List<String> control = new ArrayList();

	public String getControlStr()
	{
		String str = "";
		for(String s : control)
		{
			str = str + s;
		}
		return str;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public String getVoName() {
		return voName;
	}
	public void setVoName(String voName) {
		this.voName = voName;
	}
	public List<String> getControl() {
		return control;
	}
	public void setControl(List<String> control) {
		this.control = control;
	}

	public String getVoPackage() {
		return voPackage;
	}

	public void setVoPackage(String voPackage) {
		this.voPackage = voPackage;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getValueField() {
		return valueField;
	}

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
}
