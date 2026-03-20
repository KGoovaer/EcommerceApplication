package com.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.conn.DBConnect;
import com.dao.DAOSearch;
import com.entity.viewlist;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String q = request.getParameter("q");
		if (q == null) q = "";

		String brand    = request.getParameter("brand");
		String category = request.getParameter("category");

		int minPrice = 0;
		int maxPrice = 999999;

		try {
			String minStr = request.getParameter("minPrice");
			if (minStr != null && !minStr.isEmpty())
				minPrice = Integer.parseInt(minStr);
		} catch (NumberFormatException e) { }

		try {
			String maxStr = request.getParameter("maxPrice");
			if (maxStr != null && !maxStr.isEmpty())
				maxPrice = Integer.parseInt(maxStr);
		} catch (NumberFormatException e) { }

		try {
			DAOSearch dao = new DAOSearch(DBConnect.getConn());
			List<viewlist> results = dao.searchProducts(q, brand, category, minPrice, maxPrice);

			request.setAttribute("results", results);
			request.setAttribute("q", q);

			HttpSession session = request.getSession(false);

			String dest;
			if (session != null && session.getAttribute("A") != null) {
				dest = "/search_resultsa.jsp";
			} else if (session != null && session.getAttribute("N") != null) {
				dest = "/search_resultsc.jsp";
			} else {
				dest = "/search_results.jsp";
			}

			request.getRequestDispatcher(dest).forward(request, response);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
