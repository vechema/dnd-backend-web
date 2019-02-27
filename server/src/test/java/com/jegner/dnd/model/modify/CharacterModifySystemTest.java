package com.jegner.dnd.model.modify;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.jegner.dnd.model.Character;
import com.jegner.dnd.model.CharacterAbility;
import com.jegner.dnd.model.item.Armor;
import com.jegner.dnd.model.item.Inventory;
import com.jegner.dnd.model.predefined.AbilityScore;
import com.jegner.dnd.utility.GameEntity;

public class CharacterModifySystemTest {

	@Test
	public void armorModifiedByDexModTest() {

		// Create Character
		Character character = new Character();
		Inventory inventory = new Inventory();
		character.setInventory(inventory);

		// Set AbilityScore
		AbilityScore dex = new AbilityScore();
		int dexScore = 18; // 4
		int dexModAmount = CharacterAbility.calculateModifier(dexScore);

		// Dex modifier
		Modify dexMod = new Modify();
		dexMod.setModifyField(ModifyField.DEXTERITY_MOD);
		dexMod.setBase(dexModAmount);

		GameEntity dexGameEntity = new GameEntity();
		dexGameEntity.setName("Dexterity");
		dexGameEntity.setModify(dexMod);
		dex.setGameEntity(dexGameEntity);

		// Give the dex to the player
		character.addAbilityScore(dex, dexScore);

		// Create Armor
		Armor armor = new Armor();

		// What is armor modified by? Dex in this case
		Modify armorModify = new Modify();
		armorModify.setBase(14);
		armorModify.setModifyField(ModifyField.ARMOR_AC);
		armorModify.setFieldsIModify(Arrays.asList(ModifyField.CHARACTER_AC));

		Map<ModifyField, Integer> fieldsThatModifyMeMap = new HashMap<>();
		fieldsThatModifyMeMap.put(ModifyField.DEXTERITY_MOD, Integer.MAX_VALUE);
		armorModify.setFieldsThatModifyMe(fieldsThatModifyMeMap);

		// Wrap it up together
		GameEntity armorGameEntity = new GameEntity();
		armorGameEntity.setName("Ring Mail");
		armorGameEntity.setModify(armorModify);
		armor.setGameEntity(armorGameEntity);

		// Put armor in character inventory & equip
		character.addItem(armor);
		character.equip(armor);

		// Get Character AC
		int ac = character.getAC();
		System.out.println(ac);

		assertThat(ac, is(armor.getGameEntity().getModify().getBase() + dexModAmount));

		int dexModLimit = 2;
		fieldsThatModifyMeMap.put(ModifyField.DEXTERITY_MOD, dexModLimit);
		ac = character.getAC();
		System.out.println(ac);
		assertThat(ac, is(armor.getGameEntity().getModify().getBase() + dexModLimit));
	}

	@Test
	public void armorModifiedByDexOrStrengthModTest() {

		// Create Character
		Character character = new Character();
		Inventory inventory = new Inventory();
		character.setInventory(inventory);

		// Set AbilityScore
		AbilityScore dex = new AbilityScore();
		int dexScore = 16; // 3
		int dexModAmount = CharacterAbility.calculateModifier(dexScore);
		AbilityScore strength = new AbilityScore();
		int strengthScore = 20; // 5
		int strengthModAmount = CharacterAbility.calculateModifier(strengthScore);

		// Dex modifier
		Modify dexMod = new Modify();
		dexMod.setModifyField(ModifyField.DEXTERITY_MOD);
		dexMod.setBase(dexModAmount);

		GameEntity dexGameEntity = new GameEntity();
		dexGameEntity.setName("Dexterity");
		dexGameEntity.setModify(dexMod);
		dex.setGameEntity(dexGameEntity);

		// Strength modifier
		Modify strengthMod = new Modify();
		strengthMod.setModifyField(ModifyField.STRENGTH_MOD);
		strengthMod.setBase(strengthModAmount);

		GameEntity strengthGameEntity = new GameEntity();
		strengthGameEntity.setName("Strength");
		strengthGameEntity.setModify(strengthMod);
		strength.setGameEntity(strengthGameEntity);

		// Give the dex to the player
		character.addAbilityScore(dex, dexScore);
		character.addAbilityScore(strength, strengthScore);

		// Create Armor
		Armor armor = new Armor();

		// What is armor modified by? Dex in this case
		Modify armorModify = new Modify();
		armorModify.setBase(14);
		armorModify.setModifyField(ModifyField.ARMOR_AC);
		armorModify.setFieldsIModify(Arrays.asList(ModifyField.CHARACTER_AC));

		Map<ModifyField, Integer> fieldsThatModifyMeMap = new HashMap<>();
		fieldsThatModifyMeMap.put(ModifyField.DEXTERITY_MOD, Integer.MAX_VALUE);
		fieldsThatModifyMeMap.put(ModifyField.STRENGTH_MOD, Integer.MAX_VALUE);
		armorModify.setFieldsThatModifyMe(fieldsThatModifyMeMap);

		// Wrap it up together
		GameEntity armorGameEntity = new GameEntity();
		armorGameEntity.setName("Ring Mail");
		armorGameEntity.setModify(armorModify);
		armor.setGameEntity(armorGameEntity);

		// Put armor in character inventory & equip
		character.addItem(armor);
		character.equip(armor);

		// Get Character AC
		int ac = character.getAC();
		System.out.println(ac);

		assertThat(ac, is(armor.getGameEntity().getModify().getBase() + dexModAmount + strengthModAmount));

		int dexModLimit = 2;
		fieldsThatModifyMeMap.put(ModifyField.DEXTERITY_MOD, dexModLimit);
		ac = character.getAC();
		System.out.println(ac);
		assertThat(ac, is(armor.getGameEntity().getModify().getBase() + dexModLimit + strengthModAmount));

		armorModify.setModifyOperation(ModifyOperation.MAX);
		ac = character.getAC();
		System.out.println(ac);
		assertThat(ac, is(armor.getGameEntity().getModify().getBase() + Math.max(dexModLimit, strengthModAmount)));
	}
}
