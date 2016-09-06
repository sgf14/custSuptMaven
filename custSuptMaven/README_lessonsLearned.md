Notes for creating future JavaEE projects:
1) In src dont duplicate proj name.  ie Java path in this one is 
  src/main/java/com/prod/custSuptMaven
  in future during project creation use 
  src/main/java/com/prod
  
  the src/main/java is standard java project structure.  com/prod is used for production code.  com/unit is for unit testing
  at least from what I have figured out so far.  this is subject to change as I learn.
  
2) Leave the front end/browser code  out of the src/main path.
in this project the Java EE archetype I used it created it src/main/webapp (vs src/main/java w/ server side code).
the book created a root level web/ folder that houses the WEB-INF folder.  see book level customer-support style projects for this.
this may require a little more research when using the Java EE archetype when creating a project.
this may just be the way that archetype handles it, and it may not offer a viable alternative, but do some research.  coPid structure
also had the front end in it own root folder, although it is not a Maven project either.