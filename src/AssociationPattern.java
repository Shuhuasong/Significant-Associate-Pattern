import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Created by Shuhua Song
 */
public class AssociationPattern {
    static int numRows, numCols, numOrder;
    static double threshold;
    String inputExpression;
    Map<String, Integer>[] typeToNum;
    Map<String, String> colToLetter;
    Map<String, String> letterToCol;
    Map<String, Integer> colToIndex;
    Map<String, Integer> inputVarToVal;
    Map<String, Integer> patternToFreq;
    Map<String, Integer>[] colToFreq;
    static String[][] dataAry, numAry;
    String[] inputVariables, inputValues;
    int[] inputColIdx;
    String[] colLetters;
    int totalCount = 0;
    int totalPattern = 0;
    double expressProb = 0.0;
    double termProb = 0.0;
    DecimalFormat decimalFormat = new DecimalFormat("#.000");
    //------------------------------------------------
    String[] patternType;
    String[] paterrnStatus;
    double[] patternProb;
    String[] inValues;
    int[] colNumStates;
    TreeSet<String> allExprPatterns;
    TreeMap<String, Integer> patternToAllFreq;
    public AssociationPattern(int numRows, int numCols, double threshold, int numOrder){
        this.numRows = numRows;
        this.numCols = numCols;
        this.threshold = threshold;
        this.numOrder = numOrder;
        typeToNum = new HashMap[numCols];
        colToFreq = new HashMap[numCols];
        colToLetter = new HashMap<>();
        letterToCol = new HashMap<>();
        colToIndex = new HashMap<>();
        inputVarToVal = new HashMap<>();
        patternToFreq = new HashMap<>();
        for(int i=0; i<numCols; i++){
            typeToNum[i] = new HashMap<>();
        }
        for(int i=0; i<numCols; i++){
            colToFreq[i] = new HashMap<>();
        }
        dataAry = new String[numRows][numCols];
        numAry =  new String[numRows][numCols];
    }

