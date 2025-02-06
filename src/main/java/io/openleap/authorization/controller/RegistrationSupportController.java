package io.openleap.authorization.controller;

import io.openleap.authorization.controller.dto.UnregisterRequest;
import io.openleap.authorization.service.identity.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class RegistrationSupportController {

    Logger logger = LoggerFactory.getLogger(RegistrationSupportController.class);

    private final IdentityService identityService;

    public RegistrationSupportController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping("/client/unregister")
    public void unregister(@RequestBody UnregisterRequest unregisterRequest) {
        logger.debug("Unregistering client with id: {}", unregisterRequest.instanceId());
        identityService.unregister(unregisterRequest.instanceId());
    }
}
