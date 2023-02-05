package examples.encryptor.java.maps;

import java.util.HashMap;

public class EncryptMap<K, V> extends HashMap<Integer, String> {

	private static final long serialVersionUID = 89028642588965527L;
	public static final int CHARACTERS= 70;
	public static final String BIG_NUMBER_RANGE = "\\";
	
	public EncryptMap() {
		put(0, "B");
		put(1, "e");
		put(2, "V");
		put(3, "q");
		put(4, "U");
		put(5, "v");
		put(6, "T");
		put(7, "t");
		put(8, "ö");
		put(9, "g");
		put(10, "$");
		put(11, "s");
		put(12, "ä");
		put(13, ">");
		put(14, "O");
		put(15, "<");
		put(16, "J");
		put(17, "%");
		put(18, "Q");
		put(19, "I");
		put(20, "Ä");
		put(21, "h");
		put(22, "Z");
		put(23, "W");
		put(24, "p");
		put(25, "r");
		put(26, "M");
		put(27, "Y");
		put(28, "/");
		put(29, "c");
		put(30, "P");
		put(31, "l");
		put(32, "X");
		put(33, "?");
		put(34, "+");
		put(35, "D");
		put(36, "S");
		put(37, "!");
		put(38, "A");
		put(39, "#");
		put(40, "y");
		put(41, "C");
		put(42, "z");
		put(43, "j");
		put(44, "k");
		put(45, "d");
		put(46, "x");
		put(47, "L");
		put(48, "i");
		put(49, "w");
		put(50, "f");
		put(51, "Ü");
		put(52, "G");
		put(53, "F");
		put(54, "R");
		put(55, "o");
		put(56, "a");
		put(57, "(");
		put(58, "m");
		put(59, ")");
		put(60, "K");
		put(61, "Ö");
		put(62, "u");
		put(63, "-");
		put(64, "H");
		put(65, "ü");
		put(66, "E");
		put(67, "N");
		put(68, "b");
		put(69, "n");
	}   

	public int getKey(char value) {
		for(int key : this.keySet()) {
			if(this.get(key).equals(String.valueOf(value))) {
				return key;
			}
		}
		return -1;
	}
}
