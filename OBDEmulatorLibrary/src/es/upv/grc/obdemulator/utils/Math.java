/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.grc.obdemulator.utils;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Math {

    private static final char reg1 = '[';
    private static final char reg2 = ']';

    public static double calculate(String formula, HashMap<String, Double> values) throws Exception {
        Object[] keys = values.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            String key = (String) keys[i];
            double value = values.get(key);
            formula = formula.replace(key, String.valueOf(value));
        }
        formula = format(formula);
        boolean exit = false;
        while (!exit) {
            exit = isNumberR(formula);
            formula = calculate(formula);
        }
        return toNumber(formula);
    }

    private static String format(String formula) {
        formula = formula.trim();
        formula = formula.replace(" ", "");
        formula = formula.replace("+", " + ");
        formula = formula.replace("-", " - ");
        formula = formula.replace("/", " / ");
        formula = formula.replace("*", " * ");
        formula = formula.replace("(", "( ");
        formula = formula.replace(")", " )");
        String[] operators = formula.split(" ");
        String res = "";

        for (int i = 0; i < operators.length; i++) {
            if (operators[i].equals("-") && isNumber(operators[i + 1])) {
                operators[i] = "+";
                operators[i + 1] = reg1 + "-" + operators[i + 1] + reg2;
            } else if (isNumber(operators[i])) {
                operators[i] = reg1 + operators[i] + reg2;
            }
            if (operators[i] != null && !operators[i].equals("")) {
                res += operators[i] + " ";
            }
        }

        return res.trim();
    }

    private static String calculate(String operation) throws Exception {
        String resp = "";
        String[] operators = operation.split(" ");
        boolean op = false;

        for (int i = 0; i < operators.length; i++) {
            if (i == 0 && operators[i].equals("-") && isNumberR(operators[i + 1])) {
                double res = calculateRes("[0]", operators[i + 1], operators[i]);
                operators[i + 1] = reg1 + String.valueOf(res) + reg2;
                operators[i] = "";
                op = true;
            } else if (i != 0 && isOperation(operators[i]) && !operators[i - 1].equals("")) {
                if (isNumberR(operators[i - 1]) && isNumberR(operators[i + 1])) {
                    if (isPrior(operators[i]) || ((operators.length > i + 2 && !isPrior(operators[i + 2])) || operators.length <= i + 3)) {
                        double res = calculateRes(operators[i - 1], operators[i + 1], operators[i]);
                        operators[i - 1] = reg1 + String.valueOf(res) + reg2;
                        operators[i] = "";
                        operators[i + 1] = "";
                        op = true;
                    }
                }
            } else if (operators[i].equals(")") && operators[i - 2].equals("(")) {
                operators[i - 2] = operators[i - 1];
                operators[i - 1] = "";
                operators[i] = "";
                op = true;
            }
        }

        for (String operator : operators) {
            if (operator != null && !operator.equals("")) {
                resp += operator + " ";
            }
        }

        if (!op && !isNumberR(resp.trim())) {
            throw new Exception("Incorrect Format");
        }

        return resp.trim();
    }

    private static boolean isOperation(String op) {
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }

    private static boolean isPrior(String op) {
        return op.equals("*") || op.equals("/");
    }

    private static boolean isNumber(String op) {
        try {
            Double.parseDouble(op);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isNumberR(String op) {
        try {
            if (op.charAt(0) == reg1 && op.charAt(op.length() - 1) == reg2) {
                Double.parseDouble(op.substring(1, op.length() - 1));
                return true;
            }

            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static double toNumber(String op) throws Exception {
        return Double.parseDouble(op.substring(1, op.length() - 1));
    }

    private static double calculateRes(String op1, String op2, String accion) {
        try {
            if (accion.equals("+")) {
                return toNumber(op1) + toNumber(op2);
            } else if (accion.equals("-")) {
                return toNumber(op1) - toNumber(op2);
            } else if (accion.equals("*")) {
                return toNumber(op1) * toNumber(op2);
            } else if (accion.equals("/")) {
                return toNumber(op1) / toNumber(op2);
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }
    
    public static void main(String args[]) {
        HashMap<String, Double> values = new HashMap<String, Double>();
        values.put("<010D>", (double)22);
        try {
            double val = Math.calculate("<010D>*2", values);
            System.out.println(val);
        } catch (Exception ex) {
            Logger.getLogger(Math.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
