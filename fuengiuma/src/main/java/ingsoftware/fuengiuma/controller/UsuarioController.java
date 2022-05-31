package ingsoftware.fuengiuma.controller;

import java.util.ArrayList;
import java.util.List;

import ingsoftware.fuengiuma.model.Viaje;
import ingsoftware.fuengiuma.service.UsuarioService;
import ingsoftware.fuengiuma.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ingsoftware.fuengiuma.model.Usuario;

@Controller
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;

	@RequestMapping("/usuario")
	public String listadoUsuario (Model model) {

		List<Usuario> usuarios = usuarioService.getAll();
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
