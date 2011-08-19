package etlfw.format.database;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.br.guilherme.etlfw.assignment.AssignmentType;
import com.br.guilherme.etlfw.assignment.ModificationAssignment;
import com.br.guilherme.etlfw.format.database.AlterationFormatBuilder;
import com.br.guilherme.etlfw.mask.FieldMask;
import com.br.guilherme.etlfw.mask.FieldType;

public class TestAlterationFormatBuilder extends Assert {
	
	private AlterationFormatBuilder alterationFormat;

	@Before
	public void setUp() { 
		alterationFormat = new AlterationFormatBuilder();
	}
	
	@After
	public void tearDown() {
		alterationFormat = null;
	}
	
	@Test
	public void shouldInstance() {
		assertTrue(alterationFormat instanceof AlterationFormatBuilder);
		assertEquals("ALTER TABLE", alterationFormat.toString());
	}
	
	@Test
	public void shouldAddTableNameOnStatement() {
		alterationFormat.addTableName("Table");
		assertEquals("ALTER TABLE Table ", alterationFormat.toString());
	}
	
	@Test
	public void shouldAddFieldOnStatement() {
		alterationFormat.addTableName("Table");
		
		ModificationAssignment assignment = new ModificationAssignment(AssignmentType.ALTER, false);
		FieldMask mask = new FieldMask("field", 1, 10, 2, FieldType.A, false, true, false);
		mask.addAssignment(assignment);
		alterationFormat.addField(mask);
		
		assertEquals("ALTER TABLE Table MODIFY FIELD VARCHAR(12,2)", alterationFormat.toString());
	}
	
	@Test
	public void shouldCreateStatement() {
		alterationFormat.addTableName("Table");
		
		ModificationAssignment assignment = new ModificationAssignment(AssignmentType.ALTER, false);
		FieldMask mask = new FieldMask("Field", 1, 10, 2, FieldType.A, false, true, false);
		mask.addAssignment(assignment);
		alterationFormat.addField(mask).addField(mask).finish();
		
		assertEquals("ALTER TABLE Table MODIFY FIELD VARCHAR(12,2),MODIFY FIELD VARCHAR(12,2);", alterationFormat.toString());
	}
	
	@Test
	public void shouldNotCreateStatementWithoutField() {
		alterationFormat.addTableName("Table");
		
		FieldMask mask = new FieldMask("Field", 1, 10, 2, FieldType.A, false, true, false);
		alterationFormat.addField(mask).finish();
		
		assertEquals("", alterationFormat.toString());
	}
	
}
