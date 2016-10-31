package uk.co.surething.ph.ws;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.co.surething.ph.common.Constants;


@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@WebAppConfiguration
@ContextConfiguration(classes = { WsConfig.class })
@ActiveProfiles(Constants.PROFILE_TEST)
public class WsTestBase {

	
}
