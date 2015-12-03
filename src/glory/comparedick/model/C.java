package glory.comparedick.model;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class C {

	public static final String BASE_URI = "http://comparedick.glory/def#";

	public static String getURI() {
		return BASE_URI;
	}

	// Classes
	public static final Resource Character = resource("Character");
	public static final Resource Corporation = resource("Corporation");
	public static final Resource Alliance = resource("Alliance");
	public static final Resource Coalition = resource("Coalition");
	public static final Resource Kill = resource("Kill");

	// Object properties
	public static final Property happenedIn = property("happenedIn");
	public static final Property relatedWithPerson = property("relatedWithPerson");
	public static final Property relatedWithFile = property("relatedWithFile");
	public static final Property relatedWithConcept = property("relatedWithConcept");
	public static final Property relationship = property("relationship");
	public static final Property marriedWith = property("marriedWith");
	public static final Property diedIn = property("diedIn");
	public static final Property bornIn = property("bornIn");

	// Data properties
	public static final Property internalPath = property("internalPath");

	// Concept individuals
	public static final Resource Environment_root = resource("Environment_root");

	private static final Resource resource(String local) {
		return ResourceFactory.createResource(BASE_URI + local);
	}

	private static final Property property(String local) {
		return ResourceFactory.createProperty(BASE_URI, local);
	}

}