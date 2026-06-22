package com.saoco.perfumes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private MensajeContactoRepository mensajeContactoRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProductos", productoRepository.count());
        model.addAttribute("totalPedidos", pedidoRepository.count());
        model.addAttribute("mensajesNuevos", mensajeContactoRepository.countByLeidoFalse());
        model.addAttribute("stockCritico", productoRepository.findAll().stream()
            .filter(p -> p.getStock() < 5).count());
        return "admin/dashboard";
    }

    @GetMapping("/productos")
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "admin/productos";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());
        return "admin/producto-form";
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProductoForm(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "admin/producto-form";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @RequestParam(required = false) Long id,
            @RequestParam String nombre,
            @RequestParam String marca,
            @RequestParam String categoria,
            @RequestParam String notasOlfativas,
            @RequestParam BigDecimal precio,
            @RequestParam Integer stock,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam String imagenUrl) {
        
        Producto producto = id != null ? 
            productoRepository.findById(id).orElse(new Producto()) : 
            new Producto();
        
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setCategoria(categoria);
        producto.setNotasOlfativas(notasOlfativas);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setDisponible(disponible != null ? disponible : true);
        producto.setImagenUrl(imagenUrl);
        
        productoRepository.save(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "redirect:/admin/productos";
    }

    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoRepository.findAllByOrderByFechaDesc());
        return "admin/pedidos";
    }

    @PostMapping("/pedidos/estado/{id}")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/mensajes")
    public String listarMensajes(Model model) {
        model.addAttribute("mensajes", mensajeContactoRepository.findAllByOrderByFechaDesc());
        return "admin/mensajes";
    }

    @GetMapping("/mensajes/leer/{id}")
    public String marcarMensajeLeido(@PathVariable Long id) {
        MensajeContacto msg = mensajeContactoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        msg.setLeido(true);
        mensajeContactoRepository.save(msg);
        return "redirect:/admin/mensajes";
    }
}