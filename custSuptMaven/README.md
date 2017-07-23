Purpose: This Maven project created from scratch in Eclipse to build JWA book 'customer-support' app as it progresses through chapters, starting with chapter 3. Using Git and Github for version control. To start project start server w/custSuptMaven and oauth-client (as of chap 28), then open browser to http://localhost:8080/ to get to login.  Still have 45 sec server timeout issue that occurs on first run.  havent changed this window in server because 2nd run is faster and has always started the 2nd time ok. GitHub project is updated to chap 28 [last chapter] changes.

TODO:
None at this time.  Finished book with main chapters complete and tested.  Some oauth items noted below.  there are also several improvments that could be made from a usability and appearance standpoint.

Supporting docs:  see README files under SGF_Projects\JavaWebAppBook folder for project creation/basic notes.

Notes/History:
07/21/17: need to make a list of things to independently work on as improvements, but book chapters themselves are complete and functionally work.
05/06/17: completed chap 28 Oauth.  based on book version created a separate project oauth-client, which is used as part of the testing.  Basic function works ok, but there are some items towards end of chapter that arent working quite as I expected.  Need to add details based on further testing.  at this point I am not exactly sure how to address them- it would take some research online.
04/05/17: completed chap 27 changes to enable Spring Authorizations.  Note difference between Authentication- verifying that the user is who they say they are and Authorization- what resources that user has legitimate access to.  chap 26 was authentication, chap 27 is authorization and chap 28 is access to web services (REST/SOAP)
--PART IV- Spring Security
01/22/17: chap24 makes some pretty extensive changes to site\entities\TicketEntity and site\Ticket classes to address instant type variable 
 and attachment mgmt w/DefaultTicketService class.  It eliminates the DTO function that ticket.java performed w/ TicketEntity- pg 720.  wrapping my head 
 around the changes.  there are also DB structure changes and table additions needed- pg 721.
 In addition to that the app itself was upgrade to allow attachments to comments, as well as the original ticket itself.  MySQL attachment table needs 
 to be refactored and two new tables added.
01/14/17: added and tested chap 23 search functions.  used with MySql FULLTEXT searches via indexes.  both java and db changes as describeed in customer-support-v17 and chap 23 notes
12/13: corrected and tested Spring data jap i18n messages.
12/10:mostly finished Spring Data JPA- still need to fix UI messages for jsps (i18n folder).  Ran into a statup problem w/ a contextLoader listener failure
  short answer need to ensure in config package(s) paths- and rootContextConfiguration class specifically that you have all the right paths.  upon edits/adaptation
  I didnt check the path closely enough and had ..custMavenSupt.repositories instead of the correct ..custSuptMaven.site.repositories and app was throwing
  a contextLoader failure on one of the classes (defaultAuthenticationService specifically) this took some time to diagnose.
11/28: finished chap 21 base MySQL/Hibernate implementation.  all changes made and tested successfully.  Updated Eclipse Tomcat version context.xml file and validated compatible POM dependency versions.  Compatible versions in POM is critical.  Made it to match book, but I am not sure how you ensure how you find compatible versions on newly created projects between the different required dependencies, see POM notes.
10/30: chap 19/20- started mySQL/HibernateORM section
--PART III- data persistence
10/28/16: chap 17- REST/SOAP services-machine to machine communication.  Ran & tested web-service imported project and in firefox RESTClient Add-on.  have not implemented chap 17 changes into custSuptMaven at this time.  -v14 version does similar REST Service w/ Tickets as web-service did with Accounts. It will rely on Validation items covered in chap 16.  Note web services are different than websockets discussed in chap 10 for chat functionality.
10/25/16: chap 16- validation.  can do most of this via js in browser,  didnt update
10/23/16: chap 15- Internationalization- not implemented,  browsed through chap.  Revisited later and added code because all the UI messages are done via this i18n structure.
10/16/16: finished both java and jsp portions, w/ all subfolders.  tested, fixed a few minor bugs and tested all functionality successfully.  New
 Spring MVC project setup properly up to chapter 14 (corresponds to customer-support-v11).
10/15/16: finished documenting config package classes and w/i site- the Authenticator group w/ CSR comments.  each method group in site package 
  (Authentication, Session, Ticket, and Chat) follows this methodology.  Although at this point I am not sure why each of these didnt have its own
  site/ sub package like the chat did.
10/09/16: updated pom for spring dependencies, implemented in java/config packages.  working on creating/migrating site package classes.  left off at D (defaultSessionRegistry).  have not done chat or tag sub packages yet. completed POM dependencies.
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