// ============================================
// SAOCO PERFUMES — app.js
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    
    // ========== CARRITO ==========
    let carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
    updateCartUI();
    
    // Toggle carrito
    const cartIcon = document.getElementById('cartIcon');
    const cartSidebar = document.getElementById('cartSidebar');
    const cartOverlay = document.getElementById('cartOverlay');
    const cartClose = document.getElementById('cartClose');
    const btnCheckout = document.getElementById('btnCheckout');
    
    if (cartIcon) cartIcon.addEventListener('click', openCart);
    if (cartClose) cartClose.addEventListener('click', closeCart);
    if (cartOverlay) cartOverlay.addEventListener('click', closeCart);
    if (btnCheckout) btnCheckout.addEventListener('click', mostrarCheckout);
    
    function openCart() {
        if (!cartSidebar || !cartOverlay) return;
        cartSidebar.classList.add('open');
        cartOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    }
    
    function closeCart() {
        if (!cartSidebar || !cartOverlay) return;
        cartSidebar.classList.remove('open');
        cartOverlay.classList.remove('open');
        document.body.style.overflow = '';
    }
    
    // ========== AGREGAR AL CARRITO ==========
    const productosGrid = document.getElementById('productosGrid');
    
    if (productosGrid) {
        productosGrid.addEventListener('click', function(e) {
            const btn = e.target.closest('.btn-add-cart');
            if (!btn) return;
            
            const id = btn.getAttribute('data-id');
            const nombre = btn.getAttribute('data-nombre');
            const marca = btn.getAttribute('data-marca');
            const precioStr = btn.getAttribute('data-precio');
            const imagen = btn.getAttribute('data-imagen');
            
            if (!id || !nombre || !precioStr) return;
            
            const precio = parseFloat(precioStr);
            if (isNaN(precio)) return;
            
            let carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
            
            const existente = carrito.find(item => item.id === id);
            if (existente) {
                existente.cantidad += 1;
            } else {
                carrito.push({
                    id: id,
                    nombre: nombre,
                    marca: marca || '',
                    precio: precio,
                    imagen: imagen || '',
                    cantidad: 1
                });
            }
            
            localStorage.setItem('saoco_carrito', JSON.stringify(carrito));
            updateCartUI();
            openCart();
        });
    }
    
    // ========== FILTROS ==========
    const filtroBtns = document.querySelectorAll('.filtro-btn');
    const productoCards = document.querySelectorAll('.producto-card');
    
    filtroBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            filtroBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            
            const categoria = this.getAttribute('data-categoria');
            
            productoCards.forEach(card => {
                const cardCategoria = card.getAttribute('data-categoria');
                card.classList.toggle('hidden', !(categoria === 'todos' || cardCategoria === categoria));
            });
        });
    });
    
    // ========== BÚSQUEDA ==========
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const query = this.value.toLowerCase();
            productoCards.forEach(card => {
                const nombre = card.getAttribute('data-nombre')?.toLowerCase() || '';
                const marca = card.querySelector('.producto-marca')?.textContent.toLowerCase() || '';
                card.classList.toggle('hidden', !(nombre.includes(query) || marca.includes(query)));
            });
        });
    }
    
    // ========== FORMULARIO CONTACTO ==========
    const alerta = document.getElementById('alertaExito');
    if (alerta) {
        setTimeout(() => alerta.scrollIntoView({ behavior: 'smooth', block: 'center' }), 300);
    }
    
    // ========== CERRAR MODAL AL HACER CLIC FUERA ==========
    const modalOverlay = document.getElementById('modalOverlay');
    if (modalOverlay) {
        modalOverlay.addEventListener('click', function(e) {
            if (e.target === modalOverlay) {
                cerrarModalCheckout();
            }
        });
    }
});

// ========== FUNCIONES GLOBALES ==========

function removeFromCart(id) {
    let carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
    carrito = carrito.filter(item => item.id !== id);
    localStorage.setItem('saoco_carrito', JSON.stringify(carrito));
    updateCartUI();
}

