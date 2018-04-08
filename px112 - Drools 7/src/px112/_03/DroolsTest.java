package px112._03;

import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

/*
 * This is largely copied from:
 * https://github.com/kiegroup/drools/blob/master/drools-compiler/src/test/java/org/drools/compiler/integrationtests/KieBuilderTest.java
 */
public class DroolsTest {

//	final static String strPackage = "package org.drools.compiler";
	final static String strPackage = "package px112._03";
	

    public static void testResourceInclusion() {
    	
    	System.out.println( "starting.." );
    	
        final String drl1 = strPackage + "\n" +
                "rule R1 when\n" +
                "   $m : Message()\n" +
                "then\n" +
                "   System.out.println( \"HIT: drl1\" );\n" +
                "end\n";

        final String drl2 = strPackage + "\n" +
                "rule R2 when\n" +
                "   $m : Message( message == \"Hi Universe\" )\n" +
                "then\n" +
                "   System.out.println( \"HIT: drl2\" );\n" +
                "end\n";

        final String drl3 = strPackage + "\n" +
                "rule R3 when\n" +
                "   $m : Message( message == \"Hello World\" )\n" +
                "then\n" +
                "   System.out.println( \"HIT: drl3\" );\n" +
                "end\n";

        final String drl4 = strPackage + "\n" +
                "rule R4 when\n" +
                "   $m : Message( message == \"Hello Earth\" )\n" +
                "then\n" +
                "   System.out.println( \"HIT: drl4\" );\n" +
                "end\n";

        final String kmodule = "<kmodule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                "         xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"kbase1\" default=\"true\" eventProcessingMode=\"stream\" equalsBehavior=\"identity\" scope=\"javax.enterprise.context.ApplicationScoped\">\n" +
                "    <ksession name=\"ksession1\" type=\"stateful\" default=\"true\" clockType=\"realtime\" scope=\"javax.enterprise.context.ApplicationScoped\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";

        final KieServices ks = KieServices.Factory.get();

    	System.out.println( "creating resources.." );

        // Create an in-memory jar for version 1.0.0
        final ReleaseId releaseId1 = ks.newReleaseId( "org.kie", "test-kie-builder", "1.0.0" );
        final Resource r1 = ResourceFactory.newByteArrayResource( drl1.getBytes() ).setResourceType( ResourceType.DRL ).setSourcePath( "kbase1/drl1.drl" );
        final Resource r2 = ResourceFactory.newByteArrayResource( drl2.getBytes() ).setResourceType( ResourceType.GDRL ).setSourcePath( "kbase1/drl2.gdrl" );
        final Resource r3 = ResourceFactory.newByteArrayResource( drl3.getBytes() ).setResourceType( ResourceType.RDRL ).setSourcePath( "kbase1/drl3.rdrl" );
        final Resource r4 = ResourceFactory.newByteArrayResource( drl4.getBytes() ).setResourceType( ResourceType.TDRL ).setSourcePath( "kbase1/drl4.tdrl" );

    	System.out.println( "calling createAndDeployJar().." );
        
        final KieModule km = CommonTestMethodBase.createAndDeployJar( ks,
                                           kmodule,
                                           releaseId1,
                                           r1,
                                           r2,
                                           r3,
                                           r4 );

//        final InternalKieModule ikm = (InternalKieModule) km;
//        assertNotNull( ikm.getResource( r1.getSourcePath() ) );
//        assertNotNull( ikm.getResource( r2.getSourcePath() ) );
//        assertNotNull( ikm.getResource( r3.getSourcePath() ) );
//        assertNotNull( ikm.getResource( r4.getSourcePath() ) );
        
    	System.out.println( "creating a session.." );

        // Create a session and fire rules
        final KieContainer kc = ks.newKieContainer( km.getReleaseId() );
        final KieSession ksession = kc.newKieSession();
        System.out.println( "KieSession: " + ksession );
        
        final Message message = new Message( "Hello World" );
        System.out.println( "Message: " + message );
        
		ksession.insert( message );
        final int iFired = ksession.fireAllRules();
        
        System.out.println( "iFired: " + iFired );

//        assertEquals( 2, ksession.fireAllRules() );
        ksession.dispose();
    }

	
    public static void main( final String[] args ) {
    	testResourceInclusion();
	}
    
	
}
