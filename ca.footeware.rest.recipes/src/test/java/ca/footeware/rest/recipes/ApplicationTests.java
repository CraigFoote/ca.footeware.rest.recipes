package ca.footeware.rest.recipes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import ca.footeware.rest.recipes.controller.RecipeController;
import ca.footeware.rest.recipes.model.Recipe;
import ca.footeware.rest.recipes.repository.RecipeRepository;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private RecipeRepository repo;
	private RecipeController controller;

	@BeforeEach
	void beforeEach() {
		controller = new RecipeController(repo);
		List<Recipe> recipes = controller.getAllRecipes().getBody();
		for (Recipe recipe : recipes) {
			controller.deleteRecipe(recipe.getId());
		}
	}

	@Test
	void testSearch() {
		Recipe recipe = new Recipe("name1", "body1", Arrays.asList("tag1"), null);
		Recipe created = controller.createRecipe(recipe).getBody();
		ResponseEntity<List<Recipe>> search = controller.search("name");
		assertEquals(1, search.getBody().size());
		search = controller.search("BoDy");
		assertEquals(1, search.getBody().size());
		search = controller.search(" tag ");
		assertEquals(1, search.getBody().size());
		List<Recipe> recipes = search.getBody();
		String id = recipes.get(0).getId();
		Recipe recipeById = controller.getRecipeById(id).getBody();
		assertNotNull(recipeById);
		recipeById.setName("new");
		Recipe updateReciped = controller.updateRecipe(recipe.getId(), recipeById).getBody();
		assertEquals("new", updateReciped.getName());
		// clean up
		controller.deleteRecipe(created.getId());
		search = controller.search("name");
		assertEquals(0, search.getBody().size());
	}

	@Test
	void contextLoads() {
	}
}
