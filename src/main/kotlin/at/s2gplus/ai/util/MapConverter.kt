package at.s2gplus.ai.util

import com.fasterxml.jackson.core.type.TypeReference

class MapConverter : BaseConverter<Map<String, Any>>(object : TypeReference<Map<String, Any>>() {})
