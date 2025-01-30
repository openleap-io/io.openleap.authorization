package io.openleap.authorization.controller;

import io.openleap.authorization.service.identity.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class ClientUnregistrationController {
    Logger logger = LoggerFactory.getLogger(ClientUnregistrationController.class);

    private IdentityService identityService;

    public ClientUnregistrationController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GetMapping("/client/unregister/{clientName}/{instanceId}")
    public void unregisterClient(@PathVariable String clientName, @PathVariable String instanceId) {
        logger.info("Unregistering client with name: {} for instance id: {}", clientName, instanceId);
        identityService.unregisterClient(clientName, instanceId);
    }
}
