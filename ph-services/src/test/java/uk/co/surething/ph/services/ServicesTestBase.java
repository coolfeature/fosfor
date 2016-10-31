package uk.co.surething.ph.services;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.co.surething.ph.common.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ServicesConfig.class })
@ActiveProfiles(Constants.PROFILE_TEST)
public class ServicesTestBase {
	
	protected static Logger LOGGER;
	
	{
		LOGGER = Logger.getLogger(this.getClass());
	}
}
