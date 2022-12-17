/**
 * 
 */
package ca.footeware.rest.recipes.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import ca.footeware.rest.recipes.model.PagingDTO;
import ca.footeware.rest.recipes.model.Recipe;
import ca.footeware.rest.recipes.model.Tag;
import ca.footeware.rest.recipes.repository.RecipeRepository;

/**
 * @author Footeware.ca
 *
 */
@RestController
@RequestMapping
public class RecipeController {

	private RecipeRepository repo;

//	@Autowired
	public RecipeController(RecipeRepository repo) {
		this.repo = repo;
//		createMockData();
	}

	private void createMockData() {
		List<Recipe> mockData = new ArrayList<>();
		mockData.add(new Recipe("name1", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name2", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name3", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name4", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name5", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name6", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name7", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name8", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name9", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name10", "body2", List.of(new Tag("tag1")), null));
		mockData.add(new Recipe("name11", "body2", List.of(new Tag("tag1")), null));
		repo.saveAll(mockData);
	}

	private List<Tag> cleanTags(List<Tag> tags) {
		List<Tag> tagList = new ArrayList<>();
		String tag2;
		for (Tag tag1 : tags) {
			tag2 = tag1.value().trim().toLowerCase();
			if (!tag2.isEmpty()) {
				tagList.add(new Tag(tag2));
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
	public ResponseEntity<PagingDTO> getAllRecipes(@RequestParam int pageNumber, @RequestParam int pageSize) {
		Page<Recipe> page = repo.findAll(PageRequest.of(pageNumber, pageSize));
		PagingDTO dto = new PagingDTO(page.getTotalElements(), page.getContent());
		return new ResponseEntity<>(dto, HttpStatus.OK);
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
	public ResponseEntity<PagingDTO> search(@RequestParam String term, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		String trimmed = term.trim();
		Page<Recipe> result = repo.findByNameContainingIgnoreCaseOrBodyContainingIgnoreCaseOrTagsValueContainingIgnoreCaseOrderByNameAsc(trimmed, trimmed, trimmed,
				PageRequest.of(pageNumber, pageSize));

		PagingDTO dto = new PagingDTO(pageSize, result.getContent());
		return new ResponseEntity<PagingDTO>(dto, HttpStatus.OK);
	}

	@GetMapping("/recipes/tags")
	public ResponseEntity<List<Tag>> getAllTags() {
		List<Recipe> allRecipes = repo.findAll();
		Set<Tag> allTags = new HashSet<>();
		for (Recipe recipe : allRecipes) {
			List<Tag> tags = recipe.getTags();
			allTags.addAll(tags);
		}
		List<Tag> allTagsList = new ArrayList<>(allTags);
		allTagsList.sort((o1, o2) -> o1.value().compareTo(o2.value()));
		return new ResponseEntity<>(allTagsList, HttpStatus.OK);
	}

	public List<Recipe> getAllRecipes() {
		return repo.findAll();
	}

}
