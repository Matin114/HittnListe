package examples.encryptor.java.maps;

import java.util.HashMap;

public class CharToIntMap<K, V> extends HashMap<Character, Integer> {

	private static final long serialVersionUID = 89028642588965527L;

	public CharToIntMap() {
		put(' ', 0);
		put('a', 1 );
		put('b', 2 );
		put('c', 3 );
		put('d', 4 );
		put('e', 5 );
		put('f', 6 );
		put('g', 7 );
		put('h', 8 );
		put('i', 9 );
		put('j', 10);
		put('k', 11);
		put('l', 12);
		put('m', 13);
		put('n', 14);
		put('o', 15);
		put('p', 16);
		put('q', 17);
		put('r', 18);
		put('s', 19);
		put('t', 20);
		put('u', 21);
		put('v', 22);
		put('w', 23);
		put('x', 24);
		put('y', 25);
		put('z', 26);
		put('ä', 27);
		put('ö', 28);
		put('ü', 29);
		put('A', 30);
		put('B', 31);
		put('C', 32);
		put('D', 33);
		put('E', 34);
		put('F', 35);
		put('G', 36);
		put('H', 37);
		put('I', 38);
		put('J', 39);
		put('K', 40);
		put('L', 41);
		put('M', 42);
		put('N', 43);
		put('O', 44);
		put('P', 45);
		put('Q', 46);
		put('R', 47);
		put('S', 48);
		put('T', 49);
		put('U', 50);
		put('V', 51);
		put('W', 52);
		put('X', 53);
		put('Y', 54);
		put('Z', 55);
		put('Ä', 56);
		put('Ö', 57);
		put('Ü', 58);
		put('!', 59);
		put('\'', 60);
		put('§', 61);
		put('$', 62);
		put('%', 63);
		put('&', 64);
		put('/', 65);
		put('{', 66);
		put('(', 67);
		put('[', 68);
		put(')', 69);
		put(']', 70);
		put('=', 71);
		put('}', 72);
		put('?', 73);
		put('\\', 74);
		put('@', 75);
		put('€', 76);
		put('+', 77);
		put('*', 78);
		put('~', 79);
		put('#', 80);
		put('<', 81);
		put('>', 82);
		put('|', 83);
		put(',', 84);
		put(';', 85);
		put('.', 86);
		put(':', 87);
		put('-', 88);
		put('_', 89);
		put('0', 90);
		put('1', 91);
		put('2', 92);
		put('3', 93);
		put('4', 94);
		put('5', 95);
		put('6', 96);
		put('7', 97);
		put('8', 98);
		put('9', 99);
		    
	}   
	
	
	public String getKey(int value) {
		for(char key : this.keySet()) {
			if(this.get(key).equals(value)) {
				return String.valueOf(key);
			}
		}
		return null;
	}
	
}           
            
            
            
            
            
            
            
            
            
            