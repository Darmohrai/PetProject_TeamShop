package org.example.teamshop.annotation.CustomAccessRightsCheck;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.teamshop.Exception.AccessDeniedRuntimeException;
import org.example.teamshop.securityModel.SecurityClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAccessRightsCheckAspect {

    @Before("@annotation(customAccessRightsCheck) && args(id, ..)")
    public void checkAccessRights(CustomAccessRightsCheck customAccessRightsCheck, Long id) throws AccessDeniedRuntimeException {
        String expression = customAccessRightsCheck.value();

        SecurityClient securityClient = (SecurityClient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long clientIdFromSecurityContext = securityClient.getClientId();

        if (!id.equals(clientIdFromSecurityContext)) {
            throw new AccessDeniedRuntimeException("Access Denied (user hasn't access for other id)");
        }
    }
}
