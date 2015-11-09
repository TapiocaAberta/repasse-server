package org.jugvale.transfgov.ranking;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.cdi.ConfigureCache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class RankingCacheConfig {

	@ConfigureCache("ranking-cache")
	@Produces
	public Configuration rankingCacheConfiguration() {
		return new ConfigurationBuilder().eviction()
				.strategy(EvictionStrategy.LRU).maxEntries(10).expiration()
				.lifespan(10, TimeUnit.DAYS).jmxStatistics().enabled(false).build();
	}

	@Produces
	@ApplicationScoped
	public EmbeddedCacheManager defaultClusteredCacheManager() {
	    return new DefaultCacheManager(
	        new GlobalConfigurationBuilder().globalJmxStatistics().allowDuplicateDomains(true).build() );
	}

}