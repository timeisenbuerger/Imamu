package com.github.tei.imamu.data.database.entity.cookbook

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class LastViewedCookBook(
    @Id
    var id: Long = 0L
)
{
    lateinit var cookBook: ToOne<CookBook>
}