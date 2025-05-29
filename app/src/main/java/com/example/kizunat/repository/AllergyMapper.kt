package com.example.kizunat.repository


fun mapAllergiesToEdamamParams(allergies: List<String>): List<String> {
    return allergies.mapNotNull {
        when (it.lowercase()) {
            "gluten" -> "gluten-free"
            "lactose" -> "dairy-free"
            "seafood" -> "seafood-free"
            "dried fruits" -> "tree-nut-free"
            "eggs" -> "egg-free"
            "soy" -> "soy-free"
            else -> null
        }
    }
}
