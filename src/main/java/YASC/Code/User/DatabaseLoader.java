package YASC.Code.User;
//package YASC.Code;
//
//import java.util.List;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
// 
//@Configuration
//public class DatabaseLoader {
// 
//    private StaffRepository repo;
//     
//    public DatabaseLoader(StaffRepository repo) {
//        this.repo = repo;
//    }
// 
//    @Bean
//    public CommandLineRunner initializeDatabase() {
//        return args -> {
//            Staff staff1 = new Staff("0123456789", "123456");
//            Staff staff2 = new Staff("0987654321", "123456");
//             
//            repo.saveAll(List.of(staff1, staff2));
//             
//            System.out.println("Database initialized");
//        };
//    }
//}