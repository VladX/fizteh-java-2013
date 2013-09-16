package ru.fizteh.fivt.students.visamsonov.calculator;

import java.util.Stack;
import java.util.Collections;
import java.util.StringTokenizer;
import java.math.BigInteger;

public class Calculator {
	
	private static int inputRadix = 17;
	private static int outputRadix = 17;
	
	private static boolean isOperator (final String token) {
		return "+-*/".contains(token);
	}
	
	private static int priority (final String token) {
		if ("+-".contains(token)) {
			return 0;
		}
		return 1; /* higher priority for "*" and "/" */
	}
	
	/* simple version of Shunting-yard algorithm */
	private static Stack<String> parseToReversePolish (final String expression) {
		Stack<String> operations = new Stack<String>();
		Stack<String> reversePolish = new Stack<String>();
		
		StringTokenizer st = new StringTokenizer(expression, "()+-*/", true);
		boolean previousWasOperator = false;
		
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("(")) {
				operations.push(token);
				previousWasOperator = false;
			}
			else if (token.equals(")")) {
				while (!operations.empty() && !operations.lastElement().equals("(")) {
					reversePolish.push(operations.pop());
				}
				operations.pop();
				previousWasOperator = false;
			}
			else if (previousWasOperator && token.equals("-")) {
				token = st.nextToken();
				reversePolish.push("-" + token);
				previousWasOperator = false;
			}
			else if (isOperator(token)) {
				while (!operations.empty() && isOperator(operations.lastElement()) &&
				       priority(token) <= priority(operations.lastElement())) {
					reversePolish.push(operations.pop());
				}
				operations.push(token);
				previousWasOperator = true;
			}
			else { /* it is a number */
				reversePolish.push(token);
				previousWasOperator = false;
			}
		}
		while (!operations.empty()) {
			reversePolish.push(operations.pop());
		}
		Collections.reverse(reversePolish);
		
		return reversePolish;
	}
	
	private static String prepareExpression (final String expression) {
		return ("(" + expression + ")").replace(" ", "").replace("(-", "(0-"); /* remove all spaces from expression */
	}
	
	private static String calculate (String expression) {
		expression = prepareExpression(expression);
		Stack<String> reversePolish = parseToReversePolish(expression);
		Stack<BigInteger> answer = new Stack<BigInteger>();
		
		while (!reversePolish.empty()) {
			String token = reversePolish.pop();
			if (isOperator(token)) {
				BigInteger x = answer.pop();
				BigInteger y = answer.pop();
				BigInteger result = BigInteger.valueOf(0);
				if (token.equals("+")) {
					result = y.add(x);
				}
				else if (token.equals("-")) {
					result = y.subtract(x);
				}
				else if (token.equals("*")) {
					result = y.multiply(x);
				}
				else if (token.equals("/")) {
					result = y.divide(x);
				}
				answer.push(result);
			}
			else {
				answer.push(new BigInteger(token, inputRadix));
			}
		}
		
		return answer.pop().toString(outputRadix);
	}
	
	public static void main (String[] args) {
		String expression = "";
		for (String arg : args) {
			expression += arg;
		}
		try {
			System.out.println(calculate(expression));
		}
		catch (java.util.EmptyStackException | java.lang.NumberFormatException e) {
			System.err.println("Error: mathematical expression is incorrect.");
		}
	}
}
