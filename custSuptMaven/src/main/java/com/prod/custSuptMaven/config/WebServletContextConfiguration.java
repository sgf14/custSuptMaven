package com.prod.custSuptMaven.config;
//class notes: see chap 13, pg 375/76 for details on ServletContext and usage w/i basic Spring project.
//there can only be one rootContext but can be multiple servletContexts if needed. in this case only one is used.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.custSuptMaven.config.annotation.WebController;

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
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc

//can place default Spring Data annotation here for default implementation.  chap 22, per page 656 book implements a custom set w/ methods below.
//@EnableSpringDataWebSupport

//see chap 14, pg 395 for good description of this annotation group and its relation to rootContext
@ComponentScan(
        basePackages = "com.prod.custSuptMaven.site",
        useDefaultFilters = false,
        //servletContext(s) replaces httpServlets in pre-Spring version and any @Controller (C in MVC) performs servlet functionality.
        includeFilters = @ComponentScan.Filter(WebController.class)
)
public class WebServletContextConfiguration extends WebMvcConfigurerAdapter
{
	private static final Logger log = LogManager.getLogger();
	//injects sources from rootContext initialization
	//1st ApplicationContext is used for chap 22 Spring Data custom implementation.  order matters in this case
	@Inject ApplicationContext applicationContext;
	@Inject ObjectMapper objectMapper;
    @Inject Marshaller marshaller;
    @Inject Unmarshaller unmarshaller;
    @Inject SpringValidatorAdapter validator;
    
    //see pg 375 for converter function- this is an override of the default WebMcv.. that is extended by this class.
    //used as an example of how to do it- often the default is sufficient
    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters
    ) {
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
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
    //see chap 13, pg 378- similar to above this is an override of the default and not always needed.
    // but done to expose the user how to perform the override w/ an example when needed.
    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer)
    {
        configurer.favorPathExtension(true).favorParameter(false)
                .parameterName("mediaType").ignoreAcceptHeader(false)
                .useJaf(false).defaultContentType(MediaType.APPLICATION_XML)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("json", MediaType.APPLICATION_JSON);
    }
    
    /*chap 22, pg 653.  This and next method are custom Spring Data implementations used by downstream (site) classes.
    you can use these or @EnableSpringDataWebSupport annotation above.  In addition in the site package the (3) Jpa Generic classes
    GenericBaseRepository, GenericJpaRepository and GenericRespository are all replaced by CrudRepository that is part of Spring-Jpa Data
    */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)
    {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.ASC, "id"));
        Pageable defaultPageable = new PageRequest(0, 10, defaultSort);

        SortHandlerMethodArgumentResolver sortResolver =
                new SortHandlerMethodArgumentResolver();
        sortResolver.setSortParameter("paging.sort");
        sortResolver.setFallbackSort(defaultSort);

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver(sortResolver);
        pageableResolver.setMaxPageSize(100);
        pageableResolver.setOneIndexedParameters(true);
        pageableResolver.setPrefix("paging.");
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
    
    //chap 16 ui data entry validation
    @Override
    public Validator getValidator()
    {
        return this.validator;
    }
    //chap 15 internationalization. next two.  these are only needed for web UI portions (vs Rest). see pg 427
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        super.addInterceptors(registry);

        registry.addInterceptor(new LocaleChangeInterceptor());
    }
    
    //chap 15 i18n- pg 425
    @Bean
    public LocaleResolver localeResolver()
    {
        return new SessionLocaleResolver();
    }
    
    //see chap 13 pg 372-3.turn @RequestMapping view names in ..Controllers into jsp file names.
    @Bean
    public ViewResolver viewResolver()
    {
        InternalResourceViewResolver resolver =
                new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/view/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    //Ch13, pg 374.  default functionality is maintained
    @Bean
    public RequestToViewNameTranslator viewNameTranslator()
    {
        return new DefaultRequestToViewNameTranslator();
    }
    
    //Ch 13 spring- supports attachment functionality, pg 385
    @Bean
    public MultipartResolver multipartResolver()
    {
        return new StandardServletMultipartResolver();
    }
}
