package examples.encryptor.java.mainPackage;

public class EncryptException extends Exception {

	private static final long serialVersionUID = 89028642588965527L;
	
	private String message;
	private char problem;
	
	public EncryptException(String message) {
		this.message = message;
	}
	
	public EncryptException(String message, char problem) {
		this.message = message;
		this.problem = problem;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public char getProblem() {
		return problem;
	}
	public void setProblem(char problem) {
		this.problem = problem;
	}
	
	
}
