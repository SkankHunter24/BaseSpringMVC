package base;

public class RollBackException extends RuntimeException{
	private static final long serialVersionUID = 1L; 
	public RollBackException(String message){
		super(message);
	}
}
