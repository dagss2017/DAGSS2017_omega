/*
 Proyecto Java EE, DAGSS-2013
 */
package es.uvigo.esei.dagss.controladores.farmacia;

import es.uvigo.esei.dagss.controladores.autenticacion.AutenticacionControlador;
import es.uvigo.esei.dagss.dominio.daos.FarmaciaDAO;
import es.uvigo.esei.dagss.dominio.daos.PacienteDAO;
import es.uvigo.esei.dagss.dominio.daos.PrescripcionDAO;
import es.uvigo.esei.dagss.dominio.daos.RecetaDAO;
import es.uvigo.esei.dagss.dominio.entidades.Farmacia;
import es.uvigo.esei.dagss.dominio.entidades.Paciente;
import es.uvigo.esei.dagss.dominio.entidades.Prescripcion;
import es.uvigo.esei.dagss.dominio.entidades.Receta;
import es.uvigo.esei.dagss.dominio.entidades.TipoUsuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author ribadas
 */
@Named(value = "farmaciaControlador")
@SessionScoped
public class FarmaciaControlador implements Serializable {

  
    private String nif;
    private String password;
    private String query;
    private String tipoQuery;

    private Farmacia farmaciaActual;
    private Paciente pacienteActual;
    private Receta recetaActual;

    private List<Prescripcion> prescripcionesPaciente;
    private List<Receta> recetasPaciente;
    private List<Farmacia> farmacias;

    @Inject
    private AutenticacionControlador autenticacionControlador;

    @EJB
    private FarmaciaDAO farmaciaDAO;

    @EJB
    private PacienteDAO pacienteDAO;

    @EJB
    private PrescripcionDAO prescripcionDAO;

    @EJB
    private RecetaDAO recetaDAO;
    /**
     * Creates a new instance of AdministradorControlador
     */
    public FarmaciaControlador() {
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Farmacia getFarmaciaActual() {
        return farmaciaActual;
    }

    public void setFarmaciaActual(Farmacia farmaciaActual) {
        this.farmaciaActual = farmaciaActual;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTipoQuery() {
        return tipoQuery;
    }

    public void setTipoQuery(String tipoQuery) {
        this.tipoQuery = tipoQuery;
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public void setPacienteActual(Paciente pacienteActual) {
        this.pacienteActual = pacienteActual;
    }

    public List<Prescripcion> getPrescripcionesPaciente() {
        return prescripcionesPaciente;
    }

    public void setPrescripcionesPaciente(List<Prescripcion> prescripcionesPaciente) {
        this.prescripcionesPaciente = prescripcionesPaciente;
    }

    private boolean parametrosAccesoInvalidos() {
        return ((nif == null) || (password == null));
    }
    
        public List<Farmacia> getFarmacias() {
        return farmacias;
    }

    public void setFarmacias(List<Farmacia> farmacias) {
        this.farmacias = farmacias;
    }

    public List<Receta> getRecetasPaciente() {
        return recetasPaciente;
    }

    public void setRecetasPaciente(List<Receta> recetasPaciente) {
        this.recetasPaciente = recetasPaciente;
    }
    
    public Receta getRecetaActual() {
        return recetaActual;
    }

    public void setRecetaActual(Receta recetaActual) {
        this.recetaActual = recetaActual;
    }

    public String doLogin() {
        String destino = null;
        if (parametrosAccesoInvalidos()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se ha indicado un nif o una contraseña", ""));
        } else {
            Farmacia farmacia = farmaciaDAO.buscarPorNIF(nif);
            if (farmacia == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe una farmacia con el NIF " + nif, ""));
            } else {
                if (autenticacionControlador.autenticarUsuario(farmacia.getId(), password,
                        TipoUsuario.FARMACIA.getEtiqueta().toLowerCase())) {
                    farmaciaActual = farmacia;
                    destino = "privado/index";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Credenciales de acceso incorrectas", ""));
                }

            }
        }
        return destino;
    }

    public String doBuscarPaciente() {
        String destino = null;
        if (!tipoQuery.equals("")) {
            switch (tipoQuery) {
                case "dni":
                    pacienteActual = pacienteDAO.buscarPorDNI(query);
                    break;
                case "ts":
                    pacienteActual = pacienteDAO.buscarPorTarjetaSanitaria(query);
                    break;
                case "nss":
                    pacienteActual = pacienteDAO.buscarPorNumeroSeguridadSocial(query);
                    break;
            }
            if (pacienteActual != null) {
                prescripcionesPaciente();
                recetasPaciente();
                farmacias();
                destino = "/farmacia/privado/receta_paciente";

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existe paciente con "+tipoQuery+": " + query, ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Es necesario un criterio de búsqueda ",""));
        }
        return destino;
    }

    public void prescripcionesPaciente() {
        Date fechaActual = new Date();
        prescripcionesPaciente = prescripcionDAO.buscarPorPaciente(pacienteActual.getId(), fechaActual);

    }
    public void farmacias(){
        List<Farmacia> f = new ArrayList<>();
        f = farmaciaDAO.buscarTodos();
        farmacias = f;
    }

    public void recetasPaciente() {
        List<Receta> recetas = new ArrayList<>();

        Iterator<Prescripcion> itP = prescripcionesPaciente.iterator();

        while (itP.hasNext()) {
            Iterator<Receta> itR = itP.next().getRecetas().iterator();

            while (itR.hasNext()) {
                recetas.add(itR.next());
            }
        }

        recetasPaciente = recetas;
    }
    
    public void doServirRecetas(){
        recetaActual.setFarmaciaDispensadora(farmaciaActual);
        recetaDAO.actualizar(recetaActual);
    }

}
