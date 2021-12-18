# Data Mining & Warehousing

# Project 3 - *Discovery Significant Associate Pattern*

## 1. What the problem is solving ? 

The program is aiming to find the significant association patterns for a large data set(market basket transaction) according to the two criteria, the program can get the 2nd, 3rd or high order pattern, and decide which pattern passing the support and which pattern is statiscally significant associate pattern. 

(1)	Support Measure (cirteria-1) </br>
(2)	Interestingness/level of dependency (criteria-2) </br>
Mutual information measure in event pattern level:  log_2[Pr(A:0,B:1)/(Pr(A:0) * Pr(B:1))] </br>
(3)	Discovering significant association patterns requires: </br>
-	Join probability information : Pr(x1, x2, ….., xn) </br>
-	Marginal Probability information: Pr(x1), Pr(x2), …, Pr(xn) </br>
-	Appropriate support threshold a related to population size N </br>
[ ]	Properties for significant association patterns:  </br>
-	Support measure Pr(x1, x2, …xn) > a%  </br>
-	MI(x1, x2, …., xn)  > (1/ Pr(x1, x2, ….., xn)) ^[(lamda^2/2N)^[(E^/E’)^(O/2)]  </br>
-	MI(x1, x2, …., xn)  = Log_2Pr(x1, x2, …, xn)/(Pr(x1)*Pr(x2)*…*Pr(xn)) </br>
-	N = smaple population size </br>
-	(lamda^2) = Pearson Chi-square test statistic defined as (Oi-Ei)^2/Ei </br>
-	E^ = Expected entropy measure of estimated probability model </br>
-	E’ = Maximal possible entropy of estimated probability model </br>
-	O = order of the association pattern (n is this case)  </br>

(4)	Unfortunately, statistical convergence does not behave well in high order patterns with multiple variables. </br>

## 2. Status of Code

   The program run well, and it can help us to detect with pattern pass the threshold, and after that, it can help us calculate the left hand side and right hand side, in order to check if it is statistically significant association pattern not. 
   
## 3. Disclose limitation and discussed the lessons learned(All the limiation on the following have been fixed, Please look at video walk through-Improving Version)  

(1)	The program doesn’t check the input which is out of range of status. </br>
(2)	The program only capture the pattern according to the input, it doesn’t permute very kind of patters, for example, if there are A, B, C, D, E, F 6 columns, the program should permute all the 3rd order patters(ABC, BCD, CDE, DEF, …)

## 4. Software Solution
###      Video Walkthrough
<img src='https://recordit.co/NU6wYDlBfq.gif' width='600' alt='Video Walkthrough' />
##      Improving Version
<img src='http://g.recordit.co/iry8W9TJeG.gif' width='600' alt='Video Walkthrough' />

## 5. how to run the code?
      In IntelliJ IDE: 
      1) Place the input file under the src folder
      2) Configure the input parameter with 3 parameter(the name of input file, threshold, patter order), eg.  inputData1.txt 0.13 Pr(B:0,C:0,D:0)
      Look at the video walkthrough


## 6. Resource reference
   https://www-users.cse.umn.edu/~kumar001/dmbook/ch6.pdf 


