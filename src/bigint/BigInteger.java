package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		
		
		BigInteger newInt= new BigInteger();
		
		checkForException(integer);
		
		
		if (isZero(integer)) {
			return newInt;
		}
		
		
		int i = integer.length()-1;
		DigitNode crnt= newInt.front;
		DigitNode lastNonZero= crnt;
		int zeroCount=0;
	
		while (i>=0){
			
			//skips over space
			if (Character.isWhitespace(integer.charAt(i))) {
				i--;
				continue;
			}
			
			//checks for + or - sign and turns appropriate sign
			if (!Character.isDigit(integer.charAt(i))) {
				if(newInt.numDigits==0) {
					newInt.front=null;
				}
				else if (integer.charAt(i)=='-') {
					newInt.negative= true;
				}
				break;
			}
			
			//adds a new DigitNode
			if (newInt.front == null) {
				newInt.front= new DigitNode(integer.charAt(i) - '0', null);
				crnt= newInt.front;
				lastNonZero= crnt;
			}
			else {
				crnt.next= new DigitNode(integer.charAt(i) - '0', null);
				crnt= crnt.next;
			}
			
			//keeps track of long strings of zeroes
			if (integer.charAt(i) == '0') {
				zeroCount++;
			}
			else {
				zeroCount=0;
				lastNonZero= crnt;
			}
			
			
			newInt.numDigits++;
			
			//iterate
			i--;
			
		}
		
		
		//deletes leading zeroes
		lastNonZero.next=null;
		newInt.numDigits= newInt.numDigits-zeroCount;
	
		System.out.println("numDigits = " + newInt.numDigits);
		
		return newInt;
	}
	
	
	private static boolean isZero(String integer) {
		
		for (int i=0; i<integer.length(); i++) {
			if (!(integer.charAt(i)=='0' || integer.charAt(i)== '+' || integer.charAt(i)=='-')){
				return false;
			}
		}
		
		return true;
	}
	
	private static String checkForException(String str) 
	throws IllegalArgumentException {
		
		
		int signCount=0;
		int digitCount=0;
		int nonZeroCount=0;
		for (int i=0; i<str.length(); i++) {
			
			if (!Character.isDigit(str.charAt(i))) {
				if(str.charAt(i)=='+' || str.charAt(i)=='-') { 
					signCount++;
					if(signCount>1 || digitCount>0) {
						throw new IllegalArgumentException();
					}
				}
				else if(Character.isWhitespace(str.charAt(i))) { 
					if (signCount>0 || digitCount>0) {
						for (int j= i+1; j< str.length(); j++) {
							if (Character.isDigit(str.charAt(j)) || Character.isLetter(str.charAt(j))) {
								throw new IllegalArgumentException();
							}
						}
						//copy= str.substring(0,i) + copy.substring(i+1)
					}
					else { 
						//copy=str.substring(0,i) + str.substring(i+1);
					}
				}
				else { 
					throw new IllegalArgumentException();
				}
			}
			
			else {
				if (str.charAt(i)=='0') {
					if (nonZeroCount==0) {
						//copy=str.substring(0,i) + str.substring(i+1); 
					}
					else {
						digitCount++;
					}
				}
				else {
					nonZeroCount++;
					digitCount++;
				}
			}
		}
		
		return str;
	}
	
	
	
	
	
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		
		
		//Assign bigger and smaller
		
		BigInteger bigger;
		BigInteger smaller;
		
		if (firstBigger(first,second)) {
			bigger= first;
			bigger.numDigits=first.numDigits;
			bigger.negative=first.negative;
			smaller= second;
			smaller.numDigits=second.numDigits;
			smaller.negative=second.negative;
		}
		else {
			bigger= second;
			bigger.numDigits=second.numDigits;
			bigger.negative=second.negative;
			smaller= first;
			smaller.numDigits=first.numDigits;
			smaller.negative=first.negative;
				
		}
		
		//add bigger and smaller and store it into answer
		
		
		BigInteger answer= new BigInteger();
		DigitNode crntAnswer= answer.front; //here answer.front= null
		DigitNode crntBigger= bigger.front;
		DigitNode crntSmaller= smaller.front;
		
		
		if (bigger.front==null) {
			answer= smaller;
		}
		else if(smaller.front==null) {
			answer= bigger;
		}
		
		else {
			if ((bigger.negative && !smaller.negative) || (smaller.negative && !bigger.negative)) { 
				//opposite sign addition: subtract corresponding nodes

				
				boolean borrowed= false;
				int digitResult= 0;
				
				while (crntBigger != null) {
					//solve for digitResult with possible borrowing
					
					digitResult=0;
					
					if (crntSmaller == null) {
						if (borrowed && crntBigger.digit==0) {
							digitResult= 9;
						}
						else if (borrowed) {
							digitResult= crntBigger.digit-1;
						}
						else {
							digitResult = crntBigger.digit;
						}
					}
					
					else {
						
						//if the upper is 0 and had been borrowed from
						if (crntBigger.digit==0 && borrowed) {
							digitResult= 9-crntSmaller.digit;
							
							//because it has to borrow from the next number
							borrowed= true;
							
						}
						
						//if the upper is less than the smaller and had been borrowed from
						else if (crntBigger.digit<crntSmaller.digit && borrowed) {
							digitResult = 10 + (crntBigger.digit-1) - crntSmaller.digit;
							
							borrowed = true;
						}
						//if the upper is less than the smaller and had not been borrowed from
						else if (crntBigger.digit < crntSmaller.digit) {
							digitResult = 10 + crntBigger.digit - crntSmaller.digit;
							
							borrowed = true;
						}
						
						//if upper is equal to smaller and had been borrowed from
						else if (crntBigger.digit == crntSmaller.digit && borrowed) {
							digitResult = 10 + (crntBigger.digit-1) - crntSmaller.digit;
							
							borrowed = true;
						}
						//if upper is equal to smaller and had not been borrowed from
						else if (crntBigger.digit == crntSmaller.digit && borrowed) {
							digitResult = 0;
							
							borrowed= false;
						}
						
						//if the upper is more than smaller and had been borrowed from
						else if (crntBigger.digit > crntSmaller.digit && borrowed){
							digitResult= (crntBigger.digit-1) - crntSmaller.digit;
							
							borrowed= false;
						}
						//if the upper is more than or equal to smaller and had not been borrowed from
						else {
							digitResult= crntBigger.digit-crntSmaller.digit;
							
							borrowed = false;
						}
					}
					
					//add node with digitResult to answer
					
					if (answer.front== null){
						answer.front= new DigitNode(digitResult, null);
						crntAnswer= answer.front; //maybe won't need this?
						answer.numDigits++;
						
					}
					else {
						crntAnswer.next= new DigitNode(digitResult, null);
						answer.numDigits++;
						crntAnswer= crntAnswer.next;
					}
					
					//System.out.println(answer);
					
					//increment
					crntBigger=crntBigger.next;
					if (crntSmaller != null) {
						crntSmaller=crntSmaller.next;
					}
				}
				
				if (bigger.negative) {
					answer.negative=true;
				}
				else {
					answer.negative=false;
				}
				
				
			}
			else { 
				//same sign addition: add corresponding nodes
				
				if (bigger.negative && smaller.negative) {
					answer.negative=true;
				}
				
				int carryingNum = 0;
				int digitResult=0;
				
				while (crntBigger != null) {
					if (crntSmaller == null) {
						digitResult= carryingNum+crntBigger.digit;
					}
					else {
						digitResult= crntBigger.digit + crntSmaller.digit + carryingNum;
					}
					
					carryingNum= digitResult/10;
					digitResult= digitResult%10;
					
					if (answer.front== null){
						answer.front= new DigitNode(digitResult, null);
						crntAnswer= answer.front; //maybe won't need this?
						answer.numDigits++;
					}
					else {
						crntAnswer.next= new DigitNode(digitResult, null);
						answer.numDigits++;
						crntAnswer= crntAnswer.next;
					}
					
					
					if (crntBigger.next==null) {
						crntAnswer.next= new DigitNode(carryingNum, null);
						answer.numDigits++;
					}
	
					//System.out.println(answer);
					
					crntBigger= crntBigger.next;
					if (crntSmaller != null) {
						crntSmaller= crntSmaller.next;
					}
					
				}
			}
		}
		
		answer= stripExtraZeroes(answer);
		//System.out.println("answer.negative= " + answer.negative);
		//System.out.println("answer.numDigits= " + answer.numDigits);
		
		System.out.println("answer.numDigits = " + answer.numDigits);
		
		return answer;
	}
	
	private static BigInteger stripExtraZeroes (BigInteger bigInt) {
		
		if (bigInt.front==null) {
			return bigInt;
		}
		
		DigitNode crnt= bigInt.front;
		DigitNode lastNonZero = crnt;
		int numExtraZeroes = 0;
		
		while (crnt != null) {
			numExtraZeroes++;
			
			if(crnt.digit != 0) {
				lastNonZero= crnt;
				numExtraZeroes = 0;
			}
			
			crnt= crnt.next;
		}
		
		if (lastNonZero.digit == 0) {
			bigInt.front= null;
			bigInt.negative=false;
			bigInt.numDigits= 0;
		}
		else {
			lastNonZero.next = null;
			bigInt.numDigits= bigInt.numDigits- numExtraZeroes;
		}
		
		return bigInt;
		
	}
	
	private static boolean firstBigger(BigInteger first, BigInteger second) {
		DigitNode crnt1st= first.front;
		DigitNode crnt2nd= second.front;
		boolean firstBigger= false;
		
		if (crnt1st == null && crnt2nd == null) {
			return false;
		}
		else if (crnt1st == null) {
			return false;
		}
		else if (crnt2nd == null) {
			return true;
		}
		else if (first.numDigits > second.numDigits) {
			return true;
		}
		else if (second.numDigits > first.numDigits) {
			return false;
		}
		else {
			while(crnt1st != null && crnt2nd != null) {
				
				if (crnt1st.digit > crnt2nd.digit) {
					firstBigger=true;
				}
				if (crnt2nd.digit > crnt1st.digit) { 
					firstBigger=false; 
					
				}
							
				crnt1st=crnt1st.next; 
				crnt2nd=crnt2nd.next;
			}
		}
		
		
		return firstBigger;
	}

	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		
		//for each second node, multiply by all the first nodes and make that a new BigInteger
			//at each subsequent iteration, add an extra zero node to the front
			//if a product is more than 10, store product/10 as carryingNum and change the node with product to product%10
		//add the bigIntegers together
		
		BigInteger bigger= new BigInteger();
		BigInteger smaller = new BigInteger();
		
		if (firstBigger(first, second)) {
			bigger= first;
			bigger.numDigits= first.numDigits;
			bigger.negative= first.negative;
			
			smaller= second;
			smaller.numDigits= second.numDigits;
			smaller.negative= second.negative;
		}
		else {
			bigger= second;
			bigger.numDigits= second.numDigits;
			bigger.negative= second.negative;
			
			smaller= first;
			smaller.numDigits= first.numDigits;
			smaller.negative= first.negative;
		}
		
		DigitNode crntBigger= bigger.front;
		DigitNode crntSmaller= smaller.front;
		int placeCount = 0;
		BigInteger oldRow = new BigInteger();
		
		int digitResult= 0;
		int oneDigitResult = 0;
		int carryingNum=0;
		
		while (crntSmaller !=null) {
			crntBigger= bigger.front;
			carryingNum= 0;
			digitResult= 0;
			BigInteger newRow= new BigInteger();
			DigitNode crntNewRow= newRow.front; //here newRow.front= null
			
			//System.out.println("Old Row beginning of Smaller loop: " + oldRow);
			
			//add additional zero node if past first place in second
			for (int i=0; i< placeCount; i++) {
				if (newRow.front == null) {
					newRow.front= new DigitNode (0,null);
					crntNewRow= newRow.front; //maybe I don't need this?
					newRow.numDigits++;
				}
				else {
					crntNewRow.next= new DigitNode (0, null);
					newRow.numDigits++;
					crntNewRow= crntNewRow.next;
				}
			}
			
			
			//System.out.println("newRow.numDigits after adding additional zeroes: " + newRow.numDigits);
			
			while (crntBigger !=null) {
				digitResult= crntBigger.digit * crntSmaller.digit;
				//System.out.println("digitResult right after multiplying: " + digitResult);
				
				oneDigitResult= digitResult%10 + carryingNum;
				carryingNum= digitResult/10;
				digitResult= oneDigitResult;
				
				//System.out.println("oneDigitResult= " + oneDigitResult);
				//System.out.println("carryingNum= " + carryingNum);
				//System.out.println("digitResult= " + digitResult);
				
				if (newRow.front == null) {
					newRow.front= new DigitNode (digitResult,null);
					newRow.numDigits++;
					crntNewRow= newRow.front;
				}
				else { 
					crntNewRow.next= new DigitNode (digitResult, null);
					newRow.numDigits++;
					crntNewRow=crntNewRow.next;
				}
				
				//System.out.println("newRow right after adding new node: " + newRow);
				
				if (crntBigger.next==null) {
					if (carryingNum != 0) {
						crntNewRow.next= new DigitNode(carryingNum, null);
						newRow.numDigits++;
					}
				} 
				
				crntBigger= crntBigger.next;
			}
			
			//System.out.println("Old row before adding rows together: " + oldRow);
			//System.out.println("New Row before adding rows together: " + newRow);
			//System.out.println("------------------");
			
			oldRow = add(oldRow, newRow);
			//System.out.println("Old Row End of Smaller loop: " + oldRow);
			crntSmaller= crntSmaller.next;
			placeCount++;
		}
		
		oldRow = stripExtraZeroes(oldRow);
		
		if ((bigger.negative && !smaller.negative) || smaller.negative && !bigger.negative) {
			//oposite signs means negative
			oldRow.negative= true;
		}
		else {
			//same sign means positive
			oldRow.negative= false;
		}
		
		//System.out.println("oldRow.numDigits (after loops)= " + oldRow.numDigits);
		//System.out.println("oldRow.negative = " + oldRow.negative);
		
		System.out.println("oldRow.numDigits = " + oldRow.numDigits);
		
		return oldRow; 
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
