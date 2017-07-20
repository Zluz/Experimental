import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

public class SWTLoader {

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
	
	public static void loadSWT() {
		
		System.out.println( "Attempting to locate a SWT library." );
		
		final String strOSName = System.getProperty( "os.name" );
		System.out.println( "\tOS Name: " + strOSName );
		final String strOSArch = System.getProperty( "sun.arch.data.model" );
		System.out.println( "\tOS Arch: " + strOSArch );
		final String strOSAbbr;
		if ( strOSName.toUpperCase().contains( "WINDOWS" ) ) {
			strOSAbbr = "Win";
		} else if ( strOSName.toUpperCase().contains( "LINUX" ) ) {
			strOSAbbr = "Lnx";
		} else {
			strOSAbbr = "UNKNOWN";
		}
//		final String strLibDir = "lib_02/" + strOSAbbr + strOSArch;
		final String strLibDir = strOSAbbr + strOSArch;
		try {
			final ClassLoader loader = ClassLoader.getSystemClassLoader();
//			final ClassLoader loader = String.class.getClassLoader();

			final String strLibFile = strLibDir + "/swt.jar";
			
			System.out.println( "\tLibrary file: " + strLibFile );

//			final String strLibFile = "lib/Win64/swt.jar";
			
			
//			   File file = ...
//					    URL url = file.toURI().toURL();
//
//					    URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();

			final URL url = loader.getResource( strLibFile );

			System.out.println( "\tLibrary URL: " + url );

			URL[] urls = new URL[] { url };
			
			System.out.print( "\tInstantiating URLClassLoader..." );
			final URLClassLoader ucl = new URLClassLoader( urls, SWTLoader.class.getClassLoader() );
			System.out.println( "Done." );
			
			System.out.print( "\tInvoking addURL()..." );
			final Method method = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
			method.setAccessible( true );
			method.invoke( ucl, url );
			System.out.println( "Done." );
			
			//display.getDefault()
			//     new org.eclipse.swt.graphics.DeviceData().

			System.out.print( "\tDefining DeviceData class..." );
			Class<?> clazz = Class.forName( "org.eclipse.swt.graphics.DeviceData", true, ucl );
			System.out.println( "Done." );
//			Method method2 = clazz.getDeclaredMethod( "toString" );
			System.out.print( "\tInstantiating DeviceData..." );
			Object instance = clazz.newInstance();
			System.out.println( "Done." );
//			Object result = method2.invoke( instance );
			System.out.println( "\tInstance: " + instance );
			
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
		}
	}
	
	public static void main( final String[] args ) {
		loadSWT();
		TestSWT.main( args );
	}

}
