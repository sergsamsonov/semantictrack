# SemanticTrack
SemanticTrack is a bug tracking system written using Spring MVC framework. 
The functionality allows users to search tickets considering synonyms for requested terms and interpretations for requested acronyms. 
The search results are ranked in order of decreasing their TF-IDF (“term frequency-inverse document frequency” ) weights. 
The approach considers frequencies of requested terms in ticket’s fields and, as well, importance of these terms in a collection of all tickets. 

For example, software product customer has the following problem: branch item of BBAN(Basic Bank Account Number) mask is processed incorrectly during account number generation. And the solution to the problem is described in the ticket with Issue "ANS: subdivision token processing". So we can see "subdivision" is a synonym for "branch" and "token" is a synonym for "item". Also acronym ANS means Account Number Service. 

To solve the problem supporting specialist tries to find tickets which correspond with the issue. For this reason the employee types the following request: "BBAN branch item". The search interface of SemanticTrack is depicted in Figure 1.

<p align="center">
<img src="https://github.com/sergsamsonov/semantictrack/blob/master/request.jpg">
<br><br>
<b>Figure 1. The search interface of SemanticTrack</b>
</p>

SemanticTrack allows the specialist to get the desired ticket with the text "ANS: subdivision token processing". Search results corresponded with the request "account number branch item" are depicted in Figure 2.

<p align="center">
<img src="https://github.com/sergsamsonov/semantictrack/blob/master/results.jpg">
<br><br>
<b>Figure 2. Search results</b>
</p>
