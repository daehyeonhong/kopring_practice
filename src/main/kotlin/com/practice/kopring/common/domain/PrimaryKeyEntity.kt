package com.practice.kopring.common.domain

import com.github.f4b6a3.ulid.UlidCreator
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class PrimaryKeyEntity : Persistable<UUID> {
    @Id
    @Column(columnDefinition = "RAW")
    private val id: UUID = UlidCreator.getMonotonicUlid().toUuid()

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(nullable = false)
    protected var updatedAt: LocalDateTime = LocalDateTime.now()

    @Transient
    private var _isNew = true

    override fun getId(): UUID = this.id
    override fun isNew(): Boolean = this._isNew

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other !is HibernateProxy && this::class != other::class) return false
        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Serializable {
        return when (obj) {
            is HibernateProxy -> obj.hibernateLazyInitializer.identifier as Serializable
            else -> (obj as PrimaryKeyEntity).id
        }
    }

    override fun hashCode() = Objects.hashCode(id)

    @PostLoad
    @PostPersist
    protected fun load() {
        _isNew = false
    }
}
