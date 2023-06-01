package YASC.Code.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import net.bytebuddy.implementation.bind.MethodDelegationBinder.ParameterBinding.Anonymous;

@Controller
public class AppController {
	@Autowired
	private UserRepository repo;
	
	@Autowired 
	private CateRepository cateRepo;
	
	@Autowired
    private ProductRepository productRepo;
	
	@GetMapping("")
	public String viewHomePage(Model model, @RequestParam(value="page", required = false) Integer p) {
		 int perPage = 4;
	        int page = (p != null) ? p: 0;

	        Pageable pageable = PageRequest.of(page, perPage);
	        long count = 0;
	        Page<Product> products = productRepo.findAll(pageable);
	        count = productRepo.count();
	        double pageCount = Math.ceil((double) count / (double) perPage);

	        model.addAttribute("pageCount", (int) pageCount);
	        model.addAttribute("perPage", perPage);
	        model.addAttribute("count", count);
	        model.addAttribute("page", page);
	        model.addAttribute("products", products);
		return "index";
	}
	
	@GetMapping("/index")
	public String viewIndex() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@GetMapping("/admin/login")
    public String viewAdminLoginPage() {
        return "admin/admin_login";
    }
	
	@GetMapping("/admin/home")
    public String viewAdminHomePage(Model model) {
		
        return "admin/admin_home";
    }
	
