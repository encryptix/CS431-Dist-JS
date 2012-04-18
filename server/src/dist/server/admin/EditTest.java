package dist.server.admin;

import dist.server.database.DatabaseCommands;

public class EditTest {
	private DatabaseCommands _dbc;
	
	public EditTest(){
		_dbc = new DatabaseCommands();
	}
	
	public boolean updateEntry(String id,String line){
		return _dbc.editTestEntry(id, line);
	}
	
	public boolean addEntry(String line){
		return _dbc.addTestEntry(line);
	}
}