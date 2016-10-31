package uk.co.surething.ph.dao;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.surething.ph.models.ModelsConfig;

@Configuration
@Import(ModelsConfig.class)
@ComponentScan
public class DaoConfig {

}
