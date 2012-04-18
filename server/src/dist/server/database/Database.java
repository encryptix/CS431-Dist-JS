package dist.server.database;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import dist.server.Logger;
import dist.server.ServerGlobals;

public class Database {
	private Connection _connection = null;
	private ResultSet _resultSet = null;
	private PreparedStatement _preparedStatment = null;

	public Database(){
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException ex) {
			Logger.severe("Database.java Constructor","MySQL error: " + ex.getMessage());
		}
	}

	protected void closeDatabase() throws SQLException{
		if (this._resultSet != null) {
			this._resultSet.close();
		}

		if(this._preparedStatment != null){
			this._preparedStatment.close();
		}

		if (this._connection != null) {
			this._connection.close();
		}
	}

	//After this method is called the database connection has to be closed
	protected ResultSet executePreparedSelectStatement(String statement,String[] inputs) throws SQLException{
		this._connection = DriverManager.getConnection(ServerGlobals.databaseUrl,ServerGlobals.databaseUser,ServerGlobals.databasePassword);
		this._preparedStatment = this._connection.prepareStatement(statement);
		for(int i=0;i<inputs.length;i++){
			try{
				this._preparedStatment.setString(i+1, inputs[i]);
			}catch(ArrayIndexOutOfBoundsException e){
				Logger.severe("Database - executePreparedSelectStatement",e.getLocalizedMessage());
			}
		}
		_resultSet = this._preparedStatment.executeQuery();
		Logger.debug("Database.java executePreparedSelectStatement", "Executed Select Statement");
		return this._resultSet;
	}

	protected void executePreparedUpdateOrInsertStatement(String statement,String[] inputs) throws SQLException{
		try{
			this._connection = DriverManager.getConnection(ServerGlobals.databaseUrl,ServerGlobals.databaseUser,ServerGlobals.databasePassword);
			this._preparedStatment = this._connection.prepareStatement(statement);
			for(int i=0;i<inputs.length;i++){
				//try catch for out of bounds
				try{
					this._preparedStatment.setString(i+1, inputs[i]);
				}catch(ArrayIndexOutOfBoundsException e){
					Logger.severe("Database - executePreparedUpdateOrInsertStatement",e.getLocalizedMessage());
				}
			}
			this._preparedStatment.executeUpdate();
			Logger.debug("Database.java executePreparedUpdateOrInsertStatement", "Executed Update Statement");
		}catch(SQLException ex){
			throw ex;
		}finally{
			try{
				this.closeDatabase();
			}catch(SQLException ex){
				throw ex;
			}
		}
	}
	
	protected void executePreparedFileInsertStatement(String statement, String[] inputs, byte[] file) throws SQLException{
		try{
			this._connection = DriverManager.getConnection(ServerGlobals.databaseUrl,ServerGlobals.databaseUser,ServerGlobals.databasePassword);
			this._preparedStatment = this._connection.prepareStatement(statement);
			for(int i=0;i<inputs.length;i++){
				//try catch for out of bounds
				try{
					this._preparedStatment.setString(i+1, inputs[i]);
				}catch(ArrayIndexOutOfBoundsException e){
					Logger.severe("Database - executePreparedFileInsertStatement",e.getLocalizedMessage());
				}
			}
			//add the file at the end of string inputs
			this._preparedStatment.setBytes(inputs.length+1, file);
			this._preparedStatment.executeUpdate();
			Logger.debug("Database.java executePreparedFileInsertStatement", "File Insert Statement Executed");
		}catch(SQLException ex){
			throw ex;
		}finally{
			try{
				this.closeDatabase();
			}catch(SQLException ex){
				throw ex;
			}
		}
	}
	
	protected void executeFileInputStreamInsert(String statement, String[] inputs, InputStream io, int length) throws SQLException{
		try{
			Logger.debug("Database.java executeFileInputStreamInsert", "Trying new insert 1");
			this._connection = DriverManager.getConnection(ServerGlobals.databaseUrl,ServerGlobals.databaseUser,ServerGlobals.databasePassword);
			Logger.debug("Database.java executeFileInputStreamInsert", "Trying new insert 2");
			this._preparedStatment = this._connection.prepareStatement(statement);
			
			for(int i=0;i<inputs.length;i++){
				//try catch for out of bounds
				try{
					this._preparedStatment.setString(i+1, inputs[i]);
				}catch(ArrayIndexOutOfBoundsException e){
					Logger.severe("Database - executeFileInputStreamInsert",e.getLocalizedMessage());
				}
			}
			
			Logger.debug("Database.java executeFileInputStreamInsert", "Trying new insert 3:"+statement);
			this._preparedStatment.setBinaryStream(inputs.length+1,io,length);
			Logger.debug("Database.java executeFileInputStreamInsert", "Trying new insert 4");
			this._preparedStatment.executeUpdate();
			Logger.debug("Database.java executeFileInputStreamInsert", "File Insert Statement Executed");
		}catch(SQLException ex) {
			throw ex;
		}finally{
			try{
				this.closeDatabase();
			}catch(SQLException ex){
				throw ex;
			}
		}
	}
}
