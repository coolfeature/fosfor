package uk.co.surething.ph.models;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.co.surething.ph.common.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ModelsConfig.class })
@ActiveProfiles(Constants.PROFILE_TEST)
public class ModelsTestBase {
	
	
	
}
