/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Receta;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RecetaDAO extends GenericoDAO<Receta>{
 
    public void actualizarReceta(Receta receta){
        em.merge(receta);
    }
}
