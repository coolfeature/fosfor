package uk.co.surething.ph.web.it;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.web.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfig.class })
@ActiveProfiles(Constants.PROFILE_TEST)
public class PricingHubITBase {

}
