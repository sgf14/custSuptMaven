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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
	private static final Logger log = LogManager.getLogger();
	
	//injects sources from rootContext initialization.  see WebServlet notes also
	@Inject ApplicationContext applicationContext;
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
    
    //chap 22 Spring-jpa data- see WebServlet note
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)
    {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
        Pageable defaultPageable = new PageRequest(0, 20, defaultSort);

        SortHandlerMethodArgumentResolver sortResolver =
                new SortHandlerMethodArgumentResolver();
        sortResolver.setSortParameter("$paging.sort");
        sortResolver.setFallbackSort(defaultSort);

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setMaxPageSize(200);
        pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setPrefix("$paging.");
        pageableResolver.setFallbackPageable(defaultPageable);

        resolvers.add(sortResolver);
        resolvers.add(pageableResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        if(!(registry instanceof FormattingConversionService))
        {
            log.warn("Unable to register Spring Data JPA converter.");
            return;
        }

        DomainClassConverter<FormattingConversionService> converter =
                new DomainClassConverter<>((FormattingConversionService)registry);
        converter.setApplicationContext(this.applicationContext);
    }
    
    //validator still needed to validate field data- see chap 16
    @Override
    public Validator getValidator()
    {
        return this.validator;
    }
    
    @Bean
    public LocaleResolver localeResolver()
    {
        return new AcceptHeaderLocaleResolver();
    }

}
