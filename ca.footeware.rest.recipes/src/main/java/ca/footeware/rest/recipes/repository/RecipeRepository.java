/**
 * 
 */
package ca.footeware.rest.recipes.repository;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ca.footeware.rest.recipes.model.Recipe;

/**
 * @author Footeware.ca
 *
 */
@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {

	Set<Recipe> findByNameContainingIgnoreCase(String term);

	Set<Recipe> findByBodyContainingIgnoreCase(String term);

	Set<Recipe> findByTagsContainingIgnoreCase(String trimmed);

}
