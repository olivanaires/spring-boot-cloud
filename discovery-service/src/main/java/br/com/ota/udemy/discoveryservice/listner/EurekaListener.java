package br.com.ota.udemy.discoveryservice.listner;

import br.com.ota.udemy.discoveryservice.service.CatalogService;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaListener {

  private final CatalogService catalogService;

  @Autowired
  public EurekaListener(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @EventListener
  public void listen(EurekaInstanceRegisteredEvent event) {
    InstanceInfo insInfo = event.getInstanceInfo();
    if (!event.isReplication() && InstanceInfo.InstanceStatus.UP.equals(insInfo.getStatus())) {
      System.out.println("RegisteredEvent=========>>>>" + insInfo.getAppName());
      catalogService.updateCatalog(insInfo);
    }
  }

  @EventListener
  public void listen(EurekaInstanceCanceledEvent event) {
    if (event.isReplication()) {
      String appName = event.getAppName();
      System.out.println("CanceledEvent=========>>>>" + appName);
//      catalogService.deleteService(appName);
    }
  }
}
