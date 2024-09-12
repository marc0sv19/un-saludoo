package utn.methodology.application.domain.Events

interface DomainEvent {
    abstract val eventName: String
}