package com.b2bsolutions.domain.transitions.event;

public interface DomainEventPublisher {
    void publish(Object domainEvent);
}
