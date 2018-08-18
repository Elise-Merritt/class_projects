package math;

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
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		char c;
		BigInteger bg = new BigInteger();
		integer=integer.trim();
		if(integer.charAt(0)=='+'){
			integer=integer.substring(1);
		}else if (integer.charAt(0)=='-' ){
			integer=integer.substring(1);
			bg.negative=true;
		}
		boolean isZero=false;
		if(integer.compareTo("0")==0){
			bg.front=new DigitNode(0, bg.front);
			bg.negative=false;
		}else{
		    isZero=true;
		    while(isZero==true){
		    	if(integer.charAt(0)=='0' && integer.length()>1){
		    		integer=integer.substring(1);
		    	}else{
		    		isZero=false;
		    	}
		    }
		
		for(int i=0; i<integer.length(); i++){
			c=integer.charAt(i);
			if(Character.isDigit(c)==false){
				throw new IllegalArgumentException();
			}else{
			bg.front=new DigitNode(Character.getNumericValue(c), bg.front);
			bg.numDigits++;
			}
		}
		}
		if(integer.compareTo("0")==0){
			bg.negative=false;
		}
		return bg;
	}
	
	/**
	 * Adds an integer to this integer, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY this integer.
	 * NOTE that either or both of the integers involved could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param other Other integer to be added to this integer
	 * @return Result integer
	 */
	public BigInteger add(BigInteger other) {
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		BigInteger answer=new BigInteger();
		BigInteger answer2=new BigInteger();
		DigitNode getNum1 = this.front;
		DigitNode getNum2= other.front;
		boolean carry=false, carry2=false;
		int sum, sum2, diff, d1,d2;
		if((this.negative==false && other.negative==false) || (this.negative==true && other.negative==true)) {
		if(this.numDigits==0) {
		   return other;
		}else if(other.numDigits==0) {
			return this;
		} else if(this.numDigits==other.numDigits) {
		while(getNum1!=null) {
			sum=getNum1.digit+getNum2.digit;
            if(carry==true) {
            	sum++;
            }
            if(sum<10) {
            	answer.front=new DigitNode(sum, answer.front);
                carry=false;
            }else {
            	answer.front=new DigitNode(sum-10, answer.front);
            	carry=true;
            }
			getNum1=getNum1.next;
			getNum2=getNum2.next;
			
		}
		if(carry==true) {
			answer.front=new DigitNode(1, answer.front);
		}
		}else if (this.numDigits>other.numDigits){
			while(getNum1!=null) {
			while(getNum2!=null) {
				sum=getNum1.digit+getNum2.digit;
				 if(carry==true) {
		            	sum++;
		            }
		            if(sum<10) {
		            	answer.front=new DigitNode(sum, answer.front);
		                carry=false;
		            }else {
		            	answer.front=new DigitNode(sum-10, answer.front);
		            	carry=true;
		            }
				getNum1=getNum1.next;
				getNum2=getNum2.next;
			}
			sum2=getNum1.digit;
			if(carry==true) {
			     sum2++;
			}
			if(sum2>=10) {
				answer.front=new DigitNode(10-sum2, answer.front);
				carry=true;
			}else {
			answer.front=new DigitNode(sum2, answer.front);
			carry=false;
			}
			getNum1=getNum1.next;
			}
			if(carry==true) {
				answer.front=new DigitNode(1, answer.front);
			}
		}else if(other.numDigits>this.numDigits) {
			while(getNum2!=null) {
				while(getNum1!=null) {
					sum=getNum1.digit+getNum2.digit;
					 if(carry==true) {
			            	sum++;
			            }
			            if(sum<10) {
			            	answer.front=new DigitNode(sum, answer.front);
			                carry=false;
			            }else {
			            	answer.front=new DigitNode(sum-10, answer.front);
			            	carry=true;
			            }
					getNum1=getNum1.next;
					getNum2=getNum2.next;
				}
				sum2=getNum2.digit;
				if(carry==true) {
				     sum2++;
				}
				if(sum2>=10) {
					answer.front=new DigitNode(10-sum2, answer.front);
					carry=true;
				}else {
				answer.front=new DigitNode(sum2, answer.front);
				carry=false;
				}
				getNum2=getNum2.next;
				}
			if(carry==true) {
				answer.front=new DigitNode(1, answer.front);
			}
		}
		
		
		}else if(this.isBigger(other)==true) {
			if(this.numDigits==other.numDigits) {
			   while(getNum1!=null) {
				   d1=getNum1.digit;
				   d2=getNum2.digit;
	               if(carry2==false) {
	            	   diff=d1-d2;
	               }else {
	            	   d1--;
	            	   diff=d1-d2;
	               }
				   if(diff>=0) {
					   answer.front=new DigitNode(diff, answer.front);
				   }else {
					   answer.front=new DigitNode((10+d1)-d2, answer.front);	
					   carry2=true;
				   }
				   getNum1=getNum1.next;
				   getNum2=getNum2.next;
			   }
			   }else if (this.numDigits>other.numDigits) {
				   while(getNum1!=null) {
				   while(getNum2!=null) {
					   d1=getNum1.digit;
					   d2=getNum2.digit;
					   if(carry2==false) {
		            	   diff=d1-d2;
		               }else {
		            	   d1--;
		            	   diff=d1-d2;
		               }
					   if(diff>=0) {
						   answer.front=new DigitNode(diff, answer.front);
					   }else {
						   answer.front=new DigitNode((10+d1)-d2, answer.front);	
						   carry2=true;
					   }
					   getNum1=getNum1.next;
					   getNum2=getNum2.next;
				   }
				   d1=getNum1.digit;
				   if(carry2==false) {
					   diff=d1;
				   }else {
					   diff=d1-1;
					   carry2=false;
				   }
				   answer.front=new DigitNode(diff, answer.front);
				   getNum1=getNum1.next;
				   }          
			   }
		}else if(this.isBigger(other)==false) {
			if(this.numDigits==other.numDigits) {
				while(getNum1!=null) {
					   d1=getNum1.digit;
					   d2=getNum2.digit;
		               if(carry2==false) {
		            	   diff=d2-d1;
		               }else {
		            	   d2--;
		            	   diff=d2-d1;
		               }
					   if(diff>=0) {
						   answer.front=new DigitNode(diff, answer.front);
					   }else {
						   answer.front=new DigitNode((10+d2)-d1, answer.front);	
						   carry2=true;
					   }
					   getNum1=getNum1.next;
					   getNum2=getNum2.next;
				   }
			}else {
				while(getNum2!=null) {
					   while(getNum1!=null) {
						   d1=getNum1.digit;
						   d2=getNum2.digit;
						   if(carry2==false) {
			            	   diff=d2-d1;
			               }else {
			            	   d2--;
			            	   diff=d2-d1;
			               }
						   if(diff>=0) {
							   answer.front=new DigitNode(diff, answer.front);
						   }else {
							   answer.front=new DigitNode((10+d2)-d1, answer.front);	
							   carry2=true;
						   }
						   getNum1=getNum1.next;
						   getNum2=getNum2.next;
					   }
					   d2=getNum2.digit;
					   if(carry2==false) {
						   diff=d2;
					   }else {
						   diff=d2-1;
						   carry2=false;
					   }
					   answer.front=new DigitNode(diff, answer.front);
					   getNum2=getNum2.next;
					   } 
			}
	}
		DigitNode dg= answer.front;
		while(dg!=null) {
			answer2.front=new DigitNode(dg.digit, answer2.front);
			answer2.numDigits++;
			dg=dg.next;
		}
		if(this.negative==true && other.negative==true) {
			answer2.negative=true;
		}else if(this.isBigger(other)==true && this.negative==true) {
			answer2.negative=true;
		}else if (this.isBigger(other)==true && other.negative==false && this.negative==true) {
			answer2.negative=true;
		}else if (this.isBigger(other)==false && this.negative==false && other.negative==true) {
			answer2.negative=true;
		}
		return parse(answer2.toString());
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the given BigInteger
	 * with this BigInteger - DOES NOT MODIFY this BigInteger
	 * 
	 * @param other BigInteger to be multiplied
	 * @return A new BigInteger which is the product of this BigInteger and other.
	 */
	public BigInteger multiply(BigInteger other) {
		// THE FOLLOWING LINE IS A PLACEHOLDER SO THE PROGRAM COMPILES
		// YOU WILL NEED TO CHANGE IT TO RETURN THE APPROPRIATE BigInteger
		BigInteger answer =new BigInteger();
		BigInteger product =new BigInteger();
		product.front=new DigitNode(0, product.front);
		product.numDigits++;
		BigInteger solution=new BigInteger();
		DigitNode first = this.front;
		DigitNode second= other.front;
		int pro, carry=0, counter=0, x;
		if(this.numDigits>=other.numDigits) {
			while(second!=null) {
			while(first!=null) {
			x=second.digit;
			pro=x*first.digit+carry;
			answer.front=new DigitNode(pro%10, answer.front);
			answer.numDigits++;
			carry=pro/10;
			first=first.next;
			}
			counter++;
			if(carry!=0) {
				answer.front=new DigitNode(carry, answer.front);
				answer.numDigits++;
			}
			carry=0;
			DigitNode dg= answer.front;
			while(dg!=null) {
				solution.front=new DigitNode(dg.digit, solution.front);
				solution.numDigits++;
				dg=dg.next;
			}
			product=product.add(solution);
			answer.front=null;
			answer.numDigits=0;
			solution.front=null;
			solution.numDigits=0;
			for(int i=0; i<counter; i++) {
				answer.front=new DigitNode(0, answer.front);
				answer.numDigits++;
			}
			first=this.front;
			second=second.next;
			}
		}else {
			while(first!=null) {
				while(second!=null) {
				x=first.digit;
				pro=x*second.digit+carry;
				answer.front=new DigitNode(pro%10, answer.front);
				answer.numDigits++;
				carry=pro/10;
				second=second.next;
				}
				counter++;
				if(carry!=0) {
					answer.front=new DigitNode(carry, answer.front);
					answer.numDigits++;
				}
				carry=0;
				DigitNode dg= answer.front;
				while(dg!=null) {
					solution.front=new DigitNode(dg.digit, solution.front);
					solution.numDigits++;
					dg=dg.next;
				}
				product=product.add(solution);
				answer.front=null;
				answer.numDigits=0;
				solution.front=null;
				solution.numDigits=0;
				for(int i=0; i<counter; i++) {
					answer.front=new DigitNode(0, answer.front);
					answer.numDigits++;
				}
				second=other.front;
				first=first.next;
				}
		}
		if(this.negative==true && other.negative==false) {
			product.negative=true;
		}else if (this.negative==false && other.negative==true) {
			product.negative=true;
		}
		return parse(product.toString());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	private boolean isBigger(BigInteger o) {
		if(this.numDigits>o.numDigits) {
			return true;
		}else if (o.numDigits>this.numDigits) {
			return false;
		}else {
			BigInteger one = new BigInteger();
			BigInteger two = new BigInteger();
			DigitNode dn1=this.front;
		    DigitNode dn2=o.front;
			while(dn1!=null) {
	 		one.front=new DigitNode(dn1.digit, one.front);
			two.front=new DigitNode(dn2.digit, two.front);
	
				dn1=dn1.next;
				dn2=dn2.next;
		       one.numDigits++;
		        two.numDigits++;
			}
			DigitNode next1 = one.front;
			DigitNode next2 = two.front;
			while(next1!=null) {
				if(next1.digit>next2.digit) {
					return true;
				}else if (next1.digit<next2.digit){
					return false;
				}else {
					next1=next1.next;
					next2=next2.next;
				}
			}
		}
		return false;
	}

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
