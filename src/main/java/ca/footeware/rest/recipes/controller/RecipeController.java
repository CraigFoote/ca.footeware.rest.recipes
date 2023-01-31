/**
 * 
 */
package ca.footeware.rest.recipes.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ca.footeware.rest.recipes.model.RecipeDTO;
import ca.footeware.rest.recipes.model.RecipeImage;
import ca.footeware.rest.recipes.repository.RecipeImageRepository;
import ca.footeware.rest.recipes.repository.RecipeRepository;

/**
 * @author Footeware.ca
 *
 */
@RestController
@RequestMapping
public class RecipeController {

	private RecipeRepository recipeRepo;
	private RecipeImageRepository recipeImageRepo;

	/**
	 * Constructor.
	 * 
	 * @param recipeRepo      {@link RecipeRepository}, injected
	 * @param recipeImageRepo {@link RecipeImageRepository}, injected
	 */
	public RecipeController(RecipeRepository recipeRepo, RecipeImageRepository recipeImageRepo) {
		this.recipeRepo = recipeRepo;
		this.recipeImageRepo = recipeImageRepo;
	}

	/**
	 * Trim, toLowerCase and sort provided strings.
	 * 
	 * @param tags {@link List} of {@link String}
	 * @return {@link List} of {@link String}
	 */
	private List<String> cleanTags(List<String> tags) {
		List<String> tagList = new ArrayList<>();
		String tag;
		for (String orig : tags) {
			tag = orig.trim().toLowerCase();
			if (!tag.isEmpty()) {
				tagList.add(tag);
			}
		}
		Collections.sort(tagList);
		return tagList;
	}

	/**
	 * Create a {@link Recipe} including its images.
	 * 
	 * @param recipeDTO {@link RecipeDTO}
	 * @return {@link ResponseEntity} with {@link RecipeDTO}
	 */
	@PostMapping(value = "/recipes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeDTO> createRecipe(@RequestBody RecipeDTO recipeDTO) {
		String name = recipeDTO.getName().trim();
		String body = recipeDTO.getBody().trim();
		List<String> tags = recipeDTO.getTags();
		List<String> cleanTags = cleanTags(tags);
		List<String> images = recipeDTO.getImages();

		List<String> imageIds = new ArrayList<>();
		for (String image : images) {
			RecipeImage recipeImage = new RecipeImage(image);
			RecipeImage savedImage = recipeImageRepo.save(recipeImage);
			imageIds.add(savedImage.getId());
		}

		Recipe recipe = new Recipe(name, body, cleanTags, imageIds);
		Recipe savedRecipe = recipeRepo.save(recipe);

		// return fresh from db to make sure everything saved
		images = new ArrayList<>();
		imageIds = savedRecipe.getImageIds();
		List<RecipeImage> list = recipeImageRepo.findAllById(imageIds);
		for (RecipeImage recipeImage : list) {
			images.add(recipeImage.getImage());
		}

		RecipeDTO savedRecipeDTO = new RecipeDTO(savedRecipe.getId(), savedRecipe.getName(), savedRecipe.getBody(),
				savedRecipe.getTags(), images);
		return new ResponseEntity<>(savedRecipeDTO, HttpStatus.OK);
	}

