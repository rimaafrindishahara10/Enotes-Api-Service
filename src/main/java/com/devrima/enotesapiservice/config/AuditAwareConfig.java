package com.devrima.enotesapiservice.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {
        return Optional.of ( 3 );
    }
}
