package org.sugarj.test.java;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class JavaCaseStudiesTest {

	
	@Test
	public void testPairs() {
		CompilerWrapper wrapper = new CompilerWrapper(new File(
				"../case-studies/pairs/src/"));
		wrapper.callCompiler("pair/Pair.sugj", "pair/Test.sugj");
		wrapper.callCompiler("pair/singleton/Pair.sugj");
		wrapper.callCompiler("tuples/Test.sugj" ,  "pair/singleton/Test.sugj", "pair/concrete/Test.sugj");
		wrapper.callMainMethod("pair.Test");
	}
	
	@Test
	public void testClosures() {
		CompilerWrapper wrapper = new CompilerWrapper(new File("../case-studies/closures/src/"));
		wrapper.callCompiler("javaclosure/Closure.sugj", "javaclosure/Syntax.sugj", "javaclosure/ToRefType.sugj");
		wrapper.callCompiler("javaclosure/Analysis.sugj", "javaclosure/Transformation.sugj");
		wrapper.callCompiler("javaclosure/Test.sugj");
		wrapper.callMainMethod("javaclosure.Test");
		wrapper.callCompiler("javaclosure/alternative/Arrows.sugj");
		wrapper.callCompiler("javaclosure/alternative/ArrowTest.sugj");
		wrapper.callMainMethod("javaclosure.alternative.ArrowTest");
	}
	
	@Test
	public void testQuestionnaireLanguage() {
		CompilerWrapper wrapper = new CompilerWrapper(new File("../case-studies/questionnaire-language/src/"));
		wrapper.callCompiler("quest/lang/Syntax.sugj");
		wrapper.callCompiler("quest/lang/NormalizeQuestionText.sugj");
		wrapper.callCompiler("quest/lang/Editor.sugj");
		wrapper.callCompiler(new File("quest/lang/var/"));
		wrapper.callCompiler("quest/analysis/Satisfiability.sugj", "quest/analysis/Naming.sugj");
		wrapper.callCompiler("quest/analysis/Determinism.sugj", "quest/analysis/Typing.sugj");
		wrapper.callCompiler("quest/lang/Transform.sugj");
		wrapper.callCompiler("quest/style/Syntax.sugj");
		wrapper.callCompiler("quest/Language.sugj");
		wrapper.callCompiler("test/HouseOwning.sugj");		
	}
	
	@Test
	public void testXML() {
		CompilerWrapper wrapper = new CompilerWrapper(new File("../case-studies/xml/src/"));
		wrapper.callCompiler("concretesyntax/MetaExplode.sugj");
		wrapper.callCompiler("concretesyntax/EditorServices.sugj", "concretesyntax/Java.sugj", "concretesyntax/Stratego.sugj");
		
		wrapper.callCompiler("eblock/ConcatIds.sugj");
		wrapper.callCompiler("eblock/EBlock.sugj");
		wrapper.callCompiler("editor/util/Strategies.sugj");
		wrapper.callCompiler("editor/ASTBuilder.sugj", "editor/Colors.sugj", "editor/Editor.sugj", "editor/Origin.sugj");
		
		wrapper.callCompiler("xml/XmlSyntax.sugj", "xml/PrintingHandler.sugj", "xml/CSS.sugj");
		wrapper.callCompiler("xml/XmlTextTools.sugj", "xml/XmlJavaSyntax.sugj", "xml/Editor.sugj", "xml/CSSXmlEditor.sugj", "xml/CSSEditor.sugj", "xml/ConcreteXml.sugj", "xml/Checks.sugj");
		wrapper.callCompiler("xml/TestCSS.sugj", "xml/BookTest.sugj", "xml/AsSax.sugj");
		wrapper.callCompiler("xml/Sugar.sugj");
		wrapper.callCompiler("xml/TestCSS.sugj", "xml/Test.sugj");
	
		
		wrapper.callCompiler("xml/schema/XmlSchemaJavaSyntax.sugj", "xml/schema/SharedStrategies.sugj");
		wrapper.callCompiler("xml/schema/Editor.sugj", "xml/schema/AsEditorService.sugj");
		wrapper.callCompiler("xml/schema/AsDesugaring.sugj");
		wrapper.callCompiler("xml/schema/XmlSchema.sugj");
		wrapper.callCompiler("xml/schema/TestXmlSchema.sugj", "xml/schema/TestSchema.sugj", "xml/schema/BookSchema.sugj");
		wrapper.callCompiler("xml/schema/Test.sugj", "xml/schema/BookSchemaTest.sugj");
		
		wrapper.callMainMethod("xml.Test");
		wrapper.callMainMethod("xml.TestCSS");
		wrapper.callMainMethod("xml.schema.Test");
		wrapper.callMainMethod("xml.schema.TestXmlSchema");
		
	}
	

}
