package main.java.de.klinikum.appserver.domain.skos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import main.java.de.klinikum.appserver.domain.SKOS;


/**
 * A data store that helps manage multiple different relations to different targets; 
 * e.g. an object of this class is used to manage all concept relations for one 
 * concept (be that related, has broader or has narrower)
 */
public class VariedRelationStore<T> implements Serializable {
	
	private static final long serialVersionUID = -4869884976678488660L;
	
private Map<SKOS,Set<T>> values = new HashMap<SKOS,Set<T>>();
	
	private Set<T> getPropertySet(SKOS property) {
		Set<T> propertyValues = values.get(property);
		if (propertyValues == null) {
			propertyValues = new HashSet<T>();
			values.put(property, propertyValues);
		}
		return propertyValues;
	}
	
	public void addValue(SKOS property, T value) {
		getPropertySet(property).add(value);
	}

	public void removeValue(SKOS property, T value) {
		getPropertySet(property).remove(value);
	}
	
	public Set<T> getValues(SKOS property) {
		return getPropertySet(property);
	}
	
	public Set<T> getValues(){
		Set<T> allValues = new HashSet<T>();
		for(SKOS property : values.keySet()){
			allValues.addAll(values.get(property));
		}
		return allValues;
	}
	
	public T getValue(SKOS property) {
		Iterator<T> it = getPropertySet(property).iterator();
		if (it.hasNext()) return it.next();
		else return null;
	}
	
	public boolean hasEqualValues(VariedRelationStore<T> other) {
		//slightly wasteful ..
		for (SKOS key: values.keySet()) {
			Set<T> otherValues = other.getValues(key);
			Set<T> values = getValues(key);
			for (T value: values) if (!otherValues.contains(value)) return false;
		}
		for (SKOS key: other.values.keySet()) {
			Set<T> otherValues = other.getValues(key);
			Set<T> values = getValues(key);
			for (T value: otherValues) if (!values.contains(value)) return false;
		}
		return true;
	}
}
