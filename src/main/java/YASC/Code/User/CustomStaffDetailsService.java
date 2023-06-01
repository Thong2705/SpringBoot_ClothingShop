package YASC.Code.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomStaffDetailsService implements UserDetailsService {

	@Autowired
	private StaffRepository staffRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Staff staff = staffRepo.findByPhone(username);
		if (staff == null) {
			throw new UsernameNotFoundException("Staff user not found");
		}
		return new CustomStaffDetails(staff);
	}

}
