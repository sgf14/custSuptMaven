This project setup as test of JavaEE 7 archetype with Spring MVC.  See JWA book Chap 13, pg 371. 09/24/16.

Spring will replace the HttpServlet methods of earlier chapters as part of the MVC structure.
this project was created from scratch instead of importing the book version of Model-View-Controller project.
wanted to ensure I could create a project from scratch and have it work.  See README_createMavenProj... notes for guide. 
tested successfully 09/24/16.  can use this as a template for other Spring projects.

This uses Java implementation vs web.xml directives to bootstrap Spring.  See Java config/site Package arrangement and spring-java-config app.
 Subject is reviewed in detail at the end of chapter 12 with the Spring Intro.  java or xml can be used to bootstrap and manage Spring.
 
 MVC/Spring Pattern Notes- see pg JWA Chap 12, pg 326
 M- Model (database/Persistence Interaction)
 
 V- View (User Interface)
 	The View or view name (String) returned from the Controller forwards to the appropriate JSP View.
 	
 C- Controller (interaction between Model and View)
 	Model is passed from the Controller to the View in the form Map<String, Object>.  @Controller replaces HttpServletRequest / Response.
 		Within controller on server CSR pattern - see JWA Chap 14, pg 390/392
 		R-Repository- Persistence logic.  lowest level
 		S- Services- Business logic, 2nd level. Consume Repository and other Services but not Controller 
 		C-Controller- UI logic (that needed for Desktop, Mobile etc presentation). highest level.  
 			Consumes Services, but not Repositories directly or other Controllers.
 		
 	