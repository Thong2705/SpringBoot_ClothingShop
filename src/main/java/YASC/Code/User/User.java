package YASC.Code.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Column(name = "address", nullable = false, length = 250)
    private String address;
	
	@Column(nullable = false, unique = true, length = 45)
    private String phone;
	
	@Column(nullable = false, length = 45)
    private String email;
     
    @Column(nullable = false, length = 64)
    private String password;
    
//    @Column(name = "role", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private Role role;
    
    public User() {}
    
	public User(String name, String address, String phone, String email, String password) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
     


}
