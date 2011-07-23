package edu.cibertec.buscomida.mantenimiento.managed;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import edu.cibertec.buscomida.mantenimiento.facade.MantenimientoFacade;
import edu.cibertec.buscomida.mantenimiento.facade.impl.MantenimientoFacadeImpl;
import edu.cibertec.buscomida.mantenimiento.persistence.PlatoEntity;
import edu.cibertec.buscomida.mantenimiento.persistence.RestauranteEntity;
import edu.cibertec.buscomida.mantenimiento.persistence.SucursalEntity;
import edu.cibertec.buscomida.util.facade.ComboFacade;
import edu.cibertec.buscomida.util.facade.impl.ComboFacadeImpl;
import edu.cibertec.buscomida.util.persistence.CategoriaEntity;
import edu.cibertec.buscomida.util.persistence.DistritoEntity;
import edu.cibertec.buscomida.util.persistence.RangoprecioEntity;
import edu.cibertec.buscomida.util.resources.ConstantesConfig;

public class MBRegistarRestaurante implements Serializable {

	private static final long serialVersionUID = 1L;

	private RestauranteEntity restaurante = new RestauranteEntity();
	private SucursalEntity sucursal = new SucursalEntity();
	private PlatoEntity plato = new PlatoEntity();

	private List<SucursalEntity> lstSucursal = new ArrayList<SucursalEntity>();
	private List<PlatoEntity> lstPlato = new ArrayList<PlatoEntity>();

	private LatLng center;
	private MapModel emptyModel;
	private Marker marker;

	private StreamedContent imagenMostrar;
	private StreamedContent imagenPlato;
	private StreamedContent imagenRestaurante = new DefaultStreamedContent();

	private String recontrasena;

