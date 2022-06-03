package ingsoftware.fuengiuma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ingsoftware.fuengiuma.model.Viaje;
import ingsoftware.fuengiuma.service.ViajeService;


@Controller
public class ViajeController {

	
	@Autowired
	ViajeService viajeService;
	
	
	@RequestMapping("/viajes")
	public String listadoViajes(Model model) {
		List<Viaje> viajes = viajeService.getAll();
		model.addAttribute("listaViajes", viajes);
		return "viajes/index";
	}
	
	@RequestMapping("/viajes/add")
	public String addViajes(Model model) {
		model.addAttribute("viaje", new Viaje());
		return "viajes/add";
	}

	@PostMapping("/viajes/save")
	public String saveViaje(Viaje v){
		viajeService.save(v);
		return "redirect:/viajes";
	}

	@RequestMapping("/viajes/edit/{id}")
	public String editViajes(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("viaje", viajeService.getById(id));
		return "viajes/add";
	}

	@RequestMapping("/viajes/view/{id}")
	public String viewViajes(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("viaje", viajeService.getById(id));
		return "viajes/view";
	}

	@RequestMapping("/viajes/delete/{id}")
	public String deleteViajes(@PathVariable("id") Integer id) {
		viajeService.delete(id);
		return "redirect:/viajes";
	}
	@RequestMapping("/viajes/sesionInicial")
	public String sesionInicialViajes(Model model) {
		List<Viaje> viajes = viajeService.getAll();
		model.addAttribute("listaViajes", viajes);
		return "viajes/sesionInicial";
	}
	
}
