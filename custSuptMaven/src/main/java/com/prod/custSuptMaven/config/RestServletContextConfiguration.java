package com.prod.custSuptMaven.config;
/* Class notes- REST server config.  JWA chap 17, pg 484. Similar to base level Root and Web configurators this class creates a DispatcherServlet
 * the supports machine(server) to machine communication.  it is similar to WebServlet.. except there is no browser items- ie viewResolver, amongsts
 * other methods.  but classes that refer to this still use the @Controller annotation.  A new /annotation package was added to support a few interfaces
 * that this class extends.  as part of that refactoring WebController interface used by WebServlet.. was also created.
 * in short w/spring app starts at bootstrap, then RootContext, then WebServlet and RestServlet,  that establishes framework for items in site package
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.custSuptMaven.config.annotation.RestEndpoint;
import com.prod.custSuptMaven.config.annotation.RestEndpointAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(
        basePackages = "com.prod.custSuptMaven.site",
        useDefaultFilters = false,
        includeFilters =
        @ComponentScan.Filter({RestEndpoint.class, RestEndpointAdvice.class})
)
public class RestServletContextConfiguration extends WebMvcConfigurerAdapter
{
	//injects sources from rootContext initialization
    @Inject ObjectMapper objectMapper;
    @Inject Marshaller marshaller;
    @Inject Unmarshaller unmarshaller;
    @Inject SpringValidatorAdapter validator;
    
    //message converter/marshalling still needed for REST services.  but no XML in REST (SOAP does XML), so those calls are removed
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters
    ) {
        converters.add(new SourceHttpMessageConverter<>());

        MarshallingHttpMessageConverter xmlConverter =
                new MarshallingHttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "xml"),
                new MediaType("text", "xml")
        ));
        xmlConverter.setMarshaller(this.marshaller);
        xmlConverter.setUnmarshaller(this.unmarshaller);
        converters.add(xmlConverter);

        MappingJackson2HttpMessageConverter jsonConverter =
                new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
                new MediaType("application", "json"),
                new MediaType("text", "json")
        ));
        jsonConverter.setObjectMapper(this.objectMapper);
        converters.add(jsonConverter);
    }
    
    //contentNegotiation still needed, but there is some method chaining elements that are not needed for REST.  compare to WebServlet..
    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer)
    {
        configurer.favorPathExtension(false).favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON);
    }
    
    //validator still needed to validate field data- see chap 16
    @Override
    public Validator getValidator()
    {
        return this.validator;
    }

}
