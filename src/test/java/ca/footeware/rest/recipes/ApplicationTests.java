package ca.footeware.rest.recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import ca.footeware.rest.recipes.controller.RecipeController;
import ca.footeware.rest.recipes.model.PagingDTO;
import ca.footeware.rest.recipes.model.Recipe;
import ca.footeware.rest.recipes.model.RecipeDTO;
import ca.footeware.rest.recipes.repository.RecipeImageRepository;
import ca.footeware.rest.recipes.repository.RecipeRepository;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private RecipeRepository recipeRepo;
	@Autowired
	private RecipeImageRepository imageRepo;
	private RecipeController controller;

	@BeforeEach
	void beforeEach() {
		controller = new RecipeController(recipeRepo, imageRepo);
		clearDB();
	}

	@AfterEach
	void aterEach() {
		clearDB();
	}

	private void clearDB() {
		List<Recipe> recipes = controller.getAllRecipes();
		for (Recipe recipe : recipes) {
			controller.deleteRecipe(recipe.getId());
		}
	}

	@Test
	void testSearch() {
		// create a recipe
		RecipeDTO recipeDTO = new RecipeDTO("1", "name1", "body1", List.of("tag1"), Collections.emptyList());
		controller.createRecipe(recipeDTO);

		// search for it by name
		PagingDTO result = controller.search("name1", 0, 10).getBody();
		assertEquals(1, result.recipes().size());

		// search for it by body
		result = controller.search("Bod", 0, 10).getBody();
		assertEquals(1, result.recipes().size());

		// search for it by tag
		result = controller.search(" tag ", 0, 10).getBody();
		assertEquals(1, result.recipes().size());
		List<Recipe> recipes = result.recipes();
		String id = recipes.get(0).getId();

		// get recipe by id
		recipeDTO = controller.getRecipeById(id).getBody();
		assertNotNull(recipeDTO);
		// change the client-side recipe's body
		recipeDTO.setBody("new");

		// update recipe in db
		recipeDTO = new RecipeDTO(id, recipeDTO.getName(), recipeDTO.getBody(), recipeDTO.getTags(),
				Collections.emptyList());
		RecipeDTO updatedRecipeDTO = controller.updateRecipe(id, recipeDTO).getBody();
		assertEquals("new", updatedRecipeDTO.getBody());

		// create a 2nd recipe
		recipeDTO = new RecipeDTO("2", "name2", "body2", List.of("tag2"), Collections.emptyList());
		controller.createRecipe(recipeDTO).getBody();

		// search for and get both recipes
		result = controller.search("name", 0, 10).getBody();
		assertEquals(2, result.recipes().size());
		assertEquals("name1", result.recipes().get(0).getName());
	}

	@Test
	void testGetAllTags() {
		RecipeDTO recipeDTO = new RecipeDTO("1", "name1", "body1", List.of("tag1"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		recipeDTO = new RecipeDTO("2", "name2", "body2", List.of("tag1", "tag2"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		recipeDTO = new RecipeDTO("3", "name3", "body3", List.of("tag1", "tag2"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		recipeDTO = new RecipeDTO("4", "name4", "body4", List.of("tag3"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		List<String> tags = controller.getAllTags().getBody();
		assertEquals(3, tags.size());
	}

	@Test
	void searchByTags() {
		RecipeDTO recipeDTO = new RecipeDTO("1", "name1", "body1", List.of("tag1"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		recipeDTO = new RecipeDTO("2", "name2", "body2", List.of("tag1", "tag2"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		PagingDTO dto = controller.searchByTag("tag1", 0, 10).getBody();
		List<Recipe> recipes = dto.recipes();
		assertEquals(2, recipes.size());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testGetAllRecipes() {
		RecipeDTO recipeDTO = new RecipeDTO("1", "name1", "body1", List.of("tag1"), Collections.emptyList());
		controller.createRecipe(recipeDTO);
		ResponseEntity<PagingDTO> response = controller.getAllRecipes(0, 10);
		assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
		PagingDTO body = response.getBody();
		assertEquals(1, body.recipes().size());
		Recipe recipe2 = body.recipes().get(0);
		assertEquals("name1", recipe2.getName());
		assertEquals("body1", recipe2.getBody());
		assertEquals(1, recipe2.getTags().size());
		String tag = recipe2.getTags().get(0);
		assertEquals("tag1", tag);
	}
}
