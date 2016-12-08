package redistest.model;

import java.io.Serializable;

public class Model implements Serializable{
	private static final long serialVersionUID = 1L;

	private String workStateCode;

	private String workStateName;
	
	private int index;
	
	
	public Model(String workStateCode, String workStateName, int index) {
		super();
		this.workStateCode = workStateCode;
		this.workStateName = workStateName;
		this.index = index;
	}

	public String getWorkStateCode() {
		return workStateCode;
	}

	public void setWorkStateCode(String workStateCode) {
		this.workStateCode = workStateCode;
	}

	public String getWorkStateName() {
		return workStateName;
	}

	public void setWorkStateName(String workStateName) {
		this.workStateName = workStateName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
