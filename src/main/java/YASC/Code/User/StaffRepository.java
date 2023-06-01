package YASC.Code.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StaffRepository extends JpaRepository<Staff, Long> {
	@Query("SELECT u FROM Staff u WHERE u.phone = ?1")
    public Staff findByPhone(String phone);
}
