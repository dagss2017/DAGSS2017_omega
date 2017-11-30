/*
 Proyecto Java EE, DAGSS-2014
 */

package es.uvigo.esei.dagss.dominio.daos;

import es.uvigo.esei.dagss.dominio.entidades.Cita;
import es.uvigo.esei.dagss.dominio.entidades.Medico;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;


@Stateless
@LocalBean
public class CitaDAO  extends GenericoDAO<Cita>{    

    // Completar aqui
    public List<Cita> buscarPorMedico(Medico medico,Date fechaActual){
        
        List<Cita> citas;
        
        TypedQuery<Cita> q = em.createQuery("SELECT c FROM Cita AS c WHERE c.medico.id = :medico_id AND c.fecha = :fecha_actual ", Cita.class);
        q.setParameter("medico_id", medico.getId());
        q.setParameter("fecha_actual", fechaActual);
        citas = q.getResultList();
        
        if ( (citas != null) && (citas.size() > 0)) {
            return citas;
        } else {
            return null;
        }
    }
}
