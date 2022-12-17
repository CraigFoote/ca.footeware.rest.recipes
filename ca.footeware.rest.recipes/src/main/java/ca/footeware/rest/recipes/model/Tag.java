/**
 * 
 */
package ca.footeware.rest.recipes.model;

/**
 * @author Footeware.ca
 *
 */
public record Tag(String value) {
	public Tag(String value) {
		this.value = value;
	}
}
