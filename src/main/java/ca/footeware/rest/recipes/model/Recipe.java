/**
 * 
 */
package ca.footeware.rest.recipes.model;

import java.util.List;

import org.springframework.data.annotation.Id;

/**
 * @author Footeware.ca
 *
 */
public class Recipe {

	private String body;
	@Id
	private String id;
	private List<String> imageIds;
	private String name;
	private List<String> tags;

	/**
	 * Constructor.
	 * 
	 * @param name   {@link String}
	 * @param body   {@link String}
	 * @param tags   {@link List} of {@link String}
	 * @param imageIds {@link List} of {@link String}
	 */
	public Recipe(String name, String body, List<String> tags, List<String> imageIds) {
		this.name = name;
		this.body = body;
		this.tags = tags;
		this.imageIds = imageIds;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the images
	 */
	public List<String> getImageIds() {
		return imageIds;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @param images the images to set
	 */
	public void setImageIds(List<String> imageIds) {
		this.imageIds = imageIds;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
