package com.prod.custSuptMaven.config.bootstrap;
/*package notes:  see chap 12, pg 333 for programatic (java, not xml) for how this works. /config package instantiates SpringMVC and 
 *site package contains site specific code.
 *class notes: bootstrap initializes Spring MVC.  per page 333 the key here is that Spring dependency is in POM 
 * (see artifact id spring-webmvc) and it scans the app for
 * a WebApplicationInitializer class (in onStartup method below) therefore the the path ../com.prod..config/ path is not actually
 * referenced or called directly anywhere.  Regardless of what your bootstrap class is named Spring is only looking for an implementattion of
 * WebApplicationInitializer class. Regardless of what file contains it- and it bootstraps Spring App from there.  the preferred name for 
 * this housing class contains the name bootstrap only by convention.
 * See Spring POM dependencies and explanation in chap 12.  if using Java vs XML instantiation then @annotation methods are used extensively in 
 * Spring MVC, as you will note in Bootstrap, Root Context and WebServletContext..
 * 
 * Chap 26 Spring Security refactored this original Bootstrap class to FrameworkBootstrap, as detailed in pg 767-70. to include the 
 * ordering annotation to tell Spring how to start the different bootstraps (implementations of WebApplicationInitialzer)
 */
import com.prod.custSuptMaven.config.RestServletContextConfiguration;
import com.prod.custSuptMaven.config.RootContextConfiguration;
import com.prod.custSuptMaven.config.WebServletContextConfiguration;
import com.prod.custSuptMaven.site.PreSecurityLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@SuppressWarnings("unused")
@Order(1)
public class FrameworkBootstrap implements WebApplicationInitializer {
	//note logging was added in chap 26- just to display order of bootstrap starts
	private static final Logger log = LogManager.getLogger();
	
	//as with all Overrides this annotation indicates its method overrides a method within the implemented class
	@Override
    public void onStartup(ServletContext container) throws ServletException
    {
        log.info("Executing framework bootstrap");
		//registers ancillary items to WEB-INF dir for UI.  In this case for css etc.  see JWA chap 12, pg 349
		// note that \webapp\resource is a peer to webapp\WEB-INF.  it wouldnt get registered by server w/o addMapping() method below.
		// WEB-INF and META-INF are autoregistered, anything you add for support functions isnt by default
		//Note this is different from src/main/resources which houses java resources
		//to add new right click Java resources/new Source Folder, then enter project name and src/main/xxx
        
		container.getServletRegistration("default").addMapping("/resource/*");
        
        //root context registers all other contexts including servlet via ..LoaderListener...  see pg 328, 349 
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));
        //container add below removed as part of chap 26 Spring Security- these classes extended from spring instead of independent build
        //container.addListener(SessionListener.class);
        
        //servlet context registers all Web servers (ie @Controller methods and by convention ending in ..Controller in site package)
        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebServletContextConfiguration.class);
        ServletRegistration.Dynamic dispatcher = container.addServlet(
                "springWebDispatcher", new DispatcherServlet(webContext)
        );
        dispatcher.setLoadOnStartup(1);
        //configures file attachment function of ticket
        dispatcher.setMultipartConfig(new MultipartConfigElement(
                null, 20_971_520L, 41_943_040L, 512_000
        ));
        dispatcher.addMapping("/");
        
        //registers all Rest Services, similar to Web Services above.  but REST handles machine to machine requests, vs web browser requests
        //note a similar SOAP service (similar but older version of REST) not enabled in the project.  see JWA chap 17.
        AnnotationConfigWebApplicationContext restContext =
                new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        DispatcherServlet restServlet = new DispatcherServlet(restContext);
        restServlet.setDispatchOptionsRequest(true);
        dispatcher = container.addServlet("springRestDispatcher", restServlet);
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/services/Rest/*");
        
        //SOAP services (xml)- in addition to REST(json).  disabled for now- need /resources/soapServletContext.xml file in addition to coinfig
//        AnnotationConfigWebApplicationContext soapContext =
//                new AnnotationConfigWebApplicationContext();
//        soapContext.register(SoapServletContextConfiguration.class);
//        MessageDispatcherServlet soapServlet =
//                new MessageDispatcherServlet(soapContext);
//        soapServlet.setTransformWsdlLocations(true);
//        dispatcher = container.addServlet("springSoapDispatcher", soapServlet);
//        dispatcher.setLoadOnStartup(3);
//        dispatcher.addMapping("/services/Soap/*");
        
        
        // following code blocks replaced customer-support-v8 Configurator class.  see chap 13,pg384.
        // since Bootstrap.. now configures commons/Servlet API features
        FilterRegistration.Dynamic registration = container.addFilter(
                "preSecurityLoggingFilter", new PreSecurityLoggingFilter()
        );
        registration.addMappingForUrlPatterns(null, false, "/*");
        
        //note these blocks removed by chap 26 security
        /*
        registration = container.addFilter(
                "authenticationFilter", new AuthenticationFilter()
        );
        registration.addMappingForUrlPatterns(
                null, false, "/ticket", "/ticket/*", "/chat", "/chat/*",
                "/session", "/session/*"
        );
        */
    }
}


