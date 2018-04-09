package px112._04;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.InternalKieBuilder;
import org.kie.internal.io.ResourceFactory;

import px112.Message;

public class DroolsTest_04 {


	final static String strPackage = "package px112";
	


	public static byte[] createJar(	final KieServices ks, 
									final String kmoduleContent,
									final Predicate<String> classFilter, 
									final ReleaseId releaseId,
									final Resource... resources ) {
		
		final KieFileSystem kfs = ks.newKieFileSystem()
				.generateAndWritePomXML(releaseId)
				.writeKModuleXML(kmoduleContent);
		
		for ( int i = 0; i < resources.length; i++ ) {
			if (resources[i] != null) {
				kfs.write(resources[i]);
			}
		}
		final KieBuilder kieBuilder = ks.newKieBuilder(kfs);
		((InternalKieBuilder) kieBuilder).buildAll(classFilter);
		
		final InternalKieModule kieModule = 
				(InternalKieModule) ks.getRepository().getKieModule(releaseId);
		
		final byte[] jar = kieModule.getBytes();
		return jar;
	}


	public static KieModule createAndDeployJar(	
								final KieServices ks,
								final String kmoduleContent, 
								final ReleaseId releaseId, 
								final Resource... resources ) {
		
		final byte[] jar = createJar( 
				ks, kmoduleContent, o -> true, releaseId, resources );

		Resource jarRes = ks.getResources().newByteArrayResource(jar);
		KieModule km = ks.getRepository().addKieModule(jarRes);
		return km;
	}
	

	final static String[] FILES = {
//			"/src/file.txt",
//			"/file.txt",
//			"file.txt", // seems to work
			
			"/px112/HelloWorld.drl",
			"px112/HelloWorld.drl",
			"/HelloWorld.drl",
			"HelloWorld.drl",
	};
	
	
	
	public static void main( final String[] args ) throws IOException {

		final String strResourcePath = "px112/HelloWorld.drl";
		
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		final InputStream is = cl.getResourceAsStream( strResourcePath );
        
//		for ( final String strFile : FILES ) {
//			System.out.println( "strFile = " + strFile );
//			final InputStream is2 = cl.getResourceAsStream( strFile );
//			if ( null!=is2 ) {
//				System.out.println( "Stream good?  is2 = " + is2.toString() );
//			}
//		}
		
        
        if ( null==is ) return;
        
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

        final String kmodule = 
        		"<kmodule xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                "         xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"kbase1\" default=\"true\" "
	                		+ "eventProcessingMode=\"stream\" "
	                		+ "equalsBehavior=\"identity\" "
	                		+ "scope=\"javax.enterprise.context.ApplicationScoped\">\n" +
                "    <ksession name=\"ksession1\" type=\"stateful\" "
                			+ "default=\"true\" clockType=\"realtime\" "
                			+ "scope=\"javax.enterprise.context.ApplicationScoped\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";

        final KieServices ks = KieServices.Factory.get();

    	System.out.println( "creating resources.." );

        // Create an in-memory jar for version 1.0.0
    	
        final Resource r1 = ResourceFactory.newByteArrayResource( drl1.getBytes() ).setResourceType( ResourceType.DRL ).setSourcePath( "kbase1/drl1.drl" );
        final Resource r2 = ResourceFactory.newByteArrayResource( drl2.getBytes() ).setResourceType( ResourceType.GDRL ).setSourcePath( "kbase1/drl2.gdrl" );
        final Resource r3 = ResourceFactory.newByteArrayResource( drl3.getBytes() ).setResourceType( ResourceType.RDRL ).setSourcePath( "kbase1/drl3.rdrl" );
        final Resource r4 = ResourceFactory.newByteArrayResource( drl4.getBytes() ).setResourceType( ResourceType.TDRL ).setSourcePath( "kbase1/drl4.tdrl" );
        
        // options:
//        ResourceFactory.newFileResource( strFilename );
//        ResourceFactory.newFileResource( fileRules );
//        ResourceFactory.newInputStreamResource( is );
        final Resource r5 = ResourceFactory.newInputStreamResource( is )
					        		.setResourceType( ResourceType.DRL )
					        		.setSourcePath( strResourcePath );


        final ReleaseId releaseId1 = 
        			ks.newReleaseId( "org.kie", "test-kie-builder", "1.0.0" );

    	System.out.println( "calling createAndDeployJar().." );

        final KieModule km = createAndDeployJar( ks,
                                           kmodule,
                                           releaseId1,
                                           r1,
                                           r2,
                                           r3,
                                           r4,
                                           r5 );

        
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

        ksession.dispose();
		
	}


}
