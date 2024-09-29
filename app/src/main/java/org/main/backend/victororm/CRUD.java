package org.main.backend.victororm;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
// import com.google.gson.Gson;

// import org.sqlite.SQLiteException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

public class CRUD<E extends Model> {
	private static final String DB_URL = "jdbc:sqlite:diary3.db";
	private Connection connection;
//	private E e;
	 private Class<E> clazz;
	private String tableName;

	public CRUD(Class<E> clazz) throws SQLException {
        this.clazz = clazz;
   
        try {
            // Create an instance of the class to invoke getTableName()
            E instance = clazz.getDeclaredConstructor().newInstance();
            Method me = clazz.getMethod("getTableName");
            String table = (String) me.invoke(instance); // Invoke method on instance, not clazz

            // Use the table name from the model
            this.tableName = table;

            // Check if the table exists
            this.connection = DriverManager.getConnection(CRUD.DB_URL);
            if (!doesTableExist(tableName)) {
                throw new IllegalArgumentException("Table '" + tableName + "' does not exist in the database.");
            }
            
            System.out.println("Connection stablished");

            
        } catch (Exception e) {
            throw new RuntimeException("Error initializing CRUD", e);
        }
    }
	private static String getterMethod(String field)
    {
        if(field.equals(""))
            return "";
        char ch = field.charAt(0);
        field = field.substring(1,field.length());
        if(ch>90)
        {
            ch-=32;
        }
        return "get"+ch+field;

    }
    private static String setterMethod(String field)
    {
        if(field.equals(""))
            return "";
        char ch = field.charAt(0);
        field = field.substring(1,field.length());
        if(ch>90)
        {
            ch-=32;
        }
        return "set"+ch+field;    
    }
	private Integer create(E object) throws SQLException,IllegalAccessException,NoSuchMethodException,InvocationTargetException
	{
       
		Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder placeholders = new StringBuilder("VALUES (");

        for (Field field : fields) {
            if(field.getName().equals("id"))
                continue;
            sql.append(field.getName()).append(", ");
            placeholders.append("?, ");
        }
        sql.delete(sql.length() - 2, sql.length()).append(") ");
        placeholders.delete(placeholders.length() - 2, placeholders.length()).append(")");

        String query = sql.append(placeholders).toString();
        System.out.println("Generated SQL: " + query);

        // Prepare the statement and set values dynamically
        PreparedStatement stmt = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        int index = 1;
        for (Field field : fields) {
            if(field.getName().equals("id"))
                continue;
            System.out.println("Excuting for: "+field.getName());            
            Method getNameMethod = clazz.getMethod(getterMethod(field.getName()));
            System.out.println("getMethodName: "+getNameMethod);
            if(Model.class.isAssignableFrom(field.getType())) {
                Model nestedModel = (Model) getNameMethod.invoke(object);
                System.out.println("nestedModel: "+nestedModel);
                if (nestedModel != null) {
                    Integer nestedId = new CRUD<>(nestedModel.getClass()).save(nestedModel);
                    // Replace the nested model with its ID in the main object
                    // field.set(object, nestedId);
                    stmt.setInt(index, nestedId);
                }
                index++; //make sure even if nested class is null index incremented
                continue;
            }
            //here check if field is of type Model class and handle the nested Model.
            stmt.setObject(index++, getNameMethod.invoke(object));
            System.out.println("Excuted for: "+field.getName());            

        }

        // Execute the INSERT operation
        stmt.executeUpdate();
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) 
        {
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1); // The generated ID
                return id;
            } else {
                System.out.println("No ID obtained.");
                return 0;
            }
        }
         catch(SQLException e) {
            System.out.println(e.getMessage());
            return 0;
            // System.out.println("Record inserted successfully.");
         }
	}
	private Integer update(E object) throws NoSuchMethodException,ClassCastException, SQLException,IllegalAccessException,InvocationTargetException,IllegalArgumentException
	{
      
        System.out.println("Here it comes for update: "+object);
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");

        for (Field field : fields) {
            if(!field.getName().equals("id"))
            {
                sql.append(field.getName()).append(" = ?, ");
            }
            // placeholders.append("?, ");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" WHERE id = "+object.getId()); // it is safe to write here like this, since it will be alwasy number so no risk of SQL Injection here
        String query = sql.toString();
        System.out.println("Generated SQL: " + query);

        // Prepare the statement and set values dynamically
        PreparedStatement stmt = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
        int index = 1;
        for (Field field : fields) {
            if(field.getName().equals("id"))
                continue;
                System.out.println("Excuting for: "+field.getName());
            Method getNameMethod = clazz.getMethod(getterMethod(field.getName()));
            if(Model.class.isAssignableFrom(field.getType())) {
                Model nestedModel = (Model) getNameMethod.invoke(object);
                if (nestedModel != null) {
                    Integer nestedId = new CRUD<>(nestedModel.getClass()).save(nestedModel);
                    // Replace the nested model with its ID in the main object
                    // field.set(object, nestedId);
                    stmt.setInt(index, nestedId);
                }
                index++; //make sure even if nested class is null index incremented
                continue;
            }
            stmt.setObject(index++, getNameMethod.invoke(object));
            System.out.println("Excuted for: "+field.getName());
        }
        stmt.executeUpdate();
        
		return object.getId();
	}

	public Integer save(Model model) throws SQLException
	{
        @SuppressWarnings("unchecked")
        E object = (E)(model);
        System.out.println("name: "+object);
		if(!this.tableName.equals(model.getTableName()))
			throw new SQLException("CRUD BELONGS to another model");
        
		//  Map<String,Object> fields = e.getFields();
         try
         {
            if((Integer)model.getId()==null || model.getId()<=0l)
            {
        
                    return this.create(object);
            }
            else{
                //UPDATE Operation
                return this.update(object);
                
            }
        }
        catch(InvocationTargetException ie)
        {
            System.out.println("UNable to Invoke method: getter for id:-> getId() is not defined");//only for testing, in final version this should be logged instead of print
        }
        catch(IllegalAccessException ie)
        {
            System.err.println("Illegal Access exption");
        }
        catch(NoSuchMethodException ne)
        {
            System.err.println("Getter setter are missing");
        }
        catch(SQLException se)
        {
            System.err.println("SQLexception: "+se);
        }
        return 0;
	}
	
	    private List<E> resultSetToObject(ResultSet rs) 
        throws InstantiationException, IllegalAccessException, 
               IllegalArgumentException, InvocationTargetException, 
               NoSuchMethodException, SecurityException, SQLException {
            List<E> response = new ArrayList<>();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            // E dummy = clazz.getDeclaredConstructor().newInstance();
            Field fields[] = clazz.getDeclaredFields();
            
            Map<String,Field> nesteModelName = new HashMap<>();
            System.out.println("Fields: "+Arrays.toString(fields));
            for(Field field:fields)
            {
                if(Model.class.isAssignableFrom(field.getType()))
                    nesteModelName.put(field.getName(),field);
            }
            System.out.println("nestedModelName->"+nesteModelName);
            int columnCount = rsMetaData.getColumnCount();
            
            // Iterate through the ResultSet rows
            while (rs.next()) {
                E instance = clazz.getDeclaredConstructor().newInstance(); // Create a new instance of E

                // Iterate over each column
                for (int i = 1; i <= columnCount; i++) {
                    if(rs.getObject(i)==null)
                        continue;
                    String columnName = rsMetaData.getColumnName(i);  // Get column name
                    String setterName = setterMethod(columnName); // Assuming standard naming conventions
                                // Get column value
                    // Set the value using the corresponding setter method
                    try {
                        if(nesteModelName.containsKey(columnName))
                        {
                            Class<? extends Model> nestedModel = nesteModelName.get(columnName).getType().asSubclass(Model.class);
                           
                            Model model = (Model) new CRUD<>(nestedModel).findById((Integer)rs.getObject(i));
                            Method setter = clazz.getMethod(setterName, model.getClass());
                            setter.invoke(instance,model);
                            // Create an instance of the class
                            // Model nestedModel = new CRUD(nestedModel.getClass()).findById(columnName);
                        }
                        else
                        {
                            Object columnValue = rs.getObject(i);
                            
                            System.out.println("Finding method: "+setterName);
                            System.out.println("for argument type: "+columnValue.getClass());
                            Method setter = clazz.getMethod(setterName, columnValue.getClass());
                            System.out.println("SETTER: for+"+setterName+"->"+setter);
                            setter.invoke(instance, columnValue); // Invoke the setter method
                        }
                    } catch (NoSuchMethodException e) {
                        // Handle case where setter method does not exist, if necessary
                        System.err.println("No setter found for " + columnName);
                    }
                    
                }

                response.add(instance); // Add the created instance to the response list
            }

            return response;
        }
	public E findById(Integer id)
	{
		String sql = "SELECT * FROM " + this.tableName + " WHERE id = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, id);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSetToObject(resultSet).get(0);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if not found
	}
	public List<E> findAll()
	{
		String sql = "SELECT * FROM " + this.tableName;
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSetToObject(resultSet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if not found
	}
	public List<E> findByColumn(String columnName,Object value)
	{
		String sql = "SELECT * FROM " + this.tableName+" WHERE "+columnName+" = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, value);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSetToObject(resultSet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if not found
	}
	public List<E> findAll(Integer limit,Integer offset)
	{
		String sql = "SELECT * FROM " + this.tableName+" LIMIT "+limit+" OFFSET "+offset;
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        ResultSet resultSet = preparedStatement.executeQuery();
	        return resultSetToObject(resultSet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if not found
	}
	public boolean delete(Integer id) throws SQLException
    {
        if(!this.tableName.equals(this.tableName))
			throw new SQLException("CRUD BELONGS to another model"); // the chance of this happen is exremely low but we must check, may be after proper testing I will remove this check
		 String sql = "DELETE FROM " +this.tableName + " WHERE id = "+id;
		    
		    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
		        
		        int rowsAffected = preparedStatement.executeUpdate();
		        return rowsAffected > 0; // returns true if rows were deleted
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        return false; // returns false if the delete operation failed
		    }
    }
	public boolean delete(E e) throws SQLException
	{
		return this.delete(e.getId());
	}
	 private boolean doesTableExist(String tableName) {
	        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
	        
	        try (Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(sql)) {
	            return resultSet.next(); // Returns true if the table exists
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
}