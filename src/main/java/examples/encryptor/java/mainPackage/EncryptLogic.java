package examples.encryptor.java.mainPackage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import examples.encryptor.java.maps.CharToIntMap;
import examples.encryptor.java.maps.EncryptMap;

public class EncryptLogic {

	private BigDecimal divider = new BigDecimal(EncryptMap.CHARACTERS);

	public String encrypt(String password) throws EncryptException {
		CharToIntMap<Character, Integer> charMap = new CharToIntMap<>();

		String intPassword = "";
		int calculationAmount = 2;

		for(char c : password.toCharArray()) { 
			if (charMap.get(c) == null) {
				throw new EncryptException("Cannot handle this character: ", c);
			}
			int intVal = charMap.get(c);
			intPassword += String.format("%02d", intVal);
		}
		calculationAmount += System.currentTimeMillis() % 4;
		System.out.println(calculationAmount + " calcuations");

		return convertToString(calcualtions(new BigDecimal(intPassword),
				calculationAmount));
	}
	
	public String encryptPlus(String password) throws EncryptException {
		String intPassword = "";
		int calculationAmount = getCalculationsAmount(password);

		for(char c : password.toCharArray()) { 
			if ((int) c / 1000 > 1) {
				throw new EncryptException("Cannot handle this character: ", c);
			}
			int intVal = (int) c;
			intPassword += String.format("%04d", intVal);
		}
		System.out.println(calculationAmount + " calcuations");

		String convertedPasswd = convertToString(calcualtions(new BigDecimal(intPassword),
				calculationAmount));
		// here to show the text is converted with big char range
		int randomNo = (int) (Math.random() * convertedPasswd.length()) + 1;
		return convertedPasswd.substring(0, randomNo) + EncryptMap.BIG_NUMBER_RANGE 
				+ convertedPasswd.substring(randomNo);
	}

	private int getCalculationsAmount(String password) {
		int calculationsAmount = 0;
		int minCalc = 2;
		int actualCalc = password.length() / 5;
		if (actualCalc < minCalc) {
			actualCalc = minCalc;
		}
		int minVariation = 4;
		int maxVariation = 15;
		int actualVariation = password.length() / 15;
		if(actualVariation < minVariation) {
			actualVariation = minVariation;
		} else if (actualVariation > maxVariation) {
			actualVariation = maxVariation;
		}
		
		calculationsAmount = actualCalc + (int) (System.currentTimeMillis() % actualVariation);
		
		return calculationsAmount;
	}
	
	private String convertToString(List<BigDecimal> decimalsList) {
		EncryptMap<Integer, String> numberMap = new EncryptMap<>();
		String convertedPassword = "";
		BigDecimal rawNumber = decimalsList.remove(0);
		while (rawNumber.intValue() != 0) {
			BigDecimal[] results = rawNumber.divideAndRemainder(divider);
			convertedPassword = numberMap.get(results[1].intValue()) + convertedPassword;
			rawNumber = results[0];
		}
		return addNumbers(convertedPassword, decimalsList, 
				getStringPoses(convertedPassword.length(), decimalsList.size()));
	}

	private List<Integer> getStringPoses(int stringLenght, int neededAmount) {
		// Credits: Matin (Minesweeper getMineFields)
		List<Integer> stringPoses = new LinkedList<Integer>();
		while(stringPoses.size() < neededAmount) {
			int stringPos = (int)(Math.random() * stringLenght+1);

			if (stringPoses.contains(stringPos)) {
				continue;
			}
			stringLenght++;
			stringPoses.add(stringPos);
		}
		return stringPoses;
	}

	private String addNumbers(String password, List<BigDecimal> decimalsList,
			List<Integer> stringPoses) {
		String doneString = "";
		char[] passwordArray = password.toCharArray();
		Collections.sort(stringPoses);
		int usedStringPos = 0;
		int usedPasswordIndex = 0;
		for (int i = 0; i < passwordArray.length + stringPoses.size(); i++) {
			if(stringPoses.get(usedStringPos) == i) {
				doneString += decimalsList.get(usedStringPos).intValue();
				if(stringPoses.size()-1 > usedStringPos) {
					usedStringPos++;
				}
			} else {
				doneString += passwordArray[usedPasswordIndex];
				usedPasswordIndex++;
			}
		}
		
		return doneString;
	}

	private List<BigDecimal> calcualtions(BigDecimal intPassword, int amount) {
		List<BigDecimal> decimalsList = new LinkedList<>();
		for(int i = 0; i < amount; i++) {
			if(System.currentTimeMillis() % 2 == 0) {
				BigDecimal multiplier = getRandomNumber();
				intPassword = intPassword.multiply(multiplier);
				decimalsList.add(multiplier);
				if(i != amount-1) {
					BigDecimal noGreaterMultiplier = new BigDecimal((int) (
							Math.random() * (10 - multiplier.intValue())));
					noGreaterMultiplier = noGreaterMultiplier.add(multiplier);
					decimalsList.add(noGreaterMultiplier);
				}

			} else {
				BigDecimal divider = getRandomNumber();
				BigDecimal[] result = intPassword.divideAndRemainder(divider);
				if(result[0].equals(new BigDecimal(0))) {
					i--;
					continue;
				}
				intPassword = result[0];
				decimalsList.add(divider);
				decimalsList.add(result[1]);
			}
			try {
				Thread.sleep((int) (Math.random()*10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

		decimalsList.add(0, intPassword);
		return decimalsList;
	}

	private BigDecimal getRandomNumber() {
		BigDecimal randomNo = new BigDecimal((int) (Math.random() * 9));
		randomNo = randomNo.add(new BigDecimal(1));
		return randomNo;
	}

}
