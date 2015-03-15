package com.websystique.springmvc.configuration;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import ru.kvins.draw.filter.SimpleCORSFilter;

public class AppInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext container) throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(AppConfig.class);
		ctx.setServletContext(container);
                SimpleCORSFilter filter = new SimpleCORSFilter();
                
                container.addFilter("corsFilter", filter).addMappingForUrlPatterns(null, true, "/*");
                
                CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
                encodingFilter.setEncoding("UTF-8");
                encodingFilter.setForceEncoding(true);
                container.addFilter("encodingFilter", encodingFilter).addMappingForUrlPatterns(null, true, "/*");
                
                
		ServletRegistration.Dynamic servlet = container.addServlet(
				"dispatcher", new DispatcherServlet(ctx));

		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}
        

}
