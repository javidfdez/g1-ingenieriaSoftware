package ingsoftware.fuengiuma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ingsoftware.fuengiuma.model.Usuario;

@Controller
public class UsuarioController {
	
	@RequestMapping("/usuario")
	public String listadoUsuario (Model model) {
		List<Usuario> usuarios=new ArrayList<>();
		Usuario u=new Usuario();
		u.setNombre("Ale");
		u.setTelefono("654664371");
		usuarios.add(u);
		
		model.addAttribute("listaUsuario", usuarios);
		return "usuario/index";
	}
	@RequestMapping("/usuario/add")
	public String addPersona(Model model) {
		return "usuario/add";
	}
	@RequestMapping("/usuario/edit/{id}")
	public String editPersona(Model model) {
		return "usuario/edit";
	}
	@RequestMapping("/usuario/delete/{id}")
	public String deletePersona(Model model) {
		return "usuario/delete";
	}
	
	
}
