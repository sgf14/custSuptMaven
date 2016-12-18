package com.prod.custSuptMaven.config;
//package notes:  /config package instantiates SpringMVC and /site package contains site specific code
/*class notes: bootstrap initializes Spring MVC 
 * based on POM dependencies.  if using Java vs XML instantiation then @annotation methods are used extensively in 
 * Spring MVC
 */
import com.prod.custSuptMaven.site.AuthenticationFilter;
import com.prod.custSuptMaven.site.LoggingFilter;
import com.prod.custSuptMaven.site.SessionListener;
//import com.prod.custSuptMaven.config.RootContextConfiguration;

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
public class Bootstrap implements WebApplicationInitializer {
	@Override
    public void onStartup(ServletContext container) throws ServletException
    {
        //registers ancillary items to classpath.  see JWA chap 12, pg 349
		//to add new right click Java resources/new Source Folder, then enter project name and src/main/xxx
		container.getServletRegistration("default").addMapping("/resource/*");
        
        //root context registers all other contexts including servlet via ..LoaderListener...  see pg 328, 349 
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootContextConfiguration.class);
        container.addListener(new ContextLoaderListener(rootContext));
        container.addListener(SessionListener.class);
        
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
        
        //SOAP services (xml)- in addition to REST(json).  
        AnnotationConfigWebApplicationContext soapContext =
                new AnnotationConfigWebApplicationContext();
        soapContext.register(SoapServletContextConfiguration.class);
        MessageDispatcherServlet soapServlet =
                new MessageDispatcherServlet(soapContext);
        soapServlet.setTransformWsdlLocations(true);
        dispatcher = container.addServlet("springSoapDispatcher", soapServlet);
        dispatcher.setLoadOnStartup(3);
        dispatcher.addMapping("/services/Soap/*");
        
        
        // following code blocks replaced customer-support-v8 Configurator class.  see chap 13,pg384.
        // since Bootstrap.. now configures commons/Servlet API features
        FilterRegistration.Dynamic registration = container.addFilter(
                "loggingFilter", new LoggingFilter()
        );
        registration.addMappingForUrlPatterns(null, false, "/*");

        registration = container.addFilter(
                "authenticationFilter", new AuthenticationFilter()
        );
        registration.addMappingForUrlPatterns(
                null, false, "/ticket", "/ticket/*", "/chat", "/chat/*",
                "/session", "/session/*"
        );
    }
}


