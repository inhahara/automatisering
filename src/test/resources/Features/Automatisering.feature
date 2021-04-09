Feature: Mailchimp Sign Up
Sign Up as User At https://login.mailchimp.com/signup/. An user is allowed to signup if he:
* provides an email address
* provides an unique username
* provides a password which contains one lowercase, one uppercase, one special character, one number and 8 characters minimum  

Scenario Outline: Tests
Given I go to the Mailchimp Sign Up site
When I have entered "<emailLocalPart>" into the sign up page
And I have also entered "<username>" into the sign up page
And I have entered "<password>" into the sign up page too
And I press Accept All Cookies
And I press Sign Up
Then The result of Sign Up should be "<result>"

Examples:
|emailLocalPart|username|password|result| # allt går som förväntat
|inhahara|inhahara|Inhahara123!|Success|
|inhahara2|inhaharainhaharainhaharainhaharainhaharainhaharainhaharainhaharainhaharainhaharainhaharainhaharainhahara|Inhahara123!|Failure| # långt användarnamn(mer än 100 tecken)
|inhahara3|haragote@gmail.com|Inhahara123!|Failure| # användare redan upptagen
||inhahara4|Inhahara123!|Failure| # e-mail saknas
