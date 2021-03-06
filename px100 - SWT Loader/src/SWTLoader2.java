import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.widgets.Display;

/*
 * NOTE: add a User Lib containing the SWT lib for the dev environment to
 * be able to debug. When exported the correct lib file will be loaded.
 * 
 * 
 * NOTE: THIS FINALLY WORKS ..
 * Not sure why things *stopped* working, but at some point new builds of SWT
 * apps no longer loaded SWT libraries on Linux/32 (RPi).
 * 		- win 32/64 still worked.
 * 		- existing SWT apps still worked on linux/32. 
 * 		- project was converted to a faceted form and maven, then un-faceted
 * the last change included a few things:
 * 		- copied all the libswt-*.so files into the local dir 
 * 		- copied all the swt*.jar files (appear to be 5 names for 
 * 				the same file) into the local dir
 * 		- "swt-gtk.jar" was added to *external jar loading* code (this file) 
 * 		- the embedded jar loading was disabled
 * notes/future-changes
 * 		- "lib" can now be removed. the embedded jar loading was disabled.
 */
@SuppressWarnings("unused")
public class SWTLoader2 {

	public static void doPrintProperties() {
		final Properties properties = System.getProperties();
		final Set<Object> setKeys = properties.keySet();
		final List<String> listKeys = new LinkedList<String>();
		for ( final Object objName : setKeys ) {
			listKeys.add( objName.toString() );
		}

		Collections.sort( listKeys );
		for ( final String strName : listKeys ) {
			final Object objValue = properties.getProperty( strName.toString() );
			System.out.println( "\"" + strName + "\"=\"" + objValue + "\"" );
		}
	}
	
	public static void printLoadedClasses() {
		try {
			System.out.println( "Currently loaded classes:" );

			final Field field = ClassLoader.class.getDeclaredField( "classes" );
			field.setAccessible( true );
			
//			final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
			final ClassLoader ccl = ClassLoader.getSystemClassLoader();
			@SuppressWarnings("unchecked")
			final Vector<Class<?>> classes = (Vector<Class<?>>) field.get( ccl );
			
			for ( final Class<?> clazz : classes ) {
				System.out.println( "\t" + clazz.getCanonicalName() );
			}
			
			System.out.println( "Currently loaded jars:" );
			
			if ( ccl instanceof URLClassLoader ) {
				for ( final URL url : ((URLClassLoader) ccl).getURLs() ) {
					System.out.println( "\t" + url.toString() );
				}
			}
			
		} catch ( final Exception e ) {
			System.out.println( "Exception encountered: " + e.toString() );
		}
	}
	
