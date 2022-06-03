package ingsoftware.fuengiuma.controller;

import java.util.ArrayList;
import java.util.List;

import ingsoftware.fuengiuma.model.Viaje;
import ingsoftware.fuengiuma.service.UsuarioService;
import ingsoftware.fuengiuma.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ingsoftware.fuengiuma.model.Usuario;

import javax.websocket.server.PathParam;

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
		model.addAttribute("usuario", new Usuario());
		return "usuario/add";
	}

	@PostMapping("/usuario/save")
	public String saveUsuario(Usuario u){
		usuarioService.save(u);
		return "redirect:/";
	}

	@PostMapping("/register")
	public String registerUsuario(){
		return "redirect:/usuario/add";
	}

	@PostMapping("/confirmar")
	public String comprobarUsuario(){
		return "redirect:/usuario";
	}

	@RequestMapping("/usuario/edit/{id}")
	public String editUsuario(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("usuario", usuarioService.getById(id));
		return "usuario/add";
	}
	@RequestMapping("/usuario/delete/{id}")
	public String deleteUsuario(@PathVariable("id") Integer id) {
		usuarioService.delete(id);
		return "redirect:/usuario";
	}


	
	
}
