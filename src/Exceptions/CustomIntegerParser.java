package Exceptions;

public class CustomIntegerParser {
	public static int parseInteger(String string) throws InputNumberException {
		try {	
			int number = Integer.parseInt(string);
			if (number < 0) {
				throw new InputNumberException("Caught a negative number", string);
			} 
			return number;
		} catch(NumberFormatException e) {
			throw new InputNumberException("Not a number caught", string);
		} 
		
	}
	
	public static int parseInteger(String string, int maxNumber) throws InputNumberException, OutOfFieldException {
		try {
			int number = Integer.parseInt(string);
			if(number < 0) {
				throw new InputNumberException("Caught a negative number", string);
			} if (number >= maxNumber) {
				throw new OutOfFieldException("Index of cell out of Field bounds", string, maxNumber);
			} 
			
			return number;
		} catch(NumberFormatException e) {
			throw new InputNumberException("Not a number caught", string);
		} 
	}
	
}
