package YASC.Code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import YASC.Code.User.Staff;
import YASC.Code.User.StaffRepository;
import YASC.Code.User.User;
import YASC.Code.User.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo1;
	
	@Autowired
	private StaffRepository repo2;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private TestEntityManager entityManager2;

	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setName("Dang");
		user.setAddress("Kham chi hoa");
		user.setPhone("0123456789");
		user.setEmail("dangngu@gmail.com");
		user.setPassword("123456");
		
		User saveUser = repo1.save(user);
		User existUser = entityManager.find(User.class, saveUser.getId());
		assertThat(existUser.getPhone()).isEqualTo(user.getPhone());
	}
	
	@Test
	public void testCreateStaff() {
		Staff staff = new Staff();
		staff.setPhone("1234567890");
		staff.setPassword("1234");
		
		Staff saveStaff = repo2.save(staff);
		Staff existStaff = entityManager2.find(Staff.class, saveStaff.getId());
		assertThat(existStaff.getPhone()).isEqualTo(staff.getPhone());
	}
	
}
