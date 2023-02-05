package examples.encryptor.java.mainPackage;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import examples.encryptor.java.maps.CharToIntMap;
import examples.encryptor.java.maps.EncryptMap;

public class DecryptLogic {

	EncryptMap<Integer, String> encryptMap;

	public String decrypt(String input) throws EncryptException {
		encryptMap = new EncryptMap<>();

		boolean bigNumberRange = false;
		if (input.contains(EncryptMap.BIG_NUMBER_RANGE)) {
			input = input.replace(EncryptMap.BIG_NUMBER_RANGE, "");
			bigNumberRange = true;
		}

		String encryptedPasswd = input.replaceAll("[0-9]", "");

		Pattern patternNumbersOnly = Pattern.compile("[^0-9]");
		String numbersOnly = patternNumbersOnly.matcher(input).replaceAll("");

		BigDecimal decimalPasswd = passwdToDecimal(encryptedPasswd.toCharArray());

		return converToPassword(decimalPasswd, numbersOnly, bigNumberRange);
	}

	private String converToPassword(BigDecimal decimalPasswd, String numbersOnly, 
			boolean bigNumberRange) throws EncryptException{
		List<Integer> numbersList = new LinkedList<>();
		//convert the string to List<int>
		for (char c : numbersOnly.toCharArray()) {
			numbersList.add(Integer.valueOf(String.valueOf(c)));
		}

		for(int i = numbersList.size()-2; i >= 0; i -= 2) {
			// Since the last digit won't be written if 
			// multiplied while encrypting, move one index foreward
			if (i % 2 == 1) {
				decimalPasswd = decimalPasswd.divide(new BigDecimal(numbersList.get(i+1)));
				i++;
				continue;
			}
			// find out if multiplied or divided
			if(numbersList.get(i) <= numbersList.get(i+1)) {
				try {
					decimalPasswd = decimalPasswd.divide(new BigDecimal(numbersList.get(i)));
					// here to check if the given text really was convertable
					if(!decimalPasswd.remainder(new BigDecimal(1)).equals(new BigDecimal(0))) {
						throw new ArithmeticException();
					}
				} catch (ArithmeticException e) {
					throw new EncryptException("Cannot handle the given text");
				}
			} else {
				decimalPasswd = decimalPasswd.multiply(new BigDecimal(numbersList.get(i)));
				decimalPasswd = decimalPasswd.add(new BigDecimal(numbersList.get(i+1)));
			}
		}

		return convertBigDecimalToString(decimalPasswd.toString(), bigNumberRange);
	}

	private String convertBigDecimalToString(String decimalPasswd, boolean bigNumberRange) {
		CharToIntMap<Character, Integer> charMap = new CharToIntMap<>();
		String convertedPasswd = "";
		List<Integer> valueList = new LinkedList<>();
		if (bigNumberRange) {
			// fill first characters with 0

			if(decimalPasswd.length() % 4 != 0) {
				for (int i = 0; i < decimalPasswd.length() % 4; i++) {
					decimalPasswd = "0" + decimalPasswd;
				}
			}
			
			for (int i = 0; i < decimalPasswd.length(); i += 4) {
				valueList.add(Integer.valueOf(decimalPasswd.substring(i, i+4)));
			}
			
			for (int value : valueList) {
				convertedPasswd += (char) value;
			}
			
		} else {
			// fill first character with 0
			if(decimalPasswd.length() % 2 == 1) {
				decimalPasswd = "0" + decimalPasswd;
			}
			for (int i = 0; i < decimalPasswd.length(); i += 2) {
				valueList.add(Integer.valueOf(decimalPasswd.substring(i, i+2)));
			}

			for (int value : valueList) {
				convertedPasswd += charMap.getKey(value);
			}
		}
		return convertedPasswd;
	}

	private BigDecimal passwdToDecimal(char[] encryptedPasswd) {
		BigDecimal decimalPasswd = new BigDecimal(0);
		int toThePowerOf = encryptedPasswd.length-1;
		for(int i = 0; i < encryptedPasswd.length; i++) {
			decimalPasswd = decimalPasswd.add(
					powerOf(toThePowerOf).multiply(new BigDecimal(encryptMap.getKey(encryptedPasswd[i]))));
			toThePowerOf--;
		}
		return decimalPasswd;
	}

	private BigDecimal powerOf(int toThePowerOf) {
		BigDecimal multiplier = new BigDecimal(EncryptMap.CHARACTERS);
		BigDecimal realBigBoy = new BigDecimal(1);
		if(toThePowerOf != 0) {
			for(int i = 0; i < toThePowerOf; i++) {
				realBigBoy = realBigBoy.multiply(multiplier);
			}
		}

		return realBigBoy;
	}

}
