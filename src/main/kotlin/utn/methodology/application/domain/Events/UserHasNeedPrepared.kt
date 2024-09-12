package utn.methodology.application.domain.Events

data class Userhasneedprepared(val id: String): DomainEvent {
    override val eventName: String = "user.needs.prepared"

    override fun toString(): String {
        return mapOf(
            "id" to this.id
        ).toString()
    }
}