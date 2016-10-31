package uk.co.surething.ph.ws.metrics;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

import uk.co.surething.ph.common.Config;
import uk.co.surething.ph.common.Utils;

@Configuration
@EnableMetrics
public class MetricConfig extends MetricsConfigurerAdapter {

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
		// registerReporter allows the MetricsConfigurerAdapter to
		// shut down the reporter when the Spring context is closed
		
		/*
		registerReporter(ConsoleReporter
			.forRegistry(metricRegistry)
			.convertRatesTo(TimeUnit.SECONDS)
			.convertDurationsTo(TimeUnit.MILLISECONDS)
			.build()
		).start(20, TimeUnit.SECONDS);
		*/
		
		String csvMetricsPath = Config.getStringProperty("ph.metricsCsv.dir");
		Utils.ensureDirExists(csvMetricsPath);
		
		registerReporter(
			CsvReporter
				.forRegistry(metricRegistry)
		        .formatFor(Locale.UK)
		        .convertRatesTo(TimeUnit.SECONDS)
		        .convertDurationsTo(TimeUnit.MILLISECONDS)
		        .build(new File(csvMetricsPath))
		).start(20, TimeUnit.SECONDS);
		
	}

}
