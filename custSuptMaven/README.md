Purpose: This Maven project created from scratch in Eclipse to build JWA book 'customer-support' app as it progresses through chapters, starting with chapter 3. Using Git and Github for version control.

TODO:
reading chap 17.

Notes/History:
10/30: chap 19/20- started mySQL/HibernateORM section
--PART III- data persistence
10/28/16: chap 17- REST/SOAP services-machine to machine communication.  ran & tested web-service imported project and in firefox RESTClient Add-on.  have not implemented chap 17 changes into custSuptMaven at this time.  -v14 version does similar REST Service w/ Tickets as web-service did with Acccounts. It will rely on Validation items covered in chap 16.  Note web services are different than websockets discussed in chap 10 for chat functionality.
10/25/16: chap 16- validation.  can do most of this via js in browser,  didnt update
10/23/16: chap 15- Internationalization- not implemented,  browsed through chap
10/16/16: finished both java and jsp portions, w/ all subfolders.  tested, fixed a few minor bugs and tested all functionality successfully.  New
 Spring MVC project setup properly up to chapter 14 (corresponds to customer-support-v11).
10/15/16: finished documenting config package classes and w/i site- the Authenticator group w/ CSR comments.  each method group in site package 
  (Authentication, Session, Ticket, and Chat) follows this methodology.  Although at this point I am not sure why each of these didnt have its own
  site/ sub package like the chat did.
10/09/16: updated pom for spring dependencies, implemented in java/config packages.  working on creating/migrating site package classes.  left off at D
  (defaultSessionRegistry).  have not done chat or tag sub packages yet. completed POM dependencies.
09/30/16: merged git chap 8 branch into master and then created new git/hub branch for SpringMVC.  began re-factoring project from chap 8 version.
--PART II- Spring MVC
09/24/16: finished up to chap 11 logging.  cmi = customer-support-v9
09/17/16: finished up to websocket adds.  chat sessions corresponding maven import [cmi] = customer-support-v8.
08/05/16: completed chap 7/8/9 jsp tag library mod's.  created new chap 8 branch in github.  plan to merge back to master before Spring chapters
07/24/16: created up to chap 6- jsp expression language.  started seeing a project error related to jsp's and reference to resolving to 
  type on java classes.  Possible temporary error related to javax.el- will see if it clears after chap 7. the project does clean/ 	compile and 
  run successfully, so test is ok.  but project has error decorator.
07/10/16: created basic Maven project and populated  up to Chapter 5.  project creation according to SGF_ProjectsJavaWebAppBook/ maven README file instructions. tested ok.
--PART I- Java EE and Websockets