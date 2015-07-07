import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

class BrainFuck {

	static HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();
	static String wholecode = "";
	
	static void initMap() {
		
		Stack<Integer> stack = new Stack<Integer>();
		for(int i = 0; i < wholecode.length(); i++) {
			if(wholecode.charAt(i) == '[')
				stack.push(i);
			
			else if(wholecode.charAt(i) == ']') {
				if(stack.empty()) {
					System.out.println("Unexpected ']' bracket");
					System.exit(1);
				}
				int j = stack.pop();
				hmap.put(i, j);
				hmap.put(j, i);
			}
		}
		
		if(!stack.empty()) {
			System.out.println("']' bracket expected");
			System.exit(1);
		}
	}

	public static void main(String ar[]) throws Exception {
	
		String type = "char";
		int size = 10, index = 0;
		String input;
		int[] array;
		
		ArrayList<String> code = new ArrayList<String>();
		Scanner terminal;
		
		for(int i = 0; i < ar.length; i = i + 2) {
			if(ar[i].equals("--size")) {
				size = Integer.parseInt(ar[i+1]);
				if(size > 1024) {
					System.out.println("Array size is too long.");
					System.exit(1);
				}
			}
			
			else if(ar[i].equals("--type"))
				type = ar[i+1];
						
			else if(ar[i].equals("--filename")) {
				input = ar[i+1];
				File file = new File(input);
				terminal = new Scanner(file);
				
				while(terminal.hasNextLine())
					code.add(terminal.nextLine()); //Read the program file line by line
		
				for(int j = 0; j < code.size(); j++)
					wholecode += code.get(j); //Append the complete source code into a single string

			}
		}
		
		array = new int[size];
		terminal = new Scanner(System.in);
		
		if(!(type.equals("char") || type.equals("int") || type.equals("byte"))) { 
			System.out.println("Undefined type : " + type + "\nUse int/char/byte");
			System.exit(1);
		}
		
		initMap();
		
		for(int i = 0; i < wholecode.length(); i++) {
			char ch = wholecode.charAt(i);
			switch(ch) {
				case '+' :
					array[index]++; 
					break;
				case '-' :
					array[index]--;
					break;	
				case '[' :
					if(array[index] == 0)
						i = hmap.get(i);
					
					break;
				case ']' :
					if(array[index] != 0)
						i = hmap.get(i);
					break;
				case '.' :
					if(type.equals("char"))
						System.out.print((char) array[index]);
					
					else if(type.equals("byte"))
						System.out.print((byte) array[index]);
					
					else
						System.out.print(array[index]);
					break;
				case ',' :
					if(type.equals("byte"))
						array[index] = terminal.nextByte();
					
					else
						array[index] = terminal.nextInt();
					break;
				case '<' :
					index -= 1;
					if(index < 0)
						index = size - 1;
					break;
				case '>' :
					index += 1;
					if(index == size)
						index = 0;
					break;
				default:
			}		
		}
	
	}
}

//Command line format
//java BrainFuck --size no<6 --type int BrainFuckCode.txt
