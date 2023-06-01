package YASC.Code.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name = "staffs")
	public class Staff {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
		
		@Column(nullable = false, unique = true, length = 45)
	    private String phone;

	    @Column(nullable = false, length = 64)
	    private String password;
	    
//	    @Enumerated(EnumType.STRING)
//	    private Role role;
	    
	    public Staff() {}
	    
		public Staff(String phone, String password) {
			super();
			this.phone = phone;
			this.password = password;
//			this.role = role;
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

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

//		public Role getRole() {
//			return role;
//		}
//
//		public void setRole(Role role) {
//			this.role = role;
//		}


	}