	// Metodo Constructor
	public MBRegistarRestaurante() {
		plato.setCategoria(new CategoriaEntity());
		plato.setRangoprecio(new RangoprecioEntity());
		emptyModel = new DefaultMapModel();
	}

	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}

	public String getRecontrasena() {
		return recontrasena;
	}

	public void setRecontrasena(String recontrasena) {
		this.recontrasena = recontrasena;
	}

	public StreamedContent getImagenPlato() {
		return imagenPlato;
	}

	public void setImagenPlato(StreamedContent imagenPlato) {
		this.imagenPlato = imagenPlato;
	}

	public StreamedContent getImagenRestaurante() {
		if (imagenRestaurante == null) {

		}
		return imagenRestaurante;
	}

	public void setImagenRestaurante(StreamedContent imagenRestaurante) {
		this.imagenRestaurante = imagenRestaurante;
	}

	public StreamedContent getImagenMostrar() {
		return imagenMostrar;
	}

	public void setImagenMostrar(StreamedContent imagenMostrar) {
		this.imagenMostrar = imagenMostrar;
	}

	public MapModel getEmptyModel() {
		return emptyModel;
	}

	public RestauranteEntity getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(RestauranteEntity restaurante) {
		this.restaurante = restaurante;
	}

	public SucursalEntity getSucursal() {
		return sucursal;
	}

	public void setSucursal(SucursalEntity sucursal) {
		this.sucursal = sucursal;
	}

	public PlatoEntity getPlato() {
		return plato;
	}

	public void setPlato(PlatoEntity plato) {
		this.plato = plato;
	}

	public List<SucursalEntity> getLstSucursal() {
		return lstSucursal;
	}

	public void setLstSucursal(List<SucursalEntity> lstSucursal) {
		this.lstSucursal = lstSucursal;
	}

	public List<PlatoEntity> getLstPlato() {
		return lstPlato;
	}

	public void setLstPlato(List<PlatoEntity> lstPlato) {
		this.lstPlato = lstPlato;
	}

	public Marker getMarker() {
		return marker;
	}

	// ************************************************************************
	// EVENTOS
	// ************************************************************************
	public void grabar(ActionEvent actionEvent) {
		System.out
				.println("**********************Grabar************************");
		String retorno = validarRestaurante();
		try {

			Properties propiedades = new Properties();
			propiedades.load(getClass().getResourceAsStream(
					"Mantenimiento.properties"));
			if (retorno == null) {
				// Invocamos a metodo Grabar Restaurante
				MantenimientoFacade objMantenimientoFacade = new MantenimientoFacadeImpl();

				try {
					System.out.println("RegistrarResturante");
					// Obteniendo los miliesugundos
					Date date = new Date();
					String archivoRestaurante = date.getTime() + "";
					String archivoPlato = new String(archivoRestaurante);
					for (int i = 0; i < lstPlato.size(); i++) {
						if (lstPlato.get(i).getFotoPlato() != null){
							String archivoP = archivoPlato + i + "p";
							lstPlato.get(i).setUrl("/images/plato/" + archivoP);
						}else{
							lstPlato.get(i).setUrl("/images/plato/camara.jpg");
						}
					}

					FacesContext facesContext = FacesContext
							.getCurrentInstance();
					ServletContext scontext = (ServletContext) facesContext
							.getExternalContext().getContext();
					if (restaurante.getFotoRestaurante() != null) {
						restaurante.setFoto("/images/restaurante/"
								+ archivoRestaurante);
					} else
						restaurante.setFoto("/images/restaurante/camara.jpg");
					// Grabar el restaurante
					objMantenimientoFacade.registrarMantenimiento(restaurante,
							lstSucursal, lstPlato);
					System.out
							.println(ConstantesConfig.getTRANSACCION_INSERT());
					if (ConstantesConfig.getTRANSACCION_INSERT() == 1) {
						if (restaurante.getFotoRestaurante() != null) {
							archivoRestaurante = scontext
									.getRealPath("images\\restaurante\\"
											+ archivoRestaurante);
							crearArchivo(restaurante.getFotoRestaurante(),
									archivoRestaurante);
						}
						for (int i = 0; i < lstPlato.size(); i++) {
							String archivoP = archivoPlato + i + "p";
							archivoP = scontext.getRealPath("images\\plato\\"
									+ archivoP);
							if (lstPlato.get(i).getFotoPlato() != null)
								crearArchivo(lstPlato.get(i).getFotoPlato(),
										archivoP);
						}

						addMessage(new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"Bienvenidos " + restaurante.getNombreMostrar(),
								""));
						setear();

					} else {
						addMessage(new FacesMessage(
								FacesMessage.SEVERITY_ERROR, "",
								"Ocurrio un erro al grabar ! Por favor ntente de nuevo"));

					}
				} catch (Exception e) {
					addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"",
							"Ocurrio un erro al grabar ! Por favor ntente de nuevo"));
				}
			} else {
				System.out.println("Ya estamos en el else:" + retorno);
				addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						retorno));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String loadFoto() {
		System.out.println("LOAD FOTO cambio");
		String archivo = ConstantesConfig.getRUTA_UPLOAD_RESTAURANTES()
				+ "camara.jpg";
		InputStream stream = null;
		try {
			stream = new FileInputStream(archivo);
			imagenRestaurante = new DefaultStreamedContent(stream,
					"images/jpeg");
		} catch (FileNotFoundException e) {
			System.out.println("No se encontro el archivo");
		}
		return "success";
	}

	public void agregarSucursal() {
		System.out.println("agregar sucursal");
		ComboFacade objCombo = new ComboFacadeImpl();
		if (lstSucursal.size() == 3)
			System.out.println("No se agrego porque ya tiene 3 restaurantes");
		else {
			try {
				List<DistritoEntity> lstDistrito = objCombo.listarDistrito();
				for (DistritoEntity d : lstDistrito) {
					if (sucursal.getDistrito().getIdDistrito() == d
							.getIdDistrito())
						sucursal.getDistrito().setDescripcion(
								d.getDescripcion());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		sucursal.setRestaurante(restaurante);
		lstSucursal.add(sucursal);

		emptyModel = new DefaultMapModel();
	}

	public void agregarPlato() {
		System.out.println("Agregar Plato");
		ComboFacade objCombo = new ComboFacadeImpl();
		if (lstPlato.size() == 3)
			System.out.println("No se agrego porque ya tiene 3 platos");
		else {
			try {

				List<CategoriaEntity> lstCategoria = objCombo.listarCategoria();
				for (CategoriaEntity c : lstCategoria) {
					if (plato.getCategoria().getIdCategoria() == c
							.getIdCategoria())
						plato.getCategoria().setDescripcion(c.getDescripcion());
				}
				List<RangoprecioEntity> lstRango = objCombo.listarRangoPrecio();
				for (RangoprecioEntity r : lstRango) {
					if (plato.getRangoprecio().getIdRango() == r.getIdRango())
						plato.getRangoprecio().setDescripcion(
								r.getDescripcion());
					plato.getRangoprecio().setMaxPrecio(r.getMaxPrecio());
					plato.getRangoprecio().setMinPrecio(r.getMinPrecio());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		plato.setRestaurante(restaurante);
		lstPlato.add(plato);

	}

	public void eliminarSucursal() {
		System.out.println("eliminar sucursal");
		lstSucursal.remove(sucursal);
	}

	public void eliminarPlato() {
		System.out.println("eliminar plato");
		lstPlato.remove(plato);
	}

	public void setearDialogo() {
		sucursal = new SucursalEntity();
		plato = new PlatoEntity();
		String archivo = ConstantesConfig.getRUTA_UPLOAD_RESTAURANTES()
				+ "camara.jpg";
		InputStream stream = null;

		try {
			stream = new FileInputStream(archivo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imagenPlato = new DefaultStreamedContent(stream, "images/jpeg");
	}

	public void mostrarFoto() {
		System.out.println("Mostrar Foto");
		for (PlatoEntity p : lstPlato) {
			if (p.getNombremostrar().equals(plato.getNombremostrar())) {
				Blob b;
				InputStream input = null;
				try {
					b = new SerialBlob(p.getFotoPlato());
					input = b.getBinaryStream();
				} catch (SerialException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				imagenMostrar = new DefaultStreamedContent(input, "images/jpeg");
			}
		}
	}

	public void mostrarGoogleMap() {
		System.out.println("Mostrar Google Map1");
		emptyModel.getMarkers().clear();
		for (SucursalEntity s : lstSucursal) {
			if (s.getDistrito().equals(sucursal.getDistrito())
					&& s.getTelefono().equals(sucursal.getTelefono())) {
				// sucursal.setAltitud(s.getAltitud());
				// sucursal.setLatitud(s.getLatitud());
				LatLng coord1 = new LatLng(Double.parseDouble(s.getLatitud()
						+ ""), Double.parseDouble(s.getAltitud() + ""));
				System.out.println("Latitud:" + coord1.getLat() + " "
						+ "Longitud:" + coord1.getLng());
				center = new LatLng(coord1.getLat() + 0.007,
						coord1.getLng() - 0.007);
				emptyModel.addOverlay(new Marker(coord1));
				// Marker marker = new Marker(new
				// LatLng(Double.parseDouble(s.getLatitud()+""),Double.parseDouble(s.getAltitud()+"")));
				// emptyModel.addOverlay(marker);

				// center=new LatLng(coord1.getLat()+0.04, coord1.getLng());

			}
		}
	}

	// ************************************************************************
	// METODOS
	// ************************************************************************
	private void setear() {
		System.out.println("Setear");
		restaurante = new RestauranteEntity();
		plato = new PlatoEntity();
		sucursal = new SucursalEntity();
		lstSucursal = new ArrayList<SucursalEntity>();
		lstPlato = new ArrayList<PlatoEntity>();

	}

	public void addMarker(ActionEvent actionEvent) {
		Marker marker = new Marker(new LatLng(Double.parseDouble(this.sucursal
				.getLatitud() + ""), Double.parseDouble(this.sucursal
				.getAltitud() + "")), this.restaurante.getNombreMostrar());

		emptyModel.addOverlay(marker);

		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added",
				"Lat:" + sucursal.getLatitud() + ", Lng:"
						+ sucursal.getAltitud()));
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		System.out.println("Seleccionamos la marca");
		marker = (Marker) event.getOverlay();
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Marker Selected", marker.getTitle()));
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void cargarFotoRestaurante(FileUploadEvent event) {
		System.out.println("Cargar Foto Restaurante");
		try {
			imagenRestaurante = new DefaultStreamedContent(event.getFile()
					.getInputstream());
			restaurante.setFotoRestaurante(event.getFile().getContents());
			restaurante.setFoto(event.getFile().getContentType());
		} catch (Exception e) {
			System.out.println("No se pudo cargar la foto del resturante");
		}
	}

	public void cargarFotoPlato(FileUploadEvent event) {
		System.out.println("Cargar Metodo");
		System.out.println("Nombre del archivo:"
				+ event.getFile().getFileName());

		try {
			imagenPlato = new DefaultStreamedContent(event.getFile()
					.getInputstream());
			plato.setFotoPlato(event.getFile().getContents());
			// byte[] foto = event.getFile().getContents();
		} catch (IOException ex) {
			System.out.println(ex.getStackTrace());
		}
		// byte[] img = event.getFile().getContents();
		// imagen = event.getFile().getFileName();
		// System.out.println("Cargar Imagen");
		// //FacesContext facesContext = FacesContext.getCurrentInstance();
		// //ServletContext scontext = (ServletContext) facesContext
		// // .getExternalContext().getContext();
		// //String archivo =
		// scontext.getRealPath(ConstantesConfig.getRUTA_UPLOAD_PLATOS() +
		// imagen);
		// String archivo = ConstantesConfig.getRUTA_UPLOAD_PLATOS() + imagen;
		// crearArchivo(img, archivo);
		// setExite(true);
	}

	public void crearArchivo(byte[] bytes, String archivo) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(archivo);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String validarRestaurante() {
		String retorno = null;
		System.out.println("Validar");
		try {
			Properties propiedades = new Properties();
			propiedades.load(getClass().getResourceAsStream(
					"Mantenimiento.properties"));

			if (!restaurante.getEmail().trim().equals("")) {
				if (!restaurante.getEmail().matches(".+@.+\\.[a-z]+")) {
					retorno = propiedades.getProperty("MENSAJE_VAL_RES_EMAIL");
				} else if (lstSucursal.size() == 0 || lstSucursal == null) {
					System.out.println("Entro sucursal1");
					retorno = propiedades
							.getProperty("MENSAJE_VAL_RES_SUCURSAL");
				} else if (lstPlato.size() == 0 || lstPlato == null) {
					System.out.println("Entro plato2");
					retorno = propiedades.getProperty("MENSAJE_VAL_RES_PLATO");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No exite el archivo properties");
		}
		System.out.println(retorno);
		return retorno;
	}

}
