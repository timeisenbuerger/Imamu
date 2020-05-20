package com.github.tei.imamu.wrapper

import java.io.Serializable

data class FullSearchResultWrapper(var resultList: MutableList<SingleSearchResultWrapper>): Serializable
{
}