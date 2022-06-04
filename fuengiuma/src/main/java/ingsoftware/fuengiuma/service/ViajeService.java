package ingsoftware.fuengiuma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ingsoftware.fuengiuma.model.Viaje;
import ingsoftware.fuengiuma.repository.ViajeRepository;

@Service
public class ViajeService {

	@Autowired
	ViajeRepository viajeRepository;
	
	public List<Viaje> getAll(){
		return viajeRepository.findAll();
	}

	public void save(Viaje v){
		viajeRepository.saveAndFlush(v);
	}

	public void delete(Integer id){
		viajeRepository.deleteById(id);
	}

	public Viaje getById(Integer id) {
		return viajeRepository.getOne(id);
	}

//	public Viaje inscribirse(Integer id){
//		if(viajeRepository.getById(id).getPlazas() != 0){
//			viajeRepository.getById(id).setPlazas((short) (viajeRepository.getById(id).getPlazas() - 1));
//		}
//		return viajeRepository.getById(id);
//	}
}
