package  fundamentals;

import java.util.*;
import java.util.Stack;

public class Evaluator {
    private String expression;
    private Integer result;

    public Evaluator(String expression) {
        this.expression = expression;
        this.result = null;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    public void evaluate() {
        if (this.expression.isEmpty())
            return;
        Deque<Integer> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(this.expression);
        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();
            try {
                switch (element) {
                    case "+":
                        stack.push(stack.pop() + stack.pop());
                        break;
                    case "*":
                        stack.push(stack.pop() * stack.pop());
                        break;
                    default:
                        stack.push(Integer.parseInt(element));
                }
            } catch (EmptyStackException e) {
                throw new IllegalArgumentException("Expression not valid");
            }
        }
        this.result = stack.pop();
        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("Expression not valid");
        }
    }

    public Integer getResult() {
        if (result == null)
            return null;
        return this.result;
    }

    public static void main(String[] args) {
        Evaluator eva = new Evaluator("4 12 +");
        eva.evaluate();
        System.out.println("the result of the calculation is " + eva.getResult());
    }
}
