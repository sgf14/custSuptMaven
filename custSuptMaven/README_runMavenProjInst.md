To Compile and Run a Maven Project:
1) Right click Project Name and 
2) Run As / Maven Clean.  Removes the previously compiles .class files
3) Run As / Maven Install.  compiles new/changed .java files to class files in the Target dir
4) Run As [or Debug As]/ Run On Server [Debug On Server].  then select Tomcat 8.0.  This will open the .jsp index in Firefox (since this is your default browser in on the laptop, as setup in windows).

--details--
1) Imported examples from JWA book and Project I created.  Note that imported Java for Web Applications are imported as Maven projects
they are not built under this SGF_Projects folder.  They are housed under:

2) Book examples- the Coding/Books/ dir where I downloaded the contents from the website.  Once imported these are then displayed in the Project Explorer to left, but are not actually in the Eclipse Workspace (that is part ofthe 'Import' Process- it directs to the place its imported from, not recreated in workspace.

3) Self created projects-  I have also created some projects from scratch, via Maven base (archetype) project- see readme_createJavaWebAppProj.md- and these are stored in the base eclipse workspace directory.  W/ project name highlighted right click on Project/Properties/Resource path to see full directory path.

Any file/java manual modifications for book examples are also under that Coding dir and not in the Eclipse Workspace.  See README_JavaServerNotes.md  on how to import project into Eclipse Java EE Working Set with others on the left.

4) Note there are a few differences between the base pom.xml in imported examples and codehuas self created projects.  The directory structure is a little different in the book examples because he is pulling out the non-java parts to a base web/directory.  This necessitates a few changes in the <build> directions (which are not needed in codehaus version because src is found where it is expected by default).  See /web/index.jsp vs /src/main/webapp/index.jsp.  
	- the maven youtube video has you changing the apache maven compiler to tomcat server directly.  I did not have to do this to follow the instructions at the top, because I am starting the tomcat server manually (run on server).  The video way does this automatically by incorporating the tomcat plugin into pom.xml, but you still need to launch a webpage in that version, so it didnt seem to me to any fewer steps.  the basic codehaus pom seems to work ok with the steps I listed above.
	youtube = https://www.youtube.com/watch?v=YeC7XQho-O0  basic project creation only, but a good video.  approx 3min into video
	more in depth one = https://www.youtube.com/watch?v=VOkgaq__Ff8


