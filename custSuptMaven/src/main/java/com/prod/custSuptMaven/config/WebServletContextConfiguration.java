package com.prod.custSuptMaven.config;
//class notes: see chap 13, pg 375/76 for details on ServletContext and usage w/i basic Spring project.
//there can only be one rootContext but can be multiple servletContexts if needed. in this case only one is used.
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.custSuptMaven.config.annotation.WebController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebMvc
//see chap 14, pg 395 for good description of this annotation group and its relation to rootContext
@ComponentScan(
        basePackages = "com.prod.custSuptMaven.site",
        useDefaultFilters = false,
        //servletContext(s) replaces httpServlets in pre-Spring version and any @Controller (C in MVC) performs servlet functionality.
        includeFilters = @ComponentScan.Filter(WebController.class)
)
public class WebServletContextConfiguration extends WebMvcConfigurerAdapter
{
    //injects sources from rootContext initialization
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
    
    @Override
    public Validator getValidator()
    {
        return this.validator;
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
    
    //supports attachment functionality, pg 385
    @Bean
    public MultipartResolver multipartResolver()
    {
        return new StandardServletMultipartResolver();
    }
}
