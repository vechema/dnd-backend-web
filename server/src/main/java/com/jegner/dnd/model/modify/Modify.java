package com.jegner.dnd.model.modify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
/**
 * An object that is modified by something else
 * 
 * @author Jo
 *
 */
public class Modify {

	@GeneratedValue
	@Id
	private Long id;
	private int base;
	/**
	 * Field that is modified (AC, HP, etc)
	 */
	private ModifyField modifyField;
	/**
	 * What fields this modify affects
	 */
	@ElementCollection
	private List<ModifyField> fieldsIModify;
	/**
	 * What fields affects this modify -> Limit
	 */
	@ElementCollection
	private Map<ModifyField, Integer> fieldsThatModifyMe;

	// Need to put blank constructor for JPA because adding the string one got rid
	// of the default no param constructor
	public Modify() {
		fieldsIModify = new ArrayList<>();
		fieldsThatModifyMe = new HashMap<>();
	}

	// Added because JPA complained:
	// Cannot construct instance of `com.jegner.dnd.model.modify.Modified` (although
	// at least one Creator exists): no String-argument constructor/factory method
	// to deserialize from String value ('14')
	public Modify(String noop) {

	}

	public int getFieldThatModifiesMeAmount(ModifyField modField, Modify modify) {
		if (!fieldsThatModifyMe.containsKey(modField)) {
			return modify.getBase();
		}
		return Math.min(fieldsThatModifyMe.get(modField), modify.getBase());
	}
}
