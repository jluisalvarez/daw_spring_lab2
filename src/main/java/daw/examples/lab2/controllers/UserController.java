package daw.examples.lab2.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import daw.examples.lab2.models.UserLogin;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    public Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
          .filter(cookie->name.equals(cookie.getName()))
          .map(Cookie::getValue)
          .findAny();
      }

	@GetMapping("/")
	public String index(Model model, HttpServletRequest request, HttpSession session) {
		
        String name = "Usuario no identificado";
        //System.out.println("index");

        if(!session.isNew()) {
            name = (String) session.getAttribute("user");
            if (name == null) { 
                //System.out.println("Sesion no definida");
                name = readServletCookie(request, "user").orElse(null);
                if (name == null) { 
                    //System.out.println("Cookie no definida");
                    name = "Usuario no identificado";
                }
            }
        }

        model.addAttribute("user", name);
        
		return "index";
	}

	@PostMapping("/form")
	public String form(UserLogin u, Model model, HttpServletResponse reponse, HttpSession session) {

        System.out.println("User: " + u.getName());
        System.out.println("Pass: " + u.getPass());

        String user_name = u.getName();
        String user_pass = u.getPass();

        if (user_name.equals("") || user_pass.equals("")) {
            model.addAttribute("user", "Usuario no identificado");
            model.addAttribute("pass", "Password null");
        } else {
            model.addAttribute("user", user_name);
		    model.addAttribute("pass", user_pass);

            Cookie c = new Cookie("user", user_name);
			c.setMaxAge(60*60);
			reponse.addCookie(c);
            System.out.println("Cookie user creada");

			session.setAttribute("user", user_name);
			System.out.println("Atributo session creado");

        }



		return "hello";
	}

    @GetMapping("/hello")
	public String hello(HttpSession session, Model model) {

		String user = (String) session.getAttribute("user");
		model.addAttribute("user", user);


		return "hello";
	}

}