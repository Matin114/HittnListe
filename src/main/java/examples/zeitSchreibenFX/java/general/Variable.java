package examples.zeitSchreibenFX.java.general;

import java.util.List;

public class Variable {
	
	private String key;
	private String value;
	
	public Variable(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public static Variable getVariableByKey (List<Variable> variablesList, String varKey) {
		if (variablesList == null) {
			return null;
		}
		for (Variable var : variablesList) {
			if(var.getKey().equals(varKey)) {
				return var;
			}
		}
		return null;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) {
//			return false;
//		}
//		if (!(obj instanceof Variable))  {
//			return false;
//		}
//		
//		Variable varObj = (Variable) obj;
//		return varObj.getKey().equals(key);
//	}
	
}
