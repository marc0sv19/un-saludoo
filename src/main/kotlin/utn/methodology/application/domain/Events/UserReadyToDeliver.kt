package utn.methodology.application.domain.Events

data class UserCreateReadyToDeliver(val id: String): DomainEvent {
    override val eventName: String = "user.ready"

    override fun toString(): String {
        return mapOf(
            "id" to this.id
        ).toString()
    }
}