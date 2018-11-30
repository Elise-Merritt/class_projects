package apps;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;
public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   
	
	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     */
    public void buildSymbols() {
    		/** COMPLETE THIS METHOD **/
    	    int i=0;
    	    scalars=new ArrayList<ScalarSymbol>(expr.length());
    	    arrays=new ArrayList<ArraySymbol>(expr.length());
    	    while(i<expr.length()) {
    	    	char c;
    	    	String v = "";
    	    	while(i<expr.length() && Character.isLetter(expr.charAt(i))==true) {
    	    		c=expr.charAt(i);
                  v=v+c;
                  i++;
    	    	}
    	    	if(i<expr.length() && expr.charAt(i)=='[') {
    	    		ArraySymbol a = new ArraySymbol(v);
    	    		this.arrays.add(a);
    	    		
    	    	}else if(v.length()!=0) {
    	    		int m = 0;
    	    		for(int j=0; j<scalars.size(); j++) {
    	    			if(scalars.get(j).name.equals(v)) {
    	    				m=1;
    	    				break;
    	    			}
    	    		}
    	    		if(m==0) {
    	    		ScalarSymbol s = new ScalarSymbol(v);
    	    	    this.scalars.add(s);
    	    		}
    	    	}else {
    	    		
    	    	}
    	    	i++;
    	    }
    }
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
            	asymbol = arrays.get(asi);
            	asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }
    
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
    		/** COMPLETE THIS METHOD **/
    		// following line just a placeholder for compilation
    	    Stack s = new Stack();
    	    Stack o = new Stack();
    	    String arrayname="";
    	    for(int i=0; i<this.expr.length(); i++) {
    	    	char c=this.expr.charAt(i);
    	    	if(Character.isDigit(c)==true) {
    	    		String l="";
    	    		while(i<expr.length() && Character.isDigit(expr.charAt(i))==true) {
        	    		c=expr.charAt(i);
                        l=l+c;
                        i++;
        	    	}
    	    		i=i-1;
    	    		s.push(Float.parseFloat(l));
    	    	}else if (Character.isLetter(c)==true) {
    	    		String l="";
    	    		while(i<expr.length() && Character.isLetter(expr.charAt(i))==true) {
    	    			 c=expr.charAt(i);
                        l=l+c;
                        i++;
        	    	}
    	    		i=i-1;
    	    		if(i<expr.length()-1 && expr.charAt(i+1)=='[') {
        	    		int p=0;
        	    		for(int u=(this.expr.length()-1); u>=0; u--) {
        	    			if(this.expr.charAt(u)==']') {
        	    				p=u;
        	    				break;
        	    			}
        	    		}
        	    		int jkl=0;
        	    		String xyz=this.expr.substring(i+1, p);
        	    		if(xyz.length()==1) {
        	    			
            	    		for(int j=0; j<arrays.size(); j++) {
            	    			if((arrays.get(j).name).equals(l)) {
            	    				jkl=j;
            	    				break;
            	    			}
            	    		}
            	    		i=p;
        	    			s.push(arrays.get(jkl).values[Integer.getInteger(xyz)]);
        	    		}else {
        	    			i=p;
            	    		Integer q = (int) evaluate(xyz);
            	    		s.push(arrays.get(jkl).values[q]);
        	    		}
    	    		}else {
    	    		for(int j=0; j<scalars.size(); j++) {
    	    			if((scalars.get(j).name).equals(l)) {
    	    				s.push( (float) (scalars.get(j).value));
    	    			}
    	    		}
    	    		}
                    
    	    	}else if(c== '+' || c=='-') {
    	    		o.push(new Character (c));
    	    	}else if (c== '*') {
    	            i++;
    	    		float x=(float) s.pop();
    	    		char t = this.expr.charAt(i);
    	    		float y=0;
    	    		if(Character.isDigit(t)==true) {
    	    			String l="";
    	    			if(i<expr.length()-1 && Character.isDigit(this.expr.charAt(i+1))==true){
        	    		while(i<this.expr.length() && (Character.isDigit(t)==true)) {
        	    			t=this.expr.charAt(i);
                            l=l+t;
                            i++;
                            
            	    	}
        	    		i=i+2;
        	    		y=Float.parseFloat(l);
    	    			}else {
    	    				y=Character.getNumericValue(this.expr.charAt(i));
    	    			}
        	    		Float product=x*y;
        	    		System.out.println("product1="+product);
        	    		s.push(product);
    	    		}else if(Character.isLetter(t)==true){
    	    			String l="";
        	    		while(i<expr.length() && Character.isLetter(t)==true) {
                            l=l+t;
                            i++;
                            t=this.expr.charAt(i);
            	    	}
        	    		i=i-1;
        	    		System.out.println("l="+l);
        	    		for(int k=0; k<scalars.size(); k++) {
        	    			if((scalars.get(k).name).equals(l)) {
        	    				y=(float) (scalars.get(k).value);
        	    			}
        	    		}
        	    		i=i+2;
        	    		Float product=x*y;
        	    		System.out.println("product2="+product);
        	    		s.push(product);
    	    		}else if (t=='(') {
    	    			int p=0;
        	    		for(int u=(this.expr.length()-1); u>=0; u--) {
        	    			if(this.expr.charAt(u)==')') {
        	    				p=u;
        	    				break;
        	    			}
        	    		}
        	    		String xyz=this.expr.substring(i+1, p);
        	    		if(xyz.length()==1) {
        	    			s.push(Float.parseFloat(xyz));
        	    			i=p;
        	    		}else {
        	    		i=p;
        	    		y = evaluate(xyz);
        	    		Float product=x*y;
        	    		s.push(product);
        	    		}
    	    		}
    	    	}else if (c== '/') {
    	    		i++;
    	    		float x=(float) s.pop();
    	    		char t = this.expr.charAt(i);
    	    		float y=0;
    	    		if(Character.isDigit(t)==true) {
    	    			String l="";
    	    			if(i<expr.length()-1 && Character.isDigit(this.expr.charAt(i+1))==true){
        	    		while(i<this.expr.length() && Character.isDigit(t)==true) {
        	    			t=this.expr.charAt(i);
                            l=l+t;
                            i++;
            	    	}
        	    		i=i+2;
        	    		y=Float.parseFloat(l);
    	    			}else {
    	    				y=Character.getNumericValue(this.expr.charAt(i));
    	    			}
        	    		Float quotient=x/y;
        	    		s.push(quotient);
    	    		}else if(Character.isLetter(t)==true){
    	    			String l="";
        	    		while(i<expr.length() && Character.isLetter(t)==true) {
        	    			t=this.expr.charAt(i);
        	    			l=l+t;
                            i++;
            	    	}
        	    		i=i-1;
        	    		for(int j=0; j<scalars.size(); j++) {
        	    			if((scalars.get(j).name).equals(l)) {
        	    				y=(float) (scalars.get(j).value);
        	    			}
        	    		}
        	    		i=i+2;
        	    		Float quotient=x/y;
        	    		s.push(quotient);
    	    		}else if (t=='(') {
    	    			int p=0;
        	    		for(int u=(this.expr.length()-1); u>=0; u--) {
        	    			if(this.expr.charAt(u)==')') {
        	    				p=u;
        	    				break;
        	    			}
        	    		}
        	    		String xyz=this.expr.substring(i+1, p);
        	    		if(xyz.length()==1) {
        	    			s.push(Float.parseFloat(xyz));
        	    			i=p;
        	    		}else {
        	    		i=p;
        	    		y = evaluate(xyz);
        	    		Float quotient=x/y;
        	    		s.push(quotient);
        	    		}
    	    		}
    	    		
    	    	
    	    		
    	    	}else if (c=='(') {
    	    		int p=0;
    	    		for(int u=(this.expr.length()-1); u>=0; u--) {
    	    			if(this.expr.charAt(u)==')') {
    	    				p=u;
    	    				break;
    	    			}
    	    		}
    	    		String xyz=this.expr.substring(i+1, p);
    	    		if(xyz.length()==1){
    	    			s.push(Float.parseFloat(xyz));
    	    			i=p;
    	    		}else {
    	    		i=p;
    	    		Float q = evaluate(xyz);
    	    		s.push(q);
    	    		}
    	    	}
               
    	    }
    	    if(s.size()==1) {
    	    	return (float) Float.parseFloat(s.pop()+"");
    	    }else {
    	    while(s.size()>=1) {
    	    	if(s.size()==1) {
    	    		break;
    	    	}
    	    	Float n1 = (float) s.pop();
    	    	System.out.println("n1="+n1);
    	        Float n2 = (float) s.pop();
    	        System.out.println("n2="+n2);
    	        Float answer;
    	        if(o.peek().equals('+')) {
    	        	o.pop();
    	            answer= (float) (n1+n2);
    	        	s.push(answer);
    	        }else if (o.peek().equals('-')){
    	        	o.pop();
    	        	answer=(float) (n2-n1);
    	            s.push(answer);
    	        } 
    	    }
    		return (float) Float.parseFloat(s.pop()+"");
    	    }
    }
    
    
    private float evaluate(String z) {
    	Stack e = new Stack();
	    Stack r = new Stack();
	    for(int i=0; i<z.length(); i++) {
	    	char f=z.charAt(i);
	    	if(Character.isDigit(f)==true) {
	    		String l="";
	    		while(i<z.length() && Character.isDigit(z.charAt(i))==true) {
    	    		f=z.charAt(i);
                    l=l+f;
                    i++;
    	    	}
	    		i=i-1;
	    		System.out.println("l="+l);
	    		e.push(Float.parseFloat(l));
	    	}else if (Character.isLetter(f)==true) {
	    		String l="";
	    		while(i<z.length() && Character.isLetter(z.charAt(i))==true) {
	    			f=z.charAt(i);
                    l=l+f;
                    i++;
    	    	}
                i=i-1;
	    		if(i<z.length()-1 && z.charAt(i+1)=='[') {
    	    		int p=0;
    	    		for(int u=(z.length()-1); u>=0; u--) {
    	    			if(z.charAt(u)==']') {
    	    				p=u;
    	    				break;
    	    			}
    	    		}
    	    		String xyz=z.substring(i+1, p);
    	    		int jkl=0;
    	    		if(xyz.length()==1) {
        	    		for(int j=0; j<arrays.size(); j++) {
        	    			if((arrays.get(j).name).equals(l)) {
        	    				jkl=j;
        	    				break;
        	    			}
        	    		}
        	    		i=p;
    	    			e.push(arrays.get(jkl).values[Integer.getInteger(xyz)]);
    	    		}else {
    	    			i=p;
        	    		Integer q = (int) evaluate(xyz);
        	    		e.push(arrays.get(jkl).values[q]);
    	    		}
	    		}else {
	    		for(int j=0; j<scalars.size(); j++) {
	    			if((scalars.get(j).name).equals(l)) {
	    				e.push( (float) (scalars.get(j).value));
	    			}
	    		}
	    		}
	    	}else if(f== '+' || f=='-') {
	    		r.push(new Character (f));
	    	}else if (f== '*') {
	    		i++;
	    		float x=(float) e.pop();
	    		char t =z.charAt(i);
	    		float y=0;
	    		if(Character.isDigit(t)==true) {
	    			String l="";
	    			if(i<z.length()-1 && Character.isDigit(z.charAt(i+1))==true){
	    				while(i<z.length() && Character.isDigit(t)==true) {
        	    		t=z.charAt(i);
                        l=l+t;
                        i++;
        	    	}
    	    		i=i-1;
    	    		y=Float.parseFloat(l);
	    			}else {
	    				y=Character.getNumericValue(z.charAt(i));
	    			}
    	    		Float product=x*y;
    	    		e.push(product);
	    		}else if(Character.isLetter(t)==true){
	    			String l="";
    	    		while(i<z.length() && Character.isLetter(t)==true) {
    	    			t=z.charAt(i);
                        l=l+t;
                        i++;
                        
        	  	}
    	    		i=i-1;
    	    		for(int j=0; j<scalars.size(); j++) {
    	    			if((scalars.get(j).name).equals(l)) {
    	    				y=(float) (scalars.get(j).value);
    	    			}
    	    		}
    	    		Float product=x*y;
    	    		e.push(product);
	    		}else if (t=='(') {
	    			int p=0;
    	    		for(int u=(z.length()-1); u>=0; u--) {
    	    			if(z.charAt(u)==')') {
    	    				p=u;
    	    				break;
    	    			}
    	    		}
    	    	
    	    		String xy6=z.substring(i+1, p);
    	    	    if(xy6.length()==1) {
    	    	    	e.push(x*Float.parseFloat(xy6));
    	    	    }else {
    	    		i=p;
    	    		Float y6 = evaluate(xy6);
    	    		Float product=x*y6;
    	    		e.push(product);
    	    	    }
	    		}else {
	    			
	    		}
	    	}else if (f== '/') {
	    		i++;
	    		float x=(float) e.pop();
	    		char t =z.charAt(i);
	    		float y=0;
	    		if(Character.isDigit(t)==true) {
	    			String l="";
	    			if(i<z.length()-1 && Character.isDigit(z.charAt(i+1))==true){
	    				while(i<z.length() && Character.isDigit(t)==true) {
        	    		t=z.charAt(i);
                        l=l+t;
                        i++;
        	    	}
    	    		i=i-1;
    	    		y=Float.parseFloat(l);
	    			}else {
	    				y=Character.getNumericValue(z.charAt(i));
	    			}
    	    		Float quotient=x/y;
    	    		e.push(quotient);
	    		}else if(Character.isLetter(t)==true){
	    			String l="";
    	    		while(i<z.length() && Character.isLetter(t)==true) {
    	    			 t=z.charAt(i);
                        l=l+t;
                        i++;
        	    	}
    	    		i=i-1;
    	    		for(int j=0; j<scalars.size(); j++) {
    	    			if((scalars.get(j).name).equals(l)) {
    	    				y=(float) (scalars.get(j).value);
    	    			}
    	    		}
    	    		Float quotient=x/y;
    	    		e.push(quotient);
	    		}else if (t=='(') {
	    			int p=0;
    	    		for(int u=(z.length()-1); u>=0; u--) {
    	    			if(z.charAt(u)==')') {
    	    				p=u;
    	    				break;
    	    			}
    	    		}
    	    		String xy6=z.substring(i+1, p);
    	    		i=p;
    	    		Float jkl = evaluate(xy6);
                    Float quotient=x/jkl;
    	    		e.push(quotient);
	    		}else {
	    			
	    		}
	    	}else if (f=='(') {
	    		int p=0;
	    		for(int h=z.length()-1; h>=0; h--) {
	    			if(z.charAt(h)==')') {
	    				p=h;
	    				break;
	    			}
	    		}
	    		String xy=z.substring(i+1, p);
	    		if(xy.length()==1) {
	    			e.push(Float.parseFloat(xy));
	    			i=p;
	    		}else {
	    		Float q = evaluate(xy);
	    		e.push(q);
	    		i=p;
	    		}
	    	}
	    }
	    if(e.size()==1) {
	    	return (float) Float.parseFloat(e.pop()+"");
	    }else {
	    while(e.size()>=1) {
	    	if(e.size()==1) {
	    		break;
	    	}
	    	Float n1 = (float) e.pop();
	    	System.out.println("xn1="+n1);
	        Float n2 = (float) e.pop();
	        System.out.println("xn2="+n2);
	        Float answer=(float) 0;
	        System.out.println("r="+r.peek());
	        if(e.size()==1 && r.size()==0) {
	        	break;
	        }else if(r.peek().equals('+')) {
	        	r.pop();
	            answer=new Float (n1+n2);
	            System.out.println("Answer="+answer);
	        	e.push(answer);
	        }else if (r.peek().equals('-')){
	        	r.pop();
	        	answer=new Float(n2-n1);
	            e.push(answer);
	        } 
	    }
	    System.out.println("returned:"+e.peek());
		return (float) Float.parseFloat(e.pop()+"");
	    }
    }
    private float evalArrays(String w) {
    	return 0;
    }
    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
    		for (ArraySymbol as: arrays) {
    			System.out.println(as);
    		}
    }

}