function updateCartUI() {
    const carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
    const cartCount = document.getElementById('cartCount');
    const cartItems = document.getElementById('cartItems');
    const cartTotal = document.getElementById('cartTotal');

    const totalItems = carrito.reduce((sum, item) => sum + item.cantidad, 0);
    if (cartCount) {
        cartCount.textContent = totalItems;
        cartCount.style.display = totalItems > 0 ? 'flex' : 'none';
    }

    if (cartItems) {
        if (carrito.length === 0) {
            cartItems.innerHTML = '<p class="cart-empty">Tu carrito está vacío</p>';
        } else {
            cartItems.innerHTML = carrito.map(item => `
                <div class="cart-item">
                    <img src="${item.imagen || 'https://via.placeholder.com/60?text=Perfume'}" 
                         alt="${item.nombre}" class="cart-item-img">
                    <div class="cart-item-info">
                        <div class="cart-item-name">${escapeHtml(item.nombre)}</div>
                        <div class="cart-item-marca">${escapeHtml(item.marca)} x${item.cantidad}</div>
                        <div class="cart-item-precio">$ ${formatPrice(item.precio * item.cantidad)}</div>
                    </div>
                    <button type="button" class="cart-item-remove" onclick="removeFromCart('${item.id}')">&#10005;</button>
                </div>
            `).join('');
        }
    }

    const total = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
    if (cartTotal) {
        cartTotal.textContent = '$ ' + formatPrice(total);
    }
}

// ========== MODAL CHECKOUT ==========

function mostrarCheckout() {
    const carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
    if (carrito.length === 0) {
        alert('Tu carrito está vacío');
        return;
    }
    
    // Cerrar carrito sidebar
    closeCart();
    
    // Calcular total
    const total = carrito.reduce((sum, item) => sum + (item.precio * item.cantidad), 0);
    const modalTotal = document.getElementById('modalTotal');
    if (modalTotal) {
        modalTotal.textContent = '$ ' + formatPrice(total);
    }
    
    // Abrir modal
    const modalOverlay = document.getElementById('modalOverlay');
    if (modalOverlay) {
        modalOverlay.classList.add('open');
        document.body.style.overflow = 'hidden';
    }
}

function cerrarModalCheckout() {
    const modalOverlay = document.getElementById('modalOverlay');
    if (modalOverlay) {
        modalOverlay.classList.remove('open');
        document.body.style.overflow = '';
    }
    
    // Limpiar formulario
    const campos = ['modalNombre', 'modalApellidos', 'modalDireccion', 'modalCasa', 'modalCiudad', 'modalProvincia', 'modalCodigo', 'modalTelefono'];
    campos.forEach(id => {
        const campo = document.getElementById(id);
        if (campo) campo.value = '';
    });
}

function hacerPedidoModal() {
    const carrito = JSON.parse(localStorage.getItem('saoco_carrito')) || [];
    if (carrito.length === 0) {
        alert('Tu carrito está vacío');
        return;
    }
    
    // Obtener valores del formulario
    const nombre = document.getElementById('modalNombre')?.value.trim();
    const apellidos = document.getElementById('modalApellidos')?.value.trim();
    const direccion = document.getElementById('modalDireccion')?.value.trim();
    const casaApto = document.getElementById('modalCasa')?.value.trim();
    const ciudad = document.getElementById('modalCiudad')?.value.trim();
    const provincia = document.getElementById('modalProvincia')?.value.trim();
    const codigoPostal = document.getElementById('modalCodigo')?.value.trim();
    const contacto = document.getElementById('modalTelefono')?.value.trim();
    
    // Validar campos requeridos
    if (!nombre || !apellidos || !direccion || !ciudad || !provincia || !contacto) {
        alert('Por favor completa todos los campos obligatorios.');
        return;
    }
    
    // Armar lista de productos
    let productosTexto = '';
    let total = 0;
    carrito.forEach(item => {
        productosTexto += `${item.nombre} (${item.marca}) x${item.cantidad} - $${formatPrice(item.precio * item.cantidad)}\n`;
        total += item.precio * item.cantidad;
    });
    
    // Enviar al backend
    fetch('/pedido', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombreCliente: nombre,
            apellidos: apellidos,
            contacto: contacto,
            direccion: direccion,
            casaApto: casaApto,
            ciudad: ciudad,
            provincia: provincia,
            codigoPostal: codigoPostal,
            productos: productosTexto,
            total: total
        })
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text); });
        }
        return response.json();
    })
    .then(data => {
        if (data.status === 'ok') {
            alert('¡Pedido realizado con éxito! Nos comunicaremos contigo pronto.');
            localStorage.removeItem('saoco_carrito');
            updateCartUI();
            cerrarModalCheckout();
        } else {
            alert('Error: ' + data.mensaje);
        }
    })
    .catch(error => {
        console.error('Error completo:', error);
        alert('Error al procesar el pedido: ' + error.message);
    });
}

function closeCart() {
    const cartSidebar = document.getElementById('cartSidebar');
    const cartOverlay = document.getElementById('cartOverlay');
    if (cartSidebar) cartSidebar.classList.remove('open');
    if (cartOverlay) cartOverlay.classList.remove('open');
    document.body.style.overflow = '';
}

function formatPrice(num) {
    return num.toLocaleString('es-CO');
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}