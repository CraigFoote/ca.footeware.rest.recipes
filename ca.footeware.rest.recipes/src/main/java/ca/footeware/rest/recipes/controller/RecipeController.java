/**
 * 
 */
package ca.footeware.rest.recipes.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.footeware.rest.recipes.model.Recipe;
import ca.footeware.rest.recipes.repository.RecipeRepository;

/**
 * @author Footeware.ca
 *
 */
@RestController
@RequestMapping
public class RecipeController {

	private RecipeRepository repo;
	
	@Autowired
	public RecipeController(RecipeRepository repo) {
		this.repo = repo;
	}

	private List<String> cleanTags(List<String> tags) {
		List<String> tagList = new ArrayList<>();
		String tag;
		for (String string : tags) {
			tag = string.trim().toLowerCase();
			if (!tag.isEmpty()) {
				tagList.add(tag);
			}
		}
		return tagList;
	}

	@PostMapping(value = "/recipes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
		Recipe savedRecipe = repo.save(recipe);
		return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
	}

	@DeleteMapping("/recipes/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable String id) {
		repo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Recipe>> getAllRecipes() {
		return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/recipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
		Optional<Recipe> optional = repo.findById(id);
		if (optional.isPresent()) {
			return new ResponseEntity<>(optional.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/recipes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipe) {
		Optional<Recipe> optional = repo.findById(id);
		if (optional.isPresent()) {
			Recipe existingRecipe = optional.get();
			existingRecipe.setName(recipe.getName().trim());
			existingRecipe.setBody(recipe.getBody().trim());
			existingRecipe.setTags(cleanTags(recipe.getTags()));
			existingRecipe.setImages(recipe.getImages());
			Recipe savedRecipe = repo.save(existingRecipe);
			return new ResponseEntity<>(savedRecipe, HttpStatus.OK);
		}
		return new ResponseEntity<Recipe>(HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/recipes/search")
	public ResponseEntity<List<Recipe>> search(@RequestParam String searchTerm) {
		String trimmed = searchTerm.trim();
		// get sets based on name, body and tags
		Set<Recipe> recipesByName = repo.findByNameContainingIgnoreCase(trimmed);
		Set<Recipe> recipesByBody = repo.findByBodyContainingIgnoreCase(trimmed);
		Set<Recipe> recipesByTags = repo.findByTagsContainingIgnoreCase(trimmed);
		// combine into one set
		Set<Recipe> collection = new HashSet<>();
		Stream.of(recipesByName).forEach(collection::addAll);
		Stream.of(recipesByBody).forEach(collection::addAll);
		Stream.of(recipesByTags).forEach(collection::addAll);
		// sort by name
		Comparator<Recipe> comparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
		List<Recipe> recipes = new ArrayList<>(collection);
		Collections.sort(recipes, comparator);
		return new ResponseEntity<List<Recipe>>(recipes, HttpStatus.OK);
	}
}
