package benutzerverwaltung;
import java.util.*;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassBenutzerverwaltung bver = new ClassBenutzerverwaltung("myhome","myhome","myhome");
		//ClassBenutzerverwaltung bver = new ClassBenutzerverwaltung("db_myhome","root","");
		User testUser;
		List<Right> ListUser= new ArrayList<Right>();
		//bver.setDefaultRoles();
		//bver.addUser("SebastianKania", "12345", true, "Handy",1);
		//bver.addUser("rudi", "rudi", true, "Handy/EMail",0);
		//bver.addUser("chris", "chris", false, "Handy",2);
		testUser = bver.readUser(1);
		System.out.println("ID:" + testUser.getID());
		System.out.println("Name:" + testUser.getUserName());
		System.out.println("Admin:" + testUser.getUserName());
		System.out.println("PreferredCom:" + testUser.getPrefCom());
		System.out.println("Role" + testUser.getRoleId());
		ListUser = bver.hasRights(1);
		/*...............................................*/
		bver.addRightToRole(3,2,false);
		/*...............................................*/
		/*...............................................*/
		//Test für Methode Recht löschen
		//bver.deleteRightFromRole(3,1);
		/*...............................................*/
		//Testdaten für Methode addRole
		/*List<Right> bufferRightTest = new ArrayList<Right>();
		
		for(int i = 0; i < 5; i++)
		{
			Right testRight = new Right();
			testRight.setNodeId(8+i);
			testRight.setWritable(true);
			bufferRightTest.add(testRight);
		}
		
		bver.addRole("Test4", bufferRightTest);*/
		/*...............................................*/
		
		/*
		/*LOGIN TEST*/
		/*
		if(bver.login("rudi", "rudi"))
			System.out.print("logged in!");
		else
			System.out.print("Error login");
		*/
		/*........................................................*/
		
		/*READ USER TEST*/
		/*
		testUser = bver.readUser(6);
		System.out.println("ID:" + testUser.getID());
		System.out.println("Username:" + testUser.getUserName());
		System.out.println("IsAdmin:" + testUser.getIsAdmin());
		System.out.println("PrefCom:" + testUser.getPrefCom());
		System.out.println("Rolle:" + testUser.getRoleId());
		*/
		/*........................................................*/
		
		/*READ_ALL_USER TEST*/
		/*List<User> bla = bver.readAllUserID();
		
		for(int i = 0; i < bla.size(); i++)
		{	
			User bufferUser = new User();
			bufferUser = bla.get(i);
			System.out.println("Count:" + i);
			System.out.println(bufferUser.getID());
			System.out.println(bufferUser.getUserName());
			System.out.println(bufferUser.getIsAdmin());
			System.out.println(bufferUser.getPrefCom());
			System.out.println(bufferUser.getRoleId());
			System.out.println("");
		}*/
		/*........................................................*/
	}

}
