package com.example.lab7_iweb.Controllers;

import com.example.lab7_iweb.Beans.Dueno;
import com.example.lab7_iweb.Beans.Especie;
import com.example.lab7_iweb.Beans.Mascota;
import com.example.lab7_iweb.Beans.Veterinario;
import com.example.lab7_iweb.Daos.DuenoDao;
import com.example.lab7_iweb.Daos.EspecieDao;
import com.example.lab7_iweb.Daos.MascotaDao;
import com.example.lab7_iweb.Daos.VeterinarioDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "MascotaServlet", urlPatterns = {"/MascotaServlet"})
public class MascotaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        MascotaDao mascotaDao = new MascotaDao();
        EspecieDao especieDao = new EspecieDao();
        VeterinarioDao veterinarioDao = new VeterinarioDao();
        DuenoDao duenoDao = new DuenoDao();
        RequestDispatcher view;

        switch (action) {
            case "lista": {
                // Pregunta 2 + Pregunta 4 (filtro por especie; también por veterinario)
                int especieId = parseIntOrZero(request.getParameter("especieId"));
                int veterinarioId = parseIntOrZero(request.getParameter("veterinarioId"));

                request.setAttribute("listaMascotas", mascotaDao.listar(especieId, veterinarioId));
                request.setAttribute("listaEspecies", especieDao.listar());
                request.setAttribute("listaVeterinarios", veterinarioDao.listar());
                request.setAttribute("especieSeleccionada", especieId);
                request.setAttribute("veterinarioSeleccionado", veterinarioId);

                view = request.getRequestDispatcher("mascotas/lista.jsp");
                view.forward(request, response);
                break;
            }
            case "nuevo": {
                // Pregunta 3: los ComboBox se llenan con datos de la BD
                request.setAttribute("listaEspecies", especieDao.listar());
                request.setAttribute("listaVeterinarios", veterinarioDao.listar());
                request.setAttribute("listaDuenos", duenoDao.listar());

                view = request.getRequestDispatcher("mascotas/nuevo.jsp");
                view.forward(request, response);
                break;
            }
            case "borrar": {
                // Pregunta 3: usa el método borrar() heredado de DaoBase
                int idMascota = parseIntOrZero(request.getParameter("id"));
                if (idMascota > 0 && mascotaDao.obtener(idMascota) != null) {
                    mascotaDao.borrar(idMascota);
                }
                response.sendRedirect(request.getContextPath() + "/MascotaServlet");
                break;
            }
            default:
                response.sendRedirect(request.getContextPath() + "/MascotaServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");

        if ("guardar".equals(action)) {
            String nombre = request.getParameter("nombre");
            int especieId = parseIntOrZero(request.getParameter("especie_id"));
            int veterinarioId = parseIntOrZero(request.getParameter("veterinario_id"));
            int duenoId = parseIntOrZero(request.getParameter("dueno_id"));

            int edad;
            BigDecimal peso;
            try {
                edad = Integer.parseInt(request.getParameter("edad").trim());
                peso = new BigDecimal(request.getParameter("peso").trim());
            } catch (NumberFormatException | NullPointerException e) {
                response.sendRedirect(request.getContextPath() + "/MascotaServlet?action=nuevo&error=1");
                return;
            }

            // Validación mínima: campos obligatorios (NOT NULL en la BD)
            if (nombre == null || nombre.trim().isEmpty() || edad < 0 || peso.signum() <= 0
                    || especieId == 0 || veterinarioId == 0 || duenoId == 0) {
                response.sendRedirect(request.getContextPath() + "/MascotaServlet?action=nuevo&error=1");
                return;
            }

            Mascota mascota = new Mascota();
            mascota.setNombre(nombre.trim());
            mascota.setEdad(edad);
            mascota.setPeso(peso);

            Especie especie = new Especie();
            especie.setIdEspecie(especieId);
            mascota.setEspecie(especie);

            Veterinario veterinario = new Veterinario();
            veterinario.setIdVeterinario(veterinarioId);
            mascota.setVeterinario(veterinario);

            Dueno dueno = new Dueno();
            dueno.setIdDueno(duenoId);
            mascota.setDueno(dueno);

            // Pregunta 3: usa el método crear() heredado de DaoBase
            new MascotaDao().crear(mascota);
        }

        response.sendRedirect(request.getContextPath() + "/MascotaServlet");
    }

    private int parseIntOrZero(String valor) {
        try {
            return valor == null ? 0 : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
