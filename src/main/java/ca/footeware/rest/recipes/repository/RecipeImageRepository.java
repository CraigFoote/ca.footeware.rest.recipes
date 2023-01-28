/**
 * 
 */
package ca.footeware.rest.recipes.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ca.footeware.rest.recipes.model.RecipeImage;

/**
 * @author Footeware.ca
 *
 */
public interface RecipeImageRepository extends MongoRepository<RecipeImage, String> {
}
