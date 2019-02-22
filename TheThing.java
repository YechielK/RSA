/*
Here I will be explaining how RSA works using java code. 
RSA uses modular exponentiation to encrypt messages. In short, it accomplishes this by raising a number to be encrypted to the power of a certain number,
then modding that number. The result is the encrypted number. To decode that number, you cant just raise it to the power of the previous one and mod it
it again to obtain the original number. The only way is to raise it to the power of a different number, usually of a much higher power, although it can
be lower. 

For computer science, encryption is essential. Since all web traffic can be seen by all people, there is a great need for web traffic to be encrypted.
Unfortunately, in order for two people to have a secret code, both must know the key. The problem with using a code over the internet is that 
since all communication can be intercepted, the keys used to encrypt or decrypt a message can be also be intercepted. Furthermore, if you are 
communicating with more than one person using the same code, all you need is one person to make the keys public and your code is useless.
You could make a separate code for each person you communicate with, but thats alot of time and energy. RSA solves all these problems.
Instead of one user having to have a different secret code for every different person they communicate with, they only need one. This is 
accomplished by sending out a "public" key, or an encoding formula that is known to everyone. However, once a message is encrypted using
the public key, it can only be decrypted using a "private" key, which only the original sender has. Using RSA, the user can send out the 
same key to everyone. Since no one else has the private key, no one else can decrypt the code, and it can be re-used.

We have explained above how RSA uses numbers to encrypt information and why its useful. Now we will explain how these numbers are generated,
as they are not arbitrary.

First, you pick two prime numbers (we'll call them p and q). Then you multiply them. This will be the modulus. Then you count all the 
numbers that are between one and the modulus that are co-prime with the modulus. This is the totient. It will help us find the keys.
An easier way of finding the totient as opposed to counting is by using the formula (p - 1) * (q - 1). Next, we find the encryption key,
which is any number between 1 and the totient, while also being co-prime with the totient and the modulus. The decryption key is any 
number that when multiplied by the encryption key and modded by the totient equals one.
*/
	import java.util.Scanner; 

	public class TheThing {
		//this function finds the greatest common factor
	    public static int gcf(int x, int y) { 
	        if (x % y == 0) return y; //This function will return the greatest common factor between x and y.
	        return gcf(y, x%y);
	    }
	    
	    public static int findmod(int a, int b) {
	    	return a * b; //This function finds the modulus which will be used to find
	    }
	    
	    public static int findtotient(int a, int b) {
	    	return (a - 1) * (b - 1); //This function multiplies the prime numbers to find the totient, which will be used to find
	    }
	    
	    public static int finde(int totient, int modulus) {
			for (int i = 2; i < totient; i++) { //This function finds e by finding a number that is co-prime with l and n. It uses the totient and modulus
				if ((gcf(i,totient)==1)&&(gcf(i,modulus)==1)) {
					return i; //This is e	
				}
			}
			return 0;
	    }
	    
	    public static int findd(int exponent, int totient) { 
	    	for (int i = 1, d = 1; d == 1; i++) { //This function finds d, which is a number that when multiplied by e and modded by totient, will equal 1. It uses e and totient
				if (i * exponent % totient == 1 && i != exponent) { 
					return i;//This is d
				}
			}
	    	return 0;
	    }
	    
	    public static void charToint(char [] intake,int [] toBeEncrypted) { //This function converts a char array to an int array
	    	for (int i = 0; i < intake.length; i++) {
	    		toBeEncrypted[i] = intake[i]; 
	    	}
	    }
	    
	    public static void print(int toPrint[]) { //This function prints an array of integers
	    	for (int i = 0; i < toPrint.length; i++) {
	    		System.out.print(toPrint[i] + " ");
	    	}
	    }
	    
	    public static void printAschar(int toPrint[]) { //This function prints an array of integers as chars
	    	for (int i = 0; i < toPrint.length; i++) {
	    		System.out.print((char)(toPrint[i]));
	    	}
	    }
	    
	    public static void toEncrypt(int[] toBeEncrpyted, int encryptKey,int modulus, int[] encrypted) { //This function encrypts
	    	for (int i = 0; i < toBeEncrpyted.length; i++) {
	    		toBeEncrpyted[i] -= 32; //encrypting normal ascii would cause integer overflow, subtracting all values by 32 solves this
	    		encrypted[i] = RSA(toBeEncrpyted[i],encryptKey,modulus);	
			}
	    }
	    public static void toDecrypt(int[] encrypted, int decryptKey, int modulus, int[] decrypted) {//This function decrypts
	    	for (int i = 0; i < encrypted.length; i++) {
	    		decrypted[i] = RSA(encrypted[i],decryptKey,modulus);
	    		decrypted[i] += 32; // add back the 32 that was taken away befor encrpytion
			}
	    }
	    
	    public static String bin(int x) {//This function converts the exponent into a string so modular exponentiation can be done
	        String ans = "";
	        for(int z = x;z > 0;z/=2) {
	                if (z%2==0) {
	                        ans = "0" + ans;
	                } else {
	                        ans = "1" + ans;
	                }	          
	        }
	        return ans;
	    }

	  /*
		This next function does the actual encryption/decryption. It takes in the base (the number to be encrypted/decrypted),
		the exponent (the encryption/decryption key), and the modulus. First, it converts the exponent to binary, then does
		modular exponentiation. 
		
		Remember that for each spot in binary, the position to the left is twice the value of the one
		to its right, until the first value which is one.
		
		For example, lets say we want to encrypt 2, while using 3 as our encryptor with 10 as our modulus.
		First, we would a initialize a variable to one. This variable will be multiplied using the base, 
		exponent and the modulus, and the resulting number will be our encrypted number.
		Second, we would convert 3 to binary (11 in binary). We would then use a for loop and initialize two 
		variables, i and j. The i would start at the end of the binary string (which is the 1's position in 
		binary). The j would start at 1, and with each successive loop it would double, to keep track of the value of the 
		position of i. So in the first loop, i is the last character in the string and j is one, in the 
		second loop i is the second to last character and j is 2. Every time the position that i is
		pointing to is a 1, modExpo is called. If i points to a zero, i still decrements and j doubles,
		but modExpo is not called.
	   */
	    public static int RSA(int base,int exponent, int modulus) {
	        int ans = 1;
	        String exp = bin(exponent); //Converts the exponent to binary
	        for (int i = exp.length() - 1, j = 1; i >= 0; i--, j*=2){ //loop runs for every character, or length, of the binary string
	        														  //the loop starts off with the last character in the string. j starts off at one and doubles
	                if (exp.charAt(i)=='1') {						  //if theres a 1 in the string, call function	
	                        ans *= modExpo(base,j,modulus);			  // multiply answer by number returned from modExpo
	                }
	        }
	        ans %= modulus;	       
	        return ans;
	    }

	    /*
	     This function does the modular exponentiation on each individual value. It is recursive. It is first called
	     when the for loop in RSA sees a one in the exponent bit string. If the value of the position (j in the RSA
	     function) is one, then the function returns the base, as the function is being asked to return base to the 
	     power of one, and base to the power of one is base. If the value of the exponent would be 2 (10 in binary),
	     Then this function would be called on the second iteration of the for loop. It would make 2 recursive calls
	     on itself with the position halved (so now position is 1), and the base being returned these would be modded
	     and multiplied and returned as the the encrypted answer.
	     
	     Lets use the previous example, where the base was 2, exponent 3 (11 in binary), and the modulus was 10.
	     The RSA function would modExpo on the first for loop. Since j would be 1, it would return the base,
	     which is 2, and the ans would be multiplied by it. Then the second loop would call modExpo again, but with 
	     j being 2. modExpo would make two recursive calls to itself with j being one, and multiplyng and modding the
	     answer. Since we know did modExpo with 1 and got back 2, and 2 * 2 = 4, thats our answer. If j were to be
	     a larger number, this would quickly become a very large number. But because we are modding it by 10, it would
	     never reach that large number. In our case, we used such small number that the mod 10 didnt do anything.
	     */
	    public static int modExpo(int base, int position, int modulus) { //This is actually an inefficient recursive algorithm, but 
	        if (position==1) return base;                          		//I wrote it just to demonstrate modular exponentiation
	        return ((( modExpo(base, position / 2, modulus) % modulus) * (modExpo(base, position / 2, modulus) % modulus )) % modulus);
	    }
		 
	    public static void main(String[] args) {
	    	Scanner b = new Scanner(System.in);
	    	int p = 7;     //p and q are the prime numbers
			int q = 13;
			int encryptKey; //This is the encryption key, or the first part. The number to encrypted will be raised to power of this number
			int decryptKey; //The decryption key, or the second part. The number to be decrypted will be raised to this power
			int modulus = findmod(p,q); //This is the modulus. You get it by multiplying p and q. It mods for encyption and decrpytion.
			int totient = findtotient(p,q); // The totient, which will help us find the encryption key. It's what you get when you find all the co-prime numbers between 1 and p * q.
			encryptKey = finde(totient,modulus); //finds e, which is a number between 1 and l that is coprime with l and n
			decryptKey = findd(encryptKey,totient); //finds d, which is a number that when multipled by e and mod by l, will equal 1
			
			System.out.println("The primes to be used are " + p + " and " + q);
			System.out.println("The modulus is p * q = m, or " + p + " * " + q + " = "+ modulus);
			System.out.println("The totient is (p-1) * (q-1), or "  + p + " * " + q + " = " + totient);
			System.out.println("The encryption key is coprime with the totient and mod. It is " + encryptKey);
			System.out.println("decryption key * encryption key % totient = 1, or " + decryptKey + " * " + encryptKey + " % " + totient + " = 1");
			
			System.out.println("Input string to be encrpted:");
			char [] source = b.nextLine().toCharArray();
			int [] toBeEncrypted = new int [source.length];
			int [] encrypted = new int [source.length];
			int [] decrypted = new int [source.length];
			
			charToint(source,toBeEncrypted); //The string to be encoded was taken in as a char array. It will now be converted to an int array.
			
			System.out.print("Before encrypting: ");
			print(toBeEncrypted);
			
			toEncrypt(toBeEncrypted,encryptKey,modulus,encrypted); //Encrypts string
			System.out.print("\nAfter  encrypting: ");
			print(encrypted);
						
			System.out.print("\nUsing letter form: "); //If the encrypted string were to be put in letter form, it would be gibberish.
			printAschar(encrypted);
			
			System.out.print("\nAfter  decrypting: ");
			toDecrypt(encrypted,decryptKey,modulus,decrypted); //Decrypts string
			print(decrypted);
			
			System.out.print("\nUsing letter form: ");
			printAschar(decrypted);
		}    
	}