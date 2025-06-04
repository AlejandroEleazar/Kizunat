package com.example.kizunat

import com.example.kizunat.api.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class EdamamApiTest {

    private val mockService = mockk<RecipeService>()

    @Before
    fun setup() {
        EdamamApi.service = mockService
    }

    @Test
    fun testServiceIsMocked() {
        assertNotNull(EdamamApi.service)
    }

    @Test
    fun testFetchBreakfastFiltersAllergens() = runBlocking {
        val allergies = listOf("peanuts", "gluten")
        val recipes = listOf(
            Recipe("With Allergen", 100.0, "img1", listOf("Peanuts")),
            Recipe("Without Allergen", 90.0, "img2", listOf("Tomato"))
        )
        val response = RecipesResponse(0, 2, 2, recipes.map { Hit(it) })

        coEvery { mockService.getRecipes(any(), "breakfast", any(), any(), any(), any()) } returns response

        val result = EdamamApi.fetchBreakfast(allergies, maxResults = 10)
        assertEquals(1, result.size)
        assertEquals("Without Allergen", result[0].label)
    }

    @Test
    fun testFetchLunchFiltersAllergens() = runBlocking {
        val allergies = listOf("milk")
        val recipes = listOf(
            Recipe("Milk Soup", 300.0, "img1", listOf("Milk", "Butter")),
            Recipe("Veggie Bowl", 250.0, "img2", listOf("Carrot", "Lettuce"))
        )
        val response = RecipesResponse(0, 2, 2, recipes.map { Hit(it) })

        coEvery { mockService.getRecipes(any(), "lunch", any(), any(), any(), any()) } returns response

        val result = EdamamApi.fetchLunch(allergies, maxResults = 5)
        assertEquals(1, result.size)
        assertEquals("Veggie Bowl", result[0].label)
    }

    @Test
    fun testFetchDinnerFiltersAllergens() = runBlocking {
        val allergies = listOf("egg")
        val recipes = listOf(
            Recipe("Egg Omelette", 200.0, "img1", listOf("Egg", "Salt")),
            Recipe("Pasta", 180.0, "img2", listOf("Tomato", "Pasta"))
        )
        val response = RecipesResponse(0, 2, 2, recipes.map { Hit(it) })

        coEvery { mockService.getRecipes(any(), "dinner", any(), any(), any(), any()) } returns response

        val result = EdamamApi.fetchDinner(allergies, maxResults = 5)
        assertEquals(1, result.size)
        assertEquals("Pasta", result[0].label)
    }


}
