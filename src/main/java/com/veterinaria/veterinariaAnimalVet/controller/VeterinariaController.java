package com.veterinaria.veterinariaAnimalVet.controller;

import com.veterinaria.veterinariaAnimalVet.model.Cita;
import com.veterinaria.veterinariaAnimalVet.model.Cliente;
import com.veterinaria.veterinariaAnimalVet.model.Mascota;
import com.veterinaria.veterinariaAnimalVet.model.Usuario;
import com.veterinaria.veterinariaAnimalVet.service.CitaService;
import com.veterinaria.veterinariaAnimalVet.service.ClienteService;
import com.veterinaria.veterinariaAnimalVet.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class VeterinariaController {

    private final ClienteService clienteService;
    private final MascotaService mascotaService;
    private final CitaService citaService;

    public VeterinariaController(ClienteService clienteService,
                                 MascotaService mascotaService,
                                 CitaService citaService) {
        this.clienteService = clienteService;
        this.mascotaService = mascotaService;
        this.citaService = citaService;
    }

    // Inyecta el usuario autenticado en el modelo para los templates
    private void agregarInfoUsuario(Model model, Usuario usuario) {
        if (usuario != null && usuario.getRol() != null) {
            model.addAttribute("rolNombre", usuario.getRol().getNombre());
            model.addAttribute("puedeCrear", usuario.getRol().isPuedeCrear());
            model.addAttribute("puedeEditar", usuario.getRol().isPuedeEditar());
            model.addAttribute("puedeEliminar", usuario.getRol().isPuedeEliminar());
            model.addAttribute("usuarioNombre", usuario.getUsername());
        }
    }

    // ==================== CITAS ====================
    @GetMapping("/citas")
    public String citas(Model model, @AuthenticationPrincipal Usuario usuario) {
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "citas");
        model.addAttribute("listaCitas", citaService.findAll());
        model.addAttribute("nuevaCita", new Cita());
        model.addAttribute("listaMascotas", mascotaService.findAll());
        model.addAttribute("listaClientes", clienteService.findAll());
        return "citas";
    }

    @PostMapping("/citas")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeCrear(authentication)")
    public String guardarCita(@Valid @ModelAttribute("nuevaCita") Cita nuevaCita,
                              BindingResult result,
                              Model model,
                              @AuthenticationPrincipal Usuario usuario) {
        if (nuevaCita.getMascota() == null || nuevaCita.getMascota().getId() == null) {
            result.rejectValue("mascota.id", "error.nuevaCita", "Debes seleccionar una mascota válida");
        }
        if (nuevaCita.getCliente() == null || nuevaCita.getCliente().getId() == null) {
            result.rejectValue("cliente.id", "error.nuevaCita", "Debes seleccionar un dueño válido");
        }
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "citas");
            model.addAttribute("listaCitas", citaService.findAll());
            model.addAttribute("listaMascotas", mascotaService.findAll());
            model.addAttribute("listaClientes", clienteService.findAll());
            return "citas";
        }
        citaService.save(nuevaCita);
        return "redirect:/citas";
    }

    @GetMapping("/citas/editar/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarCitaForm(@PathVariable Long id, Model model,
                                 @AuthenticationPrincipal Usuario usuario) {
        Optional<Cita> cita = citaService.findById(id);
        if (cita.isEmpty()) return "redirect:/citas";
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "citas");
        model.addAttribute("citaEditar", cita.get());
        model.addAttribute("listaMascotas", mascotaService.findAll());
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("listaCitas", citaService.findAll());
        model.addAttribute("nuevaCita", new Cita());
        return "citas";
    }

    @PostMapping("/citas/editar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarCita(@Valid @ModelAttribute("citaEditar") Cita citaEditar,
                             BindingResult result,
                             Model model,
                             @AuthenticationPrincipal Usuario usuario,
                             RedirectAttributes redirectAttributes) {
        if (citaEditar.getMascota() == null || citaEditar.getMascota().getId() == null) {
            result.rejectValue("mascota.id", "error", "Debes seleccionar una mascota válida");
        }
        if (citaEditar.getCliente() == null || citaEditar.getCliente().getId() == null) {
            result.rejectValue("cliente.id", "error", "Debes seleccionar un dueño válido");
        }
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "citas");
            model.addAttribute("listaCitas", citaService.findAll());
            model.addAttribute("listaMascotas", mascotaService.findAll());
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("nuevaCita", new Cita());
            return "citas";
        }
        citaService.save(citaEditar);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cita actualizada correctamente.");
        return "redirect:/citas";
    }

    @PostMapping("/citas/eliminar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEliminar(authentication)")
    public String eliminarCitas(@RequestParam Long id) {
        citaService.deleteById(id);
        return "redirect:/citas";
    }

    // ==================== CLIENTES ====================
    @GetMapping("/clientes")
    public String clientes(Model model, @AuthenticationPrincipal Usuario usuario) {
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "clientes");
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("nuevoCliente", new Cliente());
        return "clientes";
    }

    @PostMapping("/clientes")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeCrear(authentication)")
    public String guardarCliente(@Valid @ModelAttribute("nuevoCliente") Cliente nuevoCliente,
                                 BindingResult result,
                                 Model model,
                                 @AuthenticationPrincipal Usuario usuario) {
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "clientes");
            model.addAttribute("listaClientes", clienteService.findAll());
            return "clientes";
        }
        clienteService.save(nuevoCliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarClienteForm(@PathVariable Long id, Model model,
                                    @AuthenticationPrincipal Usuario usuario) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isEmpty()) return "redirect:/clientes";
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "clientes");
        model.addAttribute("clienteEditar", cliente.get());
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("nuevoCliente", new Cliente());
        return "clientes";
    }

    @PostMapping("/clientes/editar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarCliente(@Valid @ModelAttribute("clienteEditar") Cliente clienteEditar,
                                BindingResult result,
                                Model model,
                                @AuthenticationPrincipal Usuario usuario,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "clientes");
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("nuevoCliente", new Cliente());
            return "clientes";
        }
        clienteService.save(clienteEditar);
        redirectAttributes.addFlashAttribute("mensajeExito", "Cliente actualizado correctamente.");
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/eliminar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEliminar(authentication)")
    public String eliminarCliente(@RequestParam Long id) {
        clienteService.deleteById(id);
        return "redirect:/clientes";
    }

    // ==================== MASCOTAS ====================
    @GetMapping("/mascotas")
    public String mascotas(Model model, @AuthenticationPrincipal Usuario usuario) {
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "mascotas");
        model.addAttribute("listaMascotas", mascotaService.findAll());
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("nuevaMascota", new Mascota());
        return "mascotas";
    }

    @PostMapping("/mascotas")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeCrear(authentication)")
    public String guardarMascota(@Valid @ModelAttribute("nuevaMascota") Mascota nuevaMascota,
                                 BindingResult result,
                                 Model model,
                                 @AuthenticationPrincipal Usuario usuario) {
        if (nuevaMascota.getCliente() == null || nuevaMascota.getCliente().getId() == null) {
            result.rejectValue("cliente", "error.cliente", "Debes seleccionar un dueño para la mascota");
        }
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "mascotas");
            model.addAttribute("listaMascotas", mascotaService.findAll());
            model.addAttribute("listaClientes", clienteService.findAll());
            return "mascotas";
        }
        mascotaService.save(nuevaMascota);
        return "redirect:/mascotas";
    }

    @GetMapping("/mascotas/editar/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarMascotaForm(@PathVariable Long id, Model model,
                                    @AuthenticationPrincipal Usuario usuario) {
        Optional<Mascota> mascota = mascotaService.findById(id);
        if (mascota.isEmpty()) return "redirect:/mascotas";
        agregarInfoUsuario(model, usuario);
        model.addAttribute("activePage", "mascotas");
        model.addAttribute("mascotaEditar", mascota.get());
        model.addAttribute("listaMascotas", mascotaService.findAll());
        model.addAttribute("listaClientes", clienteService.findAll());
        model.addAttribute("nuevaMascota", new Mascota());
        return "mascotas";
    }

    @PostMapping("/mascotas/editar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEditar(authentication)")
    public String editarMascota(@Valid @ModelAttribute("mascotaEditar") Mascota mascotaEditar,
                                BindingResult result,
                                Model model,
                                @AuthenticationPrincipal Usuario usuario,
                                RedirectAttributes redirectAttributes) {
        if (mascotaEditar.getCliente() == null || mascotaEditar.getCliente().getId() == null) {
            result.rejectValue("cliente", "error.cliente", "Debes seleccionar un dueño");
        }
        if (result.hasErrors()) {
            agregarInfoUsuario(model, usuario);
            model.addAttribute("activePage", "mascotas");
            model.addAttribute("listaMascotas", mascotaService.findAll());
            model.addAttribute("listaClientes", clienteService.findAll());
            model.addAttribute("nuevaMascota", new Mascota());
            return "mascotas";
        }
        mascotaService.save(mascotaEditar);
        redirectAttributes.addFlashAttribute("mensajeExito", "Mascota actualizada correctamente.");
        return "redirect:/mascotas";
    }

    @PostMapping("/mascotas/eliminar")
    @PreAuthorize("hasRole('PROPIETARIO') or @usuarioPermiso.puedeEliminar(authentication)")
    public String eliminarMascota(@RequestParam Long id) {
        mascotaService.deleteById(id);
        return "redirect:/mascotas";
    }
}
