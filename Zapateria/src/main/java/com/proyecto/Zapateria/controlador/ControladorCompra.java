package com.proyecto.Zapateria.controlador;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.proyecto.Zapateria.Entidades.Carrito;
import com.proyecto.Zapateria.Entidades.Compra;
import com.proyecto.Zapateria.Entidades.DetalleCompra;
import com.proyecto.Zapateria.Interfaces.Icarrito;
import com.proyecto.Zapateria.Interfaces.Icompra;
import com.proyecto.Zapateria.Interfaces.Iproducto;
import com.proyecto.Zapateria.Interfaces.UsuarioRepo;
import com.proyecto.Zapateria.Servicios.PaypalService;
import com.proyecto.Zapateria.config.PaypalPaymentIntent;
import com.proyecto.Zapateria.config.PaypalPaymentMethod;
import com.proyecto.Zapateria.utilidades.URLUtils;
import com.sun.el.parser.ParseException;

@Controller
public class ControladorCompra {

	@Autowired
	private Icompra repoC;
	@Autowired
	private Iproducto repoP;
	@Autowired
	private UsuarioRepo repoU;
	@Autowired
	private Icarrito repoCar;
	
	public static final String PAYPAL_SUCCESS_URL = "pay/Menu2"; 
	public static final String PAYPAL_CANCEL_URL = "pay/Menu";
    private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PaypalService paypalService;
	
	@GetMapping("/compra")
	public String listar(Model modelo) {
    modelo.addAttribute("compra", repoC.findAll());
    modelo.addAttribute("producto2", repoP.findAll());
	return "Menu";
   }
	
	@GetMapping("/admin")
	public String admin() {
	 return "redirect:/Producto";
	}
	
	@GetMapping("/Menu")
	public String menu() {
	 return "redirect:/compra";
	}
	
	//llevar datos
		@RequestMapping(value = "/llevardatos/{id}")
		 	public String pasar(@PathVariable("id") int id, Model modelo,Principal principal) {
			   modelo.addAttribute("pro", repoP.findById(id).get());
			   modelo.addAttribute("clien", repoU.findByNick(principal.getName()).getId());
		    return "compra";
		    }
		
		@PostMapping("/GuardarCompra")
		public String registrar(
				HttpServletRequest request,
				@RequestParam(value = "fkc")int fkc,
				@RequestParam(value = "fkp")int fkp,
				@RequestParam(value = "total")double total, 
				@RequestParam(value = "Cantidad")int Cantidad, 
				@RequestParam(value = "precio")String precio,
				@RequestParam(value = "nombre")String nombre)throws ParseException{
			
			Compra U = new Compra();
			Date data = new Date();
			U.setFecha(data);
			U.setTotal(total);
			
			DetalleCompra d = new DetalleCompra();
			d.setFkCliente(repoU.findById(fkc).get());
			d.setCantidad(Cantidad);
			d.setPrecio(precio);
			d.setNombreProd(nombre);
			d.setFkCompra(U);
			d.setFkProducto(repoP.findById(fkp).get());
			
			List<DetalleCompra> detalleCompras = new ArrayList<>();
			detalleCompras.add(d);
			U.setFkCompra(detalleCompras);
			repoC.save(U);
			
			 String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
				String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
				try {
					String descripcion = "descripción del producto:";
					
					Payment payment = paypalService.createPayment(
							total, 
							"USD", 
							PaypalPaymentMethod.paypal, 
							PaypalPaymentIntent.sale,
							descripcion,
							cancelUrl, 
							successUrl);
					System.out.println(1.0);
					for(Links links : payment.getLinks()){
						if(links.getRel().equals("approval_url")){
							System.out.println(1.1);
							return "redirect:" + links.getHref();
						}
					}
				} catch (PayPalRESTException e) {
					log.error(e.getMessage());
					System.out.println(e);
				}
				return "redirect:/compra";
			
		}
		
		@PostMapping("/GuardarCompra2")
		public String registrar2( 
				HttpServletRequest request,
				@RequestParam(value = "idcar")int idcar,
				@RequestParam(value = "img")String img,
				@RequestParam(value = "fkc")int fkc,
				@RequestParam(value = "fkp")int fkp,
				@RequestParam(value = "total")double total, 
				@RequestParam(value = "Cantidad")int Cantidad, 
				@RequestParam(value = "precio")String precio,
				@RequestParam(value = "nombre")String nombre)throws ParseException{
			
			Compra U = new Compra();
			Date data = new Date();
			U.setFecha(data);
			U.setTotal(total);
			
			DetalleCompra d = new DetalleCompra();
			d.setFkCliente(repoU.findById(fkc).get());
			d.setCantidad(Cantidad);
			d.setPrecio(precio);
			d.setNombreProd(nombre);
			d.setFkCompra(U);
			d.setFkProducto(repoP.findById(fkp).get());
			
			List<DetalleCompra> detalleCompras = new ArrayList<>();
			detalleCompras.add(d);
			U.setFkCompra(detalleCompras);
			repoC.save(U);
			
			Carrito c = new Carrito();
			c.setIdCarrito(idcar);
			c.setNombre(nombre);
			c.setPrecio(precio);
			c.setEstado("Comprado");
			c.setImagen(img);
			c.setCantidad(Cantidad);
			c.setFkProducto2(repoP.findById(fkp).get());
			c.setFkCliente2(repoU.findById(fkc).get());
			c.setTotal(total);
			repoCar.save(c);
			
			String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
			String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
			try {
				String descripcion = "descripción del producto:";
				
				Payment payment = paypalService.createPayment(
						total, 
						"USD", 
						PaypalPaymentMethod.paypal, 
						PaypalPaymentIntent.sale,
						descripcion,
						cancelUrl, 
						successUrl);
				System.out.println(1.0);
				for(Links links : payment.getLinks()){
					if(links.getRel().equals("approval_url")){
						System.out.println(1.1);
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				log.error(e.getMessage());
				System.out.println(e);
			}
			System.out.println(1.2);
			return "redirect:/compra";
		}
		
		   
		 @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
			public String cancelPay(){
			 System.out.println(1.6);
			 return "redirect:/compra";
			}
		 
		 
		 @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
			public String successPay(RedirectAttributes redirectAttrs, 
					@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
				try {
					 System.out.println(1.3);
					Payment payment = paypalService.executePayment(paymentId, payerId);
					if(payment.getState().equals("approved")){
						System.out.println(1.4);
						redirectAttrs.addFlashAttribute("mensaje", "PRODUCTO COMPRADO PUEDE REVISAR EN MIS COMPRAS");
						return "redirect:/compra";
					}
				} catch (PayPalRESTException e) {
					log.error(e.getMessage());
					 System.out.println(e);
				}
					   System.out.println(1.5);
				return "redirect:/compra";
				
			}
}
