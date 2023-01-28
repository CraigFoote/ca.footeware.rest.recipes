/**
 * 
 */
package ca.footeware.rest.recipes.model;

import org.springframework.data.annotation.Id;

/**
 * @author Footeware.ca
 *
 */
public class RecipeImage {

	@Id
	private String id;
	private String image;

	/**
	 * Constructor.
	 * 
	 * @param image {@link String}, base64-encoded
	 */
	public RecipeImage(String image) {
		this.image = image;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
