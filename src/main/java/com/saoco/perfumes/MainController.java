package com.saoco.perfumes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private MensajeContactoRepository mensajeContactoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Producto> destacados = productoRepository.findAll().stream().limit(4).toList();
        model.addAttribute("destacados", destacados);
        return "index";
    }

    
    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String categoria,
            Model model) {
        
        
        Pageable pageable = PageRequest.of(page, 12, Sort.by("id").ascending());
        
        Page<Producto> productosPage;
        
        
        if (categoria != null && !categoria.isEmpty()) {
            productosPage = productoRepository.findByCategoria(categoria, pageable);
            model.addAttribute("categoriaSeleccionada", categoria);
        } else {
            productosPage = productoRepository.findByDisponibleTrue(pageable);
        }
        
        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("totalPaginas", productosPage.getTotalPages());
        model.addAttribute("totalProductos", productosPage.getTotalElements());
        model.addAttribute("tieneSiguiente", productosPage.hasNext());
        model.addAttribute("tieneAnterior", productosPage.hasPrevious());
        
        return "catalogo";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @PostMapping("/contacto")
    public String enviarContacto(
            @RequestParam String nombre,
            @RequestParam String contacto,
            @RequestParam String mensaje,
            Model model) {
        
        MensajeContacto msg = new MensajeContacto();
        msg.setNombre(nombre);
        msg.setContacto(contacto);
        msg.setMensaje(mensaje);
        mensajeContactoRepository.save(msg);
        
        model.addAttribute("confirmacion", "Gracias " + nombre + ", pronto nos comunicaremos contigo.");
        return "contacto";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/pedido")
    @ResponseBody
    public ResponseEntity<Map<String, String>> guardarPedido(@RequestBody Map<String, Object> datos) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String nombreCliente = (String) datos.get("nombreCliente");
            String apellidos = (String) datos.get("apellidos");
            String contacto = (String) datos.get("contacto");
            String direccion = (String) datos.get("direccion");
            String casaApto = (String) datos.get("casaApto");
            String ciudad = (String) datos.get("ciudad");
            String provincia = (String) datos.get("provincia");
            String codigoPostal = (String) datos.get("codigoPostal");
            String productos = (String) datos.get("productos");
            Number totalNum = (Number) datos.get("total");
            BigDecimal total = BigDecimal.valueOf(totalNum.doubleValue());
            
            Pedido pedido = new Pedido();
            pedido.setNombreCliente(nombreCliente);
            pedido.setApellidos(apellidos);
            pedido.setContacto(contacto);
            pedido.setDireccion(direccion);
            pedido.setCasaApto(casaApto);
            pedido.setCiudad(ciudad);
            pedido.setProvincia(provincia);
            pedido.setCodigoPostal(codigoPostal);
            pedido.setProductos(productos);
            pedido.setTotal(total);
            pedido.setEstado("PENDIENTE");
            
            pedidoRepository.save(pedido);
            
            response.put("status", "ok");
            response.put("mensaje", "Pedido guardado correctamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("status", "error");
            response.put("mensaje", "Error al guardar el pedido: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}