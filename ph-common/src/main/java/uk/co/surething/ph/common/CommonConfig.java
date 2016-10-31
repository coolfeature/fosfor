package uk.co.surething.ph.common;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CommonConfig {

	@Bean(name = Constants.BEAN_PROPERTIES)
	public Properties properties() {
		return Config.PROPERTIES;
	}
}
