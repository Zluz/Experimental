import java.util.List;

import es.neodoo.vehicle.tesla.api.exceptions.OauthInvokerException;
import es.neodoo.vehicle.tesla.api.exceptions.TeslaInvokerException;
import es.neodoo.vehicle.tesla.api.methods.ListVehicles;
import es.neodoo.vehicle.tesla.api.params.ListVehiclesParamResponse;
import es.neodoo.vehicle.tesla.api.params.ListVehiclesResponse;
import es.neodoo.vehicle.tesla.invoker.TeslaInvoker;

public class TeslaClientTest {

	public static void main( final String[] args ) 
					throws OauthInvokerException, TeslaInvokerException {
		
		final String strURI = "";
		final String strType = "";
		final String strClientID = "";
		final String strClientSecret = "";
		final String strEmail = "";
		final String strPassword = "";
		
//		final OauthInvoker oai = 
//				new OauthInvoker( strURI, strType, 
//									strClientID, strClientSecret, 
//									strEmail, strPassword );
//		oai.callOauthServer();
		
		final TeslaInvoker invoker = 
				new TeslaInvoker( strURI, strType, 
									strClientID, strClientSecret, 
									strEmail, strPassword );
//		invoker.setOauthInvoker( oai );
		
		final ListVehicles lv = new ListVehicles( invoker );
		final ListVehiclesResponse response = lv.execute();
		
		final List<ListVehiclesParamResponse> list = response.getResponse();
		if ( null==list ) {
			System.out.println( "Null response." );
			return;
		}
		if ( !list.isEmpty() ) {
			System.out.println( "Count: " + list.size() );
			final ListVehiclesParamResponse car = list.get( 0 );
			System.out.println( "Display name: " + car.getDisplay_name() );
			System.out.println( "Color: " + car.getColor() );
			System.out.println( "State: " + car.getState() );
		} else {
			System.out.println( "Empty list." );
			return;
		}
	}

}
