package it.unisa.nc26.digitronics.utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import it.unisa.nc26.digitronics.model.bean.Utente;

import java.io.IOException;

@WebServlet("/user-servlet")
public class UserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Utente utente =(Utente) session.getAttribute("utente");
        if(utente == null ){//caso in cui l'utente non ha ancora fatto l'accesso ma deve fare la login
            if("register".equals(action)){
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/register.jsp");
                requestDispatcher.forward(request, response);
            }else {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/login.jsp");
                requestDispatcher.forward(request, response);
            }

        }else if (utente.isRuolo()){//caso in cui l'utente è già loggato ed è un admin
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/admin.jsp");
            requestDispatcher.forward(request, response);
        }else if (!utente.isRuolo()){//caso in cui l'utente non è un admin
            session.setAttribute("utente", utente);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/results/profile.jsp");
            requestDispatcher.forward(request, response);
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);

    }

}
