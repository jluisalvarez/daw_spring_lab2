# Spring Boot Web App

## Crea un proyecto desde Spring Boot Initializr

http://start.spring.io/

Completa el asistente, seleccionando:

- Gestor Proyecto: Maven
- Lenguaje: Java
- Versión de Spring Boot: 3.2.0
- Project Metadata
    - Group: daw.example
    - Artifact: lab2
    - Name: lab2
    - Description: Lab2 project for Spring Boot
    - Package name: daw.example.lab2
    - Packaging: Jar
    - Java: 21
- Dependencias
    - Spring Boot DevTools
    - Spring Web
    - Thymeleaf

Genera el contenido, descárgalo y descomprime. Utiliza el IDE para abrir el proyecto.

## Contenido estático: Imágenes, CSS y JS

Crea las carpetas img, css y js en /src/main/resources/static.

## Vistas: index.html y hello.html

Crea los ficheros en /src/main/resources/templates

Fichero index.html

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Getting Started: Serving Web Content</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" th:href="@{/css/style.css}"/>
</head>
<body>
    <h1 th:text="'Hola, ' + ${user} + '!'">USER</h1>

    <div th:if="${user=='Usuario no identificado'}">
        <form id="form" action="/form" method="POST">
            Nombre: <input id="name" type="text" name="name" /> <span id="error_name"></span><br />
            Clave : <input id="pass" type="password" name="pass" /> <span id="error_pass"></span><br />
            <input type="submit" value="Enviar" />
        </form>
    </div>
    <div th:unless="${user=='Usuario no identificado'}">
        <h2>Contenido</h2>
        <img th:src="@{/img/spring.png}" />
    </div>

    <script th:src="@{/js/func.js}"></script>
</body>
</html>
```

Fichero hola.html

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head> 
    <title>Getting Started: Serving Web Content</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" th:href="@{/css/style.css}"/>
</head>
<body>
    <h1>Web App</h1>

    <p th:text="|Hello, ${user} !|">USER</p>
    <p th:text="|Your password is ${pass} !|">PASS</p>

    <p th:text="|login:, ${user} !|">USER</p>

    <a href="/">Volver</a>

</html>
```

## Controlador

```java
package daw.examples.lab2.controllers;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
```





