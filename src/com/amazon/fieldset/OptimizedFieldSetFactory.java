package com.amazon.fieldset;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A factory for creating OptimizedFieldSet objects.
 */
@Component("OptimizedFieldSetFactory")
@Scope("singleton")
public class OptimizedFieldSetFactory implements FieldSetFactory {
	/**
	 * Creates the.
	 * 
	 * @param values
	 *            Array.
	 * @param names
	 *            Array.
	 * @return FieldSet Implementation
	 */
	@Override
	public FieldSet create(String[] values, String[] names) {
		return new OptimizedFieldSet(values, names);
	}

	/**
	 * Creates the.
	 * 
	 * @param values
	 *            Array.
	 * @return FieldSet Implementation
	 */
	@Override
	public FieldSet create(String[] values) {
		return new OptimizedFieldSet(values);
	}
}