	/**
	 * Delete a {@link Recipe} including its images.
	 * 
	 * @param id {@link String}
	 * @return {@link ResponseEntity} with either Http status 200 or 404
	 */
	@DeleteMapping("/recipes/{id}")
	public ResponseEntity<?> deleteRecipe(@PathVariable String id) {
		Optional<Recipe> optional = recipeRepo.findById(id);
		if (optional.isPresent()) {
			Recipe recipe = optional.get();
			List<String> imageIds = recipe.getImageIds();
			for (String imageId : imageIds) {
				recipeImageRepo.deleteById(imageId);
			}
			recipeRepo.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get all recipes, without images.
	 * 
	 * @param pageNumber int
	 * @param pageSize   int
	 * @return {@link ResponseEntity} with {@link PagingDTO}
	 */
	@GetMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagingDTO> getAllRecipes(@RequestParam int pageNumber, @RequestParam int pageSize) {
		Page<Recipe> page = recipeRepo.findAll(PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending()));
		PagingDTO dto = new PagingDTO(page.getTotalElements(), page.getContent());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * Get recipe by id, with images.
	 * 
	 * @param id {@link String}
	 * @return {@link ResponseEntity} with a {@link RecipeDTO} or a Http status 404
	 *         if not found
	 */
	@GetMapping(value = "/recipes/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable String id) {
		Optional<Recipe> optional = recipeRepo.findById(id);
		if (optional.isPresent()) {
			Recipe recipe = optional.get();

			// resolve imageIds
			List<RecipeImage> recipeImages = recipeImageRepo.findAllById(recipe.getImageIds());
			List<String> images = new ArrayList<>();
			for (RecipeImage recipeImage : recipeImages) {
				images.add(recipeImage.getImage());
			}

			RecipeDTO recipeDTO = new RecipeDTO(recipe.getId(), recipe.getName(), recipe.getBody(), recipe.getTags(),
					images);
			return new ResponseEntity<>(recipeDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Update a recipe.
	 * 
	 * @param id        {@link String}
	 * @param recipeDTO {@link RecipeDTO}
	 * @return {@link ResponseEntity} with a {@link RecipeDTO}
	 */
	@PostMapping(value = "/recipes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable String id, @RequestBody RecipeDTO recipeDTO) {
		Optional<Recipe> optional = recipeRepo.findById(id);
		if (optional.isPresent()) {
			Recipe existingRecipe = optional.get();

			// update fields
			existingRecipe.setName(recipeDTO.getName().trim());
			existingRecipe.setBody(recipeDTO.getBody().trim());
			existingRecipe.setTags(cleanTags(recipeDTO.getTags()));

			// delete previously attached images before saving new ones
			recipeImageRepo.deleteAllById(existingRecipe.getImageIds());

			// save new images, storing their ids in the existing recipe
			List<String> images = recipeDTO.getImages();
			List<String> imageIds = new ArrayList<>();
			for (String image : images) {
				RecipeImage recipeImage = new RecipeImage(image);
				RecipeImage savedImage = recipeImageRepo.save(recipeImage);
				imageIds.add(savedImage.getId());
			}
			existingRecipe.setImageIds(imageIds);
			Recipe savedRecipe = recipeRepo.save(existingRecipe);

			// convert to DTO
			List<RecipeImage> recipeImages = recipeImageRepo.findAllById(savedRecipe.getImageIds());
			images = new ArrayList<>();
			for (RecipeImage recipeImage : recipeImages) {
				images.add(recipeImage.getImage());
			}
			RecipeDTO savedRecipeDTO = new RecipeDTO(savedRecipe.getId(), savedRecipe.getName(), savedRecipe.getBody(),
					savedRecipe.getTags(), images);
			return new ResponseEntity<>(savedRecipeDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Search recipes by provided term in name, body or tags. Return them without
	 * images.
	 * 
	 * @param term       {@link String}
	 * @param pageNumber int
	 * @param pageSize   int
	 * @return {@link ResponseEntity} with a {@link PagingDTO}.
	 */
	@GetMapping(value = "/recipes/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagingDTO> search(@RequestParam String term, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		String trimmed = term.trim();
		Page<Recipe> result = recipeRepo
				.findByNameContainingIgnoreCaseOrBodyContainingIgnoreCaseOrTagsContainingIgnoreCaseOrderByNameAsc(
						trimmed, trimmed, trimmed, PageRequest.of(pageNumber, pageSize));
		PagingDTO dto = new PagingDTO(result.getTotalElements(), result.getContent());
		return new ResponseEntity<PagingDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Search for recipes with provided tag in recipes' tags. Return them without
	 * images.
	 * 
	 * @param tag        {@link String}
	 * @param pageNumber int
	 * @param pageSize   int
	 * @return {@link ResponseEntity} with a {@link PagingDTO}
	 */
	@GetMapping(value = "/recipes/search/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagingDTO> searchByTag(@RequestParam String tag, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		Page<Recipe> page = recipeRepo.findByTags(tag, PageRequest.of(pageNumber, pageSize));
		PagingDTO dto = new PagingDTO(page.getTotalElements(), page.getContent());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * Get all tags.
	 * 
	 * @return {@link ResponseEntity} with a {@link List} of {@link String}.
	 */
	@GetMapping(value = "/recipes/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getAllTags() {
		Set<String> allTags = new HashSet<>();
		long rawCount = recipeRepo.count();
		Long l = Long.valueOf(rawCount);
		int count = l.intValue();
		Page<Recipe> page;
		List<Recipe> content;
		for (int i = 0; i < count; i++) {
			page = recipeRepo.findAll(PageRequest.of(i, 10));
			content = page.getContent();
			for (Recipe recipe : content) {
				allTags.addAll(recipe.getTags());
			}
		}
		List<String> allTagsList = new ArrayList<>(allTags);
		allTagsList.sort((o1, o2) -> o1.compareTo(o2));
		return new ResponseEntity<>(allTagsList, HttpStatus.OK);
	}

	/**
	 * Get an recipe image by id.
	 * 
	 * @param id {@link String}
	 * @return {@link ResponseEntity} with a byte array
	 */
	@GetMapping(value = "/recipes/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable String id) {
		Optional<RecipeImage> byId = recipeImageRepo.findById(id);
		if (byId.isPresent()) {
			byte[] decoded = Base64.getDecoder().decode(byId.get().getImage().substring(23));
			return new ResponseEntity<>(decoded, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Get all recipes, without images. FOR TESTING ONLY.
	 * 
	 * @return {@link List} of {@link Recipe}
	 */
	public List<Recipe> getAllRecipes() {
		return recipeRepo.findAll();
	}

}
