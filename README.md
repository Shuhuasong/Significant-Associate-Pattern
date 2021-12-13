# Data Mining & Warehouse-Significant-Associate-Pattern

# Project 3 - *Discovery Significant Associate Pattern*

Time spent: **13** hours spent in total

## What's the problem I am solving ? 

The program is aiming to find the significant association patterns for a large data set(market basket transaction) according to the two criteria :

(1)	Support Measure (cirteria-1) </br>
(2)	Interestingness/level of dependency (criteria-2) </br>
Mutual information measure in event pattern level:  log_2[Pr(A:0,B:1)/(Pr(A:0) * Pr(B:1))] </br>
(3)	Discovering significant association patterns requires: </br>
-	Join probability information : Pr(x1, x2, ….., xn) </br>
-	Marginal Probability information: Pr(x1), Pr(x2), …, Pr(xn) </br>
-	Appropriate support threshold a related to population size N </br>
(4)	Properties for significant association patterns:  </br>
-	Support measure Pr(x1, x2, …xn) > a%  </br>
-	MI(x1, x2, …., xn)  > (1/ Pr(x1, x2, ….., xn)) ^[(lamda^2/2N)^[(E^/E’)^(O/2)] </br>
-	MI(x1, x2, …., xn)  = Log_2Pr(x1, x2, …, xn)/(Pr(x1)*Pr(x2)*…*Pr(xn)) </br>
-	N = smaple population size </br>
-	(lamda^2) = Pearson Chi-square test statistic defined as (Oi-Ei)^2/Ei </br>
-	E^ = Expected entropy measure of estimated probability model </br>
-	E’ = Maximal possible entropy of estimated probability model </br>
-	O = order of the association pattern (n is this case)  </br>
(5)	Unfortunately, statistical convergence does not behave well in high order patterns with multiple variables. </br>



- [X] User can view the last 20 posts submitted to "Instagram".
- [X] The user should switch between different tabs - viewing all posts (feed view), compose (capture photos form camera) and profile tabs (posts made) using fragments and a Bottom Navigation View. (2 points)
- [X] User can pull to refresh the last 20 posts submitted to "Instagram".

The following **optional** features are implemented:

- [ ] User sees app icon in home screen and styled bottom navigation view
- [ ] Style the feed to look like the real Instagram feed.
- [ ] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [ ] Show the username and creation time for each post.
- [ ] User can tap a post to view post details, including timestamp and caption.
- [ ] User Profiles
      - [ ] Allow the logged in user to add a profile photo
      - [ ] Display the profile photo with each post
      - [ ] Tapping on a post's username or profile photo goes to that user's profile page and shows a grid view of the user's posts 
- [ ] User can comment on a post and see all comments for each post in the post details screen.
- [ ] User can like a post and see number of likes for each post in the post details screen.



## Video Walkthrough
<img src='https://recordit.co/W4VCL3DOCE.gif' width='200' alt='Video Walkthrough' />
<img src='https://recordit.co/E37Mfvi7eM.gif' width='200' alt='Video Walkthrough' />

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

