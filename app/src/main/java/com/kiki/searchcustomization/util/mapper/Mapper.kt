package com.kiki.searchcustomization.util.mapper

import com.kiki.searchcustomization.data.entity.SearchModel
import com.kiki.searchcustomization.util.algorithm.Score

fun Score.toSearchModel() = SearchModel(warteg = this.data.warteg, menu = this.data.menu)