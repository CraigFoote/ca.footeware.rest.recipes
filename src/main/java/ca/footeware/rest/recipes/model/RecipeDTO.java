/**
 * 
 */
package ca.footeware.rest.recipes.model;

import java.util.List;

/**
 * @author Footeware.ca
 */
public class RecipeDTO {

	private String id;
	private String name;
	private String body;
	private List<String> tags;
	private List<String> images;

	/**
	 * Constructor.
	 * 
	 * @param id     {@link String}
	 * @param name   {@link String}
	 * @param body   {@link String}
	 * @param tags   {@link List} of {@link String}
	 * @param images {@link List} of {@link String}, base64 encoded
	 */
	public RecipeDTO(String id, String name, String body, List<String> tags, List<String> images) {
		this.id = id;
		this.name = name;
		this.body = body;
		this.tags = tags;
		this.images = images;
	}

	/**
	 * @return the images
	 */
	public List<String> getImages() {
		return images;
	}

	/**
	 * @param imageIds the images to set
	 */
	public void setImages(List<String> images) {
		this.images = images;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
