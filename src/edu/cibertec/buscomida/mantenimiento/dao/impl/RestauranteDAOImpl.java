package edu.cibertec.buscomida.mantenimiento.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

import edu.cibertec.buscomida.mantenimiento.dao.RestauranteDAO;
import edu.cibertec.buscomida.mantenimiento.persistence.PlatoEntity;
import edu.cibertec.buscomida.mantenimiento.persistence.RestauranteEntity;
import edu.cibertec.buscomida.mantenimiento.persistence.SucursalEntity;
import edu.cibertec.buscomida.util.resources.ConstantesConfig;

public class RestauranteDAOImpl implements RestauranteDAO {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Buscomida");
	EntityManager em  = emf.createEntityManager();
	
	
	public boolean grabar(RestauranteEntity RestauranteEntity) throws Exception {
		// TODO Auto-generated method stub''
		return false;
	}

	public boolean modificar(RestauranteEntity RestauranteEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean anular(RestauranteEntity RestauranteEntity) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public List listarResturantes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public RestauranteEntity buscarResturante(int idRestauranteEntity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertarRestaurante(RestauranteEntity restaurante,List<SucursalEntity> sucursal, List<PlatoEntity> plato)
			throws Exception {
		em.getTransaction().begin();
		try {
		//Obtener correlativo
//		int idRest = this.obtenerCorrelativo(ConstantesConfig.getTABLA_RESTAURANTE());
//		int idSuc = this.obtenerCorrelativo(ConstantesConfig.getTABLA_SUCURSAL());
//		int idPlato = this.obtenerCorrelativo(ConstantesConfig.getTABLA_PLATO());
		
//		idRest+=1;
//		idSuc+=1;
//		idPlato+=1;
		
//		restaurante.setIdRest(idRest);
		
		//registrar resturante
//		em.persist(restaurante.getPlatos());
//		em.flush();
		em.persist(restaurante);
		em.flush();			
		
		System.out.println("ID DE RESTAURANTE ---------------> " + restaurante.getIdRest());
		for (PlatoEntity p : plato) {
			p.setRestaurante(restaurante);
			em.persist(p);
			em.flush();			
		}
		for (SucursalEntity s : sucursal) {
			s.setRestaurante(restaurante);
			em.persist(s);
			em.flush();
		}
		
//		sucursal.setRestaurante(restaurante);
//		sucursal.setIdSuc(idSuc);
//		
//		
//		em.persist(sucursal);
//		em.flush();
//		
//		plato.setRestaurante(restaurante);
//		plato.setIdPlato(idPlato);
//		
//
//		em.persist(plato);
//		em.flush();
		//Actualizamos el correlativo
//		actulizarCorrelativo(ConstantesConfig.getTABLA_RESTAURANTE(),idRest);
//		actulizarCorrelativo(ConstantesConfig.getTABLA_SUCURSAL(),idSuc);
//		actulizarCorrelativo(ConstantesConfig.getTABLA_PLATO(),idPlato);
		System.out.println("Actualizo los correaltivoas");
		//Seteamos la variables de la transaccion en cuando es Exito
		ConstantesConfig.setTRANSACCION_INSERT(1);
			
		em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			em.close();
		}
	}
	
	private void actulizarCorrelativo(String tabla, int id) {
		// TODO Auto-generated method stub
		Query sql=em.createQuery("UPDATE CorrelativoEntity c SET c.id= :correlativo " +
				"WHERE c.tabla= :tabla");
			sql.setParameter("correlativo",id);
			sql.setParameter("tabla",tabla);
			sql.executeUpdate();
	}

//	public int obtenerCorrelativo(String constante){
//		Query sql=em.createQuery("SELECT c.id FROM CorrelativoEntity c where c.tabla= :tabla ");
//		sql.setParameter("tabla",constante);
//		
//		int correlativoObtenido = (Integer) sql.getSingleResult();
//		System.out.println(correlativoObtenido);
//		return correlativoObtenido;
//	}
}