    private void loadData(Scanner inputFile, String[][] dataAry,BufferedWriter outFile) throws IOException {
        int currRow = 0;
        String s;
        while(inputFile.hasNextLine() && currRow < numRows) {
            s = inputFile.nextLine();
           // System.out.println("--  " + s);
            String[] parts = s.split("\\s+");
            int partsLen = parts.length;

            for(int c = 0; c < partsLen; c++) {
                dataAry[currRow][c] = parts[c];
                numAry[currRow][c] = parts[c];
            }
            ++currRow;
           // System.out.println();
        }

        for(int c=0; c<numCols; c++){
            int counter = 0;
            for(int r=1; r<numRows; r++){
                String data = dataAry[r][c];
                colToFreq[c].put(data, colToFreq[c].getOrDefault(data,0)+1);
                if(!typeToNum[c].containsKey(dataAry[r][c])){
                    typeToNum[c].put(dataAry[r][c], counter);
                    counter++;
                }
            }
        }
        for(int c=0; c<numCols; c++){
           // System.out.println();
            for(String key : typeToNum[c].keySet()){
                //System.out.println(key + " --##--" + typeToNum[c].get(key));
            }
            for(String key : colToFreq[c].keySet()){
              //  System.out.println(key + "--####--" + colToFreq[c].get(key));
            }
        }

        for(int c=1; c<numCols; c++){
            for(int r=1; r<numRows; r++){
                numAry[r][c] = typeToNum[c].get(numAry[r][c])+"";
            }
        }

        for(int i=1; i<numRows; i++){
            totalCount += Integer.parseInt(numAry[i][numCols-1]);
        }

        outFile.write("---------------------------------------------------------------------------" + "\n");
        for(int c=0; c<numCols; c++){
            char letter = (char)('A'+(char)(c));
            colToLetter.put(dataAry[0][c], letter+"");
            letterToCol.put(letter+"", dataAry[0][c]);
            colToIndex.put(letter+"", c);
            //System.out.println(dataAry[0][c] + " $$$ " + letter + " $$$ " + c);
        }

        colLetters = new String[letterToCol.size()];
        int index = 0;
        for(String key : letterToCol.keySet()){
            //System.out.println(key + " -&&-");
            colLetters[index++] = key;
        }

        for(int c=0; c<numCols; c++){
           // System.out.println(numAry[0][c] + "*******" + colToLetter.get(numAry[0][c]));
            numAry[0][c] = colToLetter.get(numAry[0][c]) + "";
        }


        for(int c=0; c<numCols; c++){
            outFile.write(dataAry[0][c] + "(" + numAry[0][c] + ")" +  "             " );
        }

        outFile.write("\n");
        for(int i = 1; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                outFile.write(dataAry[i][j] + "                   " );
            }
            outFile.write("\n");
        }
    }

    public double parseExpression(String expression, BufferedWriter outFile){
         int start = 0, end = expression.length()-1;
         String subExpre = "";
         while(start < end){
             while(start < end && expression.charAt(start) != '('){
                 start++;
             }
             while(start < end && expression.charAt(end) != ')'){
                 end--;
             }
             if(start < end){ //subExpre = "B:0,C:1"
                 subExpre = expression.substring(start+1, end);
                 //System.out.println("SubExpress = " + subExpre);
                 break;
             }
         }
         String[] variables = subExpre.split(",");
         //A:1,B:0,C:2
        int n = variables.length;
        String[] colVars = new String[n];
        String[] values = new String[n];
        for(int i=0; i<n; i++){
            String term = variables[i];
            String[] terms = term.split(":");
            colVars[i] = terms[0];
            values[i] = terms[1];
            //System.out.println(colVars[i] + "--***&&***--" + values[i]);
        }
        inputVariables = colVars.clone();
        //inputValues = values.clone();
        /*
       1 - B--1
       2 - C--0
       3 - D--2
         */
        int sum = 0;
        for(int r=1; r<numRows; r++){
            boolean match = true;
            for(int k=0; k<n; k++){
                int colIdx = colToIndex.get(colVars[k]);
                if(!numAry[r][colIdx].equals(values[k])){
                    match = false;
                    break;
                }
            }
            if(!match) continue;
            if(match){
                sum += Integer.parseInt(numAry[r][numCols-1]);
               // System.out.println("SUM = " + sum);
            }
        }
        expressProb = (sum+0.0)/totalCount;

        //find input variable and corresponding value
        //Pr(B:1,C:0,D:2)
        // Pr(B:1) * Pr(C:0) * Pr(D:2)
        for(int k=0; k<n; k++){
            int colIdx = colToIndex.get(colVars[k]);
            int total = 0;
            for(int r=1; r<numRows; r++){
                if(numAry[r][colIdx].equals(values[k])){
                    total += Integer.parseInt(numAry[r][numCols-1]);
                }
            }
            inputVarToVal.put(colVars[k], total);
           // System.out.println(colVars[k] + "---@@@@--" + total);
        }
        return expressProb;
    }

    public void patternFreq_cal(BufferedWriter outFile) throws IOException {
        Map<String, Integer> currPatternToFreq = new HashMap<>();
        totalPattern = 0;
        int n = inputVariables.length;
        inputColIdx = new int[n];
        StringBuilder input = new StringBuilder();
        for(int i=0; i<n; i++){
            input.append(inputVariables[i]).append(",");
            inputColIdx[i] = colToIndex.get(inputVariables[i]);
           // System.out.println("Column: " + inputVariables[i] + "---- " + "Index: " + inputColIdx[i]);
            //System.out.println(inputColIdx[i] + "--****--" + inputVariables[i]);
        }
        input.deleteCharAt(input.length()-1);
        inputExpression = input.toString();
        for(int r=1; r<numRows; r++){
            StringBuilder sb = new StringBuilder();
            for(int c=0; c<inputColIdx.length; c++){
                int curVal = Integer.parseInt(dataAry[r][inputColIdx[c]]);
                if(curVal >= colNumStates[inputColIdx[c]]){    //Checking the validation of input
                    outFile.write("Alert : the data for this data point is not valid!!!" + "\n");
                    break;
                }
                if(c!=inputColIdx.length-1){
                    sb.append(dataAry[r][inputColIdx[c]]).append(",");
                }else{
                    sb.append(dataAry[r][inputColIdx[c]]);
                }
            }
            String currPattern = sb.toString();
            currPatternToFreq.put(currPattern, currPatternToFreq.getOrDefault(currPattern,0)+1);
        }
        patternType = new String[currPatternToFreq.size()]; // "0,1  1,0  1,1  1,2
        paterrnStatus = new String[currPatternToFreq.size()]; // "Threshold Past ? Yes : No"
        for(String key : currPatternToFreq.keySet()){
            totalPattern += currPatternToFreq.get(key);
        }
        patternProb = new double[currPatternToFreq.size()];
        int i = 0;

        for(String key : currPatternToFreq.keySet()){
            patternType[i] = key;
            patternProb[i] = (currPatternToFreq.get(key)+0.0)/(double)totalPattern;
            //System.out.println(key + " " + currPatternToFreq.get(key) +  "  " + totalPattern);
            i++;
        }

        patternToFreq = currPatternToFreq ;

        //Threshold Test
        for(int j=0; j<patternProb.length; j++){
            if(patternProb[j] > threshold){
                paterrnStatus[j] = "Yes";
            }else{
                paterrnStatus[j] = "No";
            }
            //System.out.println("patternProb = " + patternProb[j] + "-----" + "threshold = " + threshold);
        }
        thresholdTestPrint(paterrnStatus, outFile);
        outFile.write("-----------------------------Statiscally Significant Test-------------------------" + "\n");
        for(int j=0; j<paterrnStatus.length; j++){
            if(paterrnStatus[j].equals("Yes")){
                termProb = patternProb[j];
                staticSignificantTest(patternType[j], outFile);
                outFile.write("------------------------------------------------------------------------" + "\n");
            }
        }
    }

    public void thresholdTestPrint(String[] paterrnStatus,BufferedWriter outFile) throws IOException {

        int k = 0;
        outFile.write("-----------------------------------Threshold Test---------------------------------" + "\n");
        outFile.write("\n");
        outFile.write("Total = " + totalPattern +  "\n");
        outFile.write("Pattern" + "(" + inputExpression + ")     Frequency Count       Pattern Probability        Passing Threshold Test" + "\n");
        for(String key : patternToFreq.keySet()){
            double prob = (patternToFreq.get(key)*1.0)/totalPattern;
            outFile.write(key + "                  " + patternToFreq.get(key) + "                   " + prob + "                     "+ paterrnStatus[k++] + "\n");
        }
    }

    private void staticSignificantTest(String pattern, BufferedWriter outFile) throws IOException {
        outFile.write("----------------------- Test pattern: (" + pattern + ") --------------------------------" + "\n");
         inValues = pattern.split(",");
         double left = leftHandSide(totalPattern, inValues, outFile);
        outFile.write("  = " + left + "\n");
        outFile.write("values : " + "\n");
         for(String var : inValues){
             outFile.write(var + "  ");
         }
        outFile.write("\n");
         double right = rightHandSide(totalPattern, outFile);
        outFile.write("Right Hand Side : " + right + "\n");
        if(left < right){
            outFile.write("Left Hand Side < Right Hand Side" + "\n");
            outFile.write("Conclusion: " + inputExpression + " is not a statiscally significant association pattern because it fails" +
                    " the independency test" + "\n");
        }else{
            outFile.write("Left Hand Side > Right Hand Side" + "\n");
            outFile.write("Conclusion: " + inputExpression + " is a statiscally significant association pattern because it passes" +
                    " the independency test"+ "\n");
        }
    }

    // MI(x1, x2, ..., xn)
    public  double leftHandSide(double N, String[] vals, BufferedWriter outFile) throws IOException {
        outFile.write("Left Hand Side: MI(" + inputExpression + ")" );
        double result = 0;
        double y = 1;
        for(int i=0; i<vals.length; i++){
            int col = inputColIdx[i];
            y *= (colToFreq[col].get(vals[i])+0.0)/totalPattern;
           // System.out.println(colToFreq[col].get(vals[i]) + "--!-- " + vals[i] + " ---!-- " + ((colToFreq[col].get(vals[i])+0.0)/totalPattern));
        }
        y = termProb/y;
        result = Math.log(y)/Math.log(2); //
        result = Math.round(result*10000.0)/10000.0;
        return result;
    }

    //  [1/pr(x1, x2, ..., xn)]*(X^2/2*N)^[(E/E')^O/2]
    public  double rightHandSide(double N, BufferedWriter outFile) throws IOException {
        double pr = termProb;
        double chi_square = getChi_Square(N, outFile);
        double result = 0;
        double E_maxEntropy = getMaxEntropy(N, outFile);
        double E_sysEntropy = getSystemEntropy(N, outFile);
        double ESys_Emax = (E_sysEntropy/E_maxEntropy);
        outFile.write( "E^/E' = " + ESys_Emax + "\n");
        double O = (inputVariables.length+0.0)/2; //
        outFile.write("O/2 = " + O + "\n");
        double expo = Math.pow((E_sysEntropy+0.0)/E_maxEntropy, O);
        outFile.write("(E^/E')^(O/2) = " + expo + "\n");
        double temp_chi = 0.0;
        if(numOrder==2){
            temp_chi = (chi_square/(2.0*N));
            result = temp_chi;
        }else{
            temp_chi = Math.pow((chi_square/(2.0*N)), expo);
            result = (1/pr) * temp_chi;
        }
        outFile.write("Chi-square/2N = " + (chi_square/(2.0*N)) + "\n");
        outFile.write("1/Pr = " + 1/pr + "\n");
        outFile.write("(Chi-square/2N)^(E^/E')^(O/2) = " + temp_chi + "\n");
        outFile.write("rightHandSide = " + result + "\n");
        return result;
    }
    //Max_Entropy = log_2(# of combination of states among all the variables)
    private  double getMaxEntropy(double N, BufferedWriter outFile) throws IOException {
        double totleCombState = 1;
        for(int c : inputColIdx){
            //System.out.print(typeToNum[c].size() + " ");
            totleCombState *= colToFreq[c].size();
        }
        double result = Math.log(totleCombState)/Math.log(2);
        //result = Math.round(result*10000.0)/10000.0;
        outFile.write("maxEntropy = " + result + "\n");
        return result;
    }
    //System_Entropy = - SUM_i (Pr_i * log_2(Pr_i))
    private  double getSystemEntropy(double N, BufferedWriter outFile) throws IOException {
        int[] elems = new int[patternToFreq.size()];
        int k = 0;
        for(String pattern : patternToFreq.keySet()){
            elems[k++] = patternToFreq.get(pattern);;
        }
        double sysEntropy = 0;
        for(int elem : elems){
            sysEntropy += (elem*1.0/N) * Math.log(N*1.0/elem)/Math.log(2);
        }
        sysEntropy = Math.round(sysEntropy*10000.0)/10000.0;
        outFile.write("SysEntropy = " + sysEntropy + "\n");
        return sysEntropy;
    }

    private double getChi_Square(double N, BufferedWriter outFile) throws IOException {
        double Oi = N * termProb;
        double ei = N;
        for(int i=0; i<inValues.length; i++){
            int col = inputColIdx[i];
            ei *= (colToFreq[col].get(inValues[i])+0.0)/totalPattern;
        }
        Oi = Double.parseDouble(decimalFormat.format(Oi));
        ei = Double.parseDouble(decimalFormat.format(ei));
        double result = Math.pow(Oi-ei, 2)/ei;
        result = Math.round(result*10000.0)/10000.0;
        outFile.write("Oi = " + Oi + "\n" );
        outFile.write("ei = " + ei + "\n");
        outFile.write(" termProb = " + termProb + "\n");
        outFile.write("Chi-square = " + result + "\n");
        return result;
    }


    private void getNumStatesInput(String numStatesSt) {
        String[] splits = numStatesSt.split("\\s+");
        colNumStates = new int[numCols];
        int i = 0;
        for(int c=1; c<numCols; c++){
            colNumStates[c] = Integer.parseInt(splits[i]);
            i++;
        }
    }


    private TreeSet<String> getPatternPermute(int numOrder,BufferedWriter outFile) throws IOException {
        Set<String> allPatterns = new TreeSet<>();
        StringBuilder sb = new StringBuilder();
        backtrack(1, numOrder, sb, allPatterns);
        int size = allPatterns.size();
        outFile.write("All combinations of " + numOrder + "_Order patterns(column representation): size = " + size + "\n");
        for(String p : allPatterns){
            outFile.write("Pattern-letter = " + p + "\n");
        }
        allExprPatterns = new TreeSet<>();
        patternToAllFreq = new TreeMap<>();
        for(String p : allPatterns){
            StringBuilder comSb = new StringBuilder();
            int freq = 1;
            String currPat = "(";
            char[] letters = p.toCharArray();
            for(char letter : letters){
                int letterIdx = colToIndex.get(letter+"");
                currPat += letter + " ";
                //System.out.println("colNum = " + letter + " " + letterIdx + " " + colNumStates[letterIdx]);
                freq *= colNumStates[letterIdx];
            }
            //System.out.println("pattern = " + p + "------" + "total = " + freq);
            currPat +=  ")";
            patternToAllFreq.put(currPat, freq);
            comSb.append("Pr(");
            for(char a : letters){
                comSb.append(a);
                comSb.append(":0,");
            }
            comSb.deleteCharAt(comSb.length()-1);
            comSb.append(")");
            allExprPatterns.add(comSb.toString());
        }
        //System.out.println("size = " + allExprPatterns.size());
        for(String pat: allExprPatterns){
            //System.out.println("Pattern-expression = " + pat);
        }
        outFile.write("\n");
        outFile.write("Order       " + "Pattern Variables        " + "Number of permutation" + "\n");
        for(String currPat : patternToAllFreq.keySet()){
            outFile.write(numOrder + "             " + currPat + "                          " + patternToAllFreq.get(currPat) + "\n");
        }
        return allExprPatterns;
    }

    private void backtrack(int start, int numOrder,  StringBuilder sb, Set<String> allPatterns) {
        if(numOrder==0){
            allPatterns.add(sb.toString());
            return;
        }
        for(int i=start; i<colLetters.length; i++){
            sb.append(colLetters[i]);
            backtrack(i+1, numOrder-1, sb, allPatterns);
            sb.deleteCharAt(sb.length()-1);
        }
    }

    public static void main(String[] args) throws IOException {
        //Read input
        Scanner inputFile = new Scanner(new FileReader(args[0]));
        String inputName = args[0];
        String thresholdSt = args[1];
        String order = args[2];
        int numOrder = Integer.parseInt(order);
        double threshold = Double.parseDouble(thresholdSt);
        String inputFileName = inputName.substring(0, 5) + "_" + order + "Order_output.txt";
        BufferedWriter outputFile = new BufferedWriter(new FileWriter(new File(inputFileName)));
        outputFile.write("threshoud = " + threshold + "\n");
        int numRows = inputFile.nextInt();
        int numCols = inputFile.nextInt();
        inputFile.nextLine();
        String numStatesSt = inputFile.nextLine();
        outputFile.write("states = " + numStatesSt + "\n");


        AssociationPattern pattern = new AssociationPattern(numRows, numCols, threshold, numOrder);
        String[][] dataAry = pattern.dataAry;

        pattern.loadData(inputFile, dataAry,outputFile);
        pattern.getNumStatesInput(numStatesSt);

        outputFile.write("------------------#########################-------------------" + "\n");
        int total = pattern.totalPattern;
        Map<String, String> colToLetter = pattern.colToLetter;
        Set<String> allExprPatterns = pattern.getPatternPermute(numOrder,outputFile);
        for(String pat : allExprPatterns){
            double  expressProb = pattern.parseExpression(pat,outputFile);
            pattern.patternFreq_cal(outputFile);
        }
        outputFile.close();
        inputFile.close();
    }

}
