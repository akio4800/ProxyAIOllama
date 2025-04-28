package at.s2g.ai.util

import com.fasterxml.jackson.core.type.TypeReference

class ListConverter : BaseConverter<List<Any>>(object : TypeReference<List<Any>>() {})
