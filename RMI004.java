import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RMI004 {
	static Thread t;
	
	@BeforeAll
	public static void runServer() throws InterruptedException {
			t = (new Thread() {
				public void run() {
					RMIRegistry.main(new String[0]);
					RMIReplicaManager.main(new String[0]);
					RMIServer.main(new String[]{"2028"});
					RMIServer.main(new String[]{"2029"});
					RMIServer.main(new String[]{"2030"});
				  	}
				 	});
			t.start();
			
			Thread.sleep(1000);
	}
	
	@AfterAll
	public static void stopServer() {
		t.interrupt();
	}

	@Test
	void test() {
		PlacesListManagerInterface plm;
		PlacesListManagerInterface plmret;
		PlacesListInterface pli;
		ObjectRegistryInterface ori;
		
		
		try{
			System.out.println("Localizar ReplicaManager...");
			plm  = (PlacesListManagerInterface) Naming.lookup("rmi://localhost:2024/replicamanager");

			Place p1 = new Place("3510", "Viseu");
			System.out.println("Invocar addPlace() no ReplicaManager para 3510...");
			plm.addPlace(p1);
			
			Place p2 = new Place("1000", "Lisboa");
			System.out.println("Invocar addPlace() no ReplicaManager para 1000...");
			plm.addPlace(p2);
			
			Place p3 = new Place("4000", "Lisboa");
			System.out.println("Invocar addPlace() no ReplicaManager para 4000...");
			plm.addPlace(p3);
			
			System.out.println("Obter o endereço do ReplicaManager no Registry() para o ObjectID 1000 ...");
			ori = (ObjectRegistryInterface) Naming.lookup("rmi://localhost:2023/registry");
			String addrRM = ori.resolve("1000");
			
			System.out.println("Pedir ao ReplicaManager para devolver um PlaceManager para o ObjectID 1000 ...");
			plm = (PlacesListManagerInterface) Naming.lookup(addrRM);
			boolean pl1done = false;
			boolean pl2done = false;
			boolean pl3done = false;
			String plAddress = null;
			for(int i=0; i<10 ; i++) {
				plAddress = plm.getPlaceListAddress("1000");
				System.out.println("\tDevolveu " + plAddress);
				if(plAddress.equals("rmi://localhost:2028/placelist")) pl1done=true;
				else if(plAddress.equals("rmi://localhost:2029/placelist")) pl2done=true;
					 else if(plAddress.equals("rmi://localhost:2030/placelist")) pl3done=true;
			} 
					
			if(!(pl1done==pl2done==pl3done==true)) fail("Não está a devolver um PlaceManager aleatório...");		
					
			System.out.println("Invocar getPlace() no PlaceManager");
			pli  = (PlacesListInterface) Naming.lookup(plAddress);
			String locality = pli.getPlace("1000").getLocality();
			
			System.out.println("\tDevolveu " + locality);
			assertEquals("Lisboa", locality);

			
		} catch(Exception e) { fail("Erro\n" + e.getMessage()); }
	}
}






