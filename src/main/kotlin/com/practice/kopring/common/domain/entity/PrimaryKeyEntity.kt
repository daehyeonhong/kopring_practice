package com.practice.kopring.common.domain.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.util.*

@MappedSuperclass
abstract class PrimaryKeyEntity : Persistable<Long> {
    @Id
    @GeneratedValue
    private val id: Long? = null

    @Transient
    private var _isNew = true

    override fun getId(): Long? = id

    override fun isNew(): Boolean = _isNew

    override fun equals(other: Any?): Boolean {
        if (other == null) return false

        if (other !is HibernateProxy && this::class != other::class) return false

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Long? {
        return if (obj is HibernateProxy) (obj.hibernateLazyInitializer.implementation as PrimaryKeyEntity).id else (obj as PrimaryKeyEntity).id
    }

    override fun hashCode() = Objects.hashCode(id)

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }
}
