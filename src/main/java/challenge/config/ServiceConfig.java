package challenge.config;

import challenge.api.MarketplaceService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ServiceConfig {
  @Bean
  public MarketplaceService marketplaceService() {
    return new MarketplaceService();
  }

  @Bean
  public SpringBus cxf() {
    return new SpringBus();
  }

  @Bean
  @DependsOn("cxf")
  public Server jaxRsServer(MarketplaceService marketplaceService) {
    final JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
    factoryBean.setServiceBeanObjects(marketplaceService);
    factoryBean.setProvider(new JacksonJsonProvider());
    factoryBean.setBus(cxf());
    factoryBean.setAddress("/");

    return factoryBean.create();
  }

}