	/*
	 * From:
	 * https://www.chrisnewland.com/select-correct-swt-jar-for-your-os-and-jvm-at-runtime-191
	 */
	public static void addJarToClasspath( final File jarFile ) {
		if ( null==jarFile ) throw new IllegalStateException( "null jarFile" );
		try {
			System.out.println( "Loading external JAR: " + jarFile );
			if ( !jarFile.exists() ) {
				System.err.println( "JAR file does not exist." );
				return;
			}

			final URL url = jarFile.toURI().toURL();
			URLClassLoader urlClassLoader = 
					(URLClassLoader) ClassLoader.getSystemClassLoader();
			
			Class<?> urlClass = URLClassLoader.class;
			
			Method method = urlClass.getDeclaredMethod( 
					"addURL", new Class<?>[] { URL.class });
			
			method.setAccessible(true);
			
			method.invoke( urlClassLoader, new Object[] { url } );
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	public static void loadEmbeddedLibrary( final String strLibFile ) 
												throws Exception {

		final ClassLoader loader = ClassLoader.getSystemClassLoader();

//		final String strLibFile = "lib/Win64/swt.jar";
		
		
//		   File file = ...
//				    URL url = file.toURI().toURL();
//
//				    URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();

		final URL url = loader.getResource( strLibFile );

		System.out.println( "\tLibrary URL: " + url );

		URL[] urls = new URL[] { url };
		
//		System.out.print( "\tInstantiating URLClassLoader..." );
//		final URLClassLoader ucl = new URLClassLoader( urls, SWTLoader.class.getClassLoader() );
//		final URLClassLoader ucl = new URLClassLoader( urls, SWTLoader.class.getClassLoader() );
//		System.out.println( "Done." );
		
		final URLClassLoader ucl = (URLClassLoader)loader;
		
		System.out.print( "\tInvoking addURL()..." );
		final Method method = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
		method.setAccessible( true );
		method.invoke( ucl, url ); // no result
		System.out.println( "Done." );
		
		//display.getDefault()
		//     new org.eclipse.swt.graphics.DeviceData().

		
		
	}
	
	
	
	public static void loadSWT() {
		System.out.println( "SWTLoader startup - " + new Date().toString() );

		boolean bWin = false;
		boolean bWorking = false;

//		try {
//			Thread.sleep( 2000 );
//		} catch ( final InterruptedException e ) {}
		
//		// check to see if SWT is already loaded..
//		printLoadedClasses();
		
		System.out.println( "Attempting to locate a SWT library." );
		
		final String strOSName = System.getProperty( "os.name" );
		System.out.println( "\tOS Name: " + strOSName );
		final String strOSArch = System.getProperty( "sun.arch.data.model" );
		System.out.println( "\tOS Arch: " + strOSArch );
		
		
		final String strOSAbbr;
		final List<File> fileSwtLib = new LinkedList<>();
		
		if ( strOSName.toUpperCase().contains( "WINDOWS" ) ) {
			strOSAbbr = "Win";
			bWin = true;
			fileSwtLib.add( new File( "S:\\Resources\\lib\\Win32\\swt.jar" ) );
		} else if ( strOSName.toUpperCase().contains( "LINUX" ) ) {
			strOSAbbr = "Lnx";
//			fileSwtLib = new File( "/Share/Development/Export/swt.jar" );
			fileSwtLib.add( new File( "/Share/Resources/lib/swt-gtk.jar" ) );
			fileSwtLib.add( new File( "/Share/Resources/lib/Lnx32/swt.jar" ) );
//			fileSwtLib.add( new File( "/Share/Resources/lib/swt.jar" ) );
//			fileSwtLib.add( new File( "/Share/Resources/lib/.jar" ) );
//			fileSwtLib.add( new File( "/Share/Resources/lib/.jar" ) );
//			fileSwtLib.add( new File( "/Share/Resources/lib/.jar" ) );
			bWin = false;
		} else {
			strOSAbbr = "UNKNOWN";
//			fileSwtLib = null;
		}
		

		
		
//		final String strPWD = System.getProperty( "user.dir" );
		
//		final String strLibDir = "lib_02/" + strOSAbbr + strOSArch;
		final String strLibDir = strOSAbbr + strOSArch;
//		final String strLibDir = strPWD + File.separator + strOSAbbr + strOSArch;
		try {
			final ClassLoader loader = ClassLoader.getSystemClassLoader();


//			final ClassLoader loader = String.class.getClassLoader();

			final String strLibFile = strLibDir + "/swt.jar";
			
			System.out.println( "\tLibrary file: " + strLibFile );

			
//			loadEmbeddedLibrary( strLibFile );

			for ( final File file : fileSwtLib ) {
				addJarToClasspath( file );
			}
			
			
			
			
			printLoadedClasses();

			
			
			System.out.print( "\tDefining DeviceData class..." );
			Class<?> clazz = Class.forName( 
						"org.eclipse.swt.graphics.DeviceData", true, loader );
			System.out.println( "Done." );
//			Method method2 = clazz.getDeclaredMethod( "toString" );
			System.out.print( "\tInstantiating DeviceData..." );
			Object instance = clazz.newInstance();
			System.out.println( "Done." );
//			Object result = method2.invoke( instance );
			System.out.println( "\tInstance: " + instance );
			
//			Display.getDefault();
			
//			JarClassLoader jcl = new JarClassLoader();
//			jcl.add("myjar.jar"); // Load jar file  
//			jcl.add(new URL("http://myserver.com/myjar.jar")); // Load jar from a URL
//			jcl.add(new FileInputStream("myotherjar.jar")); // Load jar file from stream
//			jcl.add("myclassfolder/"); // Load class folder  
//			jcl.add("myjarlib/"); // Recursively load all jar files in the folder/sub-folder(s)
//
//			JclObjectFactory factory = JclObjectFactory.getInstance();
//			// Create object of loaded class  
//			Object obj = factory.create(jcl, "mypackage.MyClass");
			
//			System.out.println( "Using ClassLoader: " + loader );
//			System.out.println( "Attempting to load: " + strLibFile );
			
//			loader.loadClass( strLibFile );
			
//			final Display display = Display.getDefault();
//			final Display display = Display.getAppName();
			
			System.out.println( "Loaded " 
						+ ( Display.getAppName() + " " + Display.getAppVersion() ).trim() + "." );
//			System.out.println( "\tDisplay.getAppName(): " + Display.getAppName() );
//			System.out.println( "\tDisplay.getAppVersion(): " + Display.getAppVersion() );
			
			bWorking = true;
			
//		} catch ( final ClassNotFoundException 
//				| NoSuchMethodException 
//				| SecurityException 
//				| IllegalAccessException 
//				| IllegalArgumentException 
//				| InvocationTargetException e ) {
		} catch ( final Throwable e ) {
			System.out.println();
			System.err.println( "Unable to load SWT class for " 
						+ strOSName + ", " + strOSArch + "." );
			e.printStackTrace();
			printLoadedClasses();
		}
		
		
	}
	
	public static void main( final String[] args ) {
		loadSWT();
		TestSWT.main( args );
	}

}
