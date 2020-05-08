package com.github.tei.imamu.custom.listener

import com.github.tei.imamu.data.entity.cookbook.CookBook

class CookBookListListener(val clickListener: (cookBook: CookBook) -> Unit)
{
    fun onClick(cookBook: CookBook) = clickListener(cookBook)
}