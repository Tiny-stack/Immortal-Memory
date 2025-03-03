package backend.models;
import java.sql.SQLException;

import org.worm.Model;
public class User extends Model
{
	// private Long id;
	// private Map<String,Object> fields;
    // private static String tableStructure[] = {"id INTEGER PRIMARY KEY AUTOINCREMENT "}
	// public static String tableName = "USER";
	private Integer id;
	private String name;
	private String dob;
	private String password;
	private String about;
	private User bestFriendId;


	@Override
	public Integer getId() {
		return this.id;
	}


	public void setId(Integer id) {
		this.id = id;
	}
    // public void setBestFriendId(User u)
    // {
    //     this.bestFriendId = u;
    // }
	public User(String name,String dob,String password, String about,User bestFriendId)
	{
		this.name = name;
		this.dob = dob;
		this.password = password;
		this.about = about;
		this.bestFriendId = bestFriendId;
        this.id = 0;
	}
	@Override
	public String getTableName() {
		System.out.println("GetTable Name Called for User");
		return "USER";
	}

	public User()
	{}
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public User getBestFriendId() {
		return bestFriendId;
	}

	public void setBestFriendId(User bestFriendId) {
		this.bestFriendId = bestFriendId;
	}


	// private Set<String> columns =Set.of("name","dob","password","about","best_friend_id"); 
	@Override
    public String toString()
    {
        return "Name: "+this.name+" id: "+this.id+"BESt friend: "+this.bestFriendId;
    }
	
	public static void main(String[] args) throws SQLException
	{
		// CRUD<User> userCrud = new CRUD<>(User.class);
		// User u = new User("Vishwajeet Rauniyar","12,3,4","09/10/2001","I am Tiny",null);
        // u.setId(1l);
		// User u2 = new User("Tiny","123","09/10/2001","I am his friend",u);
       

		// userCrud.save(u2);
		// // System.out.println("Saved");
        // System.out.println("u1: "+u);
        // System.out.println("u2: "+u2);
	}
}