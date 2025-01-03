package it.unisa.nc26.digitronics.utils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin-choice-servlet")
public class AdminChoiceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choice = request.getParameter("choice");
        if(choice==null){
            throw new MyServletException("Errore nella richiesta");
        }
        if("addproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/add-product.jsp");
            dispatcher.forward(request, response);
        }else if("modifyproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/modify-product.jsp");
            dispatcher.forward(request, response);
        }else if("deleteproduct".equals(choice)){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/delete-product.jsp");
            dispatcher.forward(request, response);
        }else{
            throw new MyServletException("Errore nella richiesta");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
