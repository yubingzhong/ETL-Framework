package com.br.guilherme.etlfw.mask.file;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.br.guilherme.etlfw.exceptions.InvalidRegistrySizeException;
import com.br.guilherme.etlfw.exceptions.UnkownRegistryException;
import com.br.guilherme.etlfw.mask.field.FieldType;
import com.br.guilherme.etlfw.mask.field.TextFieldMask;
import com.br.guilherme.etlfw.mask.file.FileMask;
import com.br.guilherme.etlfw.mask.registry.TextRegistryMask;

public class TestFileMask extends Assert {
	FileMask file;
	
	@Before
	public void setUp() {
		file = new FileMask("Prof","1.0","Arquivo de Professores");
	}
	
	@After
	public void tearDown() {
		file = null;
	}
	
	@Test
	public void shouldInstance() {
		assertTrue(file instanceof FileMask);
		assertEquals("PROF", file.getCode());
		assertEquals("1.0", file.getVersion());
		assertEquals("Arquivo de Professores", file.getDescription());
		assertTrue(file.getRegistryMasks().isEmpty());
	}
	
	@Test
	public void shouldAddRegistryMask() {
		TextRegistryMask registryMask = new TextRegistryMask("Professores", "1.0", "Professor");

		assertTrue(file.getRegistryMasks().isEmpty());
		file.addRegistryMask(registryMask);
		assertFalse(file.getRegistryMasks().isEmpty());
	}
	
	@Test
	public void shouldGetRegistryMaskWithValues() {
		String line = "Luiz Gomes               211.111.111-12";

		TextFieldMask nameMask = new TextFieldMask("Nome", 1, 25, 0, FieldType.A, false, false);
		TextFieldMask documentMask = new TextFieldMask("CPF", 26, 39, 0, FieldType.A, false, true);
		
		TextRegistryMask registryMask = new TextRegistryMask("Professores", "1.0", "Professor");

		registryMask.addField(nameMask);
		registryMask.addField(documentMask);
		file.addRegistryMask(registryMask);
		
		try {
			TextRegistryMask registry = file.getRegistryWithValues(line);
			assertEquals("Luiz Gomes               ", registry.getFields().get(0).getValue());
			assertEquals("211.111.111-12", registry.getFields().get(1).getValue());
		} 
		catch (UnkownRegistryException e) {e.printStackTrace();} 
		catch (InvalidRegistrySizeException e) {e.printStackTrace();}
	}
	
	@Test
	public void shouldCaughtExceptionWithInvalidMask() {
		try {
			file.getRegistryWithValues(new String("Luiz Gomes111.111.111-11"));
			fail();
		} 
		catch (UnkownRegistryException e) {} 
		catch (InvalidRegistrySizeException e) {}
	}
	
}