	@GetMapping("/user/login")
	public String viewLogin() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		System.out.print(authentication.toString());
//		
//		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
//			return "user/login";
//		}
			return "user/login";
	}
	
	@GetMapping("/user/store")
	public String viewSuccess(Model model, @RequestParam(value="page", required = false) Integer p) {
		int perPage = 4;
        int page = (p != null) ? p: 0;

        Pageable pageable = PageRequest.of(page, perPage);
        long count = 0;
        Page<Product> products = productRepo.findAll(pageable);
        count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("products", products);
		return "user/home";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//	    String encodedPassword = passwordEncoder.encode(user.getPassword());
//	    user.setPassword(encodedPassword);
	     
		if(repo.findByPhone(user.getPhone()) != null) {
	    	bindingResult.addError(new FieldError
                    ("user", "phone", "Phone already exists"));
	    }
	    if (bindingResult.hasErrors()) {
            return "redirect:/register?error=true";
        } else {
            repo.save(user);
            return "user/register_success";
        }
	}
	
	@GetMapping("/information")
    public String viewInformation() {
        return "information";
    }
	
	@GetMapping("recruit")
	public String viewRecruit() {
		return "recruit";
	}
	
	@GetMapping("delivery_policy")
	public String viewDelivery() {
		return "delivery_policy";
	}
	
	@GetMapping("return_policy")
	public String viewReturn() {
		return "return_policy";
	}
	
	@GetMapping("membership")
	public String viewMembership() {
		return "membership";
	}
	
	@GetMapping("/admin/categories")
	public String showCategories(Model model) {
		List<Category> categories = cateRepo.findAllByOrderBySortingAsc();
        model.addAttribute("categories", categories);
        return "admin/categories/index";
	}

	@GetMapping("/admin/categories/add")
    public String add(Category category) {
        return "admin/categories/add";
    }
	
	@PostMapping("/admin/categories/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }
        redirectAttributes.addFlashAttribute("message", "Category added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        
        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category categoryExists = cateRepo.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);
            cateRepo.save(category);
        }
        return "redirect:/admin/categories/add";
    }
	@GetMapping("/admin/categories/edit/{id}")
    public String edit_product(@PathVariable int id, Model model) {
        @SuppressWarnings("deprecation")
		Category category = cateRepo.getOne(id);
        model.addAttribute("category", category);
        return "admin/categories/edit";
    }

    @PostMapping("/admin/categories/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        @SuppressWarnings("deprecation")
		Category currentCategory = cateRepo.getOne(category.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", currentCategory);
            return "admin/categories/edit";
        }
        redirectAttributes.addFlashAttribute("message", "Category updated");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        
        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category categoryExists = cateRepo.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            category.setSlug(slug);
            cateRepo.save(category);
        }
        return "redirect:/admin/categories/edit/" + category.getId();
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String delete_product(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
        cateRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Category deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
        int count = 1;
        Category category;

        for (int categoryId : id) {
            category = cateRepo.getOne(categoryId);
            category.setSorting(count);
            cateRepo.save(category);
            count++;
        }
        return "ok";
    }
    
    
    
    
    
    //Product
    
    @GetMapping("/admin/products")
    public String index(Model model, @RequestParam(value="page", required = false) Integer p) {
        int perPage = 6;
        int page = (p != null) ? p: 0;

        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = cateRepo.findAll();

        Map<Integer, String> cats = new HashMap<>();
        for (Category cat : categories) {
            cats.put(cat.getId(), cat.getName());
        }
        model.addAttribute("products", products);
        model.addAttribute("cats", cats);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/admin/products/add")
    public String add(Product product, Model model) {
        List<Category> categories = cateRepo.findAll();
        model.addAttribute("categories", categories);
        return "admin/products/add";
    }

    @PostMapping("/admin/products/add")
    public String add(@Valid Product product, 
            BindingResult bindingResult, 
            MultipartFile file, 
            RedirectAttributes redirectAttributes, 
            Model model) throws IOException{
        List<Category> categories = cateRepo.findAll();
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        
        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepo.findBySlug(slug);

        if (!fileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);
            Files.write(path, bytes);
        }
        return "redirect:/admin/products/add";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productRepo.getOne(id);
        List<Category> categories = cateRepo.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "/admin/products/edit";
    }

    @PostMapping("/admin/products/edit")
    public String edit(@Valid Product product, 
            BindingResult bindingResult, 
            MultipartFile file, 
            RedirectAttributes redirectAttributes, 
            Model model) throws IOException{
        
        Product currentProduct = productRepo.getOne(product.getId());

        List<Category> categories = cateRepo.findAll();
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            model.addAttribute("productName", currentProduct.getName());
            return "admin/products/edit";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }
        redirectAttributes.addFlashAttribute("message", "Product updated");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        
        String slug = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepo.findBySlugAndIdNot(slug, product.getId());

        if (!fileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                Files.delete(path2);
                product.setImage(filename);
                Files.write(path, bytes);
            } else {
                product.setImage(currentProduct.getImage());
            }
            productRepo.save(product);
        }
        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/admin/products/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes, Model model) throws IOException {
        Product product = productRepo.getOne(id);
        Product currentProduct = productRepo.getOne(product.getId());
        Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
        Files.delete(path2);

        productRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/products";
    }
    
    
    
    //show index category
    @GetMapping("/category/{slug}")
    public String category(@PathVariable String slug, Model model, @RequestParam(value="page", required = false) Integer p) {
        int perPage = 8;
        int page = (p != null) ? p: 0;

        Pageable pageable = PageRequest.of(page, perPage);
        long count = 0;

        if (slug.equals("all")) {
            Page<Product> products = productRepo.findAll(pageable);
            count = productRepo.count();
            model.addAttribute("products", products);
        } else {
            Category category = cateRepo.findBySlug(slug);

            if (category == null) {
                return "redirect:/";
            }
            int catid = category.getId();
            String categoryName = category.getName();
            List<Product> products = productRepo.findAllByCategoryId(Integer.toString(catid), pageable);

            count = productRepo.countByCategoryId(Integer.toString(catid));
            model.addAttribute("products", products);
            model.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "/user/products";
    }
    
    
    
    
    
    
    
    
    //cart
    
    
    @GetMapping("/user/cart/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model, @RequestParam(value = "cartPage", required = false) String cartPage) {
        Product product = productRepo.getOne(id);
        if (session.getAttribute("cart") == null) {
            Map<Integer, Cart> cart = new HashMap<>();
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);
        } else {
            @SuppressWarnings("unchecked")
			Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
            if (cart.containsKey(id)) {
                int qty = cart.get(id).getQuantity();
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++qty, product.getImage()));
            } else {
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }

        @SuppressWarnings("unchecked")
		Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
        int size = 0;
        double total = 0;
        for (Cart value : cart.values()) {
            size += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        model.addAttribute("csize", size);
        model.addAttribute("ctotal", total);

        if (cartPage != null) {
            return "redirect:http://localhost:8080/cart/view";
        }
        
        return "redirect:http://localhost:8080/cart/view";
    }

    @RequestMapping("/cart/view")
    public String view(HttpSession session, Model model) {
        if (session.getAttribute("cart") == null) {
            return "redirect:http://localhost:8080/category/all";
        }
        @SuppressWarnings("unchecked")
		Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        model.addAttribute("notCartViewPage", true);
        return "user/cart";
    }

    @GetMapping("/cart/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {
        Product product = productRepo.getOne(id);
        @SuppressWarnings("unchecked")
		Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");

        int qty = cart.get(id).getQuantity();
        if (qty == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            }
        } else {
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), --qty, product.getImage()));
        }

        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:" + refererLink;
    }

    @GetMapping("/cart/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {
        
        @SuppressWarnings("unchecked")
		Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");
        cart.remove(id);
        if (cart.size() == 0) {
            session.removeAttribute("cart");
        }

        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:" + refererLink;
    }

    @GetMapping("/cart/clear")
    public String clear(HttpSession session, HttpServletRequest httpServletRequest) {
        session.removeAttribute("cart");

        String refererLink = httpServletRequest.getHeader("referer");
        return "redirect:http://localhost:8080/category/all";
    }
